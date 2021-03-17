package com.hf.music.manager

import android.app.Application
import android.content.*
import android.os.IBinder
import com.hf.music.config.MusicConstant
import com.hf.music.config.PlayModeEnum
import com.hf.music.model.AudioBean
import com.hf.music.playback.IjkPlayback
import com.hf.music.service.PlayService
import com.hf.music.service.PlayService.PlayBinder
import com.hf.music.utils.MusicLogUtils
import com.hf.music.utils.MusicSpUtils
import java.util.*

/**
 * 音乐播放器管理类
 */
object HFPlayer {
    private var globalContext: Application? = null

    /**
     * 播放音乐service
     */
    private var mPlayService: PlayService? = null
    private var serviceToken: ServiceToken? = null
    private val mConnectionMap = WeakHashMap<Context, ServiceConnection>()

    /**
     * 本地歌曲列表
     */
    private var mMusicList: MutableList<AudioBean>? = ArrayList()
    private var isBindService = false
    private var retryLineService = 0
    //断线重连
    private var isReconnect: Boolean = true
    //通知栏相关
    private var isOpenNotification: Boolean = false

    //最大缓冲
    private var maxBufferSize = 200 * 1024L


    @JvmStatic
    fun init(application: Application?)= apply {
        globalContext = application
    }

    fun setNotificationSwitch(isOpenNotification: Boolean)= apply  {
        this.isOpenNotification  =false
//        this.isOpenNotification  =isOpenNotification
    }

    fun setMaxBufferSize(size: Long)= apply  {
        this.maxBufferSize  =size
    }

    /**
     * 是否断线重连
     */
    fun setReconnect(reconnect: Boolean) = apply {
        this.isReconnect  =reconnect
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
        bindService()
    }

    private fun bindService() {
        if (isBindService || globalContext == null) {
            return
        }
        try {
            val contextWrapper = ContextWrapper(globalContext)
            contextWrapper.startService(Intent(contextWrapper, PlayService::class.java))
            val intent = Intent().setClass(contextWrapper, PlayService::class.java)
            val mPlayServiceConnection = PlayServiceConnection()
            if (contextWrapper.bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE)) {
                mConnectionMap[contextWrapper] = mPlayServiceConnection
                serviceToken = ServiceToken(contextWrapper)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private class PlayServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            retryLineService = 0
            isBindService = true
            mPlayService = (service as PlayBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mPlayService = null
            isBindService = false
            if (retryLineService < 3) {
                retryLineService++
                // 断开后自动再 bindService
                bindService()
            }
        }
    }

    class ServiceToken(var mWrappedContext: ContextWrapper)

    /**
     * 设置音频结合
     * @param list              音频集合
     */
    @JvmStatic
    fun setMusicList(list: List<AudioBean>?) {
        if (null != list) {
            mMusicList!!.clear()
            mMusicList!!.addAll(list)
        }
    }

    /**
     * 获取
     */
    @JvmStatic
    fun getMusicList() : MutableList<AudioBean> = mMusicList!!

    /**
     * 获取
     */
    @JvmStatic
    fun getIsReconnect() : Boolean = this.isReconnect
    @JvmStatic
    fun getIsOpenNotification() : Boolean = this.isOpenNotification
    @JvmStatic
    fun getMaxBufferSize() : Long = this.maxBufferSize

    @JvmStatic
    val playMode: Int
        get() = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0)

    @JvmStatic
    fun switchPlayMode(): Int {
        val playMode = MusicSpUtils.getInstance(MusicConstant.SP_NAME).getInt(MusicConstant.PLAY_MODE, 0)
        var mode = PlayModeEnum.valueOf(playMode)
        when (mode) {
            PlayModeEnum.LOOP -> mode = PlayModeEnum.SHUFFLE
            PlayModeEnum.SHUFFLE -> mode = PlayModeEnum.SINGLE
            PlayModeEnum.SINGLE -> mode = PlayModeEnum.LOOP
            else -> {
            }
        }
        MusicSpUtils.getInstance(MusicConstant.SP_NAME).put(MusicConstant.PLAY_MODE, mode.value())
        return mode.value()
    }

    /**
     * 获取到播放音乐的服务
     * @return              PlayService对象
     */
    @JvmStatic
    fun with(): IjkPlayback {
        return mPlayService?.playback!!
    }

    /**
     * 解除绑定
     */
    private fun unbindService() {
        if (serviceToken == null || !isBindService) {
            return
        }
        val mContextWrapper = serviceToken!!.mWrappedContext
        val mBinder = mConnectionMap[mContextWrapper] ?: return
        mContextWrapper.unbindService(mBinder)
        if (mConnectionMap.isEmpty()) {
            mPlayService = null
        }
    }

    /**
     * 对象类的全置空
     */
    @JvmStatic
    fun relese() {

        unbindService()
        globalContext = null
        mMusicList!!.clear()
        mMusicList = null
    }

}