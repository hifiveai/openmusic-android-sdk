package com.hf.playerkernel.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import com.hf.playerkernel.R;
import com.hf.playerkernel.config.MusicPlayAction;
import com.hf.playerkernel.model.AudioBean;
import com.hf.playerkernel.receiver.NotificationStatusBarReceiver;
import com.hf.playerkernel.service.PlayService;


public class NotificationHelper {


    private PlayService playService;
    private NotificationManager notificationManager;
    private boolean isOpenNotification;
    private static final int NOTIFICATION_ID = 0x123;

    public static NotificationHelper get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static NotificationHelper instance = new NotificationHelper();
    }

    private NotificationHelper() {

    }

    /**
     * 1.创建一个NotificationManager的引用
     * @param playService           PlayService对象
     * @param isOpenNotification
     */
    public void init(PlayService playService, boolean isOpenNotification) {
        this.playService = playService;
        this.isOpenNotification = isOpenNotification;
        notificationManager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 开始播放
     * @param music             music
     */
    public void showPlay(AudioBean music,MediaSessionCompat.Token token) {
        if (music == null || !isOpenNotification) {
            return;
        }
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music, token, true));
        //这个方法是启动Notification到前台
    }


    /**
     * 暂停
     * @param music             music
     */
    public void showPause(AudioBean music,MediaSessionCompat.Token token) {
        //这个方法是停止Notification
        if (music == null || !isOpenNotification) {
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music,token, false));
    }


    /**
     * 结束所有的
     */
    public void cancelAll() {
        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, AudioBean music, MediaSessionCompat.Token token, boolean isPlaying) {
//        Intent intent = new Intent(this, MusicPlaya.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//

        int playButtonResId = isPlaying
                ? R.drawable.ic_music_player_pause : R.drawable.ic_music_player_play;
        String title = isPlaying ? "Pause" : "Play";

        NotificationUtils.isVibration = false;
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils
//                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setTicker(music.getTitle())
                .addAction(R.drawable.ic_music_player_prev,
                        "Previous",
                        getReceiverPendingIntent(context, MusicPlayAction.TYPE_PRE,1))
                .addAction(playButtonResId, title,
                        getReceiverPendingIntent(context, MusicPlayAction.TYPE_START_PAUSE,3))
                .addAction(R.drawable.ic_music_player_next,
                        "Next",
                        getReceiverPendingIntent(context, MusicPlayAction.TYPE_NEXT,2))
                .setOngoing(isPlaying);


        return notificationUtils.getNotification(music.getTitle(), "测试测试", R.drawable.ic_music_player_small_icon);
    }


    /**
     * 设置自定义通知栏布局
     * @param context                   上下文
     * @param music
     * @return                          RemoteViews
     */
    private RemoteViews getCustomViews(Context context, AudioBean music, boolean isPlaying) {
        String title = music.getTitle();
        String subtitle = "";
        Bitmap cover = null;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);
        if (cover != null) {
            remoteViews.setImageViewBitmap(R.id.iv_image, cover);
        } else {
            remoteViews.setImageViewResource(R.id.iv_image, R.drawable.default_cover);
        }
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_artist, subtitle);
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_pause_normal);
        }else {
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_play_normal);
        }

        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getReceiverPendingIntent(context, MusicPlayAction.TYPE_PRE,1));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getReceiverPendingIntent(context, MusicPlayAction.TYPE_NEXT,2));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getReceiverPendingIntent(context, MusicPlayAction.TYPE_START_PAUSE,3));
//        // 设置 点击通知栏的根容器时要执行的意图
//        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(context));
        return remoteViews;
    }

    private PendingIntent getActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, null);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getReceiverPendingIntent(Context context, String type , int code) {
        Intent intent = new Intent(NotificationStatusBarReceiver.ACTION_STATUS_BAR);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
