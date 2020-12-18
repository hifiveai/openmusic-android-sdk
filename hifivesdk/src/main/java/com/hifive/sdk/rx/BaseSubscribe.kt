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
abstract class BaseSubscribe<T>(private val dataResponse: DataResponse<T>?) : ResourceSubscriber<T>() {

//    com.google.gson.JsonSyntaxException

    override fun onError(t: Throwable?) {
        t?.printStackTrace()
        when (t) {
            is BaseException -> {
                //向开发者抛出errorMsg,交给开发者处理
                HFLiveApi.callbacks?.onError(BaseException(t.status,t.msg ?: ""))
                dataResponse?.errorMsg(t.msg ?: "", t.status)
            }
            is TimeoutException -> {
                dataResponse?.errorMsg(t.message ?: "连接超时", 10002)
            }
            is HttpException -> {
                dataResponse?.errorMsg(t.message ?: "http异常", 10003)
            }
            is SocketException -> {
                dataResponse?.errorMsg(t.message ?: "链接异常", 10004)
            }
            is JSONException -> {
                dataResponse?.errorMsg(t.message ?: "JSON转换失败",10097)
            }
            is JsonSyntaxException -> {
                dataResponse?.errorMsg(t.message ?: "JSON格式不匹配", 10098)
            }
            else -> {
                dataResponse?.errorMsg(t?.message ?: "未知错误", 10099)
            }
        }

    }

    override fun onComplete() {

    }

    override fun onNext(t: T) {
        _onNext(t)
    }

    /**
     * 错误/异常回调
     *
     * @author ZhongDaFeng
     */
//    protected abstract fun _onError(e: ApiException)

    /**
     * 成功回调
     *
     * @author ZhongDaFeng
     */
    protected abstract fun _onNext(t: T)

}