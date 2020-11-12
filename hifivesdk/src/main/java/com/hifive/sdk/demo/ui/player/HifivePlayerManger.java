package com.hifive.sdk.demo.ui.player;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hifive.sdk.demo.ui.HifiveMusicListDialogFragment;
import com.hifive.sdk.demo.ui.HifiveUpdateObservable;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;

import java.lang.ref.WeakReference;

/**
 * @ClassName HifivePlayerManger
 * @Description 播放器悬浮窗管理器
 * Created by huchao on 20/11/10.
 */
public class HifivePlayerManger implements HifiveDialogListener {
    private HifivePlayerView mPlayerView;
    private static volatile HifivePlayerManger mInstance;
    private WeakReference<FrameLayout> mContainer;
    private FragmentActivity activity;
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
        this.activity = activity;
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, HifiveDisplayUtils.dip2px(activity,470));
            mPlayerView.setLayoutParams(params);
            mPlayerView.setShowDialogListener(this);
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
                if(mPlayerView.audioUtils != null){
                    mPlayerView.audioUtils.onStop();
                    mPlayerView.audioUtils.release();
                    mPlayerView.audioUtils = null;
                }
                mPlayerView = null;
                HifiveDialogManageUtil.getInstance().setPlayMusic(null);//清空当前播放的歌曲
                //关闭歌曲列表相关弹窗
                if(HifiveDialogManageUtil.getInstance().isShow){
                    HifiveDialogManageUtil.getInstance().CloseDialog();
                }
            }
        });
        return this;
    }
    //初始化一个container容器装载播放器view
    private FrameLayout getActivityRoot(Activity activity) {
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
    public HifivePlayerManger attach(Activity activity) {
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
    public HifivePlayerManger detach(Activity activity) {
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
    private HifiveMusicListDialogFragment dialogFragment;
    //显示歌曲列表弹窗
    @Override
    public void onShowDialog() {
        if(dialogFragment == null){
            dialogFragment = new HifiveMusicListDialogFragment();
        }
        dialogFragment.show(activity.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
        if(mPlayerView != null) {
            mPlayerView.updateViewY();
        }
    }
    //隐藏歌词列表弹窗
    @Override
    public void onDismissDialog() {
        HifiveDialogManageUtil.getInstance().CloseDialog();
    }
}