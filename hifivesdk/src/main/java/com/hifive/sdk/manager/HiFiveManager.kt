package com.hifive.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hifive.sdk.controller.MusicManager
import com.hifive.sdk.net.Encryption
import java.lang.IllegalArgumentException

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class HiFiveManager {

    companion object {
        var DEBUG = true
        //上下文
        @SuppressLint("StaticFieldLeak")
        var hiFiveContext: Context? = null
        //应用id
        var APP_ID: String? = null
        //平台:(PLATFORM_IOS、PLATFORM_ANDORID、PLATFORM_WEB)
        val PLATFFORM = "PLATFORM_ANDORID"
        //包路径
        var PG: String? = null
        //随机数
        var NONCE: String? = null
        //密钥
        var SECRET: String? = null
        //设备id
        var DEVICE_ID: String? = null

        fun getInstance(): MusicManager? {
            return when {
                hiFiveContext == null || APP_ID.isNullOrEmpty() || SECRET.isNullOrEmpty() -> when {
                    hiFiveContext == null -> throw IllegalArgumentException("Failed to obtain information : The Context cannot be null")
                    APP_ID.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The APP_ID cannot be null")
                    SECRET.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information :  SECRET cannot be null")
                    else -> null
                }
                else -> {
                    val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                        MusicManager(hiFiveContext!!)
                    }
                    instance
                }
            }
        }


        /**
         * 三方开发者初始化需要的参数
         * 1. 上下文
         * 2. hiFive平台申请的应用id
         * 3. 应用包名
         * 4. 直播平台的secret
         * 5. 设备id
         */
        fun start(context: Application, APP_ID: String, SECRET: String) {
            HiFiveManager.APP_ID = APP_ID
            HiFiveManager.SECRET = SECRET
            hiFiveContext = context
            DEVICE_ID = Encryption.requestDeviceId(context)
            PG = context.packageName
        }
    }


}