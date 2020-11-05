package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.common.BaseConstance
import com.hifive.sdk.common.BaseConstance.Companion.memberOutId
import com.hifive.sdk.common.BaseConstance.Companion.societyOutId
import com.hifive.sdk.dagger.DaggerServiceComponent
import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.request
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.injection.module.ServiceModule
import com.hifive.sdk.manager.HiFiveManager.Companion.APP_ID
import com.hifive.sdk.manager.HiFiveManager.Companion.SECRET
import com.hifive.sdk.net.Encryption
import com.hifive.sdk.rx.BaseSubscribe


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
                .request(object : BaseSubscribe<ArrayList<SheetTagListItem>>(response) {
                    override fun onNext(t: ArrayList<SheetTagListItem>) {
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
                .request(object : BaseSubscribe<CompanySheetMusicList>(response) {
                    override fun onNext(t: CompanySheetMusicList) {
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
                .request(object : BaseSubscribe<ArrayList<CompanyChannelList>>(response) {
                    override fun onNext(t: ArrayList<CompanyChannelList>) {
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
                .request(object : BaseSubscribe<CompanySheetList>(response) {
                    override fun onNext(t: CompanySheetList) {
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
                .request(object : BaseSubscribe<Token>(dataResponse) {
                    override fun onNext(t: Token) {
                        super.onNext(t)
                        memberOutId = memberId
                        societyOutId = societyId
                        BaseConstance.accessTokenMember = t.accessToken
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
                .request(object : BaseSubscribe<Token>(dataResponse) {
                    override fun onNext(t: Token) {
                        super.onNext(t)
                        memberOutId = null
                        societyOutId = societyId
                        BaseConstance.accessTokenMember = null
                        BaseConstance.accessTokenUnion = t.accessToken
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
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
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
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
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
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
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
                .request(object : BaseSubscribe<Any>(dataResponse) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        dataResponse.data(t)
                    }
                })
    }
}




