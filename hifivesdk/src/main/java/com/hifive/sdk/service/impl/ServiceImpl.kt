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


    override fun queryPlayList(
        num: Int?,
        size: Int?,
        userId: String,
        roomId: String?,
        mediaAction: String,
        sort: String?
    ): Flowable<List<MusicInfo>> {
        return dataRepository.queryPlayList(num, size, userId, roomId, mediaAction, sort).convert()
    }

    override fun queryCount(userId: String): Flowable<MusicCount> {
        return dataRepository.queryCount(userId).convert()
    }



    override fun initSdk(isAnchor: Boolean, userId: String, userName: String): Flowable<SdkInfo> {
        return dataRepository.initSDK(isAnchor, userId, userName).convert()
    }


    override fun addSong(
        userId: String,
        roomId: String?,
        musicNo: String,
        mediaAction: String
    ): Flowable<AddSongBean> {
        return dataRepository.addSong(userId, roomId, musicNo, mediaAction).convert()
    }

    override fun deleteSong(
        musicNo: String,
        userId: String,
        roomId: String?,
        mediaAction: String?
    ): Flowable<DeleteSongBean> {
        return dataRepository.deleteSong(musicNo, userId, roomId, mediaAction).convert()
    }

    override fun label(current: Int?, size: Int?): Flowable<List<MusicTag>> {
        return dataRepository.label(current, size).convert()
    }

    override fun searchSong(
        current: Int?,
        size: Int?,
        tag: String,
        keyword: String
    ): Flowable<List<SearchMusicInfo>> {
        return dataRepository.searchSong(current, size, tag, keyword).convert()

    }

    override fun resourceAcquisition(
        musicNo: String,
        userId: String,
        userName: String,
        roomId: String?,
        mediaAction: String
    ): Flowable<MusicResource> {
        return dataRepository.resourceAcquisition(musicNo, userId, userName, roomId, mediaAction)
                .convert()

    }


    override fun recommended(current: Int?, size: Int?): Flowable<List<RecommendMusic>> {
        return dataRepository.recommend(current, size).convert()

    }


    //开放平台
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

    override fun unbindMember(appId: String,
                              memberOutId: String?,
                              societyOutId: String?,
                              timestamp: String,
                              memberId: String, societyId: String): Flowable<Any> {
        return dataRepository.unbindingMember(appId, memberOutId, societyOutId, timestamp, memberId, societyId).convert()

    }

    override fun bind(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String,
            societyId: String
    ): Flowable<Any> {
        return dataRepository.bind(accessToken, appId, memberOutId, societyOutId, timestamp, memberId, societyId).convert()
    }

    override fun delete(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String
    ): Flowable<Any> {
        return dataRepository.delete(accessToken, appId, memberOutId, societyOutId, timestamp, memberId).convert()
    }


    override fun deleteSociaty(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            societyId: String
    ): Flowable<Any> {
        return dataRepository.deleteSociety(accessToken, appId, memberOutId, societyOutId, timestamp, societyId).convert()
    }

}