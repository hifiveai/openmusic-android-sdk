package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.hInterface.DownLoadResponse
import com.hifive.sdk.manager.HFOpenApi
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.rx.BaseSubscribe
import com.hifive.sdk.service.impl.ServiceImpl
import com.hifive.sdk.utils.NetWorkUtils
import com.tsy.sdk.myokhttp.MyOkHttp
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler
import io.reactivex.Flowable
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author lsh
 * @date 2021-3-1
 */
class OpenManager(val context: Context) {

    private val mService by lazy { ServiceImpl() }

    fun baseLogin(
            context: Context,
            clientId: String,
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
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId

        mService.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun channel(context: Context,
                    clientId: String,
                    response: DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        mService.channel()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun channelSheet(context: Context,
                    clientId: String,
                     GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                    response: DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
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
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        mService.sheetMusic(SheetId,Language,Page,PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun searchMusic(
            context: Context,
            clientId: String,
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
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId

        mService.searchMusic(TagIds, priceFromCent, priceToCent, Location, Education, Profession, IsOrganization, Reserve, Language, Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun musicConfig(context: Context,
                    clientId: String,
                    response: DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        mService.musicConfig()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseFavorite(context: Context,
                     clientId: String,
                     Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId

        return mService.baseFavorite(Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseHot(context: Context,
                clientId: String,
                StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.baseHot(StartTime, Duration, Page, PageSize)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trial(context: Context,
              clientId: String,
              MusicId: String?,
              response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.trial(MusicId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun trafficHQListen(context: Context,
                        clientId: String,
                        MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.trafficHQListen(MusicId, AudioFormat, AudioRate)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trafficListenMixed(context: Context,
                           clientId: String,
                           MusicId: String?,
                           response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.trafficListenMixed(MusicId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderMusic(context: Context,
                   clientId: String,
                   Subject: String?,
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
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.orderMusic(Subject, OrderId, Deadline, Music, Language, AudioFormat, AudioRate, TotalFee, Remark, WorkId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderDetail(context: Context,
                    clientId: String,
                    OrderId: String?,
                    response: DataResponse<Any>
    ){
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.orderDetail(OrderId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })

    }

    fun orderAuthorization(context: Context,
                           clientId: String,
                           CompanyName: String?,
                            ProjectName: String?,
                            Brand: String?,
                            Period: Int?,
                            Area: String?,
                            orderIds: String?,
                           response: DataResponse<Any>
    ){
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.orderAuthorization(CompanyName,ProjectName,Brand,Period,Area,orderIds)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseReport(context: Context,
                   clientId: String,
                   Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: Int?,
                   response: DataResponse<Any>
    ){
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.baseReport(Action,TargetId, Content, Location)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderPublish(context: Context,
                     clientId: String,
                     Action: Int?,
                     OrderId: String?,
                     WorkId: String?,
                     response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        BaseConstance.clientId = clientId
        return mService.orderPublish(Action,OrderId,WorkId)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }




    fun downLoadFile(context: Context, url: String,
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

}




