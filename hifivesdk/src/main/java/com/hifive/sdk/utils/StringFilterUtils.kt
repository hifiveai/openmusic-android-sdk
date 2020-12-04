package com.hifive.sdk.utils

object StringFilterUtils {
    fun nameFilter(str: String): Boolean {
        // 只允许字母、数字和汉字
        val regEx =Regex("[^a-zA-Z0-9\u4E00-\u9FA5]" )
        return str.matches(regEx)
    }

    fun idFilter(str: String): Boolean {
        // 只允许字母、数字但是注意引文符号和中文符号区别
        val regEx = Regex("[^a-zA-Z0-9]")
        return str.matches(regEx)
    }
}