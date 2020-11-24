package com.hifive.sdk.demo.ui.player;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hifive.sdk.R;
import com.hifive.sdk.demo.model.HifiveMusicAuthorModel;
import com.hifive.sdk.demo.model.HifiveMusicDetailModel;
import com.hifive.sdk.demo.model.HifiveMusicLyricDetailModel;
import com.hifive.sdk.demo.model.HifiveMusicModel;
import com.hifive.sdk.demo.model.HifiveMusiclyricModel;
import com.hifive.sdk.demo.ui.HifiveMusicListDialogFragment;
import com.hifive.sdk.demo.util.HifiveDialogManageUtil;
import com.hifive.sdk.demo.util.HifiveDisplayUtils;
import com.hifive.sdk.demo.util.HifiveDownloadUtile;
import com.hifive.sdk.demo.view.HifiveRoundProgressBar;
import com.hifive.sdk.demo.view.RoundedCornersTransform;
import com.hifive.sdk.hInterface.DownLoadResponse;
import com.hifive.sdk.manager.HFLiveApi;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName  HifivePlayerView
 * 播放器悬浮窗view
 * Created by huchao on 20/11/10.
 */
@SuppressLint("ViewConstructor")
public class HifivePlayerView extends FrameLayout implements Observer, HifivePlayListener {
    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private static final int TOUCH_TIME_THRESHOLD = 150;//点击事件的时长，
    private static final int LONG_TOUCH_TIME_THRESHOLD = 300;//长按事件的时长
    private int marginTop = 0;//滑动范围顶部的间距限制默认为0，
    private int marginBottom= 0;//滑动范围底部的间距限制默认为0，
    private long mLastTouchDownTime;
    private MoveAnimator mMoveAnimator;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isNearestLeft = true;
    private float mPortraitY;
    private FrameLayout fl_lyric;
    private TextView tv_lyric_static;
    private RelativeLayout rl_lyric_dynamic;
    private TextView tv_lyric_left, tv_lyric_right;
    private LinearLayout ll_player;
    private ImageView iv_music;
    private TextView tv_music_info;
    private TextView tv_accompany;
    private CheckBox cb_lyric;
    private ImageView iv_last;
    private FrameLayout fl_play;//播放进度的layout
    private HifiveRoundProgressBar pb_play;
    private ImageView iv_play;
    private FrameLayout fl_download;//下载伴奏的layout
    private TextView tv_download;
    private FrameLayout fl_loading;//加载中的layout
    private ImageView iv_loading;
    private ImageView iv_next;
    private ImageView iv_back;
    private boolean isShowAccompany;//是否是伴奏模式
    private boolean isPlay;//是否正在播放
    private boolean isStatic;//歌词是动态还是静态歌词
    private final FragmentActivity mContext;
    private AnimationDrawable animationDrawable;//加载的动画
    private Animation rotateAnimation;//音乐图片旋转的动画
    public HifivePlayerUtils playerUtils;
    private HifiveMusicListDialogFragment dialogFragment;
    public Timer mTimer;
    public TimerTask mTimerTask;
    private Toast toast;
    private String playUrl;//播放歌曲的url
    public static File accompanyFile;//播放伴奏的文件
    private int playProgress;//播放的进度
    private List<HifiveMusicLyricDetailModel> lyricDetailModels;
    public HifivePlayerView(@NonNull FragmentActivity context,int marginTop,int marginBottom) {
        this(context, null,0);
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }
    public HifivePlayerView(@NonNull FragmentActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mMoveAnimator = new MoveAnimator();
        inflate(mContext, R.layout.hifive_window_ijkplayer, this);
        initView();
        initEvent();
    }
    //初始化view
    private void initView() {
        if(playerUtils == null){
            playerUtils = new HifivePlayerUtils();
            playerUtils.setPlayCompletionListener(this);
        }
        fl_lyric =  findViewById(R.id.fl_lyric);
        tv_lyric_static =  findViewById(R.id.tv_lyric_static);
        tv_lyric_static.setMovementMethod(new ScrollingMovementMethod());
        rl_lyric_dynamic =  findViewById(R.id.rl_lyric_dynamic);
        tv_lyric_left =  findViewById(R.id.tv_lyric_left);
        tv_lyric_right =  findViewById(R.id.tv_lyric_right);
        tv_lyric_left.setSelected(true);
        tv_lyric_right.setSelected(true);
        ll_player =  findViewById(R.id.ll_player);
        iv_music =  findViewById(R.id.iv_music);
        tv_music_info =  findViewById(R.id.tv_music_info);
        tv_music_info.setSelected(true);
        tv_accompany =  findViewById(R.id.tv_accompany);
        cb_lyric =  findViewById(R.id.cb_lyric);
        iv_last =  findViewById(R.id.iv_last);
        fl_play  =  findViewById(R.id.fl_play);
        pb_play  =  findViewById(R.id.pb_play);
        iv_play =  findViewById(R.id.iv_play);
        fl_download  =  findViewById(R.id.fl_download);
        tv_download  =  findViewById(R.id.tv_download);
        fl_loading =  findViewById(R.id.fl_loading);
        iv_loading =  findViewById(R.id.iv_loading);
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
                                showDialog();
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
                    if(!TextUtils.isEmpty(playUrl)){//当前歌曲URL不为空直接切换播放
                        changePlayMusic(playUrl,false);
                        setTypeSound();
                    }else{//为空暂停播放，并开始获取歌曲URL信息
                        stopPlay();
                        showLoadVew();
                        HifiveDialogManageUtil.getInstance().getMusicDetail(1,mContext);
                    }
                }else{//不是伴奏模式
                    if(accompanyFile != null){//当前伴奏不为空直接切换播放
                        changePlayMusic(accompanyFile.getPath(),false);
                        setTypeAccompany();
                    }else{//为空暂停播放，并开始获取伴奏信息
                        stopPlay();
                        showLoadVew();
                        HifiveDialogManageUtil.getInstance().getMusicDetail(0,mContext);
                    }
                }
            }
        });
        cb_lyric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(isStatic){
                        fl_lyric.setVisibility(VISIBLE);
                    }else{
                        rl_lyric_dynamic.setVisibility(VISIBLE);
                    }
                }else{
                    if(isStatic){
                        fl_lyric.setVisibility(GONE);
                    }else{
                        rl_lyric_dynamic.setVisibility(GONE);
                    }
                }
            }
        });

        iv_last.setOnClickListener(new OnClickListener() {//上一曲
            @Override
            public void onClick(View v) {
                HifiveDialogManageUtil.getInstance().playLastMusic(mContext);
            }
        });
        iv_next.setOnClickListener(new OnClickListener() {//下一曲
            @Override
            public void onClick(View v) {
                HifiveDialogManageUtil.getInstance().playNextMusic(mContext);
            }
        });
        iv_play.setOnClickListener(new OnClickListener() {//播放
            @Override
            public void onClick(View v) {
                if(isPlay){//正在播放，点击就是暂停播放
                    stopPlay();
                }else{//暂停点击就是播放
                    if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null){
                        if(isShowAccompany) {
                            startPlayMusic(accompanyFile.getPath(),false);
                        }else{
                            startPlayMusic(playUrl,false);
                        }
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
    //显示加载view
    private void showLoadVew() {
        fl_play.setVisibility(GONE);
        fl_download.setVisibility(GONE);
        fl_loading.setVisibility(VISIBLE);
        if(animationDrawable == null)
            animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
        animationDrawable.start();
    }
    //显示播放view
    private void showPlayView() {
        fl_play.setVisibility(VISIBLE);
        fl_loading.setVisibility(GONE);
        fl_download.setVisibility(GONE);
        if(animationDrawable != null)
            animationDrawable.stop();
    }
    //显示下载view
    private void showDownLoadView() {
        fl_play.setVisibility(GONE);
        fl_loading.setVisibility(GONE);
        fl_download.setVisibility(VISIBLE);
        if(animationDrawable != null)
            animationDrawable.stop();
    }
    /**
     * 开始播放歌曲
     * @param path 音频文件路径或者url
     * @param isStart true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private void startPlayMusic(String path,boolean isStart) {
        iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_play);
        isPlay = true;
        if(playerUtils!= null) {
            if (isStart) {
                playerUtils.prepareAndPlay(path, new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        pb_play.setMax(playerUtils.duration());
                        pb_play.setProgress(0);
                        showPlayView();
                        cleanTimer();
                        setPlayProgress();
                        playerUtils.play();
                    }
                });
            } else {
                playerUtils.play();
            }
        }
        StartAnimationPlay();
    }
    /**
     * 切换播放歌曲
     * @param path 音频文件路径或者url
     * @param isRestart true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private void changePlayMusic(String path,boolean isRestart) {
        if(playerUtils!= null) {
            playerUtils.prepareAndPlay(path, new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    showPlayView();
                    cleanTimer();
                    setPlayProgress();
                    playerUtils.play();
                    playerUtils.seekTo(playProgress);
                }
            });
        }
        if(isRestart){
            iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_play);
            isPlay = true;
            StartAnimationPlay();
        }
    }
    //设置播放进度
    private void setPlayProgress() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(playerUtils!= null && playerUtils.isPlaying()) {
                    playProgress = playerUtils.progress();
                    if(!isStatic)
                        updateLyricDetail(playProgress);
                    pb_play.setProgress(playProgress);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1);
    }
    //清空播放进度
    public void cleanTimer(){
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
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
        if(playerUtils!= null)
            playerUtils.onPause();
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
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        int maxScrollY = getMaxScrollY();
        if (desY > maxScrollY) {
            desY = maxScrollY;
        }else{
            if (desY < marginTop) {
                desY = marginTop;
            }
        }
        setY(desY);
    }
    //获取最大可滑动距离
    public int getMaxScrollY() {
        //判断歌曲选择相关的弹窗是否打开
        if(dialogFragment != null && dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()){
            return mScreenHeight - getHeight()- HifiveDisplayUtils.dip2px(mContext,470);
        }else{
            return mScreenHeight - getHeight() - marginBottom;
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
        Log.e("TAG","playError");
        stopPlay();
    }
    //当前歌曲播放完成回调
    @Override
    public void playCompletion() {
        if(playerUtils!= null)
            playerUtils.onPause();
        HifiveDialogManageUtil.getInstance().playNextMusic(mContext);//播放完成自动播放下一首
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
        setX(0);
        setY(getY() + deltaY);
    }
    //播放器展开动画
    protected void AnimationOpen() {
        Animation animation = new HifiveViewChangeAnimation(ll_player, HifiveDisplayUtils.getScreenWidth(mContext)
                - HifiveDisplayUtils.dip2px(mContext, 40));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
    }
    //播放器收起动画
    protected void AnimationOFF() {
        Animation animation = new HifiveViewChangeAnimation(ll_player, HifiveDisplayUtils.dip2px(mContext, 50));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
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
                stopPlay();
                clear();
                //隐藏歌词
                rl_lyric_dynamic.setVisibility(GONE);
                fl_lyric.setVisibility(GONE);
                if(HifiveDialogManageUtil.getInstance().getPlayMusic() != null){
                    showLoadVew();
                    updateView();
                }else{//重置播放器
                    enabledLyric();
                    tv_music_info.setText("");
                    tv_accompany.setEnabled(false);
                    tv_accompany.setAlpha(0.45f);
                    if(mContext != null) {
                        RoundedCornersTransform transform = new RoundedCornersTransform(mContext, HifiveDisplayUtils.dip2px(mContext, 25));
                        Glide.with(mContext).asBitmap().load(R.mipmap.hifivesdk_icon_music_player_defaut)
                                .apply(new RequestOptions().transform(transform))
                                .into(iv_music);
                    }
                }
            }else if(type == HifiveDialogManageUtil.PALYINGMUSIC){
                updatePlayView(false);
            }else if(type == HifiveDialogManageUtil.PALYINGCHANGEMUSIC){
                updatePlayView(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //切歌后清空上首歌播放相关标志和配置
    private void clear() {
        playProgress = 0;//重置播放进度
        accompanyFile = null;//重置伴奏
        playUrl = "";//重置播放链接
        isChange = false;//重置歌词查找状态
        position = 0;//重置歌词查找下标
        pb_play.setProgress(0);
        //歌词置空
        tv_lyric_left.setText("");
        tv_lyric_right.setText("");
        tv_lyric_static.setText("");
    }
    //更新view，音乐图片，音乐名称更新
    private void updateView() {
        HifiveMusicModel playMusic = HifiveDialogManageUtil.getInstance().getPlayMusic();
        StringBuilder info = new StringBuilder();
        if(!TextUtils.isEmpty(playMusic.getMusicName())){
            info.append(playMusic.getMusicName());
        }
        if(playMusic.getArtist() != null && playMusic.getArtist().size() >0){
            for(HifiveMusicAuthorModel authorModel:playMusic.getArtist()){
                if(info.length() >0){
                    info.append("-");
                }
                info.append(authorModel.getName());
            }
        }
        tv_music_info.setText(info.toString());
        if(mContext != null) {
            RoundedCornersTransform transform = new RoundedCornersTransform(mContext, HifiveDisplayUtils.dip2px(mContext, 25));
            if (playMusic.getCover() != null && !TextUtils.isEmpty(playMusic.getCover().getUrl())) {
                Glide.with(mContext).asBitmap().load(playMusic.getCover().getUrl())
                        .error(R.mipmap.hifivesdk_icon_music_player_defaut)
                        .placeholder(R.mipmap.hifivesdk_icon_music_player_defaut)
                        .apply(new RequestOptions().transform(transform))
                        .into(iv_music);//四周都是圆角的圆角矩形图片。
            } else {
                Glide.with(mContext).asBitmap().load(R.mipmap.hifivesdk_icon_music_player_defaut)
                        .apply(new RequestOptions().transform(transform))
                        .into(iv_music);//四周都是圆角的圆角矩形图片。
            }
        }
        //当前歌曲有多个版本时支持切换原神和伴奏
        if(playMusic.getVersion()!= null && playMusic.getVersion().size() >1){
            tv_accompany.setEnabled(true);
            tv_accompany.setAlpha(1f);
        }else{
            tv_accompany.setEnabled(false);
            tv_accompany.setAlpha(0.45f);
        }
    }
    //更新播放view，下载歌词，播放音乐isChange表示是否是切换播放模式
    private void updatePlayView(boolean isChange) {
        HifiveMusicDetailModel playMusicDetail = HifiveDialogManageUtil.getInstance().playMusicDetail;
        if(playMusicDetail != null) {
            if(!isChange) {
                HifiveMusiclyricModel lyric = playMusicDetail.getLyric();
                //没有歌词的时候
                if (lyric == null || (TextUtils.isEmpty(lyric.getDynamicUrl()) && TextUtils.isEmpty(lyric.getStaticUrl()))) {
                    enabledLyric();
                } else {
                    //动态歌词不等于空就下载动态歌词否则下载静态歌词
                    if (!TextUtils.isEmpty(lyric.getDynamicUrl())) {
                        isStatic = false;
                        downloadLyric(lyric.getDynamicUrl(), 1);
                    } else {
                        isStatic = true;
                        downloadLyric(lyric.getStaticUrl(), 2);
                    }
                }
            }
            if (playMusicDetail.getIsMajor() == 1) {//主版本
                setTypeSound();
                if (playMusicDetail.getFile() != null
                        && !TextUtils.isEmpty(playMusicDetail.getFile().getUrl())) {
                    playUrl = playMusicDetail.getFile().getUrl();
                    if(!isChange) {
                        startPlayMusic(playUrl,true);//开始播放新歌曲
                    }else{
                        changePlayMusic(playUrl,true);//切换播放歌曲
                    }
                } else {
                    showToast("播放歌曲出错");
                }
            } else {//伴奏
                setTypeAccompany();
                if (playMusicDetail.getFile() != null
                        && !TextUtils.isEmpty(playMusicDetail.getFile().getUrl())) {
                    showDownLoadView();
                    downLoadAccompany(playMusicDetail.getFile().getUrl(),playMusicDetail.getMusicId(), playMusicDetail.getFile().getExt(),isChange);
                } else {
                    showToast("播放伴奏出错");
                }
            }
        }
    }
    //设置歌词不能点击
    private void enabledLyric() {
        cb_lyric.setChecked(false);
        cb_lyric.setEnabled(false);
        cb_lyric.setAlpha(0.45f);
    }

    //下载伴奏
    private void downLoadAccompany(String file, String musicId, String expand, final boolean ischange) {
        Log.e("TAG","file=="+file);
        HFLiveApi.Companion.getInstance().downLoadFile(mContext, file, mContext.getFilesDir() + "/" + musicId + "." + expand,
                new DownLoadResponse() {
                    @Override
                    public void size(long totalBytes) {

                    }

                    @Override
                    public void succeed(@NotNull File file) {
                        accompanyFile = file;
                        if (accompanyFile != null) {
                            if (ischange) {
                                changePlayMusic(accompanyFile.getPath(),true);
                            }else{
                                startPlayMusic(accompanyFile.getPath(),true);
                            }
                        }else{
                            showToast("播放伴奏出错");
                        }
                    }
                    @Override
                    public void fail(@NotNull String error_msg) {
                        showToast(error_msg);
                    }
                    @Override
                    public void progress(long currentBytes, long totalBytes) {
                        updateDownLoadProgress(currentBytes,totalBytes);
                    }
                });
    }
    //更新下载进度
    private void updateDownLoadProgress(final long currentBytes, final long totalBytes) {
        if(mContext != null){
            mContext.runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    tv_download.setText((int)(HifiveDisplayUtils.div(currentBytes,totalBytes,2)*100)+"%");
                }
            });
        }
    }
    //显示自定义toast信息
    @SuppressLint("ShowToast")
    private void showToast(String msg){
        if(mContext != null){
            if(toast == null){
                toast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
            }else {
                toast.setText(msg);
            }
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }
    }
    /**
     * 下载歌词
     * @param lyricUrl 歌词的url
     * @param type 歌词类型1.动态歌词2.静态歌词
     * @author huchao
     */
    private void downloadLyric(String lyricUrl, final int type) {
        Log.e("TAG","lyricUrl=="+lyricUrl);
        HifiveDownloadUtile.downloadFile(lyricUrl,new HifiveDownloadUtile.NetCallback() {
            @Override
            public void reqYes(Object o, String s) {
                updateLyricView(s,type);
            }
            @Override
            public void reqNo(Object o, Exception e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enabledLyric();
                    }
                });
                showToast("未找到歌词");
            }
        });
    }
    //更新歌词的view
    private void updateLyricView(final String content, final int type) {
        if(mContext != null){
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type ==1){
                        if(!TextUtils.isEmpty(content)){
                            cb_lyric.setEnabled(true);
                            cb_lyric.setAlpha(1f);
                            lyricDetailModels = HifiveDisplayUtils.getLyricDetailModels(content);
                            updateLyricDetail(0);
                            if(cb_lyric.isChecked()){
                                rl_lyric_dynamic.setVisibility(VISIBLE);
                            }
                        }else{
                            enabledLyric();
                        }
                    }else{
                        if(!TextUtils.isEmpty(content)){
                            cb_lyric.setEnabled(true);
                            cb_lyric.setAlpha(1f);
                            tv_lyric_static.setText(content);
                            if(cb_lyric.isChecked()){
                                fl_lyric.setVisibility(VISIBLE);
                            }
                        }else{
                            enabledLyric();
                        }
                    }
                }
            });
        }
    }
    private boolean isChange;//判断歌词是否正在改变
    private int position = 0;//保留歌词下标，下次从下标开始查找歌词
    private void updateLyricDetail(int playProgress) {
        if (isChange)
            return;
        isChange = true;
        if(lyricDetailModels != null) {
            for (int i = position; i < lyricDetailModels.size(); i++) {
                if (playProgress <= lyricDetailModels.get(i).getStartTime()) {
                    if (i % 2 == 0) {
                        updateTextView(tv_lyric_left,tv_lyric_right,lyricDetailModels.get(i).getContent());
                    } else {
                        updateTextView(tv_lyric_right,tv_lyric_left,lyricDetailModels.get(i).getContent());
                    }
                    position = i;
                    break;
                }
            }
        }
        isChange = false;
    }
    //更新动态歌词view
    private void updateTextView(final TextView tv1, final TextView tv2, final String content) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv1.setText(content);
                tv1.setTextColor(Color.parseColor("#FFFFFF"));
                tv2.setTextColor(Color.parseColor("#FFF69643"));

            }
        });
    }
    //显示歌曲列表弹窗
    public void showDialog() {
        if(dialogFragment != null && dialogFragment.getDialog() != null){
            if(dialogFragment.getDialog().isShowing()){
                HifiveDialogManageUtil.getInstance().CloseDialog();
            }else{
                dialogFragment.show(mContext.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
                updateViewY();
            }
        }else{
            dialogFragment = new HifiveMusicListDialogFragment();
            dialogFragment.show(mContext.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
            updateViewY();
        }
    }
}
