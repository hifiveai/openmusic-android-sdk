package com.hfliveplayer.sdk.ui.player;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hfliveplayer.sdk.R;
import com.hfliveplayer.sdk.listener.HifivePlayListener;
import com.hfliveplayer.sdk.ui.HifiveMusicListDialogFragment;
import com.hfliveplayer.sdk.util.HifiveDialogManageUtil;
import com.hfliveplayer.sdk.util.HifiveDisplayUtils;
import com.hfliveplayer.sdk.util.HifiveDownloadUtile;
import com.hfliveplayer.sdk.listener.NoDoubleClickListener;
import com.hfliveplayer.sdk.view.DraggableLinearLayout;
import com.hfliveplayer.sdk.view.HifiveRoundProgressBar;
import com.hfliveplayer.sdk.view.LyricDynamicView;
import com.hfliveplayer.sdk.view.RoundedCornersTransform;
import com.hifive.sdk.entity.HifiveMusicAuthorModel;
import com.hifive.sdk.entity.HifiveMusicDetailModel;
import com.hifive.sdk.entity.HifiveMusicLyricDetailModel;
import com.hifive.sdk.entity.HifiveMusicModel;
import com.hifive.sdk.entity.HifiveMusiclyricModel;
import com.hifive.sdk.hInterface.DownLoadResponse;
import com.hifive.sdk.manager.HFLiveApi;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 播放器悬浮窗view
 * Created by huchao on 20/11/10.
 */
@SuppressLint("ViewConstructor")
public class HifivePlayerView extends FrameLayout implements Observer, HifivePlayListener {
    private int marginTop = 0;//滑动范围顶部的间距限制默认为0，
    private int marginBottom = 0;//滑动范围底部的间距限制默认为0，
    private DraggableLinearLayout dragLayout;
    private FrameLayout fl_lyric;
    private TextView tv_lyric_static;
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
    private ImageView iv_next;
    private ImageView iv_back;
    private LyricDynamicView lyric_dynamic_view;
    private boolean isShowAccompany;//是否是伴奏模式
    private boolean isPlay;//是否正在播放
    private boolean isStatic;//歌词是动态还是静态歌词
    private boolean isError;//判断是否播放出错
    private final FragmentActivity mContext;
    private ObjectAnimator rotateAnim;//音乐图片旋转的动画
    private long rotateAnimPlayTime;//音乐图片旋转的动画执行时间
    public HifivePlayerUtils playerUtils;
    private HifiveMusicListDialogFragment dialogFragment;
    public Timer mTimer;
    public TimerTask mTimerTask;
    private String playUrl;//播放歌曲的url
    public static File musicFile;//播放伴奏的文件
    private int playProgress;//播放的进度
    private List<HifiveMusicLyricDetailModel> lyricDetailModels;
    private Call downLoadFileCall;//伴奏下载的Call
    private int initialVersion = -1;//点击列表播放的歌曲版本 1：原唱 2：伴奏
    private boolean isInitialMode = true;//切换模式时是否是初始模式


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
        initEvent();
    }

    //初始化view
    private void initView() {
        if (playerUtils == null) {
            playerUtils = HifivePlayerUtils.Companion.getInstance();
            playerUtils.setPlayCompletionListener(this);
        }
        dragLayout = findViewById(R.id.root);
        dragLayout.setMarginTop(marginTop);
        dragLayout.setMarginBottom(marginBottom);
        fl_lyric = findViewById(R.id.fl_lyric);
        tv_lyric_static = findViewById(R.id.tv_lyric_static);
        tv_lyric_static.setMovementMethod(new ScrollingMovementMethod());
        lyric_dynamic_view = findViewById(R.id.lyric_dynamic_view);
        ll_player = findViewById(R.id.ll_player);
        iv_music = findViewById(R.id.iv_music);
        tv_music_info = findViewById(R.id.tv_music_info);
        tv_music_info.setSelected(true);
        tv_accompany = findViewById(R.id.tv_accompany);
        cb_lyric = findViewById(R.id.cb_lyric);
        iv_last = findViewById(R.id.iv_last);
        fl_play = findViewById(R.id.fl_play);
        pb_play = findViewById(R.id.pb_play);
        iv_play = findViewById(R.id.iv_play);
        fl_download = findViewById(R.id.fl_download);
        tv_download = findViewById(R.id.tv_download);
        fl_loading = findViewById(R.id.fl_loading);
        iv_next = findViewById(R.id.iv_next);
        iv_back = findViewById(R.id.iv_back);


        ViewGroup.LayoutParams params = fl_lyric.getLayoutParams();
        params.height = HifiveDisplayUtils.getScreenHeight(mContext) / 4;
        fl_lyric.setLayoutParams(params);
    }

    //初始化点击事件
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        dragLayout.setDragView(iv_music, new DraggableLinearLayout.OnClickEvent() {
            @Override
            public void onClickEvent() {
                AnimationOpen();
                showDialog();
            }
        });

        tv_accompany.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                cleanTimer();
                if (playerUtils.isPlaying()) playerUtils.onStop();
                //已是伴奏模式
                if (isShowAccompany) {
                    setTypeSound();
                } else {
                    setTypeAccompany();
                }

                if (isInitialMode) {//是初始播放模式
                    //如果缓存歌曲不为空直接切换播放
                    if (musicFile != null) {
                        changePlayMusic(musicFile.getPath());
                        isInitialMode = false;
                    } else {
                        getMusicDetail(initialVersion == 1 ? 0 : 1);
                    }
                } else {
                    if (!TextUtils.isEmpty(playUrl)) {//当前歌曲URL不为空直接切换播放
                        changePlayMusic(playUrl);
                        isInitialMode = true;
                    } else {//为空暂停播放，并开始获取歌曲URL信息
                        getMusicDetail(initialVersion == 1 ? 0 : 1);
                    }
                }

            }
        });
        cb_lyric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (isStatic) {
                        fl_lyric.setVisibility(VISIBLE);
                    } else {
                        lyric_dynamic_view.setVisibility(VISIBLE);
                    }
                } else {
                    if (isStatic) {
                        fl_lyric.setVisibility(GONE);
                    } else {
                        lyric_dynamic_view.setVisibility(GONE);
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
                if (HifiveDialogManageUtil.getInstance().getCurrentList() != null
                        && HifiveDialogManageUtil.getInstance().getCurrentList().size() > 1) {
                    HifiveDialogManageUtil.getInstance().playNextMusic(mContext);
                }
            }
        });
        iv_play.setOnClickListener(new OnClickListener() {//播放
            @Override
            public void onClick(View v) {
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
                AnimationOFF();
                HifiveDialogManageUtil.getInstance().CloseDialog();
            }
        });
    }

    //获取音乐详情type ：0.伴奏1.主版本
    private void getMusicDetail(int type) {
        showLoadView();
        HifiveDialogManageUtil.getInstance().getMusicDetail(type, mContext);
    }

    //设置播放器原声模式
    private void setTypeSound() {
        tv_accompany.setText(mContext.getString(R.string.hifivesdk_music_player_accompany));
        isShowAccompany = false;
        setMusicName(HifiveDialogManageUtil.getInstance().playMusicDetail);
    }

    //设置播放器伴奏模式
    private void setTypeAccompany() {
        tv_accompany.setText(mContext.getString(R.string.hifivesdk_music_player_sound));
        isShowAccompany = true;
        setMusicName(HifiveDialogManageUtil.getInstance().accompanyDetail);
    }

    //设置歌曲名称
    private void setMusicName(HifiveMusicDetailModel playMusicDetail) {
        if (playMusicDetail != null) {
            StringBuilder info = new StringBuilder();
            if (!TextUtils.isEmpty(playMusicDetail.getMusicName())) {
                info.append(playMusicDetail.getMusicName());
            }
            if (playMusicDetail.getArtist() != null && playMusicDetail.getArtist().size() > 0) {
                for (HifiveMusicAuthorModel authorModel : playMusicDetail.getArtist()) {
                    if (info.length() > 0) {
                        info.append("-");
                    }
                    info.append(authorModel.getName());
                }
            }
            tv_music_info.setText(info.toString());
        }
    }

    //显示加载view
    private void showLoadView() {
        fl_play.setVisibility(GONE);
        fl_download.setVisibility(GONE);
        fl_loading.setVisibility(VISIBLE);
    }

    //显示播放view
    private void showPlayView() {
        fl_play.setVisibility(VISIBLE);
        fl_loading.setVisibility(GONE);
        fl_download.setVisibility(GONE);
    }

    //显示下载view
    private void showDownLoadView() {
        fl_play.setVisibility(GONE);
        fl_loading.setVisibility(GONE);
        fl_download.setVisibility(VISIBLE);
    }

    /**
     * 开始播放歌曲
     *
     * @param path    音频文件路径或者url
     * @param isStart true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private void startPlayMusic(String path, boolean isStart) {
        iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_play);
        isPlay = true;
        if (playerUtils != null) {
            if (isStart) {
                playerUtils.prepareAndPlay(path, new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        pb_play.setMax(playerUtils.duration());
                        showPlayView();
                        setPlayProgress();
                        playerUtils.play();
                    }
                });
            } else {
                //是否是播放错误后导致的暂停，播放出错暂停播放后需要重新prepare（）
                if (isError) {
                    changePlayMusic(path);
                } else {
                    playerUtils.play();
                }
            }
        }
        StartAnimationPlay();
    }

    /**
     * 开始播放歌曲
     */
    public void startPlay() {
        if (HifiveDialogManageUtil.getInstance().getPlayMusic() != null) {
            if (isShowAccompany && musicFile != null) {
                startPlayMusic(musicFile.getPath(), false);
            } else {
                startPlayMusic(playUrl, false);
            }
        }
    }

    /**
     * 切换播放歌曲
     *
     * @param path 音频文件路径或者url
     */
    private void changePlayMusic(String path) {
        //取消下载
        if (null != downLoadFileCall) {
            downLoadFileCall.cancel();
            downLoadFileCall = null;
        }
        //清空伴奏下载进度
        tv_download.setText("");

        if (playerUtils != null) {
            playerUtils.prepareAndPlay(path, new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    pb_play.setMax(playerUtils.duration());
                    showPlayView();
                    setPlayProgress();
                    playerUtils.play();
                    playerUtils.seekTo(playProgress);
                }
            });
        }
        if (!isPlay) {
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
                if (playerUtils != null && playerUtils.isPlaying()) {
                    playProgress = playerUtils.progress();
                    pb_play.setProgress(playProgress);
                    if (!isStatic) {
                        lyric_dynamic_view.setPlayProgress(playProgress);
                    }
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 100);
    }

    //清空播放进度
    public void cleanTimer() {
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
        iv_play.setImageResource(R.mipmap.hifivesdk_icon_player_suspend);
        isPlay = false;
        if (playerUtils != null && playerUtils.isPlaying())
            playerUtils.onPause();
        rotateAnimPlayTime = rotateAnim.getCurrentPlayTime();
        rotateAnim.cancel();
    }

    //当前歌曲播放错误回调
    @Override
    public void playError() {
        HifiveDialogManageUtil.getInstance().showToast(mContext, "歌曲播放出错");
        isError = true;
        stopPlay();
        clear();
        //重新播放
        updatePlayView(false);

        if (initialVersion == 1) {
            setTypeSound();
//            changePlayMusic(playUrl);
        } else {
            setTypeAccompany();
//            getMusicDetail(0);
        }
    }

    //当前歌曲播放完成回调
    @Override
    public void playCompletion() {
        if (playerUtils != null)
            playerUtils.reset();
        HifiveDialogManageUtil.getInstance().playNextMusic(mContext);//播放完成自动播放下一首
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

    //收到被观察者发出的更新通知
    @Override
    public void update(Observable o, Object arg) {
        try {
            int type = (int) arg;
            if (type == HifiveDialogManageUtil.UPDATEPALY) {
                stopPlay();
                clear();
                //隐藏歌词
                lyric_dynamic_view.setVisibility(GONE);
                fl_lyric.setVisibility(GONE);
                if (HifiveDialogManageUtil.getInstance().getPlayMusic() != null) {
                    showLoadView();
                    updateView();
                } else {//重置播放器
                    disabledLyric();
                    tv_music_info.setText("");
                    tv_accompany.setEnabled(false);
                    tv_accompany.setAlpha(0.45f);
                    if (mContext != null) {
                        RoundedCornersTransform transform = new RoundedCornersTransform(mContext, HifiveDisplayUtils.dip2px(mContext, 25));
                        Glide.with(mContext).asBitmap().load(R.mipmap.hifivesdk_icon_music_player_defaut)
                                .apply(new RequestOptions().transform(transform))
                                .into(iv_music);
                    }
                }
            } else if (type == HifiveDialogManageUtil.PALYINGMUSIC) {
                //歌词置空
                tv_lyric_static.setText("");
                lyric_dynamic_view.clearLyric();
                lyric_dynamic_view.setVisibility(GONE);
                updatePlayView(false);
            } else if (type == HifiveDialogManageUtil.PALYINGCHANGEMUSIC) {
                updatePlayView(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //切歌后清空上首歌播放相关标志和配置
    private void clear() {
        cleanTimer();
        playProgress = 0;//重置播放进度
        musicFile = null;//重置文件
        initialVersion = -1;
        isInitialMode = true;
        playUrl = "";//重置播放链接
        pb_play.setProgress(0);
        //歌词置空
        tv_lyric_static.setText("");
        lyricDetailModels = null;
        lyric_dynamic_view.clearLyric();
        //取消下载
        if (null != downLoadFileCall) {
            downLoadFileCall.cancel();
            downLoadFileCall = null;
        }
        //清空伴奏下载进度
        tv_download.setText("");

        fl_play.setVisibility(VISIBLE);
        fl_download.setVisibility(GONE);
        fl_loading.setVisibility(GONE);

        //清空动画
        if (rotateAnim != null) {
            rotateAnimPlayTime = 0;
            rotateAnim.cancel();
        }
    }

    //更新view，音乐图片，音乐名称更新
    private void updateView() {
        HifiveMusicModel playMusic = HifiveDialogManageUtil.getInstance().getPlayMusic();
        StringBuilder info = new StringBuilder();
        if (!TextUtils.isEmpty(playMusic.getMusicName())) {
            info.append(playMusic.getMusicName());
        }
        if (playMusic.getArtist() != null && playMusic.getArtist().size() > 0) {
            for (HifiveMusicAuthorModel authorModel : playMusic.getArtist()) {
                if (info.length() > 0) {
                    info.append("-");
                }
                info.append(authorModel.getName());
            }
        }
        tv_music_info.setText(info.toString());
        if (mContext != null) {
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
        if (playMusic.getVersion() != null && playMusic.getVersion().size() > 1) {
            tv_accompany.setEnabled(true);
            tv_accompany.setAlpha(1f);
        } else {
            tv_accompany.setEnabled(false);
            tv_accompany.setAlpha(0.45f);
        }
    }

    /**
     * 更新播放view，下载歌词，播放音乐isChange表示是否是切换播放模式
     *
     * @param isChangePlayMode
     */
    private void updatePlayView(boolean isChangePlayMode) {
        HifiveMusicDetailModel playMusicDetail = HifiveDialogManageUtil.getInstance().getPlayMusicDetail();
        if (playMusicDetail == null) {
            return;
        }
        //设置信息
        if (playMusicDetail.getIsMajor() == 1) {
            setTypeSound();
        } else {
            setTypeAccompany();
        }
        if (!isChangePlayMode) {
            HifiveMusiclyricModel lyric = playMusicDetail.getLyric();
            initialVersion = playMusicDetail.getIsMajor();

            //没有歌词的时候
            if (lyric == null || (TextUtils.isEmpty(lyric.getDynamicUrl()) && TextUtils.isEmpty(lyric.getStaticUrl()))) {
                disabledLyric();
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

            //切换歌曲
            if (playMusicDetail.getFile() != null
                    && !TextUtils.isEmpty(playMusicDetail.getFile().getUrl())) {
                playUrl = playMusicDetail.getFile().getUrl();
                startPlayMusic(playUrl, true);//开始播放新歌曲
            } else {
                HifiveDialogManageUtil.getInstance().showToast(mContext, "歌曲地址有误");
            }
        } else {
            isInitialMode = playMusicDetail.getIsMajor() == initialVersion;
            if (isInitialMode) {//是列表初始版本
                if (playMusicDetail.getFile() != null
                        && !TextUtils.isEmpty(playMusicDetail.getFile().getUrl())) {
                    playUrl = playMusicDetail.getFile().getUrl();
                    changePlayMusic(playUrl);//切换播放歌曲
                } else {
                    HifiveDialogManageUtil.getInstance().showToast(mContext, "歌曲地址有误");
                }
            } else {
                if (playMusicDetail.getFile() != null
                        && !TextUtils.isEmpty(playMusicDetail.getFile().getUrl())) {
                    showDownLoadView();
                    downLoadMusic(playMusicDetail.getFile().getUrl(), playMusicDetail.getMusicId(), playMusicDetail.getFile().getExt(), isChangePlayMode);
                } else {
                    HifiveDialogManageUtil.getInstance().showToast(mContext, "伴奏文件有误");
                }
            }
        }

    }

    //设置歌词点击
    private void enableLyric() {
        cb_lyric.setEnabled(true);
        cb_lyric.setAlpha(1f);
        cb_lyric.setChecked(true);
    }

    //设置歌词不能点击
    private void disabledLyric() {
        cb_lyric.setChecked(false);
        cb_lyric.setEnabled(false);
        cb_lyric.setAlpha(0.45f);
    }

    //下载歌曲
    private void downLoadMusic(String file, final String musicId, String expand, final boolean isChangePlayMode) {
        Log.e("TAG", "file==" + file);
        if (HFLiveApi.getInstance() == null)
            return;
        if (null != downLoadFileCall) {
            downLoadFileCall.cancel();
            downLoadFileCall = null;
        }
        downLoadFileCall = HFLiveApi.getInstance().downLoadFile(mContext, file, mContext.getFilesDir() + "/" + musicId + "." + expand,
                new DownLoadResponse() {
                    @Override
                    public void size(long totalBytes) {

                    }

                    @Override
                    public void succeed(@NotNull File file) {
                        //下载完成后，防止在下载过程中已经切歌，或者歌曲已经删除
                        if (HifiveDialogManageUtil.getInstance().getPlayMusicDetail() != null
                                && musicId.equals(HifiveDialogManageUtil.getInstance().getPlayMusicDetail().getMusicId())) {
                            musicFile = file;
                            downLoadFileCall = null;
                            if (isChangePlayMode) {
                                changePlayMusic(musicFile.getPath());
                            } else {
                                startPlayMusic(musicFile.getPath(), true);
                            }
                        } else {//如果已切歌就删掉文件
                            if (file.exists() && file.isFile()) {
                                if (file.delete()) Log.e("TAG", "文件删除成功");
                                else Log.e("TAG", "文件删除失败");
                            }
                            fl_play.setVisibility(VISIBLE);
                            fl_download.setVisibility(GONE);
                        }

                    }

                    @Override
                    public void fail(@NotNull String error_msg) {
                        HifiveDialogManageUtil.getInstance().showToast(mContext, error_msg);
                        stopPlay();
                        clear();
                    }

                    @Override
                    public void progress(long currentBytes, long totalBytes) {
                        if (null != downLoadFileCall && downLoadFileCall.isExecuted()) {
                            updateDownLoadProgress(currentBytes, totalBytes);
                        } else {
//                            stopPlay();
                        }
                    }
                });

    }

    //更新下载进度
    @SuppressLint("SetTextI18n")
    private void updateDownLoadProgress(final long currentBytes, final long totalBytes) {
        if (tv_download != null) {
            tv_download.setText((int) (HifiveDisplayUtils.div(currentBytes, totalBytes, 2) * 100) + "%");
        }
    }

    /**
     * 下载歌词
     *
     * @param lyricUrl 歌词的url
     * @param type     歌词类型1.动态歌词2.静态歌词
     * @author huchao
     */
    private void downloadLyric(String lyricUrl, final int type) {
        HifiveDownloadUtile.downloadFile(lyricUrl, new HifiveDownloadUtile.NetCallback() {
            @Override
            public void reqYes(Object o, String s) {
                updateLyricView(s, type);
            }

            @Override
            public void reqNo(Object o, Exception e) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disabledLyric();
                    }
                });
                HifiveDialogManageUtil.getInstance().showToast(mContext, "未找到歌词");
            }
        });
    }

    //更新歌词的view type 歌词类型1.动态歌词2.静态歌词
    private void updateLyricView(final String content, final int type) {
        if (mContext != null) {
            if (type == 1) {
                if (!TextUtils.isEmpty(content)) {
                    lyricDetailModels = HifiveDisplayUtils.getLyricDetailModels(content);
                    lyric_dynamic_view.setLyricDetailModels(lyricDetailModels);
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(content)) {
                            enableLyric();
                            lyric_dynamic_view.setVisibility(VISIBLE);
                        } else {
                            disabledLyric();
                        }
                    }
                });
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(content)) {
                            enableLyric();
                            tv_lyric_static.setText(content);
                            fl_lyric.setVisibility(VISIBLE);
                        } else {
                            disabledLyric();
                        }
                    }
                });
            }
        }
    }

    //显示歌曲列表弹窗
    public void showDialog() {
        if (dialogFragment != null && dialogFragment.getDialog() != null) {
            if (dialogFragment.getDialog().isShowing()) {
                HifiveDialogManageUtil.getInstance().CloseDialog();
            } else {
                dialogFragment.show(mContext.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
                dragLayout.updateViewY();
            }
        } else {
            dialogFragment = new HifiveMusicListDialogFragment();
            dialogFragment.show(mContext.getSupportFragmentManager(), HifiveMusicListDialogFragment.class.getSimpleName());
            dragLayout.updateViewY();
        }
    }

}
