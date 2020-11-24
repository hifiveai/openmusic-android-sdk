package com.hfliveplayer.sdk.demo.ui.player;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * @ClassName ViewSizeChangeAnimation
 * @Description view尺寸变化的动画
 * Created by huchao on 20/11/10.
 */
public  class HifiveViewChangeAnimation extends Animation {
    int initialHeight;
    int initialWidth;
    int targetWidth;
    View view;

    public HifiveViewChangeAnimation(View view, int targetWidth) {
        this.view = view;
        this.targetWidth = targetWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = initialHeight;
        view.getLayoutParams().width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.initialHeight = height;
        this.initialWidth = width;
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
