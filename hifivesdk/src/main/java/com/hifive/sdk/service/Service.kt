package com.hifive.sdk.service

import io.reactivex.Flowable

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 *
 * 数据交互接口
 */
interface Service {
    fun getCompanySheetTagList(): Flowable<String>

    fun getCompanySheetList(
            groupId: String?,
            language: String?,
            recNam: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<String>


    fun getCompanySheetMusicList(
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<String>

    fun getCompanyChannelList(): Flowable<String>
    fun token(sign: String,
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
              phone: String?): Flowable<String>

    fun societyLogin(
            sign: String,
            appId: String,
            societyName: String,
            societyId: String,
            deviceId: String,
            timestamp: String
    ): Flowable<String>

    fun unbindMember(memberId: String, societyId: String): Flowable<String>
    fun bind(memberId: String, societyId: String): Flowable<String>
    fun delete(memberId: String): Flowable<String>
    fun deleteSociety(societyId: String): Flowable<String>
    fun getMemberSheetList(): Flowable<String>
    fun getMemberSheetMusicList(sheetId: String, language: String?, field: String?, pageSize: String?, page: String?): Flowable<String>
    fun getMusicDetail(musicId: String, language: String?, mediaType: String, field: String?): Flowable<String>
    fun saveMemberSheet(sheetName: String): Flowable<String>
    fun saveMemberSheetMusic(sheetId: String, musicId: String): Flowable<String>
    fun deleteMemberSheetMusic(sheetId: String, musicId: String): Flowable<String>
    fun updateMusicRecord(recordId: String, duration: String, mediaType: String): Flowable<String>
    fun getConfigList(): Flowable<String>
    fun getMusicList(searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?): Flowable<String>
    fun getSearchRecordList(pageSize: String?, page: String?): Flowable<String>
    fun deleteSearchRecord(): Flowable<String>
    fun getMemberSheetMusicAll(sheetId: String, language: String?, field: String?): Flowable<String>

}