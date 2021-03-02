package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.hInterface.DownLoadResponse
import com.hifive.sdk.manager.HFOpenApi
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.rx.BaseSubscribe
import com.hifive.sdk.service.impl.ServiceImpl
import com.hifive.sdk.utils.NetWorkUtils
import com.tsy.sdk.myokhttp.MyOkHttp
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author lsh
 * @date 2021-3-1
 */
class OpenManager() {

    lateinit var mContext: Context

    constructor(context: Context) : this() {
        BaseConstance.clientId = Encryption.requestDeviceId(context)
        this.mContext = context
    }

    private val mService by lazy { ServiceImpl() }

    fun baseLogin(
            Nickname: String?,
            Gender: String?,
            Birthday: String?,
            Location: String?,
            Education: String?,
            Profession: String?,
            IsOrganization: String?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?,
            response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }


        mService.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun channel(response: DataResponse<Any>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.channel()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                    response: DataResponse<Any>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.channelSheet(GroupId,Language,RecoNum,Page,PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun sheetMusic(context: Context,
                    clientId: String,
                   SheetId: String?,
                   Language: Int?,
                   Page: Int?,
                   PageSize: Int?,
                    response: DataResponse<Any>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.sheetMusic(SheetId,Language,Page,PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun searchMusic(
            TagIds: String?,
            priceFromCent: Long?,
            priceToCent: Long?,
            Location: Int?,
            Education: Int?,
            Profession: Int?,
            IsOrganization: Int?,
            Reserve: String?,
            Language: Int?,
            Page: Int?,
            PageSize: Int?,
            response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        mService.searchMusic(TagIds, priceFromCent, priceToCent, Location, Education, Profession, IsOrganization, Reserve, Language, Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun musicConfig(
                    response: DataResponse<Any>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.musicConfig()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        return mService.baseFavorite(Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.baseHot(StartTime, Duration, Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trial(MusicId: String?,
              response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trafficHQListen(MusicId, AudioFormat, AudioRate)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trafficListenMixed(MusicId: String?,
                           response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trafficListenMixed(MusicId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderMusic(Subject: String?,
                   OrderId: Long?,
                   Deadline: Int?,
                   Music: String?,
                   Language: Int?,
                   AudioFormat: String?,
                   AudioRate: String?,
                   TotalFee: Int?,
                   Remark: String?,
                   WorkId: String?,
                   response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderMusic(Subject, OrderId, Deadline, Music, Language, AudioFormat, AudioRate, TotalFee, Remark, WorkId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderDetail(OrderId: String?,
                    response: DataResponse<Any>
    ){
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderDetail(OrderId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })

    }

    fun orderAuthorization(CompanyName: String?,
                            ProjectName: String?,
                            Brand: String?,
                            Period: Int?,
                            Area: String?,
                            orderIds: String?,
                           response: DataResponse<Any>
    ){
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderAuthorization(CompanyName,ProjectName,Brand,Period,Area,orderIds)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: Int?,
                   response: DataResponse<Any>
    ){
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.baseReport(Action,TargetId, Content, Location)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderPublish(Action: Int?,
                     OrderId: String?,
                     WorkId: String?,
                     response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderPublish(Action,OrderId,WorkId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }




    fun downLoadFile(url: String,
                     path: String, response: DownLoadResponse): Call {
        val down by lazy {
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .build()
            MyOkHttp(okHttpClient)
        }

        return down.download()
                .url(url)
                .filePath(path)
                .tag(this)
                .enqueue(object : DownloadResponseHandler() {
                    override fun onStart(totalBytes: Long) {
                        response.size(totalBytes);
                    }

                    override fun onFinish(downloadFile: File) {
                        response.succeed(downloadFile)
                    }

                    override fun onProgress(currentBytes: Long, totalBytes: Long) {
                        response.progress(currentBytes, totalBytes)
                    }

                    override fun onFailure(error_msg: String) {
                        if (!error_msg.contains("Canceled")) {
                            response.fail("加载错误")
                        }
                    }
                })
    }

    fun checkNetWork(context: Context): Boolean {
        if (NetWorkUtils.isNetWorkAvailable(context)) {
            return true
        }
        //向开发者抛出errorMsg,交给开发者处理
        HFOpenApi.callbacks?.onError(BaseException(10001, "网络错误"))
        return false
    }

    init {
        BaseConstance.clientId = Encryption.requestDeviceId(HFOpenApi.hiFiveContext!!)
    }

}




