package com.hifive.sdk.demo.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;

/**
 * @ClassName HifivePlayerView
 * @Description 播放器悬浮窗view
 * Created by huchao on 20/11/10.
 */
public class HifivePlayerView extends FrameLayout {

    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private MagnetViewListener mMagnetViewListener;
    private static final int TOUCH_TIME_THRESHOLD = 150;//点击事件的时长，
    private static final int LONG_TOUCH_TIME_THRESHOLD = 500;//长按事件的时长
    private long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;
    protected int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;
    private boolean isNearestLeft = true;
    private float mPortraitY;
    private FrameLayout fl_lyric;
    private TextView tv_lyric_detail;
    private TextView tv_lyric_time;
    private ImageView iv_music;
    private TextView tv_music_info;
    private TextView tv_accompany;
    private TextView tv_lyric;
    private ImageView iv_last;
    private ImageView iv_play;
    private ImageView iv_next;
    private ImageView iv_back;
    private boolean isShowLyric;//是否已显示歌词
    private boolean isShowAccompany;//是否是伴奏模式
    private boolean isPlay;//是否正在播放
    public void setMagnetViewListener(MagnetViewListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public HifivePlayerView(@NonNull Context context) {
        this(context, null);
    }

    public HifivePlayerView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HifivePlayerView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        inflate(context, R.layout.hifive_window_ijkplayer, this);
        initView();
        initEvent();

    }
    private void init() {
        mMoveAnimator = new MoveAnimator();
        mStatusBarHeight = HifiveDisplayUtils.getStatusBarHeight(getContext());
        setClickable(true);
    }
    //初始化view
    private void initView() {
        fl_lyric =  findViewById(R.id.fl_lyric);
        tv_lyric_detail =  findViewById(R.id.tv_lyric_detail);
        tv_lyric_detail.setMovementMethod(new ScrollingMovementMethod());
        tv_lyric_time =  findViewById(R.id.tv_lyric_time);
        iv_music =  findViewById(R.id.iv_music);
        tv_music_info =  findViewById(R.id.tv_music_info);
        tv_accompany =  findViewById(R.id.tv_accompany);
        tv_lyric =  findViewById(R.id.tv_lyric);
        iv_last =  findViewById(R.id.iv_last);
        iv_play =  findViewById(R.id.iv_play);
        iv_next =  findViewById(R.id.iv_next);
        iv_back =  findViewById(R.id.iv_back);
    }
    //初始化点击事件
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        iv_music.setOnTouchListener(new OnTouchListener() {
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
                        if(isOnLongClickEvent()) {
                            updateViewPosition(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isOnLongClickEvent()) {
                            clearPortraitY();
                            moveToEdge();
                        }else{
                            if(isOnClickEvent()){
                                if(mMagnetViewListener != null){
                                    mMagnetViewListener.onClick();
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });
        tv_accompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null){
                    Log.e("TAG","伴奏");
                }
            }
        });
        tv_lyric.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowLyric){
                    fl_lyric.setVisibility(GONE);
                    isShowLyric = false;
                }else{
                    if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                            && TextUtils.isEmpty(HifiveDialogManageUtil.getInstance().getPlayMusic().getLyric())) {
                        //tv_lyric_detail.setText(HifiveDialogManageUtil.getInstance().getPlayMusic().getLyric());
                        fl_lyric.setVisibility(VISIBLE);
                        isShowLyric = true;
                    }
                }
            }
        });
        iv_last.setOnClickListener(new OnClickListener() {//上一曲
            @Override
            public void onClick(View v) {
                if(HifiveDialogManageUtil.getInstance().getCurrentList() != null){
                    int positon = HifiveDialogManageUtil.getInstance().getCurrentList().indexOf(HifiveDialogManageUtil.getInstance().getPlayMusic());
                    if(positon != 0){//不是第一首
                        HifiveDialogManageUtil.getInstance().setCurrentSingle(HifiveDialogManageUtil.getInstance().getCurrentList().get(positon-1));
                    }
                }
            }
        });
        iv_next.setOnClickListener(new OnClickListener() {//下一曲
            @Override
            public void onClick(View v) {
                if(HifiveDialogManageUtil.getInstance().getCurrentList() != null){
                    int positon = HifiveDialogManageUtil.getInstance().getCurrentList().indexOf(HifiveDialogManageUtil.getInstance().getPlayMusic());
                    if(positon != (HifiveDialogManageUtil.getInstance().getCurrentList().size()-1)){//不是最后一首
                        HifiveDialogManageUtil.getInstance().setCurrentSingle(HifiveDialogManageUtil.getInstance().getCurrentList().get(positon+1));
                    }else{
                        HifiveDialogManageUtil.getInstance().setCurrentSingle(HifiveDialogManageUtil.getInstance().getCurrentList().get(0));
                    }
                }
            }
        });
        iv_play.setOnClickListener(new OnClickListener() {//播放
            @Override
            public void onClick(View v) {

            }
        });
        iv_back.setOnClickListener(new OnClickListener() {//收起
            @Override
            public void onClick(View v) {

            }
        });
    }



    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }
    protected boolean isOnLongClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime > LONG_TOUCH_TIME_THRESHOLD;
    }
    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        }
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        ViewGroup viewGroup = (ViewGroup) getParent();
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
        float y = getY();
        if (!isLandscape && mPortraitY != 0) {
            y = mPortraitY;
            clearPortraitY();
        }
        mMoveAnimator.start(moveDistance, Math.min(Math.max(0, y), mScreenHeight - getHeight()));
    }

    private void clearPortraitY() {
        mPortraitY = 0;
    }

    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        isNearestLeft = getX() < middle;
        return isNearestLeft;
    }

    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
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
            float deltaY = (destinationY - getY()) * progress;
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
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getParent() != null) {
            final boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
            markPortraitY(isLandscape);
            ((ViewGroup) getParent()).post(new Runnable() {
                @Override
                public void run() {
                    updateSize();
                    moveToEdge(isNearestLeft, isLandscape);
                }
            });
        }
    }

    private void markPortraitY(boolean isLandscape) {
        if (isLandscape) {
            mPortraitY = getY();
        }
    }
}
