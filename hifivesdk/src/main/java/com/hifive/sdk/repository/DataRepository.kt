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

    fun getMemberSheetList(
            language: String, reconNumb: String, field: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getMemberSheetList(language, reconNumb, field)
    }

    fun getMemberSheetMusicList(
            sheetId: String, language: String, field: String, pageSize: String, page: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getMemberSheetMusicList(sheetId, language, field, pageSize, page)
    }


    fun getMusicDetail(
            musicId: String, language: String, mediaType: String, field: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getMusicDetail(musicId, language, mediaType, field)
    }


    fun saveMemberSheet(
            sheetName: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().saveMemberSheet(sheetName)
    }


    fun saveMemberSheetMusic(
            sheetId: String, musicId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().saveMemberSheetMusic(sheetId, musicId)
    }


    fun deleteMemberSheetMusic(
            sheetId: String, musicId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().deleteMemberSheetMusic(sheetId, musicId)
    }


    fun updateMusicRecord(
            recordId: String, duration: String, mediaType: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().updateMusicRecord(recordId, duration, mediaType)
    }

    fun getConfigList(): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getConfigList()
    }

    fun getMusicList(searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getMusicList(searchId, keyword, language, field, pageSize, page)
    }

    fun getSearchRecordList(pageSize: String?, page: String?): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().getSearchRecordList(pageSize, page)
    }

    fun deleteSearchRecord(): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().deleteSearchRecord()
    }

}

