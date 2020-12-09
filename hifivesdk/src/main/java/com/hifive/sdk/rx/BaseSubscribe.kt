package com.hifive.sdk.rx

import com.google.gson.JsonSyntaxException
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.manager.HFLiveApi
import io.reactivex.subscribers.ResourceSubscriber
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
open class BaseSubscribe<T>(private val dataResponse: DataResponse?) : ResourceSubscriber<T>() {

    override fun onError(t: Throwable?) {
        t?.printStackTrace()
        when (t) {
            is BaseException -> {
                //向开发者抛出errorMsg,交给开发者处理
                HFLiveApi.callbacks?.onError(BaseException(t.status,t.msg ?: ""))
                dataResponse?.errorMsg(t.msg ?: "", t.status)
            }
            is TimeoutException -> {
                dataResponse?.errorMsg(t.message ?: "连接超时", null)
            }
            is HttpException -> {
                dataResponse?.errorMsg(t.message ?: "http异常", null)
            }
            is SocketException -> {
                dataResponse?.errorMsg(t.message ?: "链接异常", null)
            }
            is JSONException -> {
                dataResponse?.errorMsg(t.message ?: "JSON转换失败", null)
            }
            is JsonSyntaxException -> {
                dataResponse?.errorMsg(t.message ?: "JSON格式不匹配", null)
            }
            else -> {
                dataResponse?.errorMsg(t?.message ?: "未知错误", null)
            }
        }

    }

    override fun onComplete() {

    }

    override fun onNext(t: T) {
        val json = HFLiveApi.gson.toJson(t)
        dataResponse?.data(json)
    }
}