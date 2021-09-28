package com.hfopen.sdk.common

/**
 * @author lsh
 * @date 2021-3-2
 */
class BaseConstance {
    companion object {
        const val TIME_OUT: Long = 30
        //测试
//        var BASE_URL_MUSIC: String = "https://hifive-gateway-test.hifiveai.com"
        //沙箱环境
//        var BASE_URL_MUSIC: String = "https://hifive-openapi-qa.hifiveai.com"
//        预发
//        var BASE_URL_MUSIC: String = "https://hifive-gateway-pre.hifiveai.com"
        //正式
        var BASE_URL_MUSIC: String = "https://gateway.open.hifiveai.com"
        var verison : String = "V4.1.2"
        var token: String = ""
        @JvmField
        var taskId: String = ""
        const val SUCCEED_200 = 200
        const val SUCCEED_10200 = 10200

//        fun getSign(secret: String, message: String): String? {
//            var sign = ""
//            try {
//                val sha256Hmac: Mac = Mac.getInstance("HmacSHA256")
//                val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
//                sha256Hmac.init(secretKey)
//                sign = Base64.encodeToString(sha256Hmac.doFinal(message.toByteArray()), Base64.DEFAULT)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return sign
//        }
//        fun getSignToken(secret: String, token: String, time: String): String? {
//            if (token.isBlank()) return null
//            val message = token + time
//            return getSign(secret, message)?.trim()
//        }
    }




}