package com.hfliveplayer.sdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.util.HifiveDisplayUtils;

/**
 * 可拖动的LinearLayout
 */
public class DraggableLinearLayout extends LinearLayout {

    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private static final int TOUCH_TIME_THRESHOLD = 150;//点击事件的时长，
    private static final int LONG_TOUCH_TIME_THRESHOLD = 300;//长按事件的时长

    private int marginTop = 0;//滑动范围顶部的间距限制默认为0，
    private int marginBottom = 0;//滑动范围底部的间距限制默认为0，
    private long mLastTouchDownTime;
    private final MoveAnimator mMoveAnimator = new MoveAnimator();
    private int mScreenWidth;
    private int mScreenHeight;
    private float mPortraitY;

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public DraggableLinearLayout(Context context) {
        this(context, null);
    }

    public DraggableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    public void setDragView(View view,final OnClickEvent clickevent){
        view.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event == null) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        changeOriginalTouchParams(event);
                        updateSize();
                        mMoveAnimator.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isOnLongClickEvent()) {//长按时执行滑动
                            updateViewPosition(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isOnLongClickEvent()) {//长按结束执行滑动
                            clearPortraitY();
                            moveToEdge();
                        } else {
                            if (isOnClickEvent()) {//短按结束执行点击事件
                                clickevent.onClickEvent();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    //判断是否执行事件
    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    //判断是否执行长按事件
    protected boolean isOnLongClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime > LONG_TOUCH_TIME_THRESHOLD;
    }

    //滑动更新view位置
    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        int maxScrollY = getMaxScrollY();
        if (desY > maxScrollY) {
            desY = maxScrollY;
        } else {
            if (desY < marginTop) {
                desY = marginTop;
            }
        }
        //设置父布局H
        ((View)getParent()).setY(desY);
    }

    //获取最大可滑动距离
    public int getMaxScrollY() {
        //判断歌曲选择相关的弹窗是否打开
        if (HifiveDialogManageUtil.dialogFragments != null && HifiveDialogManageUtil.dialogFragments.size()>0) {
            return mScreenHeight - ((View)getParent()).getHeight() - HifiveDisplayUtils.dip2px(getContext(), 30) - HifiveDisplayUtils.getPlayerHeight(getContext());
        } else {
            return mScreenHeight - ((View)getParent()).getHeight() - marginBottom;
        }

    }

    //歌曲弹窗显示时更新最大可滑动距离
    public void updateViewY() {
        final int maxScrollY =  mScreenHeight - ((View)getParent()).getHeight() - HifiveDisplayUtils.dip2px(getContext(), 30) - HifiveDisplayUtils.getPlayerHeight(getContext());
        if (((View)getParent()).getY() > maxScrollY) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, maxScrollY - ((View)getParent()).getY());
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((View)getParent()).setY(maxScrollY);
                    ((View)getParent()).clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            ((View)getParent()).startAnimation(translateAnimation);
        }
    }

    //获取touch时初始值
    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = ((View)getParent()).getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    //获取宽高
    protected void updateSize() {
        ViewGroup viewGroup = (ViewGroup) getParent().getParent();
        if (viewGroup != null) {
            mScreenWidth = viewGroup.getWidth() - getWidth();
            mScreenHeight = viewGroup.getHeight();
        }
    }

    public void moveToEdge() {
        moveToEdge(isNearestLeft(), false);
    }

    public void moveToEdge(boolean isLeft, boolean isLandscape) {
        float moveDistance = isLeft ? MARGIN_EDGE : mScreenWidth - MARGIN_EDGE;
        float y = ((View)getParent()).getY();
        if (!isLandscape && mPortraitY != 0) {
            y = mPortraitY;
            clearPortraitY();
        }
        mMoveAnimator.start(moveDistance, Math.min(Math.max(0, y), mScreenHeight - getHeight()));
    }

    private void clearPortraitY() {
        mPortraitY = 0;
    }

    //是否靠左
    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        return getX() < middle;
    }

    //移动view的动画
    protected class MoveAnimator implements Runnable {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - ((View)getParent()).getY()) * progress;
            move(deltaX, deltaY);
            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    private void move(float deltaX, float deltaY) {
        //setX(getX() + deltaX);
        setX(0);
        ((View)getParent()).setY(((View)getParent()).getY() + deltaY);
    }

    public interface OnClickEvent {
        void onClickEvent();
    }
}
