package com.hifive.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.hifive.sdk.BuildConfig
import com.hifive.sdk.common.HFLiveCallback
import com.hifive.sdk.controller.MusicManager
import com.hifive.sdk.utils.MetaDataUtils
import com.hifive.sdk.utils.StringFilterUtils

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
        var callbacks : HFLiveCallback? = null

        fun getInstance(): MusicManager? {
            return when {
                hiFiveContext == null || APP_ID.isNullOrEmpty() || SECRET.isNullOrEmpty() -> when {
                    hiFiveContext == null -> throw IllegalArgumentException("Failed to obtain information : The Context cannot be null")
                    APP_ID.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_APPID cannot be null")
                    SECRET.isNullOrEmpty() -> throw IllegalArgumentException("Failed to obtain information : The HIFive_SECRET cannot be null")
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

        fun registerApp(application: Application?) {
            if(application == null){
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }
            hiFiveContext = application
            APP_ID = MetaDataUtils.getApplicationMetaData(application,"HIFIVE_APPID")
            SECRET = MetaDataUtils.getApplicationMetaData(application,"HIFIVE_SECRET")

        }

        fun registerApp(application: Application?,callbacks: HFLiveCallback?) {
            if(application == null){
                throw IllegalArgumentException("Failed to obtain information : The application cannot be null")
            }

            hiFiveContext = application
            HFLiveApi.callbacks = callbacks
            APP_ID = MetaDataUtils.getApplicationMetaData(application,"HIFIVE_APPID")
            SECRET = MetaDataUtils.getApplicationMetaData(application,"HIFIVE_SECRET")
            callbacks?.onSuccess()
        }

    }


}