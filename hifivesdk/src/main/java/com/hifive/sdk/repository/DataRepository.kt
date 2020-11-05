package com.hifive.sdk.repository

import com.hifive.sdk.entity.*
import com.hifive.sdk.net.LiveRetrofitFactory
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import javax.inject.Inject


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class DataRepository @Inject constructor() {


    fun getCompanySheetTagList(): Flowable<BaseResp<ArrayList<SheetTagListItem>>> {
        return LiveRetrofitFactory.api().getCompanySheetTagList()
    }


    fun getCompanySheetList(
            groupId: String?,
            language: String?,
            recoNum: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<BaseResp<CompanySheetList>> {
        return LiveRetrofitFactory.api()
                .getCompanySheetList(groupId, language, recoNum, type, tagIdList, field, pageSize, page)
    }


    fun getCompanySheetMusicList(
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<BaseResp<CompanySheetMusicList>> {
        return LiveRetrofitFactory.api()
                .getCompanySheetMusicList(sheetId, language, field, pageSize, page)
    }


    fun getCompanyChannelList(
    ): Flowable<BaseResp<ArrayList<CompanyChannelList>>> {
        return LiveRetrofitFactory.api()
                .getCompanyChannelList()
    }


    fun token(
            sign: String,
            appId: String,
            memberName: String,
            memberId: String,
            societyName: String?,
            societyId: String?,
            deviceId: String,
            timestamp: String,
            headerUrl: String?,
            gender: String?,
            birthday: String?,
            location: String?,
            favoriteSinger: String?,
            phone: String?
    ): Flowable<BaseResp<Token>> {
        return LiveRetrofitFactory.api().token(
                sign,
                appId,
                memberName,
                memberId,
                societyName,
                societyId,
                deviceId,
                timestamp,
                headerUrl,
                gender,
                birthday,
                location,
                favoriteSinger,
                phone
        )

    }


    fun societyLogin(
            sign: String,
            appId: String,
            societyName: String,
            societyId: String,
            deviceId: String,
            timestamp: String
    ):
            Flowable<BaseResp<Token>> {
        return LiveRetrofitFactory.api()
                .societyLogin(sign, appId, societyName, societyId, deviceId, timestamp)
    }


    fun unbindingMember(
            memberId: String,
            societyId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().unbendingMember(
                memberId, societyId
        )
    }


    fun bind(

            memberId: String,
            societyId: String
    ): Flowable<BaseResp<Any>> {


        return LiveRetrofitFactory.api().bind(
                memberId,
                societyId
        )
    }


    fun delete(

            memberId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().delete(
                memberId
        )
    }


    fun deleteSociety(

            societyId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().deleteSociaty(
                societyId
        )
    }


}

