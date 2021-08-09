package com.hfopen.sdk.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val proceed = chain.proceed(request.build())
        //如果token过期 再去重新请求token 然后设置token的请求头 重新发起请求 用户无感
        if (isTokenExpired(proceed)) {
            val newHeaderToken: String = getNewToken()
            //使用新的Token，创建新的请求
            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", newHeaderToken)
                    .build()
            return chain.proceed(newRequest)
        }
        return proceed


    }

    /**
     * 根据Response，判断Token是否失效
     * 401表示token过期
     * @param response
     * @return
     */
    private fun isTokenExpired(response: Response): Boolean {
        return response.code == 406
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    @Throws(IOException::class)
    private fun getNewToken(): String {

        return ""
    }
}