package com.hifive.sdk.service

import io.reactivex.Flowable

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 *
 * 数据交互接口
 */
interface Service {
    fun getCompanySheetTagList(): Flowable<Any>

    fun getCompanySheetList(
            groupId: String?,
            language: String?,
            recNam: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<Any>

    fun getCompanySheetMusicList(
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?
    ): Flowable<Any>

    fun getCompanySheetMusicAll(
            sheetId: String?,
            language: String?,
            field: String?
    ): Flowable<Any>

    fun getCompanyChannelList(): Flowable<Any>
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
              phone: String?): Flowable<Any>

    fun societyLogin(
            sign: String,
            appId: String,
            societyName: String,
            societyId: String,
            deviceId: String,
            timestamp: String
    ): Flowable<Any>

    fun unbindMember(memberId: String, societyId: String): Flowable<Any>
    fun bind(memberId: String, societyId: String): Flowable<Any>
    fun delete(memberId: String): Flowable<Any>
    fun deleteSociety(societyId: String): Flowable<Any>
    fun getMemberSheetList(page: String?, pageSize: String?): Flowable<Any>
    fun getMemberSheetMusicList(sheetId: String, language: String?, field: String?, pageSize: String?, page: String?): Flowable<Any>
    fun getMusicDetail(musicId: String, language: String?, mediaType: String, audioFormat: String?, audioRate: String?, field: String?): Flowable<Any>
    fun saveMemberSheet(sheetName: String): Flowable<Any>
    fun saveMemberSheetMusic(sheetId: String, musicId: String): Flowable<Any>
    fun deleteMemberSheetMusic(sheetId: String, musicId: String): Flowable<Any>
    fun updateMusicRecord(recordId: String, duration: String, mediaType: String): Flowable<Any>
    fun getConfigList(): Flowable<Any>
    fun getMusicList(searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?): Flowable<Any>
    fun getSearchRecordList(pageSize: String?, page: String?): Flowable<Any>
    fun deleteSearchRecord(): Flowable<Any>
    fun getMemberSheetMusicAll(sheetId: String, language: String?, field: String?): Flowable<Any>

}