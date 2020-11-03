package com.hifive.sdk.common

import android.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class BaseConstance {

    companion object {
        /**
         * okHttp请求超时时间
         */
        const val TIME_OUT: Long = 30
        /**
         * 主机地址
         */
        const val BASE_URL: String = "http://openmusic-api.hifiveai.com"
        const val BASE_URL_MUSIC: String = "https://hifive-gateway-test.hifiveai.com"


//        const val BASE_URL: String = "http://172.16.212.40:9093"
        /**
         * 后台返回值code 200代表成功
         */
        const val SUCCEED=200
//        const val SUCCEED = 10200


        /**
         * 加密获取签名
         * @param secret
         * @param message
         * @return
         */
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

    }

}