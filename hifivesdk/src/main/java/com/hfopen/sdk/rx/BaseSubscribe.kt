package com.hfopen.sdk.rx

import com.google.gson.JsonSyntaxException
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.manager.HFOpenApi
import io.reactivex.subscribers.ResourceSubscriber
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
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
                HFOpenApi.callbacks?.onError(BaseException(t.code, t.msg ?: ""))
                dataResponse?.onError(BaseException(t.code, t.msg ?: ""))
            }
            is SocketTimeoutException -> {
                HFOpenApi.callbacks?.onError(BaseException(10002, "连接超时"))
                dataResponse?.onError(BaseException(10002, "连接超时"))
            }
            is TimeoutException -> {
                HFOpenApi.callbacks?.onError(BaseException(10002, "连接超时"))
                dataResponse?.onError(BaseException(10002, "连接超时"))
            }
            is HttpException -> {
                HFOpenApi.callbacks?.onError(BaseException(10003, "http异常"))
                dataResponse?.onError(BaseException(10003, "http异常"))
            }
            is SocketException -> {
                HFOpenApi.callbacks?.onError(BaseException(10004, "链接异常"))
                dataResponse?.onError(BaseException(10004, "链接异常"))
            }
            is JSONException -> {
                HFOpenApi.callbacks?.onError(BaseException(10097, "JSON转换失败"))
                dataResponse?.onError(BaseException(10097, "JSON转换失败"))
            }
            is JsonSyntaxException -> {
                HFOpenApi.callbacks?.onError(BaseException(10098, "JSON格式不匹配"))
                dataResponse?.onError(BaseException(10098, "JSON格式不匹配"))
            }
            else -> {
                HFOpenApi.callbacks?.onError(BaseException(10099, "未知错误"))
                dataResponse?.onError(BaseException(10099, "未知错误"))
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