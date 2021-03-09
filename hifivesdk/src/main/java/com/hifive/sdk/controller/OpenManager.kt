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
            response: DataResponse<LoginBean>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre)
                .request(object : BaseSubscribe<LoginBean>(response) {
                    override fun _onNext(t: LoginBean) {
                        response.onSuccess(t)
                        BaseConstance.token = t.token
                    }
                })
    }

    fun channel(response: DataResponse<ArrayList<ChannelItem>>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.channel()
                .request(object : BaseSubscribe<ArrayList<ChannelItem>>(response) {
                    override fun _onNext(t: ArrayList<ChannelItem>) {
                        response.onSuccess(t)
                    }
                })
    }

    fun channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<ChannelSheet>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.channelSheet(GroupId, Language, RecoNum, Page, PageSize)
                .request(object : BaseSubscribe<ChannelSheet>(response) {
                    override fun _onNext(t: ChannelSheet) {
                        response.onSuccess(t)
                    }
                })
    }

    fun sheetMusic(SheetId: String?,
                   Language: Int?,
                   Page: Int?,
                   PageSize: Int?,
                   response: DataResponse<SheetMusic>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.sheetMusic(SheetId, Language, Page, PageSize)
                .request(object : BaseSubscribe<SheetMusic>(response) {
                    override fun _onNext(t: SheetMusic) {
                        response.onSuccess(t)
                    }
                })
    }


    fun searchMusic(TagIds: String?,
                    priceFromCent: Long?,
                    priceToCent: Long?,
                    BpmForm: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?,
                    response: DataResponse<SearchMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        mService.searchMusic(TagIds, priceFromCent, priceToCent, BpmForm, BpmTo, DurationFrom, DurationTo, Keyword, Language, Page, PageSize)
                .request(object : BaseSubscribe<SearchMusic>(response) {
                    override fun _onNext(t: SearchMusic) {
                        response.onSuccess(t)
                    }
                })
    }

    fun musicConfig(response: DataResponse<MusicConfig>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.musicConfig()
                .request(object : BaseSubscribe<MusicConfig>(response) {
                    override fun _onNext(t: MusicConfig) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<BaseFavorite>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        return mService.baseFavorite(Page, PageSize)
                .request(object : BaseSubscribe<BaseFavorite>(response) {
                    override fun _onNext(t: BaseFavorite) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<BaseHot>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.baseHot(StartTime, Duration, Page, PageSize)
                .request(object : BaseSubscribe<BaseHot>(response) {
                    override fun _onNext(t: BaseHot) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trial(MusicId: String?,
              response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId)
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t)
                    }
                })
    }

    fun trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<TrafficHQListen>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trafficHQListen(MusicId, AudioFormat, AudioRate)
                .request(object : BaseSubscribe<TrafficHQListen>(response) {
                    override fun _onNext(t: TrafficHQListen) {
                        response.onSuccess(t)
                    }
                })
    }


    fun trafficListenMixed(MusicId: String?,
                           response: DataResponse<TrafficListenMixed>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trafficListenMixed(MusicId)
                .request(object : BaseSubscribe<TrafficListenMixed>(response) {
                    override fun _onNext(t: TrafficListenMixed) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderMusic(Subject: String?,
                   OrderId: String?,
                   Deadline: Int?,
                   Music: String?,
                   Language: Int?,
                   AudioFormat: String?,
                   AudioRate: String?,
                   TotalFee: Int?,
                   Remark: String?,
                   WorkId: String?,
                   response: DataResponse<OrderMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderMusic(Subject, OrderId, Deadline, Music, Language, AudioFormat, AudioRate, TotalFee, Remark, WorkId)
                .request(object : BaseSubscribe<OrderMusic>(response) {
                    override fun _onNext(t: OrderMusic) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderDetail(OrderId: String?,
                    response: DataResponse<OrderMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderDetail(OrderId)
                .request(object : BaseSubscribe<OrderMusic>(response) {
                    override fun _onNext(t: OrderMusic) {
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
                           response: DataResponse<OrderAuthorization>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, orderIds)
                .request(object : BaseSubscribe<OrderAuthorization>(response) {
                    override fun _onNext(t: OrderAuthorization) {
                        response.onSuccess(t)
                    }
                })
    }

    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?,
                   response: DataResponse<Any>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.baseReport(Action, TargetId, Content, Location)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t)
                    }
                })
    }

    fun orderPublish(Action: String?,
                     OrderId: String?,
                     WorkId: String?,
                     response: DataResponse<OrderPublish>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderPublish(Action, OrderId, WorkId)
                .request(object : BaseSubscribe<OrderPublish>(response) {
                    override fun _onNext(t: OrderPublish) {
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

}




