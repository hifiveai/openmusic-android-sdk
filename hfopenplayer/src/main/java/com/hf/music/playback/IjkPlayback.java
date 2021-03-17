package com.hf.music.playback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import androidx.annotation.NonNull;

import com.hf.music.config.MusicConstant;
import com.hf.music.config.MusicPlayAction;
import com.hf.music.config.PlayModeEnum;
import com.hf.music.inter.EventCallback;
import com.hf.music.inter.OnPlayerEventListener;
import com.hf.music.manager.AudioFocusManager;
import com.hf.music.manager.AudioSoundManager;
import com.hf.music.manager.HFPlayer;
import com.hf.music.manager.MediaSessionManager;
import com.hf.music.model.AudioBean;
import com.hf.music.receiver.AudioBroadcastReceiver;
import com.hf.music.receiver.AudioEarPhoneReceiver;
import com.hf.music.service.PlayService;
import com.hf.music.tool.NetworkCallbackImpl;
import com.hf.music.tool.QuitTimerHelper;
import com.hf.music.utils.MusicLogUtils;
import com.hf.music.utils.MusicSpUtils;
import com.hf.music.utils.NotificationHelper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IAndroidIO;

public class IjkPlayback {

    private PlayService mPlayService;
    /**
     * 正在播放的歌曲的序号
     */
    private int mPlayingPosition = -1;
    /**
     * 正在播放的歌曲[本地|网络]
     */
    private AudioBean mPlayingMusic;
    /**
     * 音频list集合
     */
    private List<AudioBean> audioMusics;
    /**
     * 播放状态
     */
    private int mPlayState = MusicPlayAction.STATE_IDLE;
    /**
     * 播放器
     */
    private IjkMediaPlayer mPlayer;
    /**
     * 播放进度监听器
     */
    private OnPlayerEventListener mListener;
    /**
     * 更新播放进度的显示，时间的显示
     */
    private static final int UPDATE_PLAY_PROGRESS_SHOW = 0;
    /**
     * 是否锁屏了，默认是false
     */
    private boolean mIsLocked = false;
    /**
     * 是否又网络
     */
    private boolean mNetAvailable = true;
    /**
     * 广播接受者标识，避免多次注册广播
     */
    private boolean mReceiverTag = false;
    /**
     * 加入唤醒锁和WiFi锁保证我们在后台长时间播放音频的稳定
     */
    private WifiManager.WifiLock wifiLock;
    
    
    public IjkPlayback(PlayService service ){
        mPlayService = service;

        createIjkMediaPlayer();
        initQuitTimer();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PLAY_PROGRESS_SHOW:
                    updatePlayProgressShow();
                    break;
                case MusicPlayAction.STATE_PAUSE:
                    mNetAvailable = false;
                    pause();
                    break;
                case MusicPlayAction.STATE_PLAYING:
                    mNetAvailable = true;
                    if (HFPlayer.getIsReconnect()){
                        playPause();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 服务在销毁时调用该方法
     */
    public void release() {
        //销毁handler
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        //销毁IjkMediaPlayer
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        // 关闭wifi锁
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
        wifiLock = null;
    }



    /**
     * 创建IjkMediaPlayer对象
     */
    private void createIjkMediaPlayer() {
        if (mPlayer == null) {
            //MediaCodec codec = new MediaCodec();
            mPlayer = new IjkMediaPlayer();

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 使用唤醒锁
            mPlayer.setWakeMode(mPlayService, PowerManager.PARTIAL_WAKE_LOCK);
            // 初始化wifi锁
            wifiLock = ((WifiManager) mPlayService.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            //播放重连次数
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 5);
            //SeekTo设置优化
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1L);
        }
    }



    /**
     * 初始化计时器
     */
    private void initQuitTimer() {
        QuitTimerHelper.getInstance().init(mPlayService, handler, new EventCallback<Long>() {
            @Override
            public void onEvent(Long aLong) {
                if (mListener != null) {
                    mListener.onTimer(aLong);
                }


            }
        });
    }


    /**---------------------播放或暂停，上一首，下一首-----------------------------------------*/

    /**
     * 播放或暂停
     * 逻辑：
     * 1.如果正在准备，点击则是停止播放
     * 2.如果是正在播放，点击则是暂停
     * 3.如果是暂停状态，点击则是开始播放
     * 4.其他情况是直接播放
     */
    public void playPause() {
        if(mPlayer == null) return;
        if (isPreparing()) {
            stop();
        } else if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            start();
        } else {
            play(getPlayingPosition());
        }
    }


    /**
     * 上一首
     * 记住有播放类型，单曲循环，顺序循环，随机播放
     * 逻辑：如果不是第一首，则还有上一首；如果没有上一首，则切换到最后一首
     */
    public void prev() {
        //建议都添加这个判断
        if (null == audioMusics || audioMusics.isEmpty()) {
            return;
        }
        int playMode = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0);
        int size = audioMusics.size();
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            //随机
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(size);
                play(mPlayingPosition);
                break;
            //单曲
            case SINGLE:
                play(mPlayingPosition);
                break;
            //顺序播放并且循环
            case LOOP:
            default:
                if (mPlayingPosition != 0) {
                    // 如果不是第一首，则还有上一首
                    mPlayingPosition--;
                } else {
                    // 如果没有上一首，则切换到最后一首
                    mPlayingPosition = size;
                }
                play(mPlayingPosition);
                break;
        }
    }


    /**
     * 下一首
     * 记住有播放类型，单曲循环，顺序循环，随机播放
     * 逻辑：如果不是最后一首，则还有下一首；如果是最后一首，则切换回第一首
     */
    public void next() {
        //建议都添加这个判断
        if (null == audioMusics || audioMusics.isEmpty()) {
            return;
        }
        int playMode = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0);
        int size = audioMusics.size();
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            //随机
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(size);
                play(mPlayingPosition);
                break;
            //单曲
            case SINGLE:
                play(mPlayingPosition);
                break;
            //顺序播放并且循环
            case LOOP:
            default:
                if (mPlayingPosition != size - 1) {
                    // 如果不是最后一首，则还有下一首
                    mPlayingPosition++;
                } else {
                    // 如果是最后一首，则切换回第一首
                    mPlayingPosition = 0;
                }
                MusicLogUtils.e("PlayService" + "----mPlayingPosition----" + mPlayingPosition);
                play(mPlayingPosition);
                break;
        }
    }

    /**---------------------开始播放，暂停播放，停止播放等-----------------------------------------*/


    /**
     * 开始播放
     */
    public void start() {
        if(!mNetAvailable) return;
        if (!isPreparing() && !isPausing()) {
            return;
        }
        if (mPlayingMusic == null) {
            return;
        }
        if (mPlayService.mAudioFocusManager.requestAudioFocus()) {
            if (mPlayer != null) {
                mPlayer.start();
                // 启用wifi锁
                wifiLock.acquire();
                //同步播放状态
                mPlayState = MusicPlayAction.STATE_PLAYING;
                //开始发送消息，执行进度条进度更新
                handler.sendEmptyMessage(UPDATE_PLAY_PROGRESS_SHOW);
                if (mListener != null) {
                    mListener.onPlayStateChanged(mPlayState);
                }
                //当点击播放按钮时(播放详情页面或者底部控制栏)，同步通知栏中播放按钮状态
                NotificationHelper.get().showPlay(mPlayingMusic, mPlayService.mMediaSessionManager.getMediaSession());
                //注册监听来电/耳机拔出时暂停播放广播
                if (!mReceiverTag) {
                    mReceiverTag = true;
                    mPlayService.registerReceiver(mPlayService.mNoisyReceiver,new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
);
                }
                mPlayService.mMediaSessionManager.updatePlaybackState();
            }
        }
    }


    /**
     * 暂停
     */
    public void pause() {
        if (mPlayer != null) {
            //暂停
            mPlayer.pause();
            // 关闭wifi锁
            if (wifiLock.isHeld()) {
                wifiLock.release();
            }
            //切换状态
            mPlayState = MusicPlayAction.STATE_PAUSE;
            //移除，注意一定要移除，否则一直走更新方法
            handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
            //监听
            if (mListener != null) {
                mListener.onPlayStateChanged(mPlayState);
            }

            //当点击暂停按钮时(播放详情页面或者底部控制栏)，同步通知栏中暂停按钮状态
            NotificationHelper.get().showPause(mPlayingMusic, mPlayService.mMediaSessionManager.getMediaSession());
            //注销监听来电/耳机拔出时暂停播放广播
            //判断广播是否注册
            if (mReceiverTag) {
                //Tag值 赋值为false 表示该广播已被注销
                mReceiverTag = false;
                mPlayService.unregisterReceiver(mPlayService.mNoisyReceiver);
            }

            mPlayService.mMediaSessionManager.updatePlaybackState();
        }
    }


    /**
     * 停止播放
     */
    public void stop() {
        if (isDefault()) {
            return;
        }
        pause();
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayState = MusicPlayAction.STATE_IDLE;
            if (mListener != null) {
                mListener.onPlayStateChanged(mPlayState);
            }
        }
    }


    /**
     * 播放索引为position的音乐
     *
     * @param position 索引
     */
    public void play(int position) {
        if(!mNetAvailable) return;
        audioMusics = HFPlayer.getMusicList();
        if (audioMusics == null || audioMusics.isEmpty()) {
            return;
        }

        if (position < 0) {
            position = audioMusics.size() - 1;
        } else if (position >= audioMusics.size()) {
            //如果是最后一首音乐，则播放时直接播放第一首音乐
            position = 0;
        }

        mPlayingPosition = position;
        AudioBean music = audioMusics.get(mPlayingPosition);
        String id = music.getId();
        MusicLogUtils.e("PlayService" + "----id----" + id);
        //保存当前播放的musicId，下次进来可以记录状态
        long musicId = Long.parseLong(id);
        MusicSpUtils.getInstance(MusicConstant.SP_NAME).put(MusicConstant.MUSIC_ID, musicId);
        play(music);
    }


    /**
     * 拖动seekBar时，调节进度
     *
     * @param progress 进度
     */
    public boolean seekTo(int progress) {
        if(!mNetAvailable) return false;
        //只有当播放或者暂停的时候才允许拖动bar
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(progress);
            if (mListener != null) {
                mListener.onProgressUpdate(progress, (int) getDuration());
            }

            mPlayService.mMediaSessionManager.updatePlaybackState();
            return true;
        }
        return false;
    }

    /**
     * 播放，通过URL播放
     *
     * @param music music
     */
    public void play(AudioBean music) {
        if(!mNetAvailable) return;
        mPlayingMusic = music;
        playWhitUrl(mPlayingMusic.getPath());
        //当播放的时候，需要刷新界面信息
        if (mListener != null) {
            mListener.onChange(mPlayingMusic);
        }
        //更新通知栏
        NotificationHelper.get().showPlay(mPlayingMusic, mPlayService.mMediaSessionManager.getMediaSession());

        //更新
        mPlayService.mMediaSessionManager.updateMetaData(mPlayingMusic);
        mPlayService.mMediaSessionManager.updatePlaybackState();

    }


    /**
     * 通过URl播放
     *
     * @param url
     */
    public void playWhitUrl(String url) {
        if(!mNetAvailable) return;
        createIjkMediaPlayer();
        try {
            mPlayer.reset();
            //把音频路径传给播放器
//            "ijkio:cache:ffio:"+
            mPlayer.setDataSource("ijkio:cache:ffio:" + url);

//            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "auto_save_map", 1);
//            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "parse_cache_map", 1);
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "cache_file_path", mPlayService.getCacheDir() + "/1.tmp");
//            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "cache_map_path",getCacheDir()+"/2.tmp");

            // 无限制收流
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 0);
            // 设置无缓冲，这是播放器的缓冲区，有数据就播放
//            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", HFPlayer.getMaxBufferSize());
            //配置成1是变声，0是不变声
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 0);
            //准备
            mPlayer.prepareAsync();
            //设置状态为准备中
            mPlayState = MusicPlayAction.STATE_PREPARING;
            if (mListener != null) {
                mListener.onPlayStateChanged(mPlayState);
            }
            //监听
            mPlayer.setOnPreparedListener(mOnPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mPlayer.setOnCompletionListener(mOnCompletionListener);
            mPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mPlayer.setOnErrorListener(mOnErrorListener);
            mPlayer.setOnInfoListener(mOnInfoListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新播放进度的显示，时间的显示
     */
    private void updatePlayProgressShow() {
        if (isPlaying()) {
            int currentPosition = (int) mPlayer.getCurrentPosition();
            if (mListener != null) {
                mListener.onProgressUpdate(currentPosition, (int) getDuration());
            }
        }

        MusicLogUtils.e("updatePlayProgressShow" + mPlayer.getCurrentPosition());
        // 每30毫秒更新一下显示的内容，注意这里时间不要太短，因为这个是一个循环
        // 经过测试，60毫秒更新一次有点卡，30毫秒最为顺畅
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS_SHOW, 300);
    }


    /**
     * 音频准备好的监听器
     */
    private IjkMediaPlayer.OnPreparedListener mOnPreparedListener = new IjkMediaPlayer.OnPreparedListener() {
        /** 当音频准备好可以播放了，则这个方法会被调用  */
        @Override
        public void onPrepared(IMediaPlayer mediaPlayer) {
            if (isPreparing()) {
                start();
            }
        }
    };


    /**
     * 当音频播放结束的时候的监听器
     */
    private IjkMediaPlayer.OnCompletionListener mOnCompletionListener = new IjkMediaPlayer.OnCompletionListener() {
        /** 当音频播放结果的时候这个方法会被调用 */
        @Override
        public void onCompletion(IMediaPlayer mediaPlayer) {
            if(mPlayer.getCurrentPosition() + 1000 >= getDuration() && getDuration() != 0){
                mPlayState = MusicPlayAction.STATE_IDLE;
                next();
            }else{
                mPlayState = MusicPlayAction.STATE_ERROR;
            }
            if (mListener != null) {
                mListener.onPlayStateChanged(mPlayState);
            }
        }
    };


    /**
     * 当音频缓冲的监听器
     */
    private IjkMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IjkMediaPlayer.OnBufferingUpdateListener() {

        @Override
        public void onBufferingUpdate(IMediaPlayer mediaPlayer, int i) {
            if (mListener != null) {
                if (i >= 99) i = 100;
                mListener.onBufferingUpdate(i);
            }
            MusicLogUtils.e("updateBuffering" + i);
        }
    };


    /**
     * 跳转完成时的监听
     */
    private IjkMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IjkMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mediaPlayer) {

        }
    };

    /**
     * 播放错误的监听
     * int MEDIA_ERROR_SERVER_DIED = 100;//服务挂掉，视频中断，一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     * int MEDIA_ERROR_IO = -1004;//IO 错误
     * int MEDIA_ERROR_MALFORMED = -1007;
     * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类
     * int MEDIA_ERROR_UNSUPPORTED = -1010;//数据不支持
     * int MEDIA_ERROR_TIMED_OUT = -110;//数据超时
     */
    private IjkMediaPlayer.OnErrorListener mOnErrorListener = new IjkMediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(IMediaPlayer mediaPlayer, int i, int i1) {
            MusicLogUtils.e("MediaPlayer.onError" + i);
            switch (i){
                case -10000:
                    mPlayState = MusicPlayAction.STATE_ERROR_AUDIO;
                    break;
                default:
                    mPlayState = MusicPlayAction.STATE_ERROR;
                    break;

            }
            if (mListener != null) {
                mListener.onPlayStateChanged(mPlayState);
            }
            return false;
        }
    };

    /**
     * 设置音频信息监听器
     * int MEDIA_INFO_UNKNOWN = 1;//未知信息
     * int MEDIA_INFO_STARTED_AS_NEXT = 2;//播放下一条
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频开始整备中，准备渲染
     * int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;//视频日志跟踪
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲中 开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_NETWORK_BANDWIDTH = 703;//网络带宽，网速方面
     * int MEDIA_INFO_BAD_INTERLEAVING = 800;//
     * int MEDIA_INFO_NOT_SEEKABLE = 801;//不可设置播放位置，直播方面
     * int MEDIA_INFO_METADATA_UPDATE = 802;//
     * int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
     * int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;//不支持字幕
     * int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;//字幕超时
     * int MEDIA_INFO_VIDEO_INTERRUPT= -10000;//数据连接中断，一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频方向改变，视频选择信息
     * int MEDIA_INFO_AUDIO_RENDERING_START = 10002;//音频开始整备中
     */
    private IjkMediaPlayer.OnInfoListener mOnInfoListener = new IjkMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(IMediaPlayer mediaPlayer, int i, int i1) {
            MusicLogUtils.e("MediaPlayer.onInfo" + i);
            switch (i){
                case 701:
                    mPlayState = MusicPlayAction.STATE_BUFFERING;
                    break;
                case 702:
                case 10002:
                    mPlayState = MusicPlayAction.STATE_PLAYING;
                    break;
                case 10009:
                    if(isPlaying()){
                        mPlayState = MusicPlayAction.STATE_PLAYING;
                    }
                    break;
                default:
                    break;

            }
//            if (mListener != null) {
//                mListener.onPlayStateChanged(mPlayState);
//            }

            return false;
        }
    };

    /**
     * 是否正在播放
     *
     * @return true表示正在播放
     */
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }


    /**
     * 是否暂停
     *
     * @return true表示暂停
     */
    public boolean isPausing() {
        return mPlayState == MusicPlayAction.STATE_PAUSE;
    }


    /**
     * 是否正在准备中
     *
     * @return true表示正在准备中
     */
    public boolean isPreparing() {
        return mPlayState == MusicPlayAction.STATE_PREPARING;
    }


    /**
     * 是否正在准备中
     *
     * @return true表示正在准备中
     */
    public boolean isDefault() {
        return mPlayState == MusicPlayAction.STATE_IDLE;
    }



    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }


    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public AudioBean getPlayingMusic() {
        return mPlayingMusic;
    }


    /**
     * 获取播放的进度
     *
     * @return long类型值
     */
    public long getCurrentPosition() {
        if (isPlaying() || isPausing()) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 设置音量
     *
     * @param volume
     */
    public void setVolume(int volume) {
//        if(mPlayer != null){
//            mPlayer.setVolume(volume,volume);
//        }
        AudioSoundManager soundManager = new AudioSoundManager(mPlayService);
        soundManager.setVolume(volume);
    }

    /**
     * 获取音量
     */
    public int getVolume() {
        AudioSoundManager soundManager = new AudioSoundManager(mPlayService);
        return soundManager.getVolume();
    }

    /**
     * 获取音量
     */
    public int getMaxVolume() {
        AudioSoundManager soundManager = new AudioSoundManager(mPlayService);
        return soundManager.getMaxVolume();
    }

    /**
     * 设置速率
     *
     * @param speed   0.5~2
     */
    public void setSpeed(float speed) {
        if (mPlayer != null) {
            mPlayer.setSpeed(speed);
        }
    }

    /**
     * 获取总时长
     */
    public long getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    public void sendMessage(int what){
        handler.sendEmptyMessage(what);
    }


    /**
     * 判斷是否有上一首音頻
     *
     * @return true表示有
     */
    public boolean isHavePre() {
        if (audioMusics != null && audioMusics.size() > 0) {
            if (mPlayingPosition != 0) {
                // 如果不是第一首，则还有上一首
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判斷是否有下一首音頻
     *
     * @return true表示有
     */
    public boolean isHaveNext() {
        if (audioMusics != null && audioMusics.size() > 0) {
            if (mPlayingPosition != audioMusics.size() - 1) {
                // 如果不是最后一首，则还有下一首
                return true;
            } else {
                // 如果是最后一首，则切换回第一首
                return false;
            }
        } else {
            return false;
        }
    }


//
//    /**
//     * 扫描音乐
//     */
//    @SuppressLint("StaticFieldLeak")
//    public void updateMusicList(final EventCallback<Void> callback) {
//        new AsyncTask<Void, Void, List<AudioBean>>() {
//            @Override
//            protected List<AudioBean> doInBackground(Void... params) {
//                return FileMusicScanManager.getInstance().scanMusic(PlayService.this);
//            }
//
//            @Override
//            protected void onPostExecute(List<AudioBean> musicList) {
//                //首先先清空
//                //然后添加所有扫描到的音乐
//                HFPlayer.get().setMusicList(musicList);
//
//                //如果获取音乐数据集合不为空
//                if (!HFPlayer.get().getMusicList().isEmpty()) {
//                    //音频的集合
//                    audioMusics = HFPlayer.get().getMusicList();
//                    //刷新正在播放的本地歌曲的序号
//                    updatePlayingPosition();
//                    //获取正在播放的音乐
//                    if(mPlayingPosition>=0){
//                        mPlayingMusic = HFPlayer.get().getMusicList().get(mPlayingPosition);
//                    }
//                }
//                if (callback != null) {
//                    callback.onEvent(null);
//                }
//            }
//        }.execute();
//    }


    /**
     * 删除或下载歌曲后刷新正在播放的本地歌曲的序号
     */
    public void updatePlayingPosition() {
        int position = 0;
        long id = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getLong(MusicConstant.MUSIC_ID, -1);
        if (audioMusics.isEmpty()) {
            return;
        }
        for (int i = 0; i < audioMusics.size(); i++) {
            String musicId = audioMusics.get(i).getId();
            MusicLogUtils.e("PlayService" + "----musicId----" + musicId);
            if (Long.parseLong(musicId) == id) {
                position = i;
                break;
            }
        }
        mPlayingPosition = position;
        long musicId = Long.parseLong(audioMusics.get(mPlayingPosition).getId());
        MusicSpUtils.getInstance(MusicConstant.SP_NAME).put(MusicConstant.MUSIC_ID, musicId);
    }


    /**
     * 设置播放进度监听器
     *
     * @param listener listener
     */
    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }


    /**-------------------------------------添加锁屏界面----------------------------------------*/


    /**
     * 打开锁屏页面
     * 不管是播放状态是哪一个，只要屏幕灭了到亮了，就展现这个锁屏页面
     * 有些APP限制了状态，比如只有播放时才走这个逻辑
     */
    private void startLockAudioActivity() {
        if (!mIsLocked && isPlaying()) {
//            Intent lockScreen = new Intent(this, LockAudioActivity.class);
//            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(lockScreen);
//            BaseConfig.INSTANCE.setLocked(true);
        }
    }
}
