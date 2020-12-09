package com.hifive.sdk.common

import com.hifive.sdk.rx.BaseException

/**
 * 全局错误监听
 * @author lsh
 */
interface HFLiveCallback {
    fun onError(exception : BaseException) //出错
    fun onSuccess() //完成
}