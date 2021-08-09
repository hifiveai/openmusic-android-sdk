package com.hfopen.sdk.common

/**
 * @author lsh
 * @date 2021-3-2
 */
class BaseConstance {
    companion object {
        const val TIME_OUT: Long = 30
        var BASE_URL_MUSIC: String = "https://gateway.open.hifiveai.com"
        var verison: String = "V4.1.2"
        var token: String = ""

        @JvmField
        var taskId: String = ""
        const val SUCCEED = 10200
    }
}