package com.hfopen.sdk.net

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Base64
import okhttp3.internal.and
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-23
 */
class Encryption {

    companion object {

        private var uniqueID: String? = null
        private val UNIQUE_ID = "UNIQUE_ID"


        /**
         * 64加密 转大写
         */
        fun eccry64(s: ByteArray): String {
            val result = Base64.encodeToString(s, Base64.DEFAULT)
            return result.toUpperCase()
        }

        /**
         * 256加密算法
         */
        fun hmacSHA256Andeccry64(KEY: String, VALUE: String): String {
            try {
                val signingKey = SecretKeySpec(KEY.toByteArray(charset("UTF-8")), "hmacSHA256")
                val mac = Mac.getInstance("hmacSHA256")
                mac.init(signingKey)
                val rawHmac = mac.doFinal(VALUE.toByteArray(charset("UTF-8")))
                val hexArray = byteArrayOf(
                        '0'.toByte(),
                        '1'.toByte(),
                        '2'.toByte(),
                        '3'.toByte(),
                        '4'.toByte(),
                        '5'.toByte(),
                        '6'.toByte(),
                        '7'.toByte(),
                        '8'.toByte(),
                        '9'.toByte(),
                        'a'.toByte(),
                        'b'.toByte(),
                        'c'.toByte(),
                        'd'.toByte(),
                        'e'.toByte(),
                        'f'.toByte()
                )
                val hexChars = ByteArray(rawHmac.size * 2)
                for (j in rawHmac.indices) {
                    val v = rawHmac[j] and 0xFF
                    hexChars[j * 2] = hexArray[v.ushr(4)]
                    hexChars[j * 2 + 1] = hexArray[v and 0x0F]
                }
                return eccry64(hexChars)
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }

        }


        /**
         * 获取设备唯一id
         *1.不需要特定权限.
         * 2.在100% Android装置（包括root过的）上，保证唯一性
         * 3.重装app之后不能取得相同唯一值
         */
        @Synchronized
        fun requestDeviceId(context: Context): String {
            if (uniqueID == null) {
                val sharedPrefs = context.getSharedPreferences(
                        UNIQUE_ID, MODE_PRIVATE
                )
                uniqueID = sharedPrefs.getString(UNIQUE_ID, null)
                if (uniqueID == null) {
                    uniqueID = UUID.randomUUID().toString()
                    val editor = sharedPrefs.edit()
                    editor.putString(UNIQUE_ID, uniqueID)
                    editor.apply()
                }
            }
            return uniqueID as String
        }


        /**
         * 生成随机数
         */
        fun getNum(): Int {
            val random = Random()
            return random.nextInt(10000)
        }
    }


    /**
     * 获取设备唯一id
     *1.不需要特定权限.
     * 2.在100% Android装置（包括root过的）上，保证唯一性
     * 3.重装app之后能取得相同唯一值
     * 4.需要权限 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
//    @SuppressLint("MissingPermission", "HardwareIds")
//    fun getDeviceId(context: Context): String {
//        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val tmDevice: String
//        val tmSerial: String
//        val androidId: String
//        tmDevice = "" + tm.deviceId
//        tmSerial = "" + tm.simSerialNumber
//        androidId = "" + android.provider.Settings.Secure.getString(context, android.provider.Settings.Secure.ANDROID_ID)
//        val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
//        return deviceUuid.toString()
//    }


}