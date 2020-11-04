package com.hifive.sdk.repository

import com.hifive.sdk.entity.*
import com.hifive.sdk.net.LiveRetrofitFactory
import com.hifive.sdk.net.RetrofitFactory
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import javax.inject.Inject


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class DataRepository @Inject constructor() {


    /**
     * 获取授权信息/ SDK初始化信息
     */
    fun initSDK(isAnchor: Boolean, userId: String, userName: String): Flowable<BaseResp<SdkInfo>> {
        return RetrofitFactory.api().initSDK(userId, isAnchor, userName)
    }

    /**
     * 查询主播歌单
     */
    fun queryPlayList(
        num: Int?,
        size: Int?,
        userId: String,
        roomId: String?,
        mediaAction: String,
        sort: String?
    ): Flowable<BaseResp<List<MusicInfo>>> {
        return RetrofitFactory.api()
            .queryPlayList(num?.toLong(), size, userId, roomId, mediaAction, sort)
    }

    /**
     * 添加歌单，k歌/听歌
     */
    fun addSong(
        userId: String,
        roomId: String?,
        musicNo: String,
        mediaAction: String
    ): Flowable<BaseResp<AddSongBean>> {
        val json = AddSongJson(userId, roomId, musicNo, mediaAction)
        return RetrofitFactory.api().addSong(json)
    }

    /**
     * 歌单删除
     */
    fun deleteSong(
        musicNo: String,
        userId: String,
        roomId: String?,
        mediaAction: String?
    ): Flowable<BaseResp<DeleteSongBean>> {
        return RetrofitFactory.api().deleteSong(musicNo, userId, roomId, mediaAction)
    }

    /**
     * 歌曲标签
     */
    fun label(current: Int?, size: Int?): Flowable<BaseResp<List<MusicTag>>> {
        return RetrofitFactory.api().label(current, size)
    }


    /**
     * 歌曲搜索
     */
    fun searchSong(
        current: Int?,
        size: Int?,
        tag: String,
        keyword: String
    ): Flowable<BaseResp<List<SearchMusicInfo>>> {
        return RetrofitFactory.api().searchSong(current, size, tag, keyword)
    }

    /**
     * 音乐点播/音乐播放资源获取
     */
    fun resourceAcquisition(
        musicNo: String,
        userId: String,
        userName: String,
        roomId: String?,
        mediaAction: String
    ): Flowable<BaseResp<MusicResource>> {
        return RetrofitFactory.api().resource(musicNo, userId, userName, roomId, mediaAction)
    }

    /**
     * 歌曲推荐
     */
    fun recommend(
        current: Int?,
        size: Int?
    ): Flowable<BaseResp<List<RecommendMusic>>> {
        return RetrofitFactory.api().recommend(current, size)
    }

    /**
     * 获取歌曲统计
     */
    fun queryCount(userId: String): Flowable<BaseResp<MusicCount>> {
        return RetrofitFactory.api().queryCount(userId)

    }


    /**
     * 获取歌曲统计
     */
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
            token: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String,
            societyId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().unbendingMember(
                "application/x-www-form-urlencoded",
                token,
                appId,
                memberOutId,
                societyOutId,
                timestamp
                , memberId, societyId
        )
    }


    fun bind(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String,
            societyId: String
    ): Flowable<BaseResp<Any>> {


        return LiveRetrofitFactory.api().bind(
                "application/x-www-form-urlencoded",
                accessToken,
                appId,
                memberOutId,
                societyOutId,
                timestamp,
                memberId,
                societyId
        )
    }


    fun delete(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            memberId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().delete(
                "application/x-www-form-urlencoded",
                accessToken,
                appId,
                memberOutId,
                societyOutId,
                timestamp,
                memberId
        )
    }


    fun deleteSociety(
            accessToken: String,
            appId: String,
            memberOutId: String?,
            societyOutId: String?,
            timestamp: String,
            societyId: String
    ): Flowable<BaseResp<Any>> {
        return LiveRetrofitFactory.api().deleteSociaty(
                "application/x-www-form-urlencoded",
                accessToken,
                appId,
                memberOutId,
                societyOutId,
                timestamp,
                societyId
        )
    }


}

