package com.hifive.sdk.controller

import android.app.Application
import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.SUCCEED
import com.hifive.sdk.dagger.DaggerServiceComponent
import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.injection.module.ServiceModule
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.rx.BaseSubscribe
import com.hifive.sdk.widget.CircleProgressDialog
import com.tsy.sdk.myokhttp.MyOkHttp
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class MusicManager(val context: Context) : BaseController() {


    init {
        DaggerServiceComponent.builder().serviceModule(ServiceModule()).build().inject(this)
    }

    override fun creditUser(
            context: Context,
            isAnchor: Boolean,
            userId: String,
            userName: String,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.initSdk(isAnchor, userId, userName)
                .request(object : BaseSubscribe<SdkInfo>(response) {
                    override fun onNext(t: SdkInfo) {
                        super.onNext(t)
                        response.data(t)
                    }


                })
    }


    override fun musicSmallerThan(
            context: Context,
            num: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.queryPlayList(num, size, userId, roomId, mediaAction, "ASC")
                .request(object : BaseSubscribe<List<MusicInfo>>(response) {
                    override fun onNext(t: List<MusicInfo>) {
                        super.onNext(t)
                        response.data(t)
                    }
                })
    }

    override fun musicBiggerThan(
            context: Context,
            num: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            sort: String?,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.queryPlayList(num, size, userId, roomId, mediaAction, "DESC")
                .request(object : BaseSubscribe<List<MusicInfo>>(response) {
                    override fun onNext(t: List<MusicInfo>) {
                        super.onNext(t)
                        response.data(t)
                    }
                })
    }


    override fun queryPlayList(
            context: Context,
            num: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            sort: String?,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.queryPlayList(num, size, userId, roomId, mediaAction, sort)
                .request(object : BaseSubscribe<List<MusicInfo>>(response) {
                    override fun onNext(t: List<MusicInfo>) {
                        super.onNext(t)
                        response.data(t)
                    }
                })
    }


    override fun addToPlayList(
            context: Context,
            userId: String,
            roomId: String?,
            musicNo: String,
            mediaAction: String,
            response: DataResponse
    ) {
        val dialog = CircleProgressDialog(context)
        dialog.showDialog()
        if (!checkNetWork(context, response)) {
            if (dialog.isShowing) dialog.dismiss()
            return
        }
        mService.addSong(userId, roomId, musicNo, mediaAction)
                .request(object : BaseSubscribe<AddSongBean>(response) {
                    override fun onNext(t: AddSongBean) {
                        super.onNext(t)
                        response.data(t)
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        if (dialog.isShowing) dialog.dismiss()
                        when (t) {
                            is BaseException -> when (SUCCEED) {
                                t.status -> response.data(t)
                            }
                        }
                    }


                    override fun onComplete() {
                        super.onComplete()
                        if (dialog.isShowing) dialog.dismiss()
                    }
                })
    }


    override fun deleteFromPlayList(
            context: Context,
            musicNo: String,
            userId: String,
            roomId: String?,
            mediaAction: String?,
            response: DataResponse
    ) {
        val dialog = CircleProgressDialog(context)

        dialog.showDialog()
        if (!checkNetWork(context, response)) {
            if (dialog.isShowing) dialog.dismiss()
            return
        }

        mService.deleteSong(musicNo, userId, roomId, mediaAction)
                .request(object : BaseSubscribe<DeleteSongBean>(response) {
                    override fun onNext(t: DeleteSongBean) {
                        super.onNext(t)
                        response.data(t)

                    }


                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        if (dialog.isShowing) dialog.dismiss()
                        when (t) {
                            is BaseException -> when (SUCCEED) {
                                t.status -> response.data(t)
                            }
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        if (dialog.isShowing) dialog.dismiss()
                    }
                })
    }

    override fun musicTags(context: Context, current: Int?, size: Int?, response: DataResponse) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.label(current, size)
                .request(object : BaseSubscribe<List<MusicTag>>(response) {
                    override fun onNext(t: List<MusicTag>) {
                        super.onNext(t)
                        response.data(t)
                    }
                })
    }

    override fun searchMusicByTag(
            context: Context,
            current: Int?,
            size: Int?,
            tag: String,
            keyword: String,
            response: DataResponse
    ) {

        if (!checkNetWork(context, response)) {
            return
        }
        mService.searchSong(current, size, tag, keyword)
                .request(object : BaseSubscribe<List<SearchMusicInfo>>(response) {
                    override fun onNext(t: List<SearchMusicInfo>) {
                        super.onNext(t)
                        response.data(t)
                    }
                })
    }


    override fun resource(
            context: Context,
            musicNo: String,
            userId: String,
            userName: String,
            roomId: String?,
            mediaAction: String,
            response: DataResponse
    ) {
        val dialog = CircleProgressDialog(context)

        dialog.showDialog()
        if (!checkNetWork(context, response)) {
            if (dialog.isShowing) dialog.dismiss()
            return
        }
        mService.resourceAcquisition(musicNo, userId, userName, roomId, mediaAction)
                .request(object : BaseSubscribe<MusicResource>(response) {
                    override fun onNext(t: MusicResource) {
                        super.onNext(t)
                        response.data(t)
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        if (dialog.isShowing) dialog.dismiss()
                    }


                    override fun onComplete() {
                        super.onComplete()
                        if (dialog.isShowing) dialog.dismiss()
                    }
                })

    }

    override fun recommendMusic(
            context: Context,
            current: Int?,
            size: Int?,
            response: DataResponse
    ) {
        val dialog = CircleProgressDialog(context)

        dialog.showDialog()
        if (!checkNetWork(context, response)) {
            if (dialog.isShowing) dialog.dismiss()
            return
        }
        mService.recommended(current, size)
                .request(object : BaseSubscribe<List<RecommendMusic>>(response) {
                    override fun onNext(t: List<RecommendMusic>) {
                        super.onNext(t)
                        response.data(t)
                    }

                    override fun onError(t: Throwable?) {
                        super.onError(t)
                        if (dialog.isShowing) dialog.dismiss()
                    }

                    override fun onComplete() {
                        super.onComplete()
                        if (dialog.isShowing) dialog.dismiss()
                    }
                })
    }


    override fun downLoadLRC(
            context: Application,
            lrc: String,
            path: String,
            dataResponse: DataResponse
    ) {
        val down by lazy {
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    //其他配置
                    .build()
            MyOkHttp(okHttpClient)
        }

        down.download()
                .url(lrc)
                .filePath(path)
                .tag(this)
                .enqueue(object : DownloadResponseHandler() {
                    override fun onStart(totalBytes: Long) {
                    }

                    override fun onFinish(downloadFile: File) {
                        dataResponse.data(downloadFile)
                    }

                    override fun onProgress(currentBytes: Long, totalBytes: Long) {

                    }

                    override fun onFailure(error_msg: String) {
                        dataResponse.errorMsg(error_msg, null)
                    }
                })
    }


    override fun musicCount(context: Context, userId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.queryCount(userId)
                .request(object : BaseSubscribe<MusicCount>(dataResponse) {
                    override fun onNext(t: MusicCount) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun memberLogin(context: Context, secretKey: String, appId: String, memberName: String, memberId: String, societyName: String?, societyId: String?, headerUrl: String?, gender: String?, birthday: String?, location: String?, favoriteSinger: String?, phone: String?, dataResponse: DataResponse) {
        val time = System.currentTimeMillis().toString()
        val deviceId = Encryption.requestDeviceId(context)
        val message = appId + memberId + deviceId + time
        val sign = BaseConstance.getSign(secretKey, message)?.trim()
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.token(sign
                ?: "", appId, memberName, memberId, societyName, societyId, deviceId, time, headerUrl, gender, birthday, location, favoriteSinger, phone)
                .request(object : BaseSubscribe<Token>(dataResponse) {
                    override fun onNext(t: Token) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }


    override fun societyLogin(context: Context, secretKey: String, appId: String, societyName: String, societyId: String, dataResponse: DataResponse) {

        val deviceId = Encryption.requestDeviceId(context)
        val time = System.currentTimeMillis().toString()
        val message = appId + societyId + deviceId + time
        val sign = BaseConstance.getSign(secretKey, message)?.trim()
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.societyLogin(sign ?: "", appId, societyName, societyId, deviceId, time)
                .request(object : BaseSubscribe<Token>(dataResponse) {
                    override fun onNext(t: Token) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun unbindingMember(context: Context,
                                 appId: String,
                                 memberOutId: String?,
                                 societyOutId: String?,
                                 timestamp: String,
                                 memberId: String, societyId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.unbindMember(appId, memberOutId, societyOutId, timestamp, memberId, societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun bindingMember(
            context: Context,
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.bind(accessToken, appId, memberOutId, societyOutId, timestamp, memberId, societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun deleteMember(
            context: Context,
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.delete(accessToken, appId, memberOutId, societyOutId, timestamp, memberId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun deleteSociety(
            context: Context,
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            societyId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteSociaty(accessToken, appId, memberOutId, societyOutId, timestamp, societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }
}




