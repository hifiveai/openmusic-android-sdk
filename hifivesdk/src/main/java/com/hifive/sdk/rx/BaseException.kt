package com.hifive.sdk.rx

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class BaseException(val code: Int?, val msg: String?, val taskId: String? = null) : Throwable()