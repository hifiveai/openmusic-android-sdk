package com.hf.player.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hf.player.utils.GlideUtil;
import com.hf.playerkernel.inter.HFPlayerMediaCallback;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hf.playerkernel.model.AudioBean;
import com.hf.playerkernel.utils.DisplayUtils;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * 播放器UI
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
        return showPlayer(activity, 0, 0);
    }

    public HFPlayer showPlayer(FragmentActivity activity, int marginTop, int marginBottom) {
        LifeFragmentManager.Companion.getInstances().addLifeListener(activity, "HFPlayer", null);
        synchronized (this) {
            if (mPlayerView != null) {
                return this;
            }
            mPlayerView = new HifivePlayerView(activity);
            mPlayerView.setMargin(marginTop, marginBottom);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, DisplayUtils.getScreenHeight(activity) / 24 + DisplayUtils.getPlayerHeight(activity));
            mPlayerView.setLayoutParams(params);
            if (getContainer() != null) {
                getContainer().addView(mPlayerView);
            }
        }
        return this;
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public HFPlayer setListener(HFPlayerViewListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置媒体监听
     *
     * @param callback
     * @return
     */
    public HFPlayer setMediaCallback(HFPlayerMediaCallback callback) {
        HFPlayerApi.setMediaCallback(callback);
        return this;
    }


    /**
     * 播放歌曲
     *
     * @param title    音乐标题
     * @param url      音乐链接
     * @param coverUrl 音乐封面
     */
    public HFPlayer playMusic(String title, String url, String coverUrl) {
        if (mPlayerView != null)
            mPlayerView.setTitle(title)
                    .setCover(coverUrl)
                    .playWithUrl(url);
        return this;
    }

    /**
     * 播放歌曲
     *
     * @param musicID  音乐id
     * @param title    音乐标题
     * @param url      音乐链接
     * @param coverUrl 音乐封面
     * @param duration 音乐时长
     * @param album    音乐专辑
     * @param artist   音乐作者
     * @return
     */
    public HFPlayer playMusic(String musicID, String title, String url, String coverUrl, int duration, String album, String artist) {
        if (mPlayerView != null) {
            AudioBean musicInfo = new AudioBean();
            musicInfo.setId(musicID);
            musicInfo.setTitle(title);
            musicInfo.setPath(url);
            musicInfo.setCover(coverUrl);
            musicInfo.setDuration(duration*1000);
            musicInfo.setAlbum(album);
            musicInfo.setArtist(artist);
            HFPlayerApi.with().setPlayingMusic(musicInfo);

            mPlayerView.setTitle(title)
                    .setCover(coverUrl)
                    .playWithUrl(url);
        }
        return this;
    }


    /**
     * 设置版本信息
     *
     * @param isMajor
     */
    public HFPlayer setMajorVersion(Boolean isMajor) {
        if (mPlayerView != null)
            mPlayerView.setMajorVersion(isMajor);
        return this;
    }

    /**
     * 移动播放器位置
     */
    public HFPlayer setMarginBottom(int marginBottom) {
        if (mPlayerView != null)
            mPlayerView.setMarginBottom(marginBottom);
        return this;
    }

    /**
     * 停止播放
     */
    public HFPlayer stopPlay() {
        if (mPlayerView != null) {
            mPlayerView.stopPlay();
        }
        return this;
    }

    /**
     * 收起播放器
     */
    public HFPlayer foldPlayer() {
        if (mPlayerView != null) {
            mPlayerView.animationOFF();
        }
        return this;
    }

    /**
     * 展开播放器
     */
    public HFPlayer expandedPlayer() {
        if (mPlayerView != null) {
            mPlayerView.animationOpen();
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