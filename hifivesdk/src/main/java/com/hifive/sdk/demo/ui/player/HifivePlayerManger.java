package com.hifive.sdk.demo.ui.player;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hifive.sdk.demo.ui.HifiveUpdateObservable;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;

import java.lang.ref.WeakReference;

/**
 * @ClassName HifivePlayerManger
 * @Description 播放器悬浮窗管理器
 * Created by huchao on 20/11/10.
 */
public class HifivePlayerManger{
    private HifivePlayerView mPlayerView;
    private static volatile HifivePlayerManger mInstance;
    private WeakReference<FrameLayout> mContainer;
    private HifivePlayerManger() {

    }
    public static HifivePlayerManger getInstance() {
        if (mInstance == null) {
            synchronized (HifivePlayerManger.class) {
                if (mInstance == null) {
                    mInstance = new HifivePlayerManger();
                }
            }
        }
        return mInstance;
    }
    //添加播放器view
    public HifivePlayerManger add(FragmentActivity activity) {
        return add(activity,0,0);
    }
    public HifivePlayerManger add(FragmentActivity activity,int marginTop,int marginBottom) {
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity,marginTop,marginBottom);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, HifiveDisplayUtils.dip2px(activity,470));
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
    //移除播放器view
    public HifivePlayerManger remove() {
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
        return this;
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
    public HifivePlayerManger attach(FragmentActivity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    public HifivePlayerManger attach(FrameLayout container) {
        if (container == null || mPlayerView == null) {
            mContainer = new WeakReference<>(container);
            return this;
        }
        if (mPlayerView.getParent() == container) {
            return this;
        }
        if (mPlayerView.getParent() != null) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        }
        mContainer = new WeakReference<>(container);
        container.addView(mPlayerView);
        return this;
    }
    /**
     * 呼应activity生命周期中onStop方法
     * activity停止是暂时移除播放器，实现播放器与activity绑定
     */
    public HifivePlayerManger detach(FragmentActivity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    public HifivePlayerManger detach(FrameLayout container) {
        if (mPlayerView != null && container != null && ViewCompat.isAttachedToWindow(mPlayerView)) {
            container.removeView(mPlayerView);
        }
        if (getContainer() == container) {
            mContainer = null;
        }
        return this;
    }
    //获取容器对象
    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }
}