package com.hfopen.sdk.controller

import android.content.Context
import com.hfopen.sdk.common.BaseConstance
import com.hfopen.sdk.entity.*
import com.hfopen.sdk.ext.request
import com.hfopen.sdk.hInterface.DataResponse
import com.hfopen.sdk.hInterface.DownLoadResponse

import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.rx.BaseException
import com.hfopen.sdk.rx.BaseSubscribe
import com.hfopen.sdk.service.impl.ServiceImpl
import com.hfopen.sdk.utils.NetWorkUtils
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
            Gender: Int?,
            Birthday: Long?,
            Location: String?,
            Education: Int?,
            Profession: Int?,
            IsOrganization: Boolean?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?,
            response: DataResponse<LoginBean>?
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre)
                .request(object : BaseSubscribe<LoginBean>(response) {
                    override fun _onNext(t: LoginBean) {
                        response?.onSuccess(t, BaseConstance.taskId)
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
                        response.onSuccess(t, BaseConstance.taskId)
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
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun sheetMusic(SheetId: Long?,
                   Language: Int?,
                   Page: Int?,
                   PageSize: Int?,
                   response: DataResponse<MusicList>) {
        if (!checkNetWork(mContext)) {
            return
        }
        mService.sheetMusic(SheetId, Language, Page, PageSize)
                .request(object : BaseSubscribe<MusicList>(response) {
                    override fun _onNext(t: MusicList) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }


    fun searchMusic(TagIds: String?,
                    PriceFromCent: Long?,
                    PriceToCent: Long?,
                    BpmFrom: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    SearchFiled: String?,
                    SearchSmart: Int?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?,
                    response: DataResponse<MusicList>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        mService.searchMusic(TagIds, PriceFromCent, PriceToCent, BpmFrom, BpmTo, DurationFrom, DurationTo, Keyword, SearchFiled, SearchSmart, Language, Page, PageSize)
                .request(object : BaseSubscribe<MusicList>(response) {
                    override fun _onNext(t: MusicList) {
                        response.onSuccess(t, BaseConstance.taskId)
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
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun baseFavorite(Page: Int?,
                     PageSize: Int?,
                     response: DataResponse<MusicList>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }

        return mService.baseFavorite(Page, PageSize)
                .request(object : BaseSubscribe<MusicList>(response) {
                    override fun _onNext(t: MusicList) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun baseHot(StartTime: Long,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?,
                response: DataResponse<MusicList>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.baseHot(StartTime, Duration, Page, PageSize)
                .request(object : BaseSubscribe<MusicList>(response) {
                    override fun _onNext(t: MusicList) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }


    fun trial(Action: String,
              MusicId: String?,
              response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId, Action)
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }


    fun trafficTrial(MusicId: String?,
                     response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId, "TrafficTrial")
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun ugcTrial(MusicId: String?,
                 response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId, "UGCTrial")
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun kTrial(MusicId: String?,
               response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId, "KTrial")
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun orderTrial(MusicId: String?,
                   response: DataResponse<TrialMusic>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.trial(MusicId, "OrderTrial")
                .request(object : BaseSubscribe<TrialMusic>(response) {
                    override fun _onNext(t: TrialMusic) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun hqListen(Action: String,
                 MusicId: String?,
                 AudioFormat: String?,
                 AudioRate: String?,
                 response: DataResponse<HQListen>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.hqListen(MusicId, AudioFormat, AudioRate, Action)
                .request(object : BaseSubscribe<HQListen>(response) {
                    override fun _onNext(t: HQListen) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }


    fun trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        response: DataResponse<HQListen>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.hqListen(MusicId, AudioFormat, AudioRate, "TrafficHQListen")
                .request(object : BaseSubscribe<HQListen>(response) {
                    override fun _onNext(t: HQListen) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun ugcHQListen(MusicId: String?,
                    AudioFormat: String?,
                    AudioRate: String?,
                    response: DataResponse<HQListen>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.hqListen(MusicId, AudioFormat, AudioRate, "UGCHQListen")
                .request(object : BaseSubscribe<HQListen>(response) {
                    override fun _onNext(t: HQListen) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun kHQListen(MusicId: String?,
                  AudioFormat: String?,
                  AudioRate: String?,
                  response: DataResponse<HQListen>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.hqListen(MusicId, AudioFormat, AudioRate, "KHQListen")
                .request(object : BaseSubscribe<HQListen>(response) {
                    override fun _onNext(t: HQListen) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }


//    fun trafficListenMixed(MusicId: String?,
//                           response: DataResponse<TrafficListenMixed>
//    ) {
//        if (!checkNetWork(mContext)) {
//            return
//        }
//        return mService.trafficListenMixed(MusicId)
//                .request(object : BaseSubscribe<TrafficListenMixed>(response) {
//                    override fun _onNext(t: TrafficListenMixed) {
//                        response.onSuccess(t, BaseConstance.taskId)
//                    }
//                })
//    }

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
                        response.onSuccess(t, BaseConstance.taskId)
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
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })

    }

    fun orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           OrderIds: String?,
                           response: DataResponse<OrderAuthorization>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, OrderIds)
                .request(object : BaseSubscribe<OrderAuthorization>(response) {
                    override fun _onNext(t: OrderAuthorization) {
                        response.onSuccess(t, BaseConstance.taskId)
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
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun orderPublish(OrderId: String?,
                     WorkId: String?,
                     response: DataResponse<OrderPublish>
    ) {
        if (!checkNetWork(mContext)) {
            return
        }
        return mService.orderPublish(OrderId, WorkId)
                .request(object : BaseSubscribe<OrderPublish>(response) {
                    override fun _onNext(t: OrderPublish) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun reportListen(Action: String,
                     MusicId: String,
                     Duration: Int,
                     Timestamp: Long,
                     AudioFormat: String,
                     AudioRate: String,
                     response: DataResponse<Any>
    ) {
        return mService.report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, Action)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun trafficReportListen(MusicId: String,
                            Duration: Int,
                            Timestamp: Long,
                            AudioFormat: String,
                            AudioRate: String,
                            response: DataResponse<Any>
    ) {
        return mService.report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, "TrafficReportListen")
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun ugcReportListen(MusicId: String,
                        Duration: Int,
                        Timestamp: Long,
                        AudioFormat: String,
                        AudioRate: String,
                        response: DataResponse<Any>
    ) {
        return mService.report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, "UGCReportListen")
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t, BaseConstance.taskId)
                    }
                })
    }

    fun kReportListen(MusicId: String,
                      Duration: Int,
                      Timestamp: Long,
                      AudioFormat: String,
                      AudioRate: String,
                      response: DataResponse<Any>
    ) {
        return mService.report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, "KReportListen")
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.onSuccess(t, BaseConstance.taskId)
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




