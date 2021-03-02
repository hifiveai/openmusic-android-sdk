package com.hifive.sdk.net

import android.util.Log
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.manager.HFOpenApi
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.utils.HiFiveUtils
import com.hifive.sdk.utils.HiFiveUtils.Companion.md5
import okhttp3.*
import java.io.IOException
import java.net.URLDecoder
import kotlin.collections.HashMap


/**
 * lsh 2021-3-1
 * 参数签名处理
 */
class DefaultHeaderInterceptor : Interceptor {

    private val headers = hashMapOf<String, Any>()
    private val commonParams: Map<String, String>? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        val time = System.currentTimeMillis().toString()
        headers["X-HF-Version"] = BaseConstance.verison
        headers["X-HF-AppId"] = HFOpenApi.APP_ID ?: ""
        headers["X-HF-Timestamp"] = time
        headers["X-HF-Nonce"] = HiFiveUtils.randomString()
        headers["X-HF-ClientId"] = BaseConstance.clientId
        headers["Authorization"] = "HF3-HMAC-SHA1"

        val request = chain.request()
        return chain.proceed(rebuildRequest(request))
    }

    @Throws(IOException::class)
    private fun rebuildRequest(request: Request): Request {
        headers["X-HF-Method"] = request.method
        val newRequest = when (request.method) {
            "POST" -> {
                rebuildPostRequest(request)
            }
            "GET" -> {
                rebuildGetRequest(request)
            }
            else -> {
                request
            }
        }

        return newRequest
    }


    /**
     * 对post请求添加统一参数
     */
    private fun rebuildPostRequest(request: Request): Request {
        val builder = request.newBuilder()
        when (request.body!! ){
            is FormBody -> { // 传统表单
                val signParams = hashMapOf<String, Any>()
                val newFormBody = FormBody.Builder()
                val oidFormBody = request.body as FormBody?
                val fieldSize = oidFormBody?.size ?: 0
                for (i in 0 until fieldSize) {
                    if(oidFormBody!!.encodedName(i) != "X-HF-Action"){
                        newFormBody.add(oidFormBody.encodedName(i), URLDecoder.decode(oidFormBody.encodedValue(i), "UTF-8"))
                    }
                    signParams[oidFormBody.encodedName(i)] = URLDecoder.decode(oidFormBody.encodedValue(i), "UTF-8")
                }
                headers["X-HF-Action"] = signParams["X-HF-Action"]!!
                //移除不必要的X-HF-Action
                signParams.remove("X-HF-Action")
                //获取sign
                sign(signParams)

                builder.method(request.method, newFormBody.build()).build()
            }
            is MultipartBody -> {

            }
            else -> {

            }
        }

        //添加header
        addHeader(builder)

        return builder.build()
    }


    /**
     * 对get请求做统一参数处理
     */
    private fun rebuildGetRequest(request: Request): Request {
        val builder = request.newBuilder()
        val url = request.url
        var encodedQuery = url.encodedQuery

        val separatorIndex = url.toString().lastIndexOf("?")
        val path = url.toString().subSequence(0,separatorIndex)

        val signParams = hashMapOf<String, Any>()
        val paramsArr = encodedQuery?.split("&")!!
        for (param in paramsArr){
            val split = param.split("=")
            signParams[split[0]] = split[1]
        }
        headers["X-HF-Action"] = signParams["X-HF-Action"]!!
        //移除不必要的X-HF-Action
        signParams.remove("X-HF-Action")
        //获取sign
        sign(signParams)
        //添加header
        addHeader(builder)
        //还原Url
        encodedQuery = HiFiveUtils.buildParam(signParams)
        val finalUrl = "$path?$encodedQuery"
        Log.e("requestUrl: ", finalUrl)
        return builder.url(finalUrl).build()
    }

    /**
     * 添加公共参数
     */
    private fun addHeader(builder: Request.Builder) {
        builder
                .addHeader("X-HF-Action", headers["X-HF-Action"].toString())
                .addHeader("X-HF-Version", headers["X-HF-Version"].toString())
                .addHeader("X-HF-AppId", HFOpenApi.APP_ID ?: "")
                .addHeader("X-HF-Timestamp", headers["X-HF-Timestamp"].toString())
                .addHeader("X-HF-Nonce", headers["X-HF-Nonce"].toString())
                .addHeader("X-HF-ClientId", headers["X-HF-ClientId"].toString())
                .addHeader("Authorization", headers["Authorization"].toString())
                .addHeader("X-HF-Token", BaseConstance.token)
    }

    /**
     * 获取公共参数
     */
    private fun sign(signParams: HashMap<String, Any>) {
        /**1.使用请求参数构造规范化的请求字符串。**/
        var param = HiFiveUtils.buildParam(signParams)

        /**
         * //2.按照规定顺序用空格拼接请求类型和公共参数构造字符串
         *  //3.进行base64编码
         **/
        val headerBase64 = HiFiveUtils.headersBase64(headers)
        /**4.将步骤 1 中构造的规范化字符串拼接步骤 3 得到的base64编码，即得到待签名的字符串**/
        param = if (param.isEmpty()) headerBase64 else "$param&$headerBase64"
        /**5.得到签名值（Signature）**/
        //a.将上一步骤得到的字符串进行base64编码  （换行符会影响加密）
        val base64String: String = HiFiveUtils.base64(param.replace("\n",""))
        //b.对步骤a的结果，通过 hmacSha1 摘要算法签名 （换行符会影响加密）
        val hmacSha1byte: ByteArray = HiFiveUtils.hmacSha1(base64String.replace("\n",""), HFOpenApi.SERVER_CODE!!)
        if (hmacSha1byte.isEmpty()) {
            //抛出异常
            throw BaseException(401, "签名错误")
        }
        //c.将上一步 b 的结果，做md5Hex处理，所得结果全部转为大写，即得到签名值（Signature）
        val signature = hmacSha1byte.md5()
        headers["Authorization"] = "HF3-HMAC-SHA1 Signature=$signature"
    }


}