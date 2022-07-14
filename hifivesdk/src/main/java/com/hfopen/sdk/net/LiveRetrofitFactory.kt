package com.hfopen.sdk.net

import android.os.Build
import com.hfopen.sdk.common.BaseConstance
import com.hfopen.sdk.utils.MySSLSocketClient
import com.hfopen.sdk.utils.RxUtils
import com.hfopen.sdk.utils.TrustAllCerts
import com.tsy.sdk.myokhttp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class LiveRetrofitFactory private constructor() {


    companion object {
        private val instance: LiveRetrofitFactory by lazy { LiveRetrofitFactory() }

        /**
         * 提供api接口
         */
        fun api() = instance.createApi(Api::class.java)
    }

    private val retrofit: Retrofit


    private val encryptionInterceptor: Interceptor

    init {
        encryptionInterceptor = object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                return chain.proceed(request)
            }

        }


        retrofit = Retrofit
            .Builder()
            .baseUrl(BaseConstance.BASE_URL_MUSIC)
            .client(initClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(HandleErrorInterceptor())
            .addInterceptor(DefaultHeaderInterceptor())
            .addInterceptor(encryptionInterceptor)
            .addInterceptor(initLogInterceptor())
            .retryOnConnectionFailure(true)
            .connectTimeout(BaseConstance.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(BaseConstance.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(BaseConstance.TIME_OUT, TimeUnit.SECONDS)
            .hostnameVerifier(MySSLSocketClient.getHostnameVerifier())
            .sslSocketFactory(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) RxUtils.createSSLSocketFactory() else MySSLSocketClient.getSSLSocketFactory(),
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) TrustAllCerts() else MySSLSocketClient.X509
            )
            .build()
    }


    private fun initLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.NONE
        return interceptor
    }


    private fun <T> createApi(api: Class<T>): T {
        return retrofit.create(api)
    }


}