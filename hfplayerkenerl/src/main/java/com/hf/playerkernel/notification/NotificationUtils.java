package com.hf.playerkernel.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.hf.playerkernel.R;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hf.playerkernel.model.AudioBean;
import com.hf.playerkernel.notification.imageloader.ImageLoaderCallBack;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import static androidx.core.app.NotificationCompat.VISIBILITY_SECRET;

/**
 * 通知栏工具类
 */
public class NotificationUtils extends ContextWrapper {


    public static final String CHANNEL_ID = "hifive";
    private static final String CHANNEL_NAME = "hifive";
    public static boolean isVibration = false;
    private NotificationManager mManager;

    public NotificationUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        //第一个参数：channel_id
        //第二个参数：channel_name
        //第三个参数：设置通知重要性级别
        //注意：该级别必须要在 NotificationChannel 的构造函数中指定，总共要五个级别；
        //范围是从 NotificationManager.IMPORTANCE_NONE(0) ~ NotificationManager.IMPORTANCE_HIGH(4)
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.canBypassDnd();//是否绕过请勿打扰模式
        channel.enableLights(true);//闪光灯
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
        channel.canShowBadge();//桌面launcher的消息角标
        channel.enableVibration(isVibration);//是否允许震动
        channel.getAudioAttributes();//获取系统通知响铃声音的配置
        channel.getGroup();//获取通知取到组
        channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
        channel.shouldShowLights();//是否会有灯光
        getManager().createNotificationChannel(channel);
    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    /**
     * 清空所有的通知
     */
    public void clearNotification() {
        getManager().cancelAll();
    }

    /**
     * 获取Notification
     *
     * @param musicInfo   music
     */
    public Notification getNotification(AudioBean musicInfo) {
        return  getNotificationCompat(musicInfo).build();
    }


    /**
     * 调用该方法可以发送通知
     *
     * @param notifyId notifyId
     * @param musicInfo    music
     */
    public void sendNotification(int notifyId, AudioBean musicInfo) {
        Notification build = getNotificationCompat(musicInfo).build();
        getManager().notify(notifyId, build);
    }


    private NotificationCompat.Builder getNotificationCompat(AudioBean musicInfo) {
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setStyle(new  androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(token)
                        .setShowCancelButton(true)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setSmallIcon(R.drawable.ic_music_player_small_icon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(musicInfo.getTitle())
                .setTicker(musicInfo.getTitle())
                .setContentText(musicInfo.getAlbum())
                .setPriority(priority)
                .setOnlyAlertOnce(onlyAlertOnce)
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                this, PlaybackStateCompat.ACTION_STOP))
                .setOngoing(ongoing);
        if (mActions.size() > 0) {
            for (int i = 0; i < mActions.size(); i++) {
                builder.addAction(mActions.get(i));
            }
        }

        //设置封面图
        if(musicInfo.getCoverBitmap() == null && musicInfo.getCover() == null){
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_cover));
        }else{
            builder.setLargeIcon(musicInfo.getCoverBitmap());
        }

        if (intent != null) {
            builder.setContentIntent(intent);
        }
        if (when != 0) {
            builder.setWhen(when);
        }
        if (sound != null) {
            builder.setSound(sound);
        }
        if (defaults != 0) {
            builder.setDefaults(defaults);
        }
        //点击自动删除通知
        builder.setAutoCancel(true);
        return builder;
    }

    private boolean ongoing = false;
    private PendingIntent intent = null;
    private int priority = Notification.PRIORITY_DEFAULT;
    private boolean onlyAlertOnce = false;
    private long when = 0;
    private Uri sound = null;
    private int defaults = 0;
    private long[] pattern = null;
    private ArrayList<NotificationCompat.Action> mActions = new ArrayList<>();
    private MediaSessionCompat.Token token;

    /**
     * 让通知左右滑的时候是否可以取消通知
     *
     * @param ongoing 是否可以取消通知
     * @return
     */
    public NotificationUtils setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
        return this;
    }

    /**
     * 设置内容点击
     *
     * @param intent intent
     * @return
     */
    public NotificationUtils setContentIntent(PendingIntent intent) {
        this.intent = intent;
        return this;
    }


    /**
     * 设置优先级
     * 注意：
     * Android 8.0以及上，在 NotificationChannel 的构造函数中指定，总共要五个级别；
     * Android 7.1（API 25）及以下的设备，还得调用NotificationCompat 的 setPriority方法来设置
     *
     * @param priority 优先级，默认是Notification.PRIORITY_DEFAULT
     * @return
     */
    public NotificationUtils setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * 是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
     *
     * @param onlyAlertOnce 是否只提示一次，默认是false
     * @return
     */
    public NotificationUtils setOnlyAlertOnce(boolean onlyAlertOnce) {
        this.onlyAlertOnce = onlyAlertOnce;
        return this;
    }

    /**
     * 设置通知时间，默认为系统发出通知的时间，通常不用设置
     *
     * @param when when
     * @return
     */
    public NotificationUtils setWhen(long when) {
        this.when = when;
        return this;
    }

    /**
     * 设置sound
     *
     * @param sound sound
     * @return
     */
    public NotificationUtils setSound(Uri sound) {
        this.sound = sound;
        return this;
    }


    /**
     * 设置默认的提示音
     *
     * @param defaults defaults
     * @return
     */
    public NotificationUtils setDefaults(int defaults) {
        this.defaults = defaults;
        return this;
    }

    /**
     * 自定义震动效果
     *
     * @param pattern pattern
     * @return
     */
    public NotificationUtils setVibrate(long[] pattern) {
        this.pattern = pattern;
        return this;
    }


    /**
     * addAction
     *
     * @param icon
     * @param title
     * @param intent
     */
    public NotificationUtils addAction(int icon, String title, PendingIntent intent) {
        mActions.add(new NotificationCompat.Action(icon, title, intent));
        return this;
    }

    /**
     * 设置Token
     *
     * @param token
     * @return
     */
    public NotificationUtils setToken(MediaSessionCompat.Token token) {
        token = token;
        return this;
    }


}

