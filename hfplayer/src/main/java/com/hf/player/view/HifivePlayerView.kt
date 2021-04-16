package com.hf.player.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hf.player.R
import com.hf.player.utils.NoDoubleClickListener
import com.hf.player.utils.RoundedCornersTransform
import com.hf.playerkernel.config.MusicPlayAction
import com.hf.playerkernel.inter.HFPlayerEventListener
import com.hf.playerkernel.manager.HFPlayerApi.with
import com.hf.playerkernel.model.AudioBean
import com.hf.playerkernel.playback.IjkPlayback
import com.hf.playerkernel.utils.DisplayUtils
import java.util.*

/**
 * 播放器悬浮窗view
 *
 */
@SuppressLint("ViewConstructor")
open class HifivePlayerView(context: FragmentActivity, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr), Observer {
    private var marginTop = 0 //滑动范围顶部的间距限制默认为0，
    private var marginBottom = 0 //滑动范围底部的间距限制默认为0，
    private var dragLayout: DraggableLinearLayout? = null
    private var llPlayer: FrameLayout? = null
    private var ivMusic: ImageView? = null
    private var tvMusicInfo: TextView? = null
    private var tvAccompany: TextView? = null
    private var cbLyric: CheckBox? = null
    private var ivLast: ImageView? = null
    private var pbPlay: SeekBar? = null
    private var ivPlay: ImageView? = null
    private var flLoading //加载中的layout
            : FrameLayout? = null
    private var ivNext: ImageView? = null
    private var ivBack: ImageView? = null
    private var isPlay //是否正在播放
            = false
    private var isError //判断是否播放出错
            = false
    private val mContext: FragmentActivity?
    private var rotateAnim //音乐图片旋转的动画
            : ObjectAnimator? = null
    private var rotateAnimPlayTime //音乐图片旋转的动画执行时间
            : Long = 0
    private var playUrl //播放歌曲的url
            : String? = null
    private var playProgress //播放的进度
            = 0
    var hfPlayer: IjkPlayback? = null
    private var onTrackingTouch //是否在拖拽进度条
            = false
    private var isExpanded //判断是否展开
            = false

    constructor(context: FragmentActivity) : this(context, null, 0) {
    }

    fun setMargin(top: Int, bottom: Int) {
        marginTop = Math.max(top, 0)
        marginBottom = Math.max(bottom, 0)
        dragLayout!!.setMarginTop(marginTop)
        dragLayout!!.setMarginBottom(marginBottom)
    }

    //初始化view
    private fun initView() {
        dragLayout = findViewById(R.id.root)
        dragLayout!!.setMarginTop(marginTop)
        dragLayout!!.setMarginBottom(marginBottom)
        llPlayer = findViewById(R.id.ll_player)
        ivMusic = findViewById(R.id.iv_music)
        tvMusicInfo = findViewById(R.id.tv_music_info)
        tvMusicInfo!!.isSelected = true
        tvAccompany = findViewById(R.id.tv_accompany)
        cbLyric = findViewById(R.id.cb_lyric)
        ivLast = findViewById(R.id.iv_last)
        pbPlay = findViewById(R.id.pb_play)
        ivPlay = findViewById(R.id.iv_play)
        flLoading = findViewById(R.id.fl_loading)
        ivNext = findViewById(R.id.iv_next)
        ivBack = findViewById(R.id.iv_back)
    }

    //初始化点击事件
    @SuppressLint("ClickableViewAccessibility")
    private fun initEvent() {
        dragLayout!!.setDragView(ivMusic) {
            if (!playUrl.isNullOrEmpty()) {
                animationOpen()
            }
            HFPlayer.getInstance().mListener?.onClick()
        }
        tvAccompany!!.setOnClickListener(object : NoDoubleClickListener() {
            override fun onNoDoubleClick(v: View?) {}
        })
        cbLyric!!.setOnCheckedChangeListener { buttonView, isChecked -> }

        ivLast!!.setOnClickListener(object : NoDoubleClickListener(){
            override fun onNoDoubleClick(v: View?) {
                HFPlayer.getInstance().mListener?.onPre()
            }
        })
        ivNext!!.setOnClickListener(object : NoDoubleClickListener(){
            override fun onNoDoubleClick(v: View?) {
                HFPlayer.getInstance().mListener?.onNext()
            }
        })
//        ivLast!!.setOnClickListener { HFPlayer.getInstance().mListener?.onPre() }
//        ivNext!!.setOnClickListener { HFPlayer.getInstance().mListener?.onNext() }
        ivPlay!!.setOnClickListener {
            if (playUrl == null) return@setOnClickListener
            HFPlayer.getInstance().mListener?.onPlayPause(isPlay)
            if (isPlay) { //正在播放，点击就是暂停播放
                pausePlay()
            } else { //暂停点击就是播放
                startPlay()
            }
        }
        ivBack!!.setOnClickListener {
            if (isExpanded) {
                animationOFF()
            } else {
                animationOpen()
            }
        }
        pbPlay!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                onTrackingTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                onTrackingTouch = false
                if(seekBar.progress + 1000 >= seekBar.max){
                    HFPlayer.getInstance().mListener?.onComplete()
                    return
                }
                val seekTo = hfPlayer!!.seekTo(seekBar.progress)
                if (!seekTo) {
                    hfPlayer!!.pause()
                }
            }
        })
    }

    //显示加载view
    private fun showLoadView() {
        ivPlay!!.visibility = GONE
        flLoading!!.visibility = VISIBLE
    }

    //显示播放view
    private fun showPlayView() {
        ivPlay!!.visibility = VISIBLE
        flLoading!!.visibility = GONE
    }

    /**
     * 播放歌曲UrL
     * @param url
     */
    fun playWithUrl(url: String) {
        animationOpen()
        startPlayMusic(url, true)
    }

    /**
     * 设置标题
     * @param title
     */
    fun setTitle(title: String?) {
        if (tvMusicInfo != null) {
            tvMusicInfo!!.text = title
        }
    }

    /**
     * 设置图片
     */
    fun setCover(coverUrl: String?) {
        try {
            if (mContext != null) {
                val transform = RoundedCornersTransform(mContext, DisplayUtils.dip2px(mContext, 25f).toFloat())
                transform.setNeedCorner(true, true, true, true)
                if (coverUrl != null) {
                    Glide.with(mContext).asBitmap().load(coverUrl)
                            .error(R.drawable.hifivesdk_icon_music_player_defaut)
                            .placeholder(R.drawable.hifivesdk_icon_music_player_defaut)
                            .apply(RequestOptions().transform(transform))
                            .into(ivMusic!!) //四周都是圆角的圆角矩形图片。
                } else {
                    Glide.with(mContext).asBitmap().load(R.drawable.hifivesdk_icon_music_player_defaut)
                            .apply(RequestOptions().transform(transform))
                            .into(ivMusic!!) //四周都是圆角的圆角矩形图片。
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置版本信息
     * @param isMajor
     */
    fun setMajorVersion(isMajor: Boolean) {
        if (tvAccompany != null) {
            tvAccompany!!.alpha = if (isMajor) 1.0f else 0.45f
            tvAccompany!!.setText(if (isMajor) R.string.hifivesdk_music_player_sound else R.string.hifivesdk_music_player_accompany)
        }
    }

    /**
     * 移动播放器位置
     */
    fun setMarginBottom(marginBottom: Int) {
        if (dragLayout != null) {
            dragLayout!!.updateViewY(marginBottom)
        }
    }

    /**
     * 开始播放歌曲
     *
     * @param path    音频文件路径或者url
     * @param isStart true表示切歌播放新歌曲，false表示暂停后继续播放
     */
    private fun startPlayMusic(path: String, isStart: Boolean) {
        ivPlay!!.setImageResource(R.drawable.hifivesdk_icon_player_play)
        playUrl = path
        isPlay = true
        if (isStart) {
            hfPlayer!!.playWhitUrl(path)
        } else {
            //是否是播放错误后导致的暂停，播放出错暂停播放后需要重新prepare（）
            if (isError) {
                changePlayMusic(path)
            } else {
                hfPlayer!!.playPause()
            }
        }
        startAnimationPlay()
    }

    /**
     * 开始播放歌曲
     */
    private fun startPlay() {
        if (playUrl.isNullOrEmpty()) return
        startPlayMusic(playUrl!!, false)
    }

    /**
     * 切换播放歌曲
     *
     * @param path 音频文件路径或者url
     */
    private fun changePlayMusic(path: String) {
        hfPlayer!!.playWhitUrl(path)
        if (!isPlay) {
            ivPlay!!.setImageResource(R.drawable.hifivesdk_icon_player_play)
            isPlay = true
            startAnimationPlay()
        }
    }

    //开始播放动画
    @SuppressLint("ObjectAnimatorBinding")
    private fun startAnimationPlay() {
        //构造ObjectAnimator对象的方法
        ivMusic!!.clearAnimation()
        rotateAnim?.cancel()
        rotateAnim = ObjectAnimator.ofFloat(ivMusic, "rotation", 0.0f, 360.0f)
        rotateAnim!!.repeatCount = ValueAnimator.INFINITE
        rotateAnim!!.duration = 4000
        rotateAnim!!.interpolator = LinearInterpolator()
        rotateAnim!!.start()
        rotateAnim!!.currentPlayTime = rotateAnimPlayTime
    }

    //暂停播放
    fun pausePlay() {
        ivPlay!!.setImageResource(R.drawable.hifivesdk_icon_player_suspend)
        isPlay = false
        if (hfPlayer != null && hfPlayer!!.isPlaying) {
            hfPlayer!!.pause()
        }
        if (rotateAnim != null) {
            rotateAnimPlayTime = rotateAnim!!.currentPlayTime
            rotateAnim?.cancel()
            ivMusic?.clearAnimation()
        }
    }

    //停止播放
    fun stopPlay() {
        ivPlay!!.setImageResource(R.drawable.hifivesdk_icon_player_suspend)
        isPlay = false
        if (hfPlayer != null) {
            hfPlayer!!.stop()
        }
        if (rotateAnim != null) {
            rotateAnimPlayTime = rotateAnim!!.currentPlayTime
            rotateAnim?.cancel()
            ivMusic?.clearAnimation()
        }
        animationOFF()
        clear()
    }

    //播放器展开动画
    fun animationOpen() {
        val animation: Animation = HifiveViewChangeAnimation(llPlayer, DisplayUtils.getScreenWidth(mContext)
                - DisplayUtils.dip2px(mContext, 40f))
        animation.duration = 500
        llPlayer!!.startAnimation(animation)
        isExpanded = true
        ivBack?.rotation = 0f
        HFPlayer.getInstance().mListener?.onExpanded()
    }

    //播放器收起动画
    fun animationOFF() {
        val animation: Animation = HifiveViewChangeAnimation(llPlayer, DisplayUtils.dip2px(mContext, if (playUrl.isNullOrEmpty()) 50f else 80f))
        animation.duration = 500
        llPlayer!!.startAnimation(animation)
        isExpanded = false
        ivBack?.rotation = 180f
        HFPlayer.getInstance().mListener?.onFold()
    }

    //收到被观察者发出的更新通知
    override fun update(o: Observable, arg: Any) {
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //切歌后清空上首歌播放相关标志和配置
    fun clear() {
        setTitle("")
        setCover("")
        playProgress = 0 //重置播放进度
        playUrl = "" //重置播放链接
        pbPlay!!.progress = 0
        pbPlay!!.secondaryProgress = 0
        ivPlay!!.visibility = VISIBLE
        flLoading!!.visibility = GONE
        //清空动画
        if (rotateAnim != null) {
            rotateAnimPlayTime = 0
            ivMusic!!.clearAnimation()
            rotateAnim!!.cancel()
        }
    }

    private fun initPlayListener() {
        hfPlayer = with()
        hfPlayer!!.setOnPlayEventListener(object : HFPlayerEventListener {
            override fun onChange(music: AudioBean) {}
            override fun onPlayStateChanged(state: Int) {
                when (state) {
                    MusicPlayAction.STATE_IDLE -> {
                    }
                    MusicPlayAction.STATE_PAUSE -> if (hfPlayer != null && hfPlayer!!.isPlaying) {
                        pausePlay()
                    }
                    MusicPlayAction.STATE_ERROR -> {
                        isError = true
                        pausePlay()
                        clear()
                        HFPlayer.getInstance().mListener?.onError()
                    }
                    MusicPlayAction.STATE_PREPARING -> pbPlay!!.max = hfPlayer!!.duration.toInt()
                    MusicPlayAction.STATE_PLAYING -> {
                        ivPlay!!.setImageResource(R.drawable.hifivesdk_icon_player_play)
                        showPlayView()
                    }
                    MusicPlayAction.STATE_BUFFERING -> showLoadView()
                    MusicPlayAction.STATE_COMPLETE -> {
                        pausePlay()
                        clear()
                        HFPlayer.getInstance().mListener?.onComplete()
                    }
                }
            }

            override fun onProgressUpdate(progress: Int, duration: Int) {
                if (!onTrackingTouch) {
                    pbPlay!!.progress = progress
                }
            }

            override fun onBufferingUpdate(percent: Int) {
                if (!onTrackingTouch) {
                    pbPlay!!.secondaryProgress = pbPlay!!.max * percent / 100
                }
            }

            override fun onTimer(remain: Long) {}
        })
    }

    init {
        mContext = context
        inflate(mContext, R.layout.hifive_window_ijkplayer, this)
        initView()
        initPlayListener()
        initEvent()
    }
}