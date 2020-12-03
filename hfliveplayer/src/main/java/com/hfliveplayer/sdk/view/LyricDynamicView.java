package com.hfliveplayer.sdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.hfliveplayer.sdk.model.HifiveMusicLyricDetailModel;
import com.hfliveplayer.sdk.util.HifiveDisplayUtils;

import java.util.List;


/**
 * 动态歌词view
 */
public class LyricDynamicView extends View {

    //歌词数组
    private List<HifiveMusicLyricDetailModel> lyricDetailModels;

    private boolean isChange;//判断歌词是否正在改变
    private int position = 0;//保留歌词下标，下次从下标开始查找歌词

    //Paint和颜色
    Paint leftTextPaint;
    Paint rightTextPaint;
    private int backgroundTextColor = Color.parseColor("#FFFFFF");
    private int foregroundTextColor = Color.parseColor("#F69643");

    //歌词内容
    private String leftLyric;
    private String rightLyric;
    private Canvas mCanvers;
    private int textSize;

    public LyricDynamicView(Context context) {
        super(context);
        init();
    }

    public LyricDynamicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricDynamicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LyricDynamicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, List<HifiveMusicLyricDetailModel> lyricDetailModels) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        textSize = HifiveDisplayUtils.sp2px(getContext(),20);
        // 创建画笔
        leftTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        leftTextPaint.setTextSize(textSize);
        leftTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        leftTextPaint.setColor(backgroundTextColor);
        leftTextPaint.setStrokeWidth(5f);

        rightTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        rightTextPaint.setTextSize(textSize);
        rightTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        rightTextPaint.setColor(backgroundTextColor);
        rightTextPaint.setStrokeWidth(5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//宽的测量大小，模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//高的测量大小，模式
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = widthSpecSize;   //定义测量宽，高(不包含测量模式),并设置默认值，查看View#getDefaultSize可知
        int h = heightSpecSize;
        //处理wrap_content的几种特殊情况
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            w = 400;  //单位是px
            h = textSize*4+10;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //只要宽度布局参数为wrap_content， 宽度给固定值200dp(处理方式不一，按照需求来)
            w = 400;
            //按照View处理的方法，查看View#getDefaultSize可知
            h = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            w = widthSpecSize;
            h = textSize*4+10;
        }
        //给两个字段设置值，完成最终测量
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvers = canvas;
        drawLyric();
    }

    private void drawLyric(){
        if(lyricDetailModels != null && leftLyric!= null && rightLyric != null) {
            mCanvers.drawText(leftLyric,0,textSize,leftTextPaint);
            mCanvers.drawText(rightLyric,getWidth()-rightTextPaint.measureText(rightLyric),textSize*2+40,rightTextPaint);
        }
    }

    public void setPlayProgress( int playProgress) {
        if (isChange)
            return;
        isChange = true;
        if(lyricDetailModels != null) {
            for (int i = position; i < lyricDetailModels.size(); i++) {
                if (playProgress <= lyricDetailModels.get(i).getStartTime()) {
                    String content = lyricDetailModels.get(i).getContent();
                    if (i % 2 == 0) {
                        leftLyric = content;
                        leftTextPaint.setColor(backgroundTextColor);
                        rightTextPaint.setColor(foregroundTextColor);
                    } else {
                        rightLyric = content;
                        leftTextPaint.setColor(foregroundTextColor);
                        rightTextPaint.setColor(backgroundTextColor);
                    }
                    postInvalidate();
                    position = i;
                    break;
                }
            }
        }
        isChange = false;
    }

    public void clearLyric(){
        isChange = false;
        position = 0;
        this.lyricDetailModels=null;
        this.leftLyric = "";
        this.rightLyric = "";
        drawLyric();
    }

    public void setLyricDetailModels(List<HifiveMusicLyricDetailModel> lyricDetailModels) {
        this.lyricDetailModels = lyricDetailModels;
        setPlayProgress(0);
    }

    public void setBackgroundTextColor(int backgroundTextColor) {
        this.backgroundTextColor = backgroundTextColor;
    }

    public void setForegroundTextColor(int foregroundTextColor) {
        this.foregroundTextColor = foregroundTextColor;
    }
}
