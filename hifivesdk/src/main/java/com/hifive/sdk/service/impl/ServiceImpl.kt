package com.hifive.sdk.service.impl

import com.hifive.sdk.ext.convert
import com.hifive.sdk.repository.DataRepository
import com.hifive.sdk.service.Service
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class ServiceImpl @Inject constructor() : Service {


    @Inject
    lateinit var dataRepository: DataRepository

    override fun getCompanySheetTagList(): Flowable<Any> {
        return dataRepository.getCompanySheetTagList().convert()
    }

    override fun getCompanySheetList(groupId: String?,
                                     language: String?,
                                     recNam: String?,
                                     type: String?,
                                     tagIdList: String?,
                                     field: String?,
                                     pageSize: String?,
                                     page: String?): Flowable<Any> {
        return dataRepository.getCompanySheetList(groupId, language, recNam, type, tagIdList, field, pageSize, page).convert()
    }

    override fun getCompanySheetMusicList(sheetId: String?,
                                          language: String?,
                                          field: String?,
                                          pageSize: String?,
                                          page: String?): Flowable<Any> {
        return dataRepository.getCompanySheetMusicList(sheetId, language, field, pageSize, page).convert()
    }

    override fun getCompanyChannelList(): Flowable<Any> {
        return dataRepository.getCompanyChannelList().convert()
    }

    override fun token(sign: String,
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
                       phone: String?): Flowable<Any> {
        return dataRepository.token(sign, appId, memberName, memberId, societyName, societyId, deviceId, timestamp, headerUrl, gender, birthday, location, favoriteSinger, phone).convert()
    }


    override fun societyLogin(sign: String, appId: String, societyName: String, societyId: String, deviceId: String, timestamp: String): Flowable<Any> {
        return dataRepository.societyLogin(sign, appId, societyName, societyId, deviceId, timestamp).convert()

    }

    override fun unbindMember(
            memberId: String, societyId: String): Flowable<Any> {
        return dataRepository.unbindingMember(memberId, societyId).convert()

    }

    override fun bind(

            memberId: String,
            societyId: String
    ): Flowable<Any> {
        return dataRepository.bind(memberId, societyId).convert()
    }

    override fun delete(

            memberId: String
    ): Flowable<Any> {
        return dataRepository.delete(memberId).convert()
    }


    override fun deleteSociety(
            societyId: String
    ): Flowable<Any> {
        return dataRepository.deleteSociety(societyId).convert()
    }

    override fun getMemberSheetList(page: String?, pageSize: String?): Flowable<Any> {
        return dataRepository.getMemberSheetList(page, pageSize).convert()

    }

    override fun getMemberSheetMusicList(sheetId: String, language: String?, field: String?, pageSize: String?, page: String?): Flowable<Any> {
        return dataRepository.getMemberSheetMusicList(sheetId, language, field, pageSize, page).convert()

    }

    override fun getMusicDetail(musicId: String, language: String?, mediaType: String, audioFormat: String?, audioRate: String?, field: String?): Flowable<Any> {
        return dataRepository.getMusicDetail(musicId, language, mediaType, audioFormat, audioRate, field).convert()

    }

    override fun saveMemberSheet(sheetName: String): Flowable<Any> {
        return dataRepository.saveMemberSheet(sheetName).convert()

    }

    override fun saveMemberSheetMusic(sheetId: String, musicId: String): Flowable<Any> {
        return dataRepository.saveMemberSheetMusic(sheetId, musicId).convert()

    }

    override fun deleteMemberSheetMusic(sheetId: String, musicId: String): Flowable<Any> {
        return dataRepository.deleteMemberSheetMusic(sheetId, musicId).convert()

    }

    override fun updateMusicRecord(recordId: String, duration: String, mediaType: String): Flowable<Any> {
        return dataRepository.updateMusicRecord(recordId, duration, mediaType).convert()

    }

    override fun getConfigList(): Flowable<Any> {
        return dataRepository.getConfigList().convert()
    }

    override fun getMusicList(searchId: String, keyword: String?, language: String?, field: String?, pageSize: String?, page: String?): Flowable<Any> {
        return dataRepository.getMusicList(searchId, keyword, language, field, pageSize, page).convert()
    }

    override fun getSearchRecordList(pageSize: String?, page: String?): Flowable<Any> {
        return dataRepository.getSearchRecordList(pageSize, page).convert()

    }

    override fun deleteSearchRecord(): Flowable<Any> {
        return dataRepository.deleteSearchRecord().convert()

    }

    override fun getMemberSheetMusicAll(sheetId: String, language: String?, field: String?): Flowable<Any> {
        return dataRepository.getMemberSheetMusicAll(sheetId, language, field).convert()
    }


}