package com.hf.playerkernel.playback

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.WifiLock
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import com.hf.playerkernel.config.MusicConstant
import com.hf.playerkernel.config.MusicPlayAction
import com.hf.playerkernel.config.PlayModeEnum
import com.hf.playerkernel.inter.EventCallback
import com.hf.playerkernel.inter.HFPlayerEventListener
import com.hf.playerkernel.manager.AudioSoundManager
import com.hf.playerkernel.manager.HFPlayerApi.getCallback
import com.hf.playerkernel.manager.HFPlayerApi.getImageLoader
import com.hf.playerkernel.manager.HFPlayerApi.getIsReconnect
import com.hf.playerkernel.manager.HFPlayerApi.getIsUseCache
import com.hf.playerkernel.manager.HFPlayerApi.getMaxBufferSize
import com.hf.playerkernel.manager.HFPlayerApi.getMusicList
import com.hf.playerkernel.model.AudioBean
import com.hf.playerkernel.notification.NotificationHelper
import com.hf.playerkernel.notification.imageloader.ImageLoaderCallBack
import com.hf.playerkernel.service.PlayService
import com.hf.playerkernel.tool.QuitTimerHelper
import com.hf.playerkernel.utils.MusicLogUtils
import com.hf.playerkernel.utils.MusicSpUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException
import java.util.*

class IjkPlayback(private val mPlayService: PlayService) {
    /**
     * 当前缓冲进度
     */
    var mBufferedProgress = 0
    /**
     * 断网时缓冲进度
     */
    var mTargetProgress = 0
    /**
     * 是否暂停
     */
    var paused = false
    /**
     * 正在播放的歌曲的序号
     */
    var playingPosition = -1
        private set
    /**
     * 正在播放的歌曲[本地|网络]
     */
    var playingMusic: AudioBean? = null

    /**
     * 正在播放的歌曲url
     */
    var playingUrl: String? = null
        private set

    /**
     * 音频list集合
     */
    private var audioMusics: List<AudioBean>? = null

    /**
     * 播放状态
     */
    private var mPlayState = MusicPlayAction.STATE_IDLE

    /**
     * 播放器
     */
    private var mPlayer: IjkMediaPlayer? = null

    /**
     * 播放进度监听器
     */
    private var mListener: HFPlayerEventListener? = null

    /**
     * 是否锁屏了，默认是false
     */
    private val mIsLocked = false

    /**
     * 是否又网络
     */
    var mNetAvailable = true
        get

    /**
     * 广播接受者标识，避免多次注册广播
     */
    private var mReceiverTag = false

    /**
     * 加入唤醒锁和WiFi锁保证我们在后台长时间播放音频的稳定
     */
    private var wifiLock: WifiLock? = null

    @SuppressLint("HandlerLeak")
    private var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_PLAY_PROGRESS_SHOW -> updatePlayProgressShow()
                MusicPlayAction.STATE_PAUSE -> {
                    mNetAvailable = false
                    mTargetProgress = (mBufferedProgress * mPlayer!!.duration / 100).toInt()
//                    pause()
                }
                MusicPlayAction.STATE_PLAYING -> {
                    mNetAvailable = true
                    if (getIsReconnect() && isPausing) {
                        playPause()
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * 服务在销毁时调用该方法
     */
    fun release() {
        //销毁handler
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
        //销毁IjkMediaPlayer
        mPlayer!!.reset()
        mPlayer!!.release()
        mPlayer = null
        // 关闭wifi锁
        if (wifiLock!!.isHeld) {
            wifiLock!!.release()
        }
        wifiLock = null
    }

    /**
     * 创建IjkMediaPlayer对象
     */
    private fun createIjkMediaPlayer() {
        if (mPlayer == null) {
            //MediaCodec codec = new MediaCodec();
            mPlayer = IjkMediaPlayer()
            mPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            // 使用唤醒锁
            mPlayer!!.setWakeMode(mPlayService, PowerManager.PARTIAL_WAKE_LOCK)
            // 初始化wifi锁
            wifiLock = (mPlayService.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock")
            //播放重连次数
            mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 5)
            //SeekTo设置优化
            mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1L)
        }
    }

    /**
     * 初始化计时器
     */
    private fun initQuitTimer() {
        QuitTimerHelper.getInstance().init(mPlayService, handler!!, object : EventCallback<Long> {
            override fun onEvent(aLong: Long) {
                if (mListener != null) {
                    mListener!!.onTimer(aLong)
                }
            }
        })
    }
    /**---------------------播放或暂停，上一首，下一首----------------------------------------- */
    /**
     * 播放或暂停
     * 逻辑：
     * 1.如果正在准备，点击则是停止播放
     * 2.如果是正在播放，点击则是暂停
     * 3.如果是暂停状态，点击则是开始播放
     * 4.其他情况是直接播放
     */
    fun playPause() {
        if (mPlayer == null) return
        if (isPreparing) {
            stop()
        } else if (isPlaying) {
            pause()
        } else if (isPausing) {
            start()
        } else if (isBuffering) {
            start()
        }else {
            if(playingUrl!= null){
                playWhitUrl(playingUrl!!)
            }else{
                release()
            }
        }
        if (getCallback() != null) {
            getCallback()!!.playPause()
        }
    }

    /**
     * 上一首
     * 记住有播放类型，单曲循环，顺序循环，随机播放
     * 逻辑：如果不是第一首，则还有上一首；如果没有上一首，则切换到最后一首
     */
    fun prev() {
        if (getCallback() != null) {
            getCallback()!!.onPre()
        }
        //建议都添加这个判断
        if (null == audioMusics || audioMusics!!.isEmpty()) {
            return
        }
        val playMode = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0)
        val size = audioMusics!!.size
        val mode = PlayModeEnum.valueOf(playMode)
        when (mode) {
            PlayModeEnum.SHUFFLE -> {
                playingPosition = Random().nextInt(size)
                play(playingPosition)
            }
            PlayModeEnum.SINGLE -> play(playingPosition)
            PlayModeEnum.LOOP -> {
                if (playingPosition != 0) {
                    // 如果不是第一首，则还有上一首
                    playingPosition--
                } else {
                    // 如果没有上一首，则切换到最后一首
                    playingPosition = size
                }
                play(playingPosition)
            }
            else -> {
                if (playingPosition != 0) {
                    playingPosition--
                } else {
                    playingPosition = size
                }
                play(playingPosition)
            }
        }
    }

    /**
     * 下一首
     * 记住有播放类型，单曲循环，顺序循环，随机播放
     * 逻辑：如果不是最后一首，则还有下一首；如果是最后一首，则切换回第一首
     */
    operator fun next() {
        if (getCallback() != null) {
            getCallback()!!.onNext()
        }
        //建议都添加这个判断
        if (null == audioMusics || audioMusics!!.isEmpty()) {
            return
        }
        val playMode = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0)
        val size = audioMusics!!.size
        val mode = PlayModeEnum.valueOf(playMode)
        when (mode) {
            PlayModeEnum.SHUFFLE -> {
                playingPosition = Random().nextInt(size)
                play(playingPosition)
            }
            PlayModeEnum.SINGLE -> play(playingPosition)
            PlayModeEnum.LOOP -> {
                if (playingPosition != size - 1) {
                    // 如果不是最后一首，则还有下一首
                    playingPosition++
                } else {
                    // 如果是最后一首，则切换回第一首
                    playingPosition = 0
                }
                MusicLogUtils.e("PlayService" + "----mPlayingPosition----" + playingPosition)
                play(playingPosition)
            }
            else -> {
                if (playingPosition != size - 1) {
                    playingPosition++
                } else {
                    playingPosition = 0
                }
                MusicLogUtils.e("PlayService" + "----mPlayingPosition----" + playingPosition)
                play(playingPosition)
            }
        }
    }
    /**---------------------开始播放，暂停播放，停止播放等----------------------------------------- */
    /**
     * 开始播放
     */
    fun start() {
        if (!mNetAvailable) return
        if (!isPreparing && !isPausing && !isBuffering) {
            return
        }
        if (mPlayService.mAudioFocusManager.requestAudioFocus()) {
            if (mPlayer != null) {
                mPlayer!!.start()
                paused = false
                // 启用wifi锁
                wifiLock!!.acquire()
                //同步播放状态
                mPlayState = MusicPlayAction.STATE_PLAYING
                //开始发送消息，执行进度条进度更新
                handler!!.sendEmptyMessage(UPDATE_PLAY_PROGRESS_SHOW)
                if (mListener != null) {
                    mListener!!.onPlayStateChanged(mPlayState)
                }
                if (playingMusic != null) {
                    //当点击播放按钮时(播放详情页面或者底部控制栏)，同步通知栏中播放按钮状态
                    NotificationHelper.get().showPlay(playingMusic, mPlayService.mMediaSessionManager.mediaSession)
                    playingMusic!!.duration = mPlayer!!.duration
                    mPlayService.mMediaSessionManager.updatePlaybackState()
                }
                //注册监听来电/耳机拔出时暂停播放广播
                if (!mReceiverTag) {
                    mReceiverTag = true
                    mPlayService.registerReceiver(mPlayService.mNoisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
                    )
                }
            }
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        if (mPlayer != null) {
            //暂停
            mPlayer!!.pause()
            paused = true
            // 关闭wifi锁
            if (wifiLock!!.isHeld) {
                wifiLock!!.release()
            }
            //切换状态
            mPlayState = MusicPlayAction.STATE_PAUSE
            //移除，注意一定要移除，否则一直走更新方法
            handler!!.removeMessages(UPDATE_PLAY_PROGRESS_SHOW)
            //监听
            if (mListener != null) {
                mListener!!.onPlayStateChanged(mPlayState)
            }
            //注销监听来电/耳机拔出时暂停播放广播
            //判断广播是否注册
            if (mReceiverTag) {
                //Tag值 赋值为false 表示该广播已被注销
                mReceiverTag = false
                mPlayService.unregisterReceiver(mPlayService.mNoisyReceiver)
            }
            if (playingMusic != null) {
                //当点击暂停按钮时(播放详情页面或者底部控制栏)，同步通知栏中暂停按钮状态
                NotificationHelper.get().showPause(playingMusic, mPlayService.mMediaSessionManager.mediaSession)
                mPlayService.mMediaSessionManager.updatePlaybackState()
            }
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        if (isDefault) {
            return
        }
        pause()
        if (mPlayer != null) {
            mPlayer!!.reset()
            mPlayState = MusicPlayAction.STATE_IDLE
            if (mListener != null) {
                mListener!!.onPlayStateChanged(mPlayState)
            }
        }
    }

    /**
     * 播放索引为position的音乐
     *
     * @param position 索引
     */
    private fun play(position: Int) {
        var position = position
        if (!mNetAvailable) return
        audioMusics = getMusicList()
        if (audioMusics == null || audioMusics?.isEmpty()!!) {
            return
        }
        if (position < 0) {
            position = audioMusics?.size!! - 1
        } else if (position >= audioMusics?.size!!) {
            //如果是最后一首音乐，则播放时直接播放第一首音乐
            position = 0
        }
        playingPosition = position
        val music = audioMusics?.get(playingPosition)
        val id = music?.id
        MusicLogUtils.e("PlayService----id----$id")
        //保存当前播放的musicId，下次进来可以记录状态
        val musicId = id?.toLong()
        MusicSpUtils.getInstance(MusicConstant.SP_NAME).put(MusicConstant.MUSIC_ID, musicId!!)
        play(music)
    }

    /**
     * 拖动seekBar时，调节进度
     *
     * @param progress 进度
     */
    fun seekTo(progress: Int): Boolean {
        if (!mNetAvailable) return false
        //只有当播放或者暂停的时候才允许拖动bar
        if (isPlaying || isPausing) {
            mPlayer!!.seekTo(progress.toLong())
            if (mListener != null) {
                mListener!!.onProgressUpdate(progress, duration.toInt())
            }
            mPlayService.mMediaSessionManager.updatePlaybackState()
            return true
        }
        return false
    }

    /**
     * 播放，通过URL播放
     *
     * @param music music
     */
    fun play(music: AudioBean?) {
        if (!mNetAvailable) return
        playingMusic = music
        playWhitUrl(playingMusic!!.path)
        //当播放的时候，需要刷新界面信息
        if (mListener != null) {
            mListener!!.onChange(playingMusic)
        }
    }

    /**
     * 通过URl播放
     *
     * @param url
     */
    fun playWhitUrl(url: String) {
        if (!mNetAvailable) return
        playingUrl = url
        createIjkMediaPlayer()
        try {
            mPlayer!!.reset()
            if (getIsUseCache()) {
                val fileName = getFileName(url)
                mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "auto_save_map", 1)
                mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "parse_cache_map", 1)
                mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "cache_file_path", "$fileName.tmp")
                mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "cache_map_path", "${fileName}path.tmp")
            } else {
                mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "cache_file_path", "${mPlayService.cacheDir}/tmp.tmp")
            }

            // 无限制收流
            mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 0)
            // 设置无缓冲，这是播放器的缓冲区，有数据就播放
//            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
            mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", getMaxBufferSize())
            //配置成1是变声，0是不变声
            mPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 0)
            //把音频路径传给播放器
//            "ijkio:cache:ffio:"+
            mPlayer!!.dataSource = "ijkio:cache:ffio:$url"
            //准备
            mPlayer!!.prepareAsync()
            //设置状态为准备中
            mPlayState = MusicPlayAction.STATE_PREPARING
            if (mListener != null) {
                mListener!!.onPlayStateChanged(mPlayState)
            }
            //监听
            mPlayer!!.setOnPreparedListener(mOnPreparedListener)
            mPlayer!!.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
            mPlayer!!.setOnCompletionListener(mOnCompletionListener)
            mPlayer!!.setOnSeekCompleteListener(mOnSeekCompleteListener)
            mPlayer!!.setOnErrorListener(mOnErrorListener)
            mPlayer!!.setOnInfoListener(mOnInfoListener)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (playingMusic != null) {
            //设置封面图
            if (playingMusic!!.coverBitmap == null) {
                if (playingMusic!!.cover != null) {
                    fetchBitmapFromURLAsync()
                }
            }else{
                //更新通知栏
                NotificationHelper.get().showPlay(playingMusic, mPlayService.mMediaSessionManager.mediaSession)
                //更新
                mPlayService.mMediaSessionManager.updateMetaData(playingMusic)
                mPlayService.mMediaSessionManager.updatePlaybackState()
            }
        }
    }

    /**
     * 获取文件路径
     */
    private fun getFileName(url: String): String {
        val index = url.lastIndexOf(".")
        val startIndex = if (index > 15) index - 15 else index - 8
        val fileName = url.substring(startIndex, index).replace("/", "")
        return "${mPlayService.cacheDir}/$fileName"
    }

    /**
     * 封面加载
     */
    private fun fetchBitmapFromURLAsync() {
        getImageLoader()!!.load(playingMusic!!.cover, object : ImageLoaderCallBack {
            override fun onBitmapLoaded(bitmap: Bitmap?) {
                playingMusic!!.coverBitmap = bitmap
                //更新通知栏
                NotificationHelper.get().showPlay(playingMusic, mPlayService.mMediaSessionManager.mediaSession)
                //更新
                mPlayService.mMediaSessionManager.updateMetaData(playingMusic)
                mPlayService.mMediaSessionManager.updatePlaybackState()
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {}
        })
    }


    /**
     * 更新播放进度的显示，时间的显示
     */
    private fun updatePlayProgressShow() {
        if (isPlaying) {
            val currentPosition = mPlayer!!.currentPosition.toInt()
            if (mListener != null) {
                mListener!!.onProgressUpdate(currentPosition, duration.toInt())
            }
            if(!mNetAvailable && currentPosition + 100 >= mTargetProgress){
                pause()
            }
        }
//        MusicLogUtils.e("updatePlayProgressShow" + mPlayer!!.fileSize)
        // 每30毫秒更新一下显示的内容，注意这里时间不要太短，因为这个是一个循环
        // 经过测试，60毫秒更新一次有点卡，30毫秒最为顺畅
        handler!!.sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS_SHOW, 300)
    }

    /**
     * 音频准备好的监听器
     */
    private val mOnPreparedListener = IMediaPlayer.OnPreparedListener {
        /** 当音频准备好可以播放了，则这个方法会被调用   */
        if (isPreparing) {
            start()
        }
    }

    /**
     * 当音频播放结束的时候的监听器
     */
    private val mOnCompletionListener = IMediaPlayer.OnCompletionListener{
        /** 当音频播放结果的时候这个方法会被调用  */
        if (mPlayer!!.currentPosition + 1000 >= duration && duration != 0L) {
            mPlayState = MusicPlayAction.STATE_COMPLETE
            next()
        } else {
            mPlayState = MusicPlayAction.STATE_ERROR
        }
        if (mListener != null) {
            mListener!!.onPlayStateChanged(mPlayState)
        }
    }

    /**
     * 当音频缓冲的监听器
     */
    private val mOnBufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { _, i ->
        mBufferedProgress = i
        if (mListener != null && mNetAvailable) {
            if (i >= 99) mBufferedProgress = 100
            mListener!!.onBufferingUpdate(mBufferedProgress)
        }
        MusicLogUtils.e("updateBuffering$i")
    }

    /**
     * 跳转完成时的监听
     */
    private val mOnSeekCompleteListener = IMediaPlayer.OnSeekCompleteListener { }

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
    private val mOnErrorListener = IMediaPlayer.OnErrorListener { _, i, _ ->
        MusicLogUtils.e("MediaPlayer.onError$i")
        mPlayState = when (i) {
            -10000 -> MusicPlayAction.STATE_ERROR_AUDIO
            else -> MusicPlayAction.STATE_ERROR
        }
        if (mListener != null) {
            mListener!!.onPlayStateChanged(mPlayState)
        }
        false
    }

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
    private val mOnInfoListener = IMediaPlayer.OnInfoListener { _, i, _ ->
        MusicLogUtils.e("MediaPlayer.onInfo$i")
        when (i) {
            701 -> mPlayState = MusicPlayAction.STATE_BUFFERING
            702 -> {
                start()
                mPlayState = MusicPlayAction.STATE_PLAYING
            }
            10002->{
               mPlayState = if (!isPausing) {
                    start()
                    MusicPlayAction.STATE_PLAYING
                } else {
                    MusicPlayAction.STATE_PAUSE
                }
            }
            10009 -> if (isPlaying) {
                paused = false
                mPlayState = MusicPlayAction.STATE_PLAYING
            }
            else -> {
            }
        }
        if (mListener != null) {
            mListener!!.onPlayStateChanged(mPlayState)
        }
        false
    }

    /**
     * 是否正在播放
     *
     * @return true表示正在播放
     */
    val isPlaying: Boolean
        get() = mPlayer!!.isPlaying

    /**
     * 是否暂停
     *
     * @return true表示暂停
     */
    val isPausing: Boolean
        get() = mPlayState == MusicPlayAction.STATE_PAUSE

    /**
     * 是否正在准备中
     *
     * @return true表示正在准备中
     */
    val isPreparing: Boolean
        get() = mPlayState == MusicPlayAction.STATE_PREPARING

    /**
     * 是否正在缓冲
     *
     * @return true表示正在播放
     */
    val isBuffering: Boolean
        get() = mPlayState == MusicPlayAction.STATE_BUFFERING

    /**
     * 是否正在准备中
     *
     * @return true表示正在准备中
     */
    val isDefault: Boolean
        get() = mPlayState == MusicPlayAction.STATE_IDLE

    /**
     * 获取播放的进度
     *
     * @return long类型值
     */
    val currentPosition: Long
        get() = if (isPlaying || isPausing) {
            mPlayer!!.currentPosition
        } else {
            0
        }
    /**
     * 获取音量
     */
    //        if(mPlayer != null){
//            mPlayer.setVolume(volume,volume);
//        }
    /**
     * 设置音量
     *
     * @param volume
     */
    var volume: Int
        get() {
            val soundManager = AudioSoundManager(mPlayService)
            return soundManager.volume
        }
        set(volume) {
//        if(mPlayer != null){
//            mPlayer.setVolume(volume,volume);
//        }
            val soundManager = AudioSoundManager(mPlayService)
            soundManager.volume = volume
        }

    /**
     * 获取音量
     */
    val maxVolume: Int
        get() {
            val soundManager = AudioSoundManager(mPlayService)
            return soundManager.maxVolume
        }

    /**
     * 设置速率
     *
     * @param speed 0.5~2
     */
    fun setSpeed(speed: Float) {
        if (mPlayer != null) {
            mPlayer!!.setSpeed(speed)
        }
    }

    /**
     * 获取总时长
     */
    val duration: Long
        get() = if (mPlayer != null) {
            mPlayer!!.duration
        } else 0

    fun sendMessage(what: Int) {
        handler!!.sendEmptyMessage(what)
    }// 如果不是第一首，则还有上一首

    /**
     * 判斷是否有上一首音頻
     *
     * @return true表示有
     */
    val isHavePre: Boolean
        get() = if (audioMusics != null && audioMusics!!.isNotEmpty()) {
            playingPosition != 0
        } else {
            false
        }// 如果是最后一首，则切换回第一首// 如果不是最后一首，则还有下一首

    /**
     * 判斷是否有下一首音頻
     *
     * @return true表示有
     */
    val isHaveNext: Boolean
        get() = if (audioMusics != null && audioMusics!!.isNotEmpty()) {
            playingPosition != audioMusics!!.size - 1
        } else {
            false
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
    fun updatePlayingPosition() {
        var position = 0
        val id = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getLong(MusicConstant.MUSIC_ID, -1)
        if (audioMusics!!.isEmpty()) {
            return
        }
        for (i in audioMusics!!.indices) {
            val musicId = audioMusics!![i].id
            MusicLogUtils.e("PlayService----musicId----$musicId")
            if (musicId.toLong() == id) {
                position = i
                break
            }
        }
        playingPosition = position
        val musicId = audioMusics!![playingPosition].id.toLong()
        MusicSpUtils.getInstance(MusicConstant.SP_NAME).put(MusicConstant.MUSIC_ID, musicId)
    }

    /**
     * 设置播放进度监听器
     *
     * @param listener listener
     */
    fun setOnPlayEventListener(listener: HFPlayerEventListener?) {
        mListener = listener
    }
    /**-------------------------------------添加锁屏界面---------------------------------------- */
    /**
     * 打开锁屏页面
     * 不管是播放状态是哪一个，只要屏幕灭了到亮了，就展现这个锁屏页面
     * 有些APP限制了状态，比如只有播放时才走这个逻辑
     */
    private fun startLockAudioActivity() {
        if (!mIsLocked && isPlaying) {
//            Intent lockScreen = new Intent(this, LockAudioActivity.class);
//            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(lockScreen);
//            BaseConfig.INSTANCE.setLocked(true);
        }
    }

    companion object {
        /**
         * 更新播放进度的显示，时间的显示
         */
        private const val UPDATE_PLAY_PROGRESS_SHOW = 0
    }

    init {
        createIjkMediaPlayer()
        initQuitTimer()
    }
}