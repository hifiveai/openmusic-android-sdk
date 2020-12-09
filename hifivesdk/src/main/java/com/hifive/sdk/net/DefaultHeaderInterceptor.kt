package com.hifive.sdk.net

import android.util.Log
import com.hifive.sdk.BuildConfig
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.accessTokenMember
import com.hifive.sdk.common.BaseConstance.Companion.accessTokenUnion
import com.hifive.sdk.manager.HFLiveApi
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/5/8
 */
class DefaultHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var memberOutId = ""
        var societyOutId = ""
        val token = accessTokenMember ?: accessTokenUnion

        if (accessTokenMember != null) {
            memberOutId = BaseConstance.memberOutId ?: ""
            societyOutId = BaseConstance.societyOutId ?: ""
        }

        if (accessTokenUnion != null) {
            memberOutId = ""
            societyOutId = BaseConstance.societyOutId ?: ""
        }

        val time = System.currentTimeMillis().toString()
        val original = chain.request().newBuilder()
                .addHeader("accessToken", BaseConstance.getSignToken(HFLiveApi.SECRET!!, token
                        ?: "", time) ?: "")
                .addHeader("appId", HFLiveApi.APP_ID ?: "")
                .addHeader("timestamp", time)
        if (memberOutId.isNotBlank()) {
            original.addHeader("memberOutId", getValueEncoded(memberOutId))
        }
        if (societyOutId.isNotBlank()) {
            original.addHeader("sociatyOutId", getValueEncoded(societyOutId))
        }
        original.addHeader("X-HF-Version", getValueEncoded(BaseConstance.verison))
        Log.i("X-HF-Version","-----"+BaseConstance.verison)
        val authorised = original.build()
        return chain.proceed(authorised)

    }

    private fun getValueEncoded(value: String): String {
        val newValue = value.replace("\n", "")
        var i = 0
        val length = newValue.length
        while (i < length) {
            val c = newValue[i]
            if (c <= '\u001f' || c >= '\u007f') {
                return URLEncoder.encode(newValue, "UTF-8")
            }
            i++
        }
        return newValue
    }
}