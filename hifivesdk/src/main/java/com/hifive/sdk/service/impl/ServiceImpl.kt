package com.hifive.sdk.service.impl

import com.hifive.sdk.entity.*
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

    override fun getCompanySheetTagList(): Flowable<ArrayList<SheetTagListItem>> {
        return dataRepository.getCompanySheetTagList().convert()
    }

    override fun getCompanySheetList(groupId: String?,
                                     language: String?,
                                     recoNum: String?,
                                     type: String?,
                                     tagIdList: String?,
                                     field: String?,
                                     pageSize: String?,
                                     page: String?): Flowable<CompanySheetList> {
        return dataRepository.getCompanySheetList(groupId, language, recoNum, type, tagIdList, field, pageSize, page).convert()
    }

    override fun getCompanySheetMusicList(sheetId: String?,
                                          language: String?,
                                          field: String?,
                                          pageSize: String?,
                                          page: String?): Flowable<CompanySheetMusicList> {
        return dataRepository.getCompanySheetMusicList(sheetId, language, field, pageSize, page).convert()
    }

    override fun getCompanyChannelList(): Flowable<ArrayList<CompanyChannelList>> {
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
                       phone: String?): Flowable<Token> {
        return dataRepository.token(sign, appId, memberName, memberId, societyName, societyId, deviceId, timestamp, headerUrl, gender, birthday, location, favoriteSinger, phone).convert()
    }


    override fun societyLogin(sign: String, appId: String, societyName: String, societyId: String, deviceId: String, timestamp: String): Flowable<Token> {
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

}