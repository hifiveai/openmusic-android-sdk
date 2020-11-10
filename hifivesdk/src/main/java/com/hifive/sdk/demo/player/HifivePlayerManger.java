package com.hifive.sdk.demo.player;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.hifive.sdk.demo.ui.HifiveMusicListDialogFragment;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;

import java.lang.ref.WeakReference;
/**
 * @ClassName HifivePlayerManger
 * @Description 播放器悬浮窗管理器
 * Created by huchao on 20/11/10.
 */
public class HifivePlayerManger implements IFloatingView,MagnetViewListener {
    private HifiveMusicListDialogFragment dialogFragment;
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
    @Override
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
                mPlayerView = null;
            }
        });
        return this;
    }

    private void ensureFloatingView() {
        synchronized (this) {
            if (mPlayerView != null) {
                return;
            }
            mPlayerView = new HifivePlayerView(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START | Gravity.BOTTOM;
            params.setMargins(0, params.topMargin, params.rightMargin, HifiveDisplayUtils.dip2px(activity,470));
            mPlayerView.setLayoutParams(params);
            mPlayerView.setMagnetViewListener(this);
            addViewToWindow(mPlayerView);
        }
    }

    @Override
    public HifivePlayerManger add(FragmentActivity activity) {
        this.activity = activity;
        ensureFloatingView();
        return this;
    }

    @Override
    public HifivePlayerManger attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    @Override
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

    @Override
    public HifivePlayerManger detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    @Override
    public HifivePlayerManger detach(FrameLayout container) {
        if (mPlayerView != null && container != null && ViewCompat.isAttachedToWindow(mPlayerView)) {
            container.removeView(mPlayerView);
        }
        if (getContainer() == container) {
            mContainer = null;
        }
        return this;
    }
    private void addViewToWindow(final View view) {
        if (getContainer() == null) {
            return;
        }
        getContainer().addView(view);
    }

    private FrameLayout getContainer() {
        if (mContainer == null) {
            return null;
        }
        return mContainer.get();
    }
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
    @Override
    public void onClick() {
        if(dialogFragment != null && dialogFragment.getDialog() != null){
            if(dialogFragment.getDialog().isShowing()){
                HifiveDialogManageUtil.getInstance().CloseDialog();
            }else{
                dialogFragment.show(activity.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
            }
        }else{
            dialogFragment = new HifiveMusicListDialogFragment();
            dialogFragment.show(activity.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
        }



    }
}