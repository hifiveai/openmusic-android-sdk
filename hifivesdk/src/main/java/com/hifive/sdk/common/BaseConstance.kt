package com.hifive.sdk.common

import android.util.Base64
import com.hifive.sdk.BuildConfig
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class BaseConstance {
    companion object {
        const val TIME_OUT: Long = 30
        const val BASE_URL: String = "http://openmusic-api.hifiveai.com"
        //测试
//        const val BASE_URL_MUSIC: String = "https://hifive-gateway-test.hifiveai.com"
        //正式
        const val BASE_URL_MUSIC: String = "https://gateway.open.hifiveai.com"
        var verison : String = BuildConfig.VERSION_NAME
        var accessTokenMember: String? = null
        var accessTokenUnion: String? = null
        var memberOutId: String? = null
        var societyOutId: String? = null
        const val SUCCEED = 200

        fun getSign(secret: String, message: String): String? {
            var sign = ""
            try {
                val sha256Hmac: Mac = Mac.getInstance("HmacSHA256")
                val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
                sha256Hmac.init(secretKey)
                sign = Base64.encodeToString(sha256Hmac.doFinal(message.toByteArray()), Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return sign
        }


        fun getSignToken(secret: String, token: String, time: String): String? {
            if (token.isBlank()) return null
            val message = token + time
            return getSign(secret, message)?.trim()
        }
    }




}