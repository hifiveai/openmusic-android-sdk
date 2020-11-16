package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.memberOutId
import com.hifive.sdk.common.BaseConstance.Companion.societyOutId
import com.hifive.sdk.dagger.DaggerServiceComponent
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.injection.module.ServiceModule
import com.hifive.sdk.manager.HiFiveManager.Companion.APP_ID
import com.hifive.sdk.manager.HiFiveManager.Companion.SECRET
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseSubscribe
import org.json.JSONObject


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
                .request(object : BaseSubscribe<String>(response) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        response.data(t)
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
                .request(object : BaseSubscribe<String>(response) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        response.data(t)
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
                .request(object : BaseSubscribe<String>(response) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        response.data(t)
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
                .request(object : BaseSubscribe<String>(response) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        response.data(t)
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
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        memberOutId = memberId
                        societyOutId = societyId
                        val json = JSONObject(t)
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenMember = token ?: ""
                        BaseConstance.accessTokenUnion = null
                        dataResponse.data(t)
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
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        memberOutId = null
                        societyOutId = societyId
                        BaseConstance.accessTokenMember = null
                        val json = JSONObject(t)
                        val token = json.getString("accessToken")
                        BaseConstance.accessTokenUnion = token ?: ""
                        dataResponse.data(t)
                    }
                })
    }

    override fun unbindingMember(context: Context,
                                 memberId: String, societyId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.unbindMember(memberId, societyId)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
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
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
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
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
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
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getMemberSheetList(context: Context, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetList()
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getMemberSheetMusicList(context: Context, sheetId: String, language: String?, field: String?, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetMusicList(sheetId, language, field, pageSize, page)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getMusicDetail(context: Context, musicId: String, language: String?, mediaType: String, field: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMusicDetail(musicId, language, mediaType, field)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun saveMemberSheet(context: Context, sheetName: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.saveMemberSheet(sheetName)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun saveMemberSheetMusic(context: Context, sheetId: String, musicId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.saveMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun deleteMemberSheetMusic(context: Context, sheetId: String, musicId: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteMemberSheetMusic(sheetId, musicId)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun updateMusicRecord(context: Context, recordId: String, duration: String, mediaType: String, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.updateMusicRecord(recordId, duration, mediaType)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getConfigList(context: Context, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getConfigList()
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getMusicList(context: Context, searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMusicList(searchId, keyword, language, field, pageSize, page)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getSearchRecordList(context: Context, pageSize: String?, page: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getSearchRecordList(pageSize, page)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun deleteSearchRecord(context: Context, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.deleteSearchRecord()
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }

    override fun getMemberSheetMusicAll(context: Context, sheetId: String, language: String?, field: String?, dataResponse: DataResponse) {
        if (!checkNetWork(context, dataResponse)) {
            return
        }
        mService.getMemberSheetMusicAll(sheetId, language, field)
                .request(object : BaseSubscribe<String>(dataResponse) {
                    override fun onNext(t: String) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }
}




