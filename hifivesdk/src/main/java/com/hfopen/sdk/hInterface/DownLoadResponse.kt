package com.hfopen.sdk.hInterface

import java.io.File

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
interface DownLoadResponse {
    fun size(totalBytes: Long)
    fun succeed(file: File)
    fun fail(error_msg: String)
    fun progress(currentBytes: Long, totalBytes: Long)
}