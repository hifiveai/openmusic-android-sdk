package com.hf.player.view;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;


import com.hf.playerkernel.manager.HFPlayerApi;
import com.hf.playerkernel.utils.DisplayUtils;

import java.lang.ref.WeakReference;

/**
 *
 */
public class HFPlayer {
    private HifivePlayerView mPlayerView;
    private static volatile HFPlayer mInstance;
    private WeakReference<FrameLayout> mContainer;
    public static boolean isAttached;
    public HFPlayerViewListener mListener;

    private HFPlayer() {

    }
    public static HFPlayer getInstance() {
        if (mInstance == null) {
            synchronized (HFPlayer.class) {
                if (mInstance == null) {
                    mInstance = new HFPlayer();
                }
            }
        }
        return mInstance;
    }

    //添加播放器view
    public HFPlayer showPlayer(FragmentActivity activity) {
        return showPlayer(activity,0,0);
    }
    public HFPlayer showPlayer(FragmentActivity activity, int marginTop, int marginBottom) {
        LifeFragmentManager.Companion.getInstances().addLifeListener(activity, "HFPlayer", null);
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity,marginTop,marginBottom);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, DisplayUtils.getScreenHeight(activity)/24 + DisplayUtils.getPlayerHeight(activity));
            mPlayerView.setLayoutParams(params);
            if (getContainer() != null) {
                getContainer().addView(mPlayerView);
            }
        }
        return this;
    }

    public HFPlayer setListener(HFPlayerViewListener listener){
        mListener = listener;
        return this;
    }

    /**
     * 播放歌曲
     * @param url
     */
    public HFPlayer playWithUrl(String url){
        if(mPlayerView != null)
        mPlayerView.playWithUrl(url);
        return this;
    }

    /**
     * 播放歌曲
     * @param title
     */
    public HFPlayer setTitle(String title){
        if(mPlayerView != null)
            mPlayerView.setTitle(title);
        return this;
    }

    /**
     * 播放歌曲
     * @param coverUrl
     */
    public HFPlayer setCover(String coverUrl){
        if(mPlayerView != null)
            mPlayerView.setCover(coverUrl);
        return this;
    }

    /**
     * 设置版本信息
     * @param isMajor
     */
    public HFPlayer setMajorVersion(Boolean isMajor){
        if(mPlayerView != null)
            mPlayerView.setMajorVersion(isMajor);
        return this;
    }

    /**
     * 移动播放器位置
     */
    public HFPlayer updateViewY(int marginBottom){
        if(mPlayerView != null)
            mPlayerView.updateViewY(marginBottom);
        return this;
    }

    /**
     * 移动播放器位置
     */
    public HFPlayer stopPlay(){
        if(mPlayerView != null){
            mPlayerView.stopPlay();
            mPlayerView.clear();
        }
        return this;
    }


    //移除播放器view,清除缓存数据
    public void destory() {
        removePlayer();
    }
    //移除播放器view
    public void removePlayer() {
        try {
            if (mPlayerView == null) {
                return;
            }
            mPlayerView.stopPlay();
            mPlayerView.clear();

            if (mPlayerView.getParent() != null) {
                ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            }
            mPlayerView = null;
            recyclePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //播放器资源回收
    private void recyclePlayer() {
        HFPlayerApi.with().stop();
        HFPlayerApi.relese();
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
    }
    //获取容器对象
    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }
}