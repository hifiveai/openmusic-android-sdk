package com.hfopen.sdk.hInterface

import com.hfopen.sdk.rx.BaseException


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */


interface DataResponse<T> {
    /**
     * sdk返回的错误
     */
    fun onError(exception: BaseException)

    /**
     * sdk返回的数据
     */
    fun onSuccess(data: T, taskId: String)
}