package com.hfliveplayer.sdk.ui.player;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hfliveplayer.sdk.ui.HifiveUpdateObservable;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.util.HifiveDisplayUtils;

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
    //添加播放器view
    public HFLivePlayer add(FragmentActivity activity) {
        return add(activity,0,0);
    }
    public HFLivePlayer add(FragmentActivity activity, int marginTop, int marginBottom) {
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity,marginTop,marginBottom);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, HifiveDisplayUtils.dip2px(activity,30) + HifiveDisplayUtils.getPlayerHeight(activity));
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
        remove();
    }
    //移除播放器view
    public void remove() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
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
            }
        });
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
    }
    /**
     * 呼应activity生命周期中onStop方法
     * activity停止是暂时移除播放器，实现播放器与activity绑定
     */
    public void detach(FragmentActivity activity) {
        detach(getActivityRoot(activity));
    }

    public void detach(FrameLayout container) {
        if (mPlayerView != null && container != null && ViewCompat.isAttachedToWindow(mPlayerView)) {
            container.removeView(mPlayerView);
        }
        if (getContainer() == container) {
            mContainer = null;
        }
    }
    //获取容器对象
    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }
}