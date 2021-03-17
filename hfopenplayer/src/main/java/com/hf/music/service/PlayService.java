package com.hf.music.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.hf.music.config.MusicPlayAction;
import com.hf.music.config.MusicConstant;
import com.hf.music.config.PlayModeEnum;
import com.hf.music.inter.EventCallback;
import com.hf.music.inter.OnPlayerEventListener;
import com.hf.music.manager.AudioFocusManager;
import com.hf.music.manager.AudioSoundManager;
import com.hf.music.manager.MediaSessionManager;
import com.hf.music.model.AudioBean;
import com.hf.music.playback.IjkPlayback;
import com.hf.music.receiver.AudioBroadcastReceiver;
import com.hf.music.receiver.AudioEarPhoneReceiver;
import com.hf.music.manager.HFPlayer;
import com.hf.music.receiver.NetworkReceiver;
import com.hf.music.tool.NetworkCallbackImpl;
import com.hf.music.tool.QuitTimerHelper;
import com.hf.music.utils.MusicLogUtils;
import com.hf.music.utils.NotificationHelper;



/**
 * Service就是用来在后台完成一些不需要和用户交互的动作
 */
public class PlayService extends Service {

    public IjkPlayback playback;

    /**
     * 允许与媒体控制器、音量键、媒体按钮和传输控件交互
     */
    public MediaSessionManager mMediaSessionManager;
    /**
     * 捕获/丢弃音乐焦点处理
     */
    public AudioFocusManager mAudioFocusManager;
    /**
     * 是否锁屏了，默认是false
     */
    private boolean mIsLocked = false;
    /**
     * 是否又网络
     */
    private boolean mNetAvailable = true;
    /**
     * 来电/耳机拔出时暂停播放
     * 在播放时调用，在暂停时注销
     */
    public final AudioEarPhoneReceiver mNoisyReceiver = new AudioEarPhoneReceiver();
    /**
     * 其他广播
     * 比如：屏幕灭了后再次亮了，会显示锁屏页面
     * 这个在onCreate中创建，在onDestroy中销毁
     */
    public final AudioBroadcastReceiver mAudioReceiver = new AudioBroadcastReceiver();

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     *
     * @param intent intent
     * @return IBinder对象
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }


    /**
     * 比如，广播，耳机声控，通知栏广播，来电或者拔下耳机广播开启服务
     *
     * @param context 上下文
     * @param type    类型
     */
    public static void startCommand(Context context, String type) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(type);
        context.startService(intent);
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }


    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.get().init(this, HFPlayer.getIsOpenNotification());
        createIjkPlayer();
        initMediaSessionManager();
        initAudioFocusManager();
        initEarPhoneBroadcastReceiver();
        initNetWorkReceiver();
        initAudioBroadcastReceiver();
    }

    /**
     * 服务在销毁时调用该方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        playback.release();
        //放弃音频焦点
        mAudioFocusManager.abandonAudioFocus();
        mMediaSessionManager.release();
        //注销广播接收者
        unregisterReceiver(mAudioReceiver);
        //结束notification通知
        NotificationHelper.get().cancelAll();
        //relese
        HFPlayer.relese();
    }


    /**
     * 每次通过startService()方法启动Service时都会被回调。
     *
     * @param intent  intent
     * @param flags   flags
     * @param startId startId
     * @return onStartCommand方法返回值作用：
     * START_STICKY：粘性，service进程被异常杀掉，系统重新创建进程与服务，会重新执行onCreate()、onStartCommand(Intent)
     * START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * START_NOT_STICKY：非粘性，Service进程被异常杀掉，系统不会自动重启该Service。
     * START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                //上一首
                case MusicPlayAction.TYPE_PRE:
                    playback.prev();
                    break;
                //下一首
                case MusicPlayAction.TYPE_NEXT:
                    playback.next();
                    break;
                //播放或暂停
                case MusicPlayAction.TYPE_START_PAUSE:
                    playback.playPause();
                    break;
                //添加锁屏界面
                case MusicConstant.LOCK_SCREEN_ACTION:
                    MusicLogUtils.e("PlayService" + "---LOCK_SCREEN" + mIsLocked);
                    break;
                //当屏幕灭了，添加锁屏页面
                case Intent.ACTION_SCREEN_OFF:
                    startLockAudioActivity();
                    MusicLogUtils.e("PlayService" + "---当屏幕灭了");
                    break;
                case Intent.ACTION_SCREEN_ON:
                    MusicLogUtils.e("PlayService" + "---当屏幕亮了");
                    break;
                default:
                    break;
            }
        }
        return START_NOT_STICKY;
    }


    /**
     * 创建IjkMediaPlayer对象
     */
    private void createIjkPlayer() {
        playback = new IjkPlayback(this);
    }


    /**
     * 允许与媒体控制器、音量键、媒体按钮和传输控件交互。
     * 播放器除了播放了音乐之外什么都没做，就可以分别在任务管理、锁屏、负一屏控制我的播放器
     */
    private void initMediaSessionManager() {
        mMediaSessionManager = new MediaSessionManager(this, HFPlayer.getIsOpenNotification());
    }

    /**
     * 捕获/丢弃音乐焦点处理
     */
    private void initAudioFocusManager() {
        mAudioFocusManager = new AudioFocusManager(this);
    }


    /**
     * 初始化耳机插入和拔出监听
     */
    private void initEarPhoneBroadcastReceiver() {
        //这块直接在清单文件注册
    }

    /**
     * 网络变化
     */
    private void initNetWorkReceiver() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
//                @Override
//                public void onAvailable(Network network) {
//                    super.onAvailable(network);
//                    mNetAvailable = true;
//                    MusicLogUtils.i("网络已连接");
//                }
//
//                @Override
//                public void onLost(Network network) {
//                    super.onLost(network);
//                    mNetAvailable = false;
//                    MusicLogUtils.i("网络已断开");
//                    sendMessage(MusicPlayAction.STATE_PAUSE);
//                }
//
//                @Override
//                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
//                    super.onCapabilitiesChanged(network, networkCapabilities);
//                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
//                        mNetAvailable = true;
//                        sendMessage(MusicPlayAction.STATE_PLAYING);
//                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                            MusicLogUtils.i("wifi已连接");
//                        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                            MusicLogUtils.i("数据流量已经连接");
//                        } else {
//                            MusicLogUtils.i("其他网络");
//                        }
//                    }
//                }
//
//            };
//            NetworkRequest.Builder builder = new NetworkRequest.Builder();
//            NetworkRequest request = builder.build();
//            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connMgr != null) {
//                connMgr.registerNetworkCallback(request, networkCallback);
//            }
//        }
        NetworkCallbackImpl networkCallback = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr != null) {
                connMgr.registerNetworkCallback(request, networkCallback);
            }
        }

    }


    /**
     * 初始化IntentFilter添加action意图
     * 主要是监听屏幕亮了与灭了
     */
    private void initAudioBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        //来电/耳机
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        //锁屏
        filter.addAction(MusicConstant.LOCK_SCREEN_ACTION);
        //当屏幕灭了
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //当屏幕亮了
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mAudioReceiver, filter);
    }


    /**
     * 退出时候调用
     */
    public void quit() {
        // 先停止播放
        playback.stop();
        // 移除定时器
        QuitTimerHelper.getInstance().stop();
        // 当另一个组件（如 Activity）通过调用 startService() 请求启动服务时，系统将调用onStartCommand。
        // 一旦执行此方法，服务即会启动并可在后台无限期运行。 如果自己实现此方法，则需要在服务工作完成后，
        // 通过调用 stopSelf() 或 stopService() 来停止服务。
        stopSelf();
    }

    /**-------------------------------------添加锁屏界面----------------------------------------*/


    /**
     * 打开锁屏页面
     * 不管是播放状态是哪一个，只要屏幕灭了到亮了，就展现这个锁屏页面
     * 有些APP限制了状态，比如只有播放时才走这个逻辑
     */
    private void startLockAudioActivity() {
        if (!mIsLocked && playback.isPlaying()) {
//            Intent lockScreen = new Intent(this, LockAudioActivity.class);
//            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(lockScreen);
//            BaseConfig.INSTANCE.setLocked(true);
        }
    }

}
