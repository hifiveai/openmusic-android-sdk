package com.hifive.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import com.google.gson.Gson
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.HFLiveCallback
import com.hifive.sdk.controller.OpenManager
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.utils.MetaDataUtils

/**
 * lsh 2021年3月1日13:42:32
 */
class HFOpenApi {

    companion object {
        val gson by lazy { Gson() }

        //上下文
        @SuppressLint("StaticFieldLeak")
        var hiFiveContext: Context? = null

        //应用id
        var APP_ID: String? = null

        //密钥
        var SERVER_CODE: String? = null

        //回调
        var callbacks: HFLiveCallback? = null

        @JvmStatic
        fun getInstance(): OpenManager? {
            return when {
                hiFiveContext == null || APP_ID.isNullOrEmpty() || SERVER_CODE.isNullOrEmpty() -> {
                    callbacks?.onError(BaseException(10000, "SDK未初始化"))
                    when {
                        hiFiveContext == null -> throw IllegalArgumentException("Failed to obtain information : The context cannot be null")
                        APP_ID.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_APPID cannot be null")
                        SERVER_CODE.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_SERVERCODE cannot be null")
                        else -> null
                    }

                }
                else -> {
                    val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                        OpenManager(hiFiveContext!!)
                    }
                    instance
                }
            }
        }

        @JvmStatic
        fun registerApp(application: Context?, APP_ID: String, SERVER_CODE: String) {
            if (application == null) {
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            HFOpenApi.APP_ID = APP_ID
            HFOpenApi.SERVER_CODE = SERVER_CODE
            hiFiveContext = application
        }

        @JvmStatic
        fun registerApp(application: Application?) {
            if (application == null) {
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            hiFiveContext = application
            APP_ID = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_APPID")
            SERVER_CODE = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_SERVERCODE")

        }

        @JvmStatic
        fun registerApp(application: Application?, domain : String? = BaseConstance.BASE_URL_MUSIC) {
            BaseConstance.BASE_URL_MUSIC = domain!!
            registerApp(application)
        }

        @JvmStatic
        fun configCallBack(callbacks: HFLiveCallback?) {
            if (hiFiveContext == null || APP_ID.isNullOrEmpty() || SERVER_CODE.isNullOrEmpty()) {
                callbacks?.onError(BaseException(10000, "SDK未初始化"))
                return
            }
            HFOpenApi.callbacks = callbacks
            callbacks?.onSuccess()
        }

    }


}