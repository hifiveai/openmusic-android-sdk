package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.memberOutId
import com.hifive.sdk.common.BaseConstance.Companion.societyOutId
import com.hifive.sdk.dagger.DaggerServiceComponent
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.hInterface.DownLoadResponse
import com.hifive.sdk.injection.module.ServiceModule
import com.hifive.sdk.manager.HFLiveApi
import com.hifive.sdk.manager.HFLiveApi.Companion.APP_ID
import com.hifive.sdk.manager.HFLiveApi.Companion.SECRET
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseSubscribe
import com.tsy.sdk.myokhttp.MyOkHttp
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler
import okhttp3.OkHttpClient
import org.json.JSONObject
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


    override fun getCompanySheetTagList(
            context: Context,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.getCompanySheetTagList()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }


    override fun getCompanySheetMusicList(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.getCompanySheetMusicList(sheetId, language, field, pageSize, page)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getCompanyChannelList(
            context: Context,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }

        mService.getCompanyChannelList()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })

    }


    override fun getCompanySheetList(
            context: Context,
            groupId: String?,
            language: String?,
            recoNum: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    ) {
        if (!checkNetWork(context, response)) {
            return
        }
        mService.getCompanySheetList(groupId, language, recoNum, type, tagIdList, field, pageSize, page)
                .request(object : BaseSubscribe<Any>(response) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }


    override fun memberLogin(context: Context, memberName: String, memberId: String, societyName: String?, societyId: String?, headerUrl: String?, gender: String?, birthday: String?, location: String?, favoriteSinger: String?, phone: String?, dataResponse: DataResponse) {

        val time = System.currentTimeMillis().toString()
        val deviceId = Encryption.requestDeviceId(context)
        val message = APP_ID + memberId + deviceId + time
        val sign = BaseConstance.getSign(SECRET!!, message)?.trim()
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.token(sign
                ?: "", APP_ID
                ?: "", memberName, memberId, societyName, societyId, deviceId, time, headerUrl, gender, birthday, location, favoriteSinger, phone)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        memberOutId = memberId
                        societyOutId = societyId
                        val json = JSONObject(t.toString())
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenMember = token ?: ""
                        BaseConstance.accessTokenUnion = null
                        dataResponse.data(HFLiveApi.gson.toJson(t))
                    }
                })
    }


    override fun societyLogin(context: Context, societyName: String, societyId: String, dataResponse: DataResponse) {

        val deviceId = Encryption.requestDeviceId(context)
        val time = System.currentTimeMillis().toString()
        val message = APP_ID + societyId + deviceId + time
        val sign = BaseConstance.getSign(SECRET!!, message)?.trim()
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.societyLogin(sign ?: "", APP_ID!!, societyName, societyId, deviceId, time)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        memberOutId = null
                        societyOutId = societyId
                        BaseConstance.accessTokenMember = null
                        val json = JSONObject(t.toString())
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenUnion = token ?: ""
                        dataResponse.data(HFLiveApi.gson.toJson(t))
                    }
                })
    }

    override fun unbindingMember(context: Context,
                                 memberId: String, societyId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.unbindMember(memberId, societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun bindingMember(
            context: Context,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.bind(memberId, societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun deleteMember(
            context: Context,
            memberId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.delete(memberId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun deleteSociety(
            context: Context,
            societyId: String,
            dataResponse: DataResponse
    ) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteSociety(societyId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getMemberSheetList(context: Context, page: String?, pageSize: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetList(page, pageSize)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getMemberSheetMusicList(context: Context, sheetId: String, language: String?, field: String?, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetMusicList(sheetId, language, field, pageSize, page)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getMusicDetail(context: Context, musicId: String, language: String?, mediaType: String, audioFormat: String?, audioRate: String?, field: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMusicDetail(musicId, language, mediaType, audioFormat, audioRate, field)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun saveMemberSheet(context: Context, sheetName: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.saveMemberSheet(sheetName)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }


    override fun saveMemberSheetMusic(context: Context, sheetId: String, musicId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.saveMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun deleteMemberSheetMusic(context: Context, sheetId: String, musicId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun updateMusicRecord(context: Context, recordId: String, duration: String, mediaType: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.updateMusicRecord(recordId, duration, mediaType)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getConfigList(context: Context, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getConfigList()
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getMusicList(context: Context, searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMusicList(searchId, keyword, language, field, pageSize, page)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getSearchRecordList(context: Context, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getSearchRecordList(pageSize, page)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun deleteSearchRecord(context: Context, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteSearchRecord()
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }

    override fun getMemberSheetMusicAll(context: Context, sheetId: String, language: String?, field: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetMusicAll(sheetId, language, field)
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                })
    }


    override fun downLoadFile(context: Context, url: String,
                              path: String, dataResponse: DownLoadResponse) {
        val down by lazy {
            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .build()
            MyOkHttp(okHttpClient)
        }

        down.download()
                .url(url)
                .filePath(path)
                .tag(this)
                .enqueue(object : DownloadResponseHandler() {
                    override fun onStart(totalBytes: Long) {
                        dataResponse.size(totalBytes);
                    }

                    override fun onFinish(downloadFile: File) {
                        dataResponse.succeed(downloadFile)
                    }

                    override fun onProgress(currentBytes: Long, totalBytes: Long) {
                        dataResponse.progress(currentBytes, totalBytes)
                    }

                    override fun onFailure(error_msg: String) {
                        dataResponse.fail(error_msg)
                    }
                })
    }


}




