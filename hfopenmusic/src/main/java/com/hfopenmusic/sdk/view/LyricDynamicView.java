package com.hfopenmusic.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.hfopenmusic.sdk.R;
import com.hifive.sdk.entity.HifiveMusicLyricDetailModel;

import java.util.List;


/**
 * 动态歌词view
 */
public class LyricDynamicView extends RelativeLayout {

    //歌词数组
    private List<HifiveMusicLyricDetailModel> lyricDetailModels;

    private boolean isChange;//判断歌词是否正在改变
    private int position = 0;//保留歌词下标，下次从下标开始查找歌词

    private int backgroundTextColor = Color.parseColor("#FFFFFF");
    private int foregroundTextColor = Color.parseColor("#F69643");

    //歌词内容
    private String leftLyric;
    private String rightLyric;

    private MarqueeTextView tv_lyric_left;
    private MarqueeTextView tv_lyric_right;

    public LyricDynamicView(Context context) {
        super(context);
        init(context);
    }

    public LyricDynamicView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    public LyricDynamicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View inflate = inflater.inflate(R.layout.hifive_layout_dynamiclyric, null);
        this.addView(inflate, layoutParams);

//        inflate(context, R.layout.hifive_window_ijkplayer, this);

        tv_lyric_left =  findViewById(R.id.tv_lyric_left);
        tv_lyric_right =  findViewById(R.id.tv_lyric_right);


    }

    /**
     * 设置歌词进度
     * @param playProgress
     */
    public void setPlayProgress( int playProgress) {
        if (getContext() == null)
            return;
        if (isChange)
            return;
        isChange = true;
        if(lyricDetailModels != null) {
            for (int i = position; i < lyricDetailModels.size(); i++) {
                if (playProgress <= lyricDetailModels.get(i).getStartTime()) {
                    final String content = lyricDetailModels.get(i).getContent();
                    if (i % 2 == 0) {
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!leftLyric.equals(content)){
                                    leftLyric = content;
                                    tv_lyric_left.setTextColor(backgroundTextColor);
                                    tv_lyric_left.setCurrent(false);
                                    tv_lyric_right.setTextColor(foregroundTextColor);
                                    tv_lyric_right.setCurrent(true);
                                    tv_lyric_left.setText(leftLyric);
                                }

                            }
                        });
                    } else {
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!rightLyric.equals(content)) {
                                    rightLyric = content;
                                    tv_lyric_left.setTextColor(foregroundTextColor);
                                    tv_lyric_left.setCurrent(true);
                                    tv_lyric_right.setTextColor(backgroundTextColor);
                                    tv_lyric_right.setCurrent(false);
                                    tv_lyric_right.setText(rightLyric);
                                }

                            }
                        });

                    }
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
        tv_lyric_left.setText(leftLyric);
        tv_lyric_right.setText(rightLyric);
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
