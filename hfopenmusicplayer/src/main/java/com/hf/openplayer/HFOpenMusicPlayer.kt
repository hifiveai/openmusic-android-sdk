package com.hf.openplayer

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.hf.player.view.HFPlayer
import com.hf.player.view.HFPlayerViewListener
import com.hf.playerkernel.manager.HFPlayerApi
import com.hf.playerkernel.utils.MusicLogUtils
import com.hfopen.sdk.entity.MusicRecord
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopenmusic.sdk.HFOpenMusic
import com.hfopenmusic.sdk.listener.HFPlayMusicListener
import com.hfopenmusic.sdk.util.HifiveDisplayUtils

/**
 * 带列表版播放器
 */
class HFOpenMusicPlayer private constructor() {
    private var globalContext: Application? = null
    private var flag = false
    private var musicId: String? = null

    /**
     * 注册api
     * @param app
     * @param clientId
     * @return
     */
    fun registerApp(app: Application, clientId: String) = apply {
        globalContext = app
        HFOpenApi.registerApp(app, clientId)
        HFPlayerApi.init(app)
    }

    @Deprecated(message = "debug")
    fun registerApp(app: Application,appid : String,code : String, clientId: String) = apply {
        globalContext = app
        HFOpenApi.registerApp(app,appid ,code,clientId)
        HFPlayerApi.init(app)
    }

    fun setMaxBufferSize(size: Long) = apply {
        HFPlayerApi.setMaxBufferSize(size)
    }

    fun setUseCache(useCache: Boolean) = apply {
        HFPlayerApi.setUseCache(useCache)
    }

    /**
     * 是否断线重连
     */
    fun setReconnect(reconnect: Boolean) = apply {
        HFPlayerApi.setReconnect(reconnect)
    }

    /**
     * 是否debug，区别就是是否打印一些内部 log
     */
    fun setDebug(debug: Boolean) = apply {
        MusicLogUtils.setIsLog(debug)
    }

    /**
     * 初始化
     */
    fun apply() {
        if (globalContext == null) {
            throw NullPointerException("context is null")
        }
        HFPlayerApi.apply()
    }

    /**
     * 显示播放器view
     * @param activity
     * @param marginTop
     * @param marginBottom
     * @return
     */
    /**
     * 显示播放器view
     * @param activity
     * @return
     */
    @JvmOverloads
    fun showPlayer(activity: FragmentActivity, marginTop: Int = 0, marginBottom: Int = 0) = apply {
        if (globalContext == null) return@apply
        HFPlayer.getInstance().showPlayer(activity, marginTop, marginBottom)
                .setListener(object : HFPlayerViewListener {
                    override fun onClick() {
                        Log.e("HFPlayerViewListener", "onClick")
                        if (flag) {
                            HFOpenMusic.getInstance().hideOpenMusic()
                            HFPlayer.getInstance().setMarginBottom(0)
                            flag = false
                        } else {
                            showMusic(activity)
                        }
                    }

                    override fun onExpanded() {
                    }

                    override fun onFold() {
                    }

                    override fun onPre() {
                        report()
                        HFOpenMusic.getInstance().playLastMusic()
                    }

                    override fun onPlayPause(isPlaying: Boolean) {}
                    override fun onNext() {
                        report()
                        HFOpenMusic.getInstance().playNextMusic()
                    }

                    override fun onComplete() {
                        report()
                        HFOpenMusic.getInstance().playNextMusic()
                    }

                    override fun onError() {
                        report()
                        HFOpenMusic.getInstance().playNextMusic()
                    }
                })
    }

    /**
     * 移除播放器
     */
    fun removePlayer() {
        HFPlayer.getInstance().removePlayer()
        HFOpenMusic.getInstance().closeOpenMusic()
    }

    /**
     * 设置音乐授权类型
     * @param type
     * @return
     */
    fun setListenType(type: String?) = apply {
        HFOpenMusic.getInstance().setListenType(type)
    }

    /**
     * 显示OpenAPI播放列表
     * @param activity  activity
     */
    private fun showMusic(activity: FragmentActivity) {
        flag = true
        HFOpenMusic.getInstance()
                .setPlayListen(object : HFPlayMusicListener {
                    override fun onPlayMusic(musicDetail: MusicRecord, url: String) {
                        play(musicDetail, url)
                    }

                    override fun onStop() {
                        HFPlayer.getInstance().stopPlay()
                        HFOpenMusic.getInstance().hideOpenMusic()
                        HFPlayer.getInstance().setMarginBottom(0)
                        flag = false
                    }

                    override fun onCloseOpenMusic() {
                        HFPlayer.getInstance().foldPlayer()
                        HFPlayer.getInstance().setMarginBottom(0)
                        flag = false
                    }
                })
                .showOpenMusic(activity)
        HFPlayer.getInstance().setMarginBottom(HifiveDisplayUtils.getPlayerHeight(activity))
    }

    /**
     * 点击列表播放歌曲
     * @param musicDetail  歌曲详情
     * @param url  歌曲播放地址
     */
    private fun play(musicDetail: MusicRecord?, url: String) {
        if (musicDetail != null) {
            musicId = musicDetail.musicId

            var title = ""
            if(musicDetail.artist != null && musicDetail.artist!!.isNotEmpty()){
                title = musicDetail.musicName +" - "+ musicDetail.artist?.get(0)?.name
            }else if(musicDetail.composer != null && musicDetail.composer!!.isNotEmpty()){
                title =  musicDetail.musicName +" - "+ musicDetail.composer?.get(0)?.name
            }
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle(title)
                    .setMajorVersion(false)
                    .setCover(musicDetail.cover!![0].url)
                    .playWithUrl(url)
        }
    }

    /**
     * 数据上报
     */
    private fun report() {
        try {
            if (musicId != null) {
                val currentPosition = HFPlayerApi.with()!!.currentPosition.toInt()
                HFOpenMusic.getInstance().reportListen(musicId, currentPosition, System.currentTimeMillis())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @Volatile
        private var mInstance: HFOpenMusicPlayer? = null

        @JvmStatic
        fun getInstance(): HFOpenMusicPlayer {
            if (mInstance == null) {
                synchronized(HFOpenMusicPlayer::class.java) {
                    if (mInstance == null) {
                        mInstance = HFOpenMusicPlayer()
                    }
                }
            }
            return mInstance!!
        }
    }
}