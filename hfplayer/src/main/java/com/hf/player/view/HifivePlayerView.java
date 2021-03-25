package com.hf.player.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hf.player.utils.NoDoubleClickListener;
import com.hf.player.utils.RoundedCornersTransform;
import com.hf.player.R;
import com.hf.playerkernel.config.MusicPlayAction;
import com.hf.playerkernel.inter.HFPlayerEventListener;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hf.playerkernel.model.AudioBean;
import com.hf.playerkernel.playback.IjkPlayback;
import com.hf.playerkernel.utils.DisplayUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 播放器悬浮窗view
 * Created by huchao on 20/11/10.
 */
@SuppressLint("ViewConstructor")
public class HifivePlayerView extends FrameLayout implements Observer {
    private int marginTop = 0;//滑动范围顶部的间距限制默认为0，
    private int marginBottom = 0;//滑动范围底部的间距限制默认为0，
    private DraggableLinearLayout dragLayout;
    private FrameLayout ll_player;
    private ImageView iv_music;
    private TextView tv_music_info;
    private TextView tv_accompany;
    private CheckBox cb_lyric;
    private ImageView iv_last;
    private SeekBar pb_play;
    private ImageView iv_play;
    private FrameLayout fl_loading;//加载中的layout
    private ImageView iv_next;
    private ImageView iv_back;
    private boolean isPlay;//是否正在播放
    private boolean isError;//判断是否播放出错
    private final FragmentActivity mContext;
    private ObjectAnimator rotateAnim;//音乐图片旋转的动画
    private long rotateAnimPlayTime;//音乐图片旋转的动画执行时间
    private String playUrl;//播放歌曲的url
    private int playProgress;//播放的进度
    public IjkPlayback hfPlayer;
    private boolean onTrackingTouch;//是否在拖拽进度条


    public HifivePlayerView(@NonNull FragmentActivity context, int Top, int Bottom) {
        this(context, null, 0);
        marginTop = Math.max(Top, 0);
        marginBottom = Math.max(Bottom, 0);
    }

    public HifivePlayerView(@NonNull FragmentActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(mContext, R.layout.hifive_window_ijkplayer, this);
        initView();
        initPlaylistener();
        initEvent();
    }

    //初始化view
    private void initView() {
        dragLayout = findViewById(R.id.root);
        dragLayout.setMarginTop(marginTop);
        dragLayout.setMarginBottom(marginBottom);
        ll_player = findViewById(R.id.ll_player);
        iv_music = findViewById(R.id.iv_music);
        tv_music_info = findViewById(R.id.tv_music_info);
        tv_music_info.setSelected(true);
        tv_accompany = findViewById(R.id.tv_accompany);
        cb_lyric = findViewById(R.id.cb_lyric);
        iv_last = findViewById(R.id.iv_last);
        pb_play = findViewById(R.id.pb_play);
        iv_play = findViewById(R.id.iv_play);
        fl_loading = findViewById(R.id.fl_loading);
        iv_next = findViewById(R.id.iv_next);
        iv_back = findViewById(R.id.iv_back);


    }

    //初始化点击事件
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        dragLayout.setDragView(iv_music, new DraggableLinearLayout.OnClickEvent() {
            @Override
            public void onClickEvent() {
                animationOpen();
                HFPlayer.getInstance().mListener.onClick();
            }
        });

        tv_accompany.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

            }
        });
        cb_lyric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        iv_last.setOnClickListener(new OnClickListener() {//上一曲
            @Override
            public void onClick(View v) {
                HFPlayer.getInstance().mListener.onPre();
            }
        });
        iv_next.setOnClickListener(new OnClickListener() {//下一曲
            @Override
            public void onClick(View v) {
                HFPlayer.getInstance().mListener.onNext();
            }
        });
        iv_play.setOnClickListener(new OnClickListener() {//播放
            @Override
            public void onClick(View v) {
                HFPlayer.getInstance().mListener.onPlayPause(isPlay);
                if (isPlay) {//正在播放，点击就是暂停播放
                    stopPlay();
                } else {//暂停点击就是播放
                    startPlay();
                }
            }
        });
        iv_back.setOnClickListener(new OnClickListener() {//收起
            @Override
            public void onClick(View v) {
                animationOFF();
            }
        });

        pb_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                onTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onTrackingTouch = false;
                boolean seekTo = hfPlayer.seekTo(seekBar.getProgress());
                if(!seekTo){
                    hfPlayer.pause();
                }
            }
        });
    }

    //显示加载view
    private void showLoadView() {
        iv_play.setVisibility(GONE);
        fl_loading.setVisibility(VISIBLE);
    }

    //显示播放view
    private void showPlayView() {
        iv_play.setVisibility(VISIBLE);
        fl_loading.setVisibility(GONE);
    }


    /**
     * 播放歌曲UrL
     * @param url
     */
    public void playWithUrl(String url){

        startPlayMusic(url,true);

    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        tv_music_info.setText(title);
    }

    /**
     * 设置图片
     */
    public void setCover(String coverUrl){
        if (mContext != null) {
            RoundedCornersTransform transform = new RoundedCornersTransform(mContext, DisplayUtils.dip2px(mContext, 25));
            transform.setNeedCorner(true,true,true,true);
            if (coverUrl != null ) {
                Glide.with(mContext).asBitmap().load(coverUrl)
                        .error(R.drawable.hifivesdk_icon_music_player_defaut)
                        .placeholder(R.drawable.hifivesdk_icon_music_player_defaut)
                        .apply(new RequestOptions().transform(transform))
                        .into(iv_music);//四周都是圆角的圆角矩形图片。
            } else {
                Glide.with(mContext).asBitmap().load(R.drawable.hifivesdk_icon_music_player_defaut)
                        .apply(new RequestOptions().transform(transform))
                        .into(iv_music);//四周都是圆角的圆角矩形图片。
            }
        }
    }

    /**
     * 移动播放器位置
     */
    public void updateViewY(){
        if(dragLayout != null){
            dragLayout.updateViewY();
        }
    }


    /**
     * 开始播放歌曲
     *
     * @param path    音频文件路径或者url
     * @param isStart true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private void startPlayMusic(String path, boolean isStart) {
        iv_play.setImageResource(R.drawable.hifivesdk_icon_player_play);
        playUrl = path;
        isPlay = true;
        if (isStart) {
            hfPlayer.playWhitUrl(path);
        } else {
            //是否是播放错误后导致的暂停，播放出错暂停播放后需要重新prepare（）
            if (isError) {
                changePlayMusic(path);
            } else {
                hfPlayer.playPause();
            }
        }
        startAnimationPlay();
    }

    /**
     * 开始播放歌曲
     */
    public void startPlay() {
        if(playUrl.equals("")) return;
        startPlayMusic(playUrl, false);
    }

    /**
     * 切换播放歌曲
     *
     * @param path 音频文件路径或者url
     */
    private void changePlayMusic(String path) {
        hfPlayer.playWhitUrl(path);
        if (!isPlay) {
            iv_play.setImageResource(R.drawable.hifivesdk_icon_player_play);
            isPlay = true;
            startAnimationPlay();
        }
    }

    //开始播放动画
    private void startAnimationPlay() {
        //构造ObjectAnimator对象的方法
        rotateAnim = ObjectAnimator.ofFloat(iv_music, "rotation", 0.0F, 360.0F);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setDuration(4000);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.start();
        rotateAnim.setCurrentPlayTime(rotateAnimPlayTime);
    }

    //暂停播放
    public void stopPlay() {
        iv_play.setImageResource(R.drawable.hifivesdk_icon_player_suspend);
        isPlay = false;
        if(hfPlayer != null && hfPlayer.isPlaying()){
            hfPlayer.pause();
        }
        if(rotateAnim != null){
            rotateAnimPlayTime = rotateAnim.getCurrentPlayTime();
            rotateAnim.cancel();
        }
    }

    //播放器展开动画
    protected void animationOpen() {
        Animation animation = new HifiveViewChangeAnimation(ll_player, DisplayUtils.getScreenWidth(mContext)
                - DisplayUtils.dip2px(mContext, 40));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
    }

    //播放器收起动画
    protected void animationOFF() {
        Animation animation = new HifiveViewChangeAnimation(ll_player, DisplayUtils.dip2px(mContext, 50));
        animation.setDuration(500);
        ll_player.startAnimation(animation);
    }

    //收到被观察者发出的更新通知
    @Override
    public void update(Observable o, Object arg) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //切歌后清空上首歌播放相关标志和配置
    private void clear() {
        setTitle("");
        setCover("");
        playProgress = 0;//重置播放进度
        playUrl = "";//重置播放链接
        pb_play.setProgress(0);
        pb_play.setSecondaryProgress(0);
        iv_play.setVisibility(VISIBLE);
        fl_loading.setVisibility(GONE);
        //清空动画
        if (rotateAnim != null) {
            rotateAnimPlayTime = 0;
            rotateAnim.cancel();
        }
    }


    private void initPlaylistener(){
        hfPlayer = HFPlayerApi.with();
        hfPlayer.setOnPlayEventListener(new HFPlayerEventListener() {
            @Override
            public void onChange(AudioBean music) {

            }

            @Override
            public void onPlayStateChanged(int state) {
                switch (state) {
                    case MusicPlayAction.STATE_IDLE:
                        break;
                    case MusicPlayAction.STATE_PAUSE:
                        if(hfPlayer != null && hfPlayer.isPlaying()){
                            stopPlay();
                        }
                        break;
                    case MusicPlayAction.STATE_ERROR:
                        isError = true;
                        stopPlay();
                        clear();
                        HFPlayer.getInstance().mListener.onError();
                        break;
                    case MusicPlayAction.STATE_PREPARING:
                        pb_play.setMax((int) hfPlayer.getDuration());
                        break;
                    case MusicPlayAction.STATE_PLAYING:
                        iv_play.setImageResource(R.drawable.hifivesdk_icon_player_play);
                        showPlayView();
                        break;
                    case MusicPlayAction.STATE_BUFFERING:
                        showLoadView();
                        break;
                    case MusicPlayAction.STATE_COMPLETE:
                        stopPlay();
                        clear();
                        HFPlayer.getInstance().mListener.onComplete();
//                        HifiveManage.getInstance().playNextMusic(mContext);//播放完成自动播放下一首
                        break;
                }
            }

            @Override
            public void onProgressUpdate(int progress, int duration) {
                if(!onTrackingTouch){
                    pb_play.setProgress(progress);
                }

            }

            @Override
            public void onBufferingUpdate(int percent) {
                if(!onTrackingTouch){
                    pb_play.setSecondaryProgress( pb_play.getMax() * percent / 100);
                }
            }

            @Override
            public void onTimer(long remain) {

            }
        });
    }
}
