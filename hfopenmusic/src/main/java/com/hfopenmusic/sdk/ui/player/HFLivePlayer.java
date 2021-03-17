package com.hfopenmusic.sdk.ui.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hfopenmusic.sdk.ui.HifiveUpdateObservable;
import com.hfopenmusic.sdk.util.HifiveDialogManageUtil;
import com.hfopenmusic.sdk.util.HifiveDisplayUtils;

import java.lang.ref.WeakReference;

/**
 * @ClassName HifivePlayerManger
 * @Description 播放器悬浮窗管理器
 * Created by huchao on 20/11/10.
 */
public class HFLivePlayer {
    private HifivePlayerView mPlayerView;
    private static volatile HFLivePlayer mInstance;
    private WeakReference<FrameLayout> mContainer;
    public static boolean isAttached;

    /**--------------播放类型----------*/
    /** BGM音乐播放*/
    public static final String TYPE_TRAFFIC = "Traffic";
    /** 音视频作品BGM音乐播放*/
    public static final String TYPE_UGC = "UGC";
    /** K歌音乐播放 */
    public static final String TYPE_K = "K";
    public static String listenType = TYPE_TRAFFIC;

    private HFLivePlayer() {

    }
    public static HFLivePlayer getInstance() {
        if (mInstance == null) {
            synchronized (HFLivePlayer.class) {
                if (mInstance == null) {
                    mInstance = new HFLivePlayer();
                }
            }
        }
        return mInstance;
    }
    //设置音乐授权类型
    public HFLivePlayer setListenType(String type) {
        listenType = type;
        return this;
    }
    //添加播放器view
    public HFLivePlayer showPlayer(FragmentActivity activity) {
        return showPlayer(activity,0,0);
    }
    public HFLivePlayer showPlayer(FragmentActivity activity, int marginTop, int marginBottom) {
        LifeFragmentManager.Companion.getInstances().addLifeListener(activity, "HFLivePlayer", null);
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity,marginTop,marginBottom);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, HifiveDisplayUtils.getScreenHeight(activity)/24 + HifiveDisplayUtils.getPlayerHeight(activity));
            mPlayerView.setLayoutParams(params);
            //初始化被观察者
            if(HifiveDialogManageUtil.getInstance().updateObservable == null){
                HifiveDialogManageUtil.getInstance().updateObservable = new HifiveUpdateObservable();
            }
            //将播放器view添加为观察者
            HifiveDialogManageUtil.getInstance().updateObservable.addObserver(mPlayerView);
            if (getContainer() != null) {
                getContainer().addView(mPlayerView);
            }
        }
        return this;
    }

    //移除播放器view,清除缓存数据
    public void destory() {
        HifiveDialogManageUtil.getInstance().clearData();
        removePlayer();
    }
    //移除播放器view
    public void removePlayer() {
        try {
            if (mPlayerView == null) {
                return;
            }
            if (ViewCompat.isAttachedToWindow(mPlayerView) && getContainer() != null) {
                getContainer().removeView(mPlayerView);
            }
            recyclePlayer();
            mPlayerView = null;
            HifiveDialogManageUtil.getInstance().setPlayMusic(null);//清空当前播放的歌曲
            HifiveDialogManageUtil.getInstance().CloseDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //播放器资源回收
    private void recyclePlayer() {
         mPlayerView.cleanTimer();
        //回收播放器资源
        if(mPlayerView.playerUtils != null){
            mPlayerView.playerUtils.onStop();
            mPlayerView.playerUtils.release();
            mPlayerView.playerUtils = null;
        }
    }
    //初始化一个container容器装载播放器view
    private FrameLayout getActivityRoot(FragmentActivity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 呼应activity生命周期中onStart方法
     * activity启动时重新添加播放器，实现播放器与activity绑定
     */
    public void attach(FragmentActivity activity) {
        isAttached = true;
        attach(getActivityRoot(activity));
    }

    public void attach(FrameLayout container) {
        if (container == null || mPlayerView == null) {
            mContainer = new WeakReference<>(container);
            return;
        }
        if (mPlayerView.getParent() == container) {
            return;
        }
        if (mPlayerView.getParent() != null) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        }
        mContainer = new WeakReference<>(container);
        container.addView(mPlayerView);
        //注册
        registReceiver(container.getContext());
    }
    /**
     * 呼应activity生命周期中onStop方法
     * activity停止是暂时移除播放器，实现播放器与activity绑定
     */
    public void detach(FragmentActivity activity) {
        isAttached = false;
        detach(getActivityRoot(activity));
    }

    public void detach(FrameLayout container) {
        if (mPlayerView != null && container != null && ViewCompat.isAttachedToWindow(mPlayerView)) {
            container.removeView(mPlayerView);
        }
        if (getContainer() == container) {
            mContainer = null;
        }
        //反注册
        try {
            container.getContext().unregisterReceiver(mReceiver);
        } catch (Exception e) {
        } finally {
            mReceiver = null;
        }

    }
    //获取容器对象
    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }



    private void registReceiver(Context context){
        //注册广播接收器，给广播接收器添加可以接收的广播Action
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BUTTON);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        context.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    //耳机拔出时，可以暂停播放
                    if (mPlayerView != null){
                        mPlayerView.stopPlay();
                    }
                    break;
            }
        }
    };
}