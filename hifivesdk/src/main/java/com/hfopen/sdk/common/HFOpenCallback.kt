package com.hfopen.sdk.common

import com.hfopen.sdk.rx.BaseException

/**
 * 全局错误监听
 * @author lsh
 */
interface HFOpenCallback {
    fun onError(exception : BaseException) //出错
    fun onSuccess() //完成
}