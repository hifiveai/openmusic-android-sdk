package com.hifive.sdk.service

import com.hifive.sdk.entity.*
import io.reactivex.Flowable

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 *
 * 数据交互接口
 */
interface Service {
    fun initSdk(isAnchor: Boolean, userId: String, userName: String): Flowable<SdkInfo>

    fun queryPlayList(
        num: Int?,
        size: Int?,
        userId: String,
        roomId: String?,
        mediaAction: String,
        sort: String?
    ): Flowable<List<MusicInfo>>


    fun addSong(
        userId: String,
        roomId: String?,
        musicNo: String,
        mediaAction: String
    ): Flowable<AddSongBean>


    fun deleteSong(
        musicNo: String,
        userId: String,
        roomId: String?,
        mediaAction: String?
    ): Flowable<DeleteSongBean>

    fun label(current: Int?, size: Int?): Flowable<List<MusicTag>>


    fun searchSong(
        current: Int?,
        size: Int?,
        tag: String,
        keyword: String
    ): Flowable<List<SearchMusicInfo>>


    fun resourceAcquisition(
        musicNo: String,
        userId: String,
        userName: String,
        roomId: String?,
        mediaAction: String
    ): Flowable<MusicResource>


    fun recommended(
            current: Int?,
            size: Int?
    ): Flowable<List<RecommendMusic>>

    fun queryCount(userId: String): Flowable<MusicCount>


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
              phone: String?): Flowable<Token>


    fun societyLogin(
            sign: String,
            appId: String,
            societyName: String,
            societyId: String,
            deviceId: String,
            timestamp: String
    ): Flowable<Token>


    fun unbindMember(
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String, societyId: String
    ): Flowable<Any>


    fun bind(accessToken: String,
             appId: String,
             memberOutId: String?,
             societyOutId: String?,
             timestamp: String,
             memberId: String,
             societyId: String): Flowable<Any>


    fun delete(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String
    ): Flowable<Any>


    fun deleteSociaty(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            societyId: String
    ): Flowable<Any>


}