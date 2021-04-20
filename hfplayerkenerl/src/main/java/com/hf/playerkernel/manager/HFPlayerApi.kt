package com.hf.playerkernel.manager

import android.app.Application
import android.content.*
import android.os.IBinder
import com.hf.playerkernel.config.MusicConstant
import com.hf.playerkernel.config.PlayModeEnum
import com.hf.playerkernel.inter.HFPlayerMediaCallback
import com.hf.playerkernel.model.AudioBean
import com.hf.playerkernel.notification.imageloader.DefaultImageLoader
import com.hf.playerkernel.notification.imageloader.ImageLoader
import com.hf.playerkernel.notification.imageloader.ImageLoaderStrategy
import com.hf.playerkernel.playback.IjkPlayback
import com.hf.playerkernel.service.PlayService
import com.hf.playerkernel.service.PlayService.PlayBinder
import com.hf.playerkernel.utils.MusicLogUtils
import com.hf.playerkernel.utils.MusicSpUtils
import java.util.*

/**
 * 音乐播放器管理类
 */
object HFPlayerApi {
    private var globalContext: Application? = null

    /**
     * 播放音乐service
     */
    var mPlayService: PlayService? = null
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

    //锁屏控制
    private var isOpenMediaSession: Boolean = false

    //最大缓冲
    private var maxBufferSize = 270 * 1024L

    //是否缓存
    private var useCache = false

    //图片加载相关
    private var imageStrategy: ImageLoaderStrategy? = null
    private var imageLoader: ImageLoader? = null


    private var mCallback : HFPlayerMediaCallback ?= null


    @JvmStatic
    fun init(application: Application) = apply {
        globalContext = application
    }

    fun setNotificationSwitch(isOpenNotification: Boolean) = apply {
        this.isOpenNotification  = isOpenNotification
    }

    fun setMediaSessionSwitch(isOpenMediaSession: Boolean) = apply {
        this.isOpenMediaSession  = isOpenMediaSession
    }

    fun setMaxBufferSize(size: Long) = apply {
        var tmp = size / 1024
        if (tmp > 270){
            this.maxBufferSize = size
        }
    }

    fun setUseCache(useCache: Boolean) = apply {
        this.useCache = useCache
    }

    /**
     * 是否断线重连
     */
    fun setReconnect(reconnect: Boolean) = apply {
        this.isReconnect = reconnect
    }


    /**
     * 自定义图片加载
     */
    fun setImageLoader(loader: ImageLoaderStrategy) = apply {
        this.imageStrategy = loader
    }

    /**
     * 是否debug，区别就是是否打印一些内部 log
     */
    fun setDebug(debug: Boolean) = apply {
        MusicLogUtils.setIsLog(debug)
    }

    /**
     * 设置媒体监听
     *
     * @param listener
     * @return
     */
    @JvmStatic
    fun setMediaCallback(callback : HFPlayerMediaCallback?)= apply {
        mCallback = callback
    }

    /**
     * 初始化
     */
    fun apply() {
        if (globalContext == null) {
            throw NullPointerException("context is null")
        }
        imageLoader = ImageLoader(globalContext)
        if (imageStrategy == null) {
            imageLoader?.init(DefaultImageLoader())
        } else {
            imageLoader?.init(imageStrategy!!)
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
    fun getMusicList() = mMusicList

    /**
     * 获取
     */
    @JvmStatic
    fun getIsReconnect() = this.isReconnect

    @JvmStatic
    fun getIsOpenNotification() = this.isOpenNotification

    @JvmStatic
    fun getIsOpenMediaSession() = this.isOpenMediaSession

    @JvmStatic
    fun getMaxBufferSize() = this.maxBufferSize

    @JvmStatic
    fun getIsUseCache() = this.useCache

    @JvmStatic
    fun getCallback() = this.mCallback

    /**
     * 获取图片加载器
     */
    @JvmStatic
    fun getImageLoader() = this.imageLoader

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
    fun with(): IjkPlayback? {
//        if(mPlayService == null){
//            bindService()
//        }

        if (mPlayService == null) {
            MusicLogUtils.e("PlayService" + "mPlayService is null")
            return null
        }
        return mPlayService?.playback
    }

    /**
     * 解除绑定
     */
    private fun unbindService() {
        try {
            if (serviceToken == null || !isBindService) {
                return
            }
            val mContextWrapper = serviceToken!!.mWrappedContext
            val mBinder = mConnectionMap[mContextWrapper] ?: return
            mContextWrapper.unbindService(mBinder)
            if (mConnectionMap.isEmpty()) {
                mPlayService = null
            }
        } catch (e: Exception) {
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