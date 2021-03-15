package com.hfopen.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import com.google.gson.Gson
import com.hfopen.sdk.common.BaseConstance
import com.hfopen.sdk.common.HFOpenCallback
import com.hfopen.sdk.controller.OpenManager
import com.hfopen.sdk.rx.BaseException
import com.hfopen.sdk.utils.MetaDataUtils

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

        //clientid
        var CLIENT_ID: String = ""

        //回调
        var callbacks: HFOpenCallback? = null

        @JvmStatic
        fun getInstance(): OpenManager {
            return when {
                hiFiveContext == null || APP_ID.isNullOrEmpty() || SERVER_CODE.isNullOrEmpty() -> {
                    callbacks?.onError(BaseException(10000, "SDK未初始化"))
                    when {
                        hiFiveContext == null -> throw IllegalArgumentException("Failed to obtain information : The context cannot be null")
                        APP_ID.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_APPID cannot be null")
                        SERVER_CODE.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_SERVERCODE cannot be null")
                        else -> {
                            val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                                OpenManager(hiFiveContext!!)
                            }
                            instance
                        }
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
        fun setVersion(VERSION: String)  = apply{
            if (VERSION.isEmpty()) {
                throw IllegalArgumentException("Failed to obtain information : The VERSION cannot be null")
            }
            BaseConstance.verison = VERSION
        }

        @JvmStatic
        @Deprecated("manifests")
        fun registerApp(application: Context, APP_ID: String, SERVER_CODE: String, clientId: String) {
            HFOpenApi.APP_ID = APP_ID
            HFOpenApi.SERVER_CODE = SERVER_CODE
            CLIENT_ID = clientId
            hiFiveContext = application
        }

        @JvmStatic
        fun registerApp(application: Application, clientId: String) {
            hiFiveContext = application
            CLIENT_ID = clientId
            APP_ID = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_APPID")
            SERVER_CODE = MetaDataUtils.getApplicationMetaData(application, "HIFIVE_SERVERCODE")

        }

        @JvmStatic
        fun registerApp(application: Application, CLIENT_ID: String, domain : String? = BaseConstance.BASE_URL_MUSIC) {
            BaseConstance.BASE_URL_MUSIC = domain!!
            registerApp(application,CLIENT_ID)
        }

        @JvmStatic
        fun configCallBack(callbacks: HFOpenCallback) {
//            if (hiFiveContext == null || APP_ID.isNullOrEmpty() || SERVER_CODE.isNullOrEmpty()) {
//                callbacks?.onError(BaseException(10000, "SDK未初始化"))
//                return
//            }
            HFOpenApi.callbacks = callbacks
            callbacks?.onSuccess()
        }

    }


}