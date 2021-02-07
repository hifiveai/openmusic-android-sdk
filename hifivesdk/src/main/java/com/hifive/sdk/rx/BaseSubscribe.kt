package com.hifive.sdk.rx

import com.google.gson.JsonSyntaxException
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.manager.HFOpenApi
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

    override fun onError(t: Throwable?) {
        t?.printStackTrace()
        when (t) {
            is BaseException -> {
                //向开发者抛出errorMsg,交给开发者处理
                HFOpenApi.callbacks?.onError(BaseException(t.status,t.msg ?: ""))
                dataResponse?.onError(t.msg ?: "", t.status)
            }
            is TimeoutException -> {
                HFOpenApi.callbacks?.onError(BaseException(10002,"连接超时"))
                dataResponse?.onError(t.message ?: "连接超时", 10002)
            }
            is HttpException -> {
                HFOpenApi.callbacks?.onError(BaseException(10003,"http异常"))
                dataResponse?.onError(t.message ?: "http异常", 10003)
            }
            is SocketException -> {
                HFOpenApi.callbacks?.onError(BaseException(10004,"链接异常"))
                dataResponse?.onError(t.message ?: "链接异常", 10004)
            }
            is JSONException -> {
                HFOpenApi.callbacks?.onError(BaseException(10097,"JSON转换失败"))
                dataResponse?.onError(t.message ?: "JSON转换失败",10097)
            }
            is JsonSyntaxException -> {
                HFOpenApi.callbacks?.onError(BaseException(10098,"JSON格式不匹配"))
                dataResponse?.onError(t.message ?: "JSON格式不匹配", 10098)
            }
            else -> {
                HFOpenApi.callbacks?.onError(BaseException(10099,"未知错误"))
                dataResponse?.onError(t?.message ?: "未知错误", 10099)
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