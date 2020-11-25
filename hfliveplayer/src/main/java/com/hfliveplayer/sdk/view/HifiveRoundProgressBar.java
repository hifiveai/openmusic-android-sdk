package com.hfliveplayer.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hfliveplayer.sdk.R;


public class HifiveRoundProgressBar extends View {
    // 画笔对象的引用
    private Paint paint;
    // 圆环的颜色
    private int roundColor;
    // 圆环进度的颜色
    private int roundProgressColor;
    // 圆环的宽度
    private float roundWidth;
    // 最大进度
    private int max;
    // 当前进度
    private int progress;
    /**
     * 构造方法
     */
    public HifiveRoundProgressBar(Context context) {
        this(context, null);
    }

    public HifiveRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HifiveRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        // 获取自定义属性和默认值
        roundColor = mTypedArray.getColor(
                R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(
                R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(
                R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        mTypedArray.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画最外层的大圆环
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); // 圆环的半径
        paint.setColor(roundColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环
        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 270, 360 * progress / max, false, paint); // 根据进度画圆弧
    }
    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max > 0) {
            this.max = max;
        }
    }
    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }
}
