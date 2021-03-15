package com.hfopen.sdk.protocol

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
//class BaseResp<out T>(val code: Int, val msg: String, val data: T)
class BaseResp<out T>(val code: Int, val msg: String, val data: T, val taskId: String)