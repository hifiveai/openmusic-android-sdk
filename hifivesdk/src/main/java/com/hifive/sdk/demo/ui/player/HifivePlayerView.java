package com.hifive.sdk.demo.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hifive.music.utils.AudioUtils;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;

import java.util.Observable;
import java.util.Observer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @ClassName  HifivePlayerView
 * 播放器悬浮窗view
 * Created by huchao on 20/11/10.
 */
public class HifivePlayerView extends FrameLayout implements Observer, HifivePlayListener {
    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private HifiveDialogListener showDialogListener;
    private static final int TOUCH_TIME_THRESHOLD = 150;//点击事件的时长，
    private static final int LONG_TOUCH_TIME_THRESHOLD = 300;//长按事件的时长
    private long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;
    protected int mScreenWidth;
    private int mScreenHeight;
    private boolean isNearestLeft = true;
    private float mPortraitY;
    private FrameLayout fl_lyric;
    private TextView tv_lyric_detail;
    private TextView tv_lyric_time;
    private LinearLayout ll_player;
    private ImageView iv_music;
    private TextView tv_music_info;
    private TextView tv_accompany;
    private CheckBox cb_lyric;
    private ImageView iv_last;
    private ImageView iv_play;
    private ImageView iv_next;
    private ImageView iv_back;
    private boolean isShowAccompany;//是否是伴奏模式
    private boolean isPlay;//是否正在播放
    private final Context mContext;
    private Animation rotateAnimation;//音乐图片旋转的动画
    public   AudioUtils audioUtils;

    public void setShowDialogListener(HifiveDialogListener showDialogListener) {
        this.showDialogListener = showDialogListener;
    }
    public HifivePlayerView(@NonNull Context context) {
        this(context, null);
    }

    public HifivePlayerView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HifivePlayerView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mMoveAnimator = new MoveAnimator();
        inflate(context, R.layout.hifive_window_ijkplayer, this);
        initView();
        initEvent();
    }
    //初始化view
    private void initView() {
        if(audioUtils == null){
            audioUtils = new AudioUtils();
            audioUtils.setPlayCompletionListener(this);
        }
        fl_lyric =  findViewById(R.id.fl_lyric);
        tv_lyric_detail =  findViewById(R.id.tv_lyric_detail);
        tv_lyric_detail.setMovementMethod(new ScrollingMovementMethod());
        tv_lyric_time =  findViewById(R.id.tv_lyric_time);
        ll_player =  findViewById(R.id.ll_player);
        iv_music =  findViewById(R.id.iv_music);
        tv_music_info =  findViewById(R.id.tv_music_info);
        tv_accompany =  findViewById(R.id.tv_accompany);
        cb_lyric =  findViewById(R.id.cb_lyric);
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
                        if(isOnLongClickEvent()) {//长按时执行滑动
                            updateViewPosition(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isOnLongClickEvent()) {//长按结束执行滑动
                            clearPortraitY();
                            moveToEdge();
                        }else{
                            if(isOnClickEvent()){//短按结束执行点击事件
                                AnimationOpen();
                                if(showDialogListener != null){
                                    if(HifiveDialogManageUtil.getInstance().isShow){
                                        showDialogListener.onDismissDialog();
                                    }else{
                                        showDialogListener.onShowDialog();
                                    }
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
                if(isShowAccompany){//已是伴奏模式
                    setTypeSound();
                }else{//不是伴奏模式
                    setTypeAccompany();
                }
            }
        });
        cb_lyric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null
                            && !TextUtils.isEmpty(HifiveDialogManageUtil.getInstance().getPlayMusic().getLyric())) {
                        fl_lyric.setVisibility(VISIBLE);
                    }
                }else{
                    fl_lyric.setVisibility(GONE);
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
                if(isPlay){//正在播放，点击就是暂停播放
                    stopPlay();
                }else{//暂停点击就是播放
                    if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null){
                        startPlay(false);
                    }
                }
            }
        });
        iv_back.setOnClickListener(new OnClickListener() {//收起
            @Override
            public void onClick(View v) {
                AnimationOFF();
                HifiveDialogManageUtil.getInstance().CloseDialog();
            }
        });
    }
    //设置播放器原声模式
    private void setTypeSound() {
        tv_accompany.setText(mContext.getString(R.string.hifivesdk_music_player_sound));
        isShowAccompany = false;
    }
    //设置播放器伴奏模式
    private void setTypeAccompany() {
        tv_accompany.setText(mContext.getString(R.string.hifivesdk_music_player_accompany));
        isShowAccompany = true;
    }
    /**
     * 开始播放
     * @param isStart 是否播放新歌曲，true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private void startPlay(boolean isStart) {
        iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_play);
        isPlay = true;
        if(isStart){
            audioUtils.prepareAndPlay(HifiveDialogManageUtil.getInstance().getPlayMusic().getUrl(), new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {

                }
            });
        }else{
            audioUtils.play();
        }
        StartAnimationPlay();
    }
    //开始播放动画
    private void StartAnimationPlay() {
        if(rotateAnimation == null) {
            rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.hifive_anim_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            rotateAnimation.setInterpolator(lin);
        }
        iv_music.startAnimation(rotateAnimation);
    }
    //暂停播放
    private void stopPlay() {
        iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_suspend);
        isPlay = false;
        audioUtils.onPause();
        iv_music.clearAnimation();
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
        // setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        int maxScrollY = getMaxScrollY();
        if (desY > maxScrollY) {
            desY = maxScrollY;
        }
        setY(desY);
    }
    //获取最大可滑动距离
    public int getMaxScrollY() {
        //判断歌曲选择相关的弹窗是否打开
        if(HifiveDialogManageUtil.getInstance().isShow){
            return mScreenHeight - getHeight()- HifiveDisplayUtils.dip2px(mContext,470);
        }else{
            return mScreenHeight - getHeight();
        }
    }
    //歌曲弹窗显示时更新最大可滑动距离
    public void updateViewY() {
        final int   maxScrollY = mScreenHeight - getHeight()-HifiveDisplayUtils.dip2px(mContext,470);
        if(getY() > maxScrollY){
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,0,maxScrollY-getY());
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setY(maxScrollY);
                    clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(translateAnimation);
        }
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
    //当前歌曲播放错误回调
    @Override
    public void playError() {
        stopPlay();
    }
    //当前歌曲播放完成回调
    @Override
    public void playCompletion() {
        audioUtils.onPause();
        iv_next.performClick();//播放完成自动播放下一首，执行切歌逻辑
    }

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
        // setX(getX() + deltaX);
        setY(getY() + deltaY);
    }
    //播放器展开动画
    protected void AnimationOpen() {
        Animation animation = new ViewSizeChangeAnimation(ll_player, HifiveDisplayUtils.getScreenWidth(mContext)
                - HifiveDisplayUtils.dip2px(mContext, 40));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
    }
    //播放器收起动画
    protected void AnimationOFF() {
        Animation animation = new ViewSizeChangeAnimation(ll_player, HifiveDisplayUtils.dip2px(mContext, 50));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
    }
    public static class ViewSizeChangeAnimation extends Animation {
        int initialHeight;
        int initialWidth;
        int targetWidth;
        View view;

        public ViewSizeChangeAnimation(View view,int targetWidth) {
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
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getParent() != null) {
            final boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
            if (isLandscape)
                mPortraitY = getY();
            ((ViewGroup) getParent()).post(new Runnable() {
                @Override
                public void run() {
                    updateSize();
                    moveToEdge(isNearestLeft, isLandscape);
                }
            });
        }
    }
    //收到被观察者发出的更新通知
    @Override
    public void update(Observable o, Object arg) {
        try{
            int type = (int) arg;
            if(type == HifiveDialogManageUtil.UPDATEPALY){
                updateView();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //更新view
    private void updateView() {
        HifiveMusicModel playMusic = HifiveDialogManageUtil.getInstance().getPlayMusic();
        if(playMusic != null){
            StringBuilder info = new StringBuilder();
            if(!TextUtils.isEmpty(playMusic.getName())){
                info.append(playMusic.getName());
            }
            if(!TextUtils.isEmpty(playMusic.getAuthor())){
                info.append("-");
                info.append(playMusic.getAuthor());
            }
            tv_music_info.setText(info.toString());
            if(!TextUtils.isEmpty(playMusic.getLyric())){
                tv_lyric_detail.setText(playMusic.getLyric());
                if(cb_lyric.isChecked()){
                    fl_lyric.setVisibility(VISIBLE);
                }else{
                    fl_lyric.setVisibility(GONE);
                }
            }else{
                fl_lyric.setVisibility(GONE);
            }
            startPlay(true);//开始播放新歌曲
        }
    }
}
