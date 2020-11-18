package com.hifive.sdk.manager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.hifive.sdk.controller.MusicManager

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class HiFiveManager {

    companion object {
        val gson by lazy { Gson() }

        //上下文
        @SuppressLint("StaticFieldLeak")
        var hiFiveContext: Context? = null

        //应用id
        var APP_ID: String? = null

        //密钥
        var SECRET: String? = null

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


        fun start(context: Application, APP_ID: String, SECRET: String) {
            HiFiveManager.APP_ID = APP_ID
            HiFiveManager.SECRET = SECRET
            hiFiveContext = context
        }
    }


}