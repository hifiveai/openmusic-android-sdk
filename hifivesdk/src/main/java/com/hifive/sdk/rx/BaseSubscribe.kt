package com.hifive.sdk.rx

import com.google.gson.JsonSyntaxException
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.common.BaseConstance.Companion.SUCCEED
import io.reactivex.subscribers.ResourceSubscriber
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
open class BaseSubscribe<T>(private val errInfo: DataResponse?) : ResourceSubscriber<T>() {

    override fun onError(t: Throwable?) {
        t?.printStackTrace()
        when (t) {
            is BaseException -> {
                errInfo?.errorMsg(t.msg ?: "", t.status)
            }
            is TimeoutException -> {
                errInfo?.errorMsg(t.message ?: "连接超时", null)
            }
            is HttpException -> {
                errInfo?.errorMsg(t.message ?: "http异常", null)
            }
            is SocketException -> {
                errInfo?.errorMsg(t.message ?: "链接异常", null)
            }
            is JSONException -> {
                errInfo?.errorMsg(t.message ?: "JSON转换失败", null)
            }
            is JsonSyntaxException -> {
                errInfo?.errorMsg(t.message ?: "JSON格式不匹配", null)
            }
            else -> {
                errInfo?.errorMsg(t?.message ?: "未知错误", null)
            }
        }

    }

    override fun onComplete() {

    }

    override fun onNext(t: T) {
    }
}