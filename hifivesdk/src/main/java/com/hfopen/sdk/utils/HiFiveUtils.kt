package com.hfopen.sdk.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HiFiveUtils {
    companion object {
        fun randomString(): String {
            val dictChars = mutableListOf<Char>().apply { "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".forEach { this.add(it) } }
            val randomStr = StringBuilder().apply { (1..32).onEach { append(dictChars.random()) } }
            return randomStr.toString()
        }


        @Synchronized
        fun base64(str: String): String {
            return Base64.encodeToString(str.toByteArray(), Base64.DEFAULT)
        }


        /**
         * //2.按照规定顺序用空格拼接请求类型和公共参数构造字符串
         * //3.进行base64编码
         */
        fun headersBase64(headers: Map<String, Any>): String {
            val buffer = StringBuffer()
            buffer.append(headers["X-HF-Method"]).append(" ")
            buffer.append(headers["X-HF-Action"]).append(" ")
            buffer.append(headers["X-HF-Version"]).append(" ")
            buffer.append(headers["X-HF-AppId"]).append(" ")
            buffer.append(headers["X-HF-Nonce"]).append(" ")
            buffer.append(headers["X-HF-ClientId"]).append(" ")
            buffer.append(headers["Authorization"]).append(" ")
            buffer.append(headers["X-HF-Timestamp"])
            return base64(buffer.toString())
        }

        /**
         * a.按照参数名称的字典顺序对请求中所有的请求参数进行排序。
         * b.将编码后的参数名称和值用英文等号（=）进行连接
         * c.将等号连接得到的参数组合按步骤 1.a 排好的顺序依次使用“&”符号连接，即得到规范化请求字符串。
         */
        fun buildParam(param: Map<String, Any>): String {
            // 字典序排序
            val keys = arrayListOf<String>()
            for (key in param.keys) {
                keys.add(key)
            }
            keys.sort()
            val result = mutableListOf<String>()
            for (key in keys) {
                if (param[key] != null) {
                    result.add(key + "=" + param[key])
                }
            }

            return result.joinToString(separator = "&")
        }


        @Synchronized
        fun hmacSha1(input: String, key: String): ByteArray {
            try {
                val mac: Mac = Mac.getInstance("HmacSHA1")
                val signingKey = SecretKeySpec(key.toByteArray(), "HmacSHA1")
                mac.init(signingKey)
                return mac.doFinal(input.toByteArray())
            } catch (ignored: Exception) {
            }
            return ByteArray(0)
        }

        /**
         * Kotlin 的标准实现
         */
        fun md5Encode(data: ByteArray): String {
            try {
                //获取md5加密对象
                val instance: MessageDigest = MessageDigest.getInstance("MD5")
                //对字符串加密，返回字节数组
                val digest: ByteArray = instance.digest(data)
                var sb = StringBuffer()
                for (b in digest) {
                    //获取低八位有效值
                    var i: Int = b.toInt() and 0xff
                    //将整数转化为16进制
                    var hexString = Integer.toHexString(i)
                    if (hexString.length < 2) {
                        //如果是一位的话，补0
                        hexString = "0$hexString"
                    }
                    sb.append(hexString)
                }
                return sb.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * Kotlin 扩展函数
         */
        fun ByteArray.md5(): String {
            val bytes = MessageDigest.getInstance("MD5").digest(this)
            return bytes.hex()
        }

        fun ByteArray.hex(): String {
            return joinToString("") { "%02X".format(it) }
        }


        fun encrypt2ToMD5(data: ByteArray): String {
            // 加密后的16进制字符串
            var hexStr = ""
            try {
                // 此 MessageDigest 类为应用程序提供信息摘要算法的功能
                val md5 = MessageDigest.getInstance("MD5")
                // 转换为MD5码
                val digest = md5.digest(data)
                hexStr = digest.hex()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return hexStr
        }

    }
}