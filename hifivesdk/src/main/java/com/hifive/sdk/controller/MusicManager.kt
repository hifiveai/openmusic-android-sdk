package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.memberOutId
import com.hifive.sdk.common.BaseConstance.Companion.societyOutId
import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.hInterface.DownLoadResponse
import com.hifive.sdk.manager.HFLiveApi
import com.hifive.sdk.manager.HFLiveApi.Companion.APP_ID
import com.hifive.sdk.manager.HFLiveApi.Companion.SECRET
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.rx.BaseSubscribe
import com.hifive.sdk.service.impl.ServiceImpl
import com.hifive.sdk.utils.NetWorkUtils
import com.tsy.sdk.myokhttp.MyOkHttp
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler
import okhttp3.Call
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class MusicManager(val context: Context){

    private val mService by lazy { ServiceImpl() }

     fun getCompanySheetTagList(
            context: Context,
            response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getCompanySheetTagList()
                .request(object : BaseSubscribe<Any>(response) {
                    override fun _onNext(t: Any) {
                        response.data(t)
                    }
                })
    }


     fun getCompanySheetMusicAll(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
            response: DataResponse<List<HifiveMusicModel>>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getCompanySheetMusicAll(sheetId, language, field)
                .request(object : BaseSubscribe<List<HifiveMusicModel>>(response) {
                    override fun _onNext(t: List<HifiveMusicModel>) {
                         response.data(t)
                    }
                })
    }

     fun getCompanySheetMusicList(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getCompanySheetMusicList(sheetId, language, field, pageSize, page)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun getCompanyChannelList(
            context: Context,
            response: DataResponse<List<HifiveMusicChannelModel>>
    ) {
        if (!checkNetWork(context)) {
            return
        }

        mService.getCompanyChannelList()
                .request(object : BaseSubscribe<List<HifiveMusicChannelModel>>(response) {
                     override fun _onNext(t: List<HifiveMusicChannelModel>) {
                         response.data(t)
                    
                    }
                })

    }


     fun getCompanySheetList(
            context: Context,
            groupId: String?,
            language: String?,
            recoNum: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse<HifiveMusicBean<HifiveMusicSheetModel>>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getCompanySheetList(groupId, language, recoNum, type, tagIdList, field, pageSize, page)
                .request(object : BaseSubscribe<HifiveMusicBean<HifiveMusicSheetModel>>(response) {
                     override fun _onNext(t: HifiveMusicBean<HifiveMusicSheetModel>) {
                         response.data(t)
                    
                    }
                })
    }


     fun memberLogin(context: Context, memberName: String, memberId: String, societyName: String?, societyId: String?, headerUrl: String?, gender: String?, birthday: String?, location: String?, favoriteSinger: String?, phone: String?, response: DataResponse<Any>) {

        val time = System.currentTimeMillis().toString()
        val deviceId = Encryption.requestDeviceId(context)
        val message = APP_ID + memberId + deviceId + time
        val sign = BaseConstance.getSign(SECRET!!, message)?.trim()
        if (!checkNetWork(context)) {
            return
        }
        mService.token(sign
                ?: "", APP_ID
                ?: "", memberName, memberId, societyName, societyId, deviceId, time, headerUrl, gender, birthday, location, favoriteSinger, phone)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                        memberOutId = memberId
                        societyOutId = null
                        val json = JSONObject(t.toString())
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenMember = token ?: ""
                        BaseConstance.accessTokenUnion = null
                        response.data(HFLiveApi.gson.toJson(t))
                    }
                })
    }


     fun societyLogin(context: Context, societyName: String, societyId: String, response: DataResponse<Any>) {

        val deviceId = Encryption.requestDeviceId(context)
        val time = System.currentTimeMillis().toString()
        val message = APP_ID + societyId + deviceId + time
        val sign = BaseConstance.getSign(SECRET!!, message)?.trim()
        if (!checkNetWork(context)) {
            return
        }
        mService.societyLogin(sign ?: "", APP_ID!!, societyName, societyId, deviceId, time)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                        memberOutId = null
                        societyOutId = societyId
                        BaseConstance.accessTokenMember = null
                        val json = JSONObject(t.toString())
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenUnion = token ?: ""
                        response.data(HFLiveApi.gson.toJson(t))
                    }
                })
    }

     fun unbindingMember(context: Context,
                                 memberId: String, societyId: String, response: DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.unbindMember(memberId, societyId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun bindingMember(
            context: Context,
            memberId: String,
            societyId: String,
            response : DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.bind(memberId, societyId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun deleteMember(
            context: Context,
            memberId: String,
            response : DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.delete(memberId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun deleteSociety(
            context: Context,
            societyId: String,
            response : DataResponse<Any>
    ) {
        if (!checkNetWork(context)) {
            return
        }
        mService.deleteSociety(societyId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun getMemberSheetList(context: Context, page: String?, pageSize: String?, response : DataResponse<HifiveMusicBean<HifiveMusicUserSheetModel>>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getMemberSheetList(page, pageSize)
                .request(object : BaseSubscribe<HifiveMusicBean<HifiveMusicUserSheetModel>>(response) {
                     override fun _onNext(t: HifiveMusicBean<HifiveMusicUserSheetModel>) {
                         response.data(t)
                    
                    }
                })
    }

     fun getMemberSheetMusicList(context: Context, sheetId: String, language: String?, field: String?, pageSize: String?, page: String?, response: DataResponse<HifiveMusicBean<HifiveMusicModel>>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getMemberSheetMusicList(sheetId, language, field, pageSize, page)
                .request(object : BaseSubscribe<HifiveMusicBean<HifiveMusicModel>>(response) {
                     override fun _onNext(t: HifiveMusicBean<HifiveMusicModel>) {
                         response.data(t)
                    
                    }
                })
    }

     fun getMusicDetail(context: Context, musicId: String, language: String?, mediaType: String, audioFormat: String?, audioRate: String?, field: String?, response : DataResponse<HifiveMusicDetailModel>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getMusicDetail(musicId, language, mediaType, audioFormat, audioRate, field)
                .request(object : BaseSubscribe<HifiveMusicDetailModel>(response) {
                     override fun _onNext(t: HifiveMusicDetailModel) {
                         response.data(t)
                    
                    }
                })
    }

     fun saveMemberSheet(context: Context, sheetName: String, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.saveMemberSheet(sheetName)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }


     fun saveMemberSheetMusic(context: Context, sheetId: String, musicId: String, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.saveMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun deleteMemberSheetMusic(context: Context, sheetId: String, musicId: String, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.deleteMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun updateMusicRecord(context: Context, recordId: String, duration: String, mediaType: String, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.updateMusicRecord(recordId, duration, mediaType)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun getConfigList(context: Context, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getConfigList()
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun getMusicList(context: Context, searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?, response : DataResponse<HifiveMusicBean<HifiveMusicModel>>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getMusicList(searchId, keyword, language, field, pageSize, page)
                .request(object : BaseSubscribe<HifiveMusicBean<HifiveMusicModel>>(response) {
                     override fun _onNext(t: HifiveMusicBean<HifiveMusicModel>) {
                         response.data(t)
                    
                    }
                })
    }

     fun getSearchRecordList(context: Context, pageSize: String?, page: String?, response : DataResponse<HifiveMusicBean<HifiveMusicSearchrModel>>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getSearchRecordList(pageSize, page)
                .request(object : BaseSubscribe<HifiveMusicBean<HifiveMusicSearchrModel>>(response) {
                     override fun _onNext(t: HifiveMusicBean<HifiveMusicSearchrModel>) {
                         response.data(t)
                    
                    }
                })
    }

     fun deleteSearchRecord(context: Context, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.deleteSearchRecord()
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }

     fun getMemberSheetMusicAll(context: Context, sheetId: String, language: String?, field: String?, response : DataResponse<Any>) {
        if (!checkNetWork(context)) {
            return
        }
        mService.getMemberSheetMusicAll(sheetId, language, field)
                .request(object : BaseSubscribe<Any>(response) {
                     override fun _onNext(t: Any) {
                         response.data(t)
                    
                    }
                })
    }


    fun downLoadFile(context: Context, url: String,
                              path: String, response : DownLoadResponse) : Call {
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
                     override  fun onStart(totalBytes: Long) {
                        response.size(totalBytes);
                    }

                    override fun onFinish(downloadFile: File) {
                        response.succeed(downloadFile)
                    }

                    override fun onProgress(currentBytes: Long, totalBytes: Long) {
                        response.progress(currentBytes, totalBytes)
                    }

                    override fun onFailure(error_msg: String) {
                        if(!error_msg.contains("Canceled")){
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
        HFLiveApi.callbacks?.onError(BaseException(10001,"网络错误"))
        return false
    }

}




