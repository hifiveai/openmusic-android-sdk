package com.hifive.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import com.google.gson.Gson
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.HFLiveCallback
import com.hifive.sdk.controller.MusicManager
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.utils.MetaDataUtils

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class HFLiveApi {

    companion object {
        val gson by lazy { Gson() }

        //上下文
        @SuppressLint("StaticFieldLeak")
        var hiFiveContext: Context? = null

        //应用id
        var APP_ID: String? = null

        //密钥
        var SECRET: String? = null

        //回调
        var callbacks: HFLiveCallback? = null

        @JvmStatic
        fun getInstance(): MusicManager? {
            return when {
                hiFiveContext == null || APP_ID.isNullOrEmpty() || SECRET.isNullOrEmpty() -> {
                    callbacks?.onError(BaseException(10000, "SDK未初始化"))
                    when {
                        hiFiveContext == null -> throw IllegalArgumentException("Failed to obtain information : The context cannot be null")
                        APP_ID.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_APPID cannot be null")
                        SECRET.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_SECRET cannot be null")
                        else -> null
                    }

                }
                else -> {
                    val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                        MusicManager(hiFiveContext!!)
                    }
                    instance
                }
            }
        }

        @JvmStatic
        fun registerApp(application: Context?, APP_ID: String, SECRET: String) {
            if (application == null) {
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            HFLiveApi.APP_ID = APP_ID
            HFLiveApi.SECRET = SECRET
            hiFiveContext = application
        }

        @JvmStatic
        fun registerApp(application: Application?) {
            if (application == null) {
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            hiFiveContext = application
            APP_ID = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_APPID")
            SECRET = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_SECRET")

        }

        @JvmStatic
        fun registerApp(application: Application?, domain : String = BaseConstance.BASE_URL_MUSIC) {
            if (application == null) {
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            BaseConstance.BASE_URL_MUSIC = domain
            hiFiveContext = application
            APP_ID = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_APPID")
            SECRET = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_SECRET")

        }

        @JvmStatic
        fun configCallBack(callbacks: HFLiveCallback?) {
            if (hiFiveContext == null || APP_ID.isNullOrEmpty() || SECRET.isNullOrEmpty()) {
                callbacks?.onError(BaseException(10000, "SDK未初始化"))
                return
            }
            HFLiveApi.callbacks = callbacks
            callbacks?.onSuccess()
        }

    }


}