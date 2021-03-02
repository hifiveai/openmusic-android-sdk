package com.hifive.sdk.hInterface


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */


interface DataResponse<T> {
    /**
     * sdk返回的错误
     */
    fun onError(string: String, code: Int?)

    /**
     * sdk返回的数据
     */
    fun onSuccess(any: T)
}