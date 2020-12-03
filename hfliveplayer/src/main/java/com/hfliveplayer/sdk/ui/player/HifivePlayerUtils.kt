package com.hfliveplayer.sdk.ui.player;

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar
import java.io.IOException

/**
 * @author huchao
 * @date 2020/11/10
 */


class HifivePlayerUtils private constructor(){

    private var playCompletionListener: HifivePlayListener? = null
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        val instance by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ HifivePlayerUtils() }
        var isPause = false
        var isMute = false
        var soundA: Float = 1f
        var soundB: Float = 1f
    }

    /**
     * 获取播放器
     */
    private fun getMediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            val mediaPlayer by lazy { MediaPlayer() }
            this.mediaPlayer = mediaPlayer
        }
        return mediaPlayer!!
    }


    /**
     * 设置音量
     */
    fun setVolume(a: Float, b: Float) {
        getMediaPlayer().setVolume(a, b)
    }

    /**
     * 设置播放完成的回调
     */
    private fun playCompletion() {
        getMediaPlayer().setOnCompletionListener {
            Log.d("内部播放", "完成")
            playCompletionListener?.playCompletion()
        }
    }

    /**
     * 设置播放出错的回调
     */
    private fun playError() {
        getMediaPlayer().setOnErrorListener { _, a, b ->
            Log.d("内部播放", "错误 $a   $b")
            playCompletionListener?.playError()
            true
        }
    }

    /**
     * 开始缓冲播放
     */
    fun prepareAndPlay(
        url: String,
        onPreparedListener: MediaPlayer.OnPreparedListener
    ): HifivePlayerUtils {
        try {
            val play = getMediaPlayer()
            play.reset()
            play.setDataSource(url)
            play.setAudioStreamType(AudioManager.STREAM_MUSIC)
            play.setOnPreparedListener(onPreparedListener!!)
            play.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return this
    }


    /**
     * 释放资源
     */
    fun release() {
        getMediaPlayer().release()
        mediaPlayer = null
    }


    /**
     * 暂停
     */
    fun onPause(): HifivePlayerUtils {
        getMediaPlayer().pause()
        Log.d("暂停情况", "已经暂停")
        return this
    }


    /**
     * 停止
     */
    fun onStop(seekBar: SeekBar): HifivePlayerUtils {
        seekBar.progress = 0
        getMediaPlayer().stop()
        return this
    }
    /**
     * 停止
     */
    fun onStop(): HifivePlayerUtils {
        getMediaPlayer().stop()
        return this
    }

    /**
     * 从进度开始播放
     */
    fun seekTo(progress: Int): HifivePlayerUtils {
        getMediaPlayer().seekTo(progress)
        return this
    }

    /**
     * 取得总时长
     */
    fun duration(): Int = getMediaPlayer().duration


    /**
     * 获取当前进度
     */
    fun progress(): Int = getMediaPlayer().currentPosition

    /**
     * 判断是否在循环播放
     */
    fun isLoop(): Boolean = getMediaPlayer().isLooping

    /**
     * 设置循环播放
     */
    fun setLoop(loop: Boolean) {
        getMediaPlayer().isLooping = loop
    }


    /**
     * 判断是否在播放
     */
    fun isPlaying(): Boolean = getMediaPlayer().isPlaying


    /**
     * 从暂停状态变为播放状态
     */
    fun play() {
        getMediaPlayer().start()
    }

    /**
     * 重置MediaPlayer进入未初始化状态。
     */
    fun reset() = getMediaPlayer().reset()


    /**
     * 设置音频流的类型
     */
    fun streamtype(streamline: Int) = getMediaPlayer().setAudioStreamType(streamline)


    /**
     * 设置监听
     */
    fun setPlayCompletionListener(PlayCompletionListener: HifivePlayListener?) {
        this.playCompletionListener = PlayCompletionListener
        playCompletion()
        playError()
    }



}