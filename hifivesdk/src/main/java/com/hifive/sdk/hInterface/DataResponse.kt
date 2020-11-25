package com.hifive.sdk.hInterface

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
interface DataResponse {
    /**
     * sdk返回的错误
     */
    fun errorMsg(string: String, code: Int?)

    /**
     * sdk返回的数据
     */
    fun data(any: Any)
}