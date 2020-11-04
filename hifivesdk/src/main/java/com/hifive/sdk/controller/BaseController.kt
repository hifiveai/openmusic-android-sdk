package com.hifive.sdk.controller

import android.app.Application
import android.content.Context
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.service.Service
import com.hifive.sdk.utils.NetWorkUtils
import javax.inject.Inject


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
abstract class BaseController {

    @Inject
    lateinit var mService: Service

    fun checkNetWork(context: Context, info: DataResponse): Boolean {
        if (NetWorkUtils.isNetWorkAvailable(context)) {
            return true
        }
        info.errorMsg("网络错误", null)
        return false
    }


    /**
     * 获取授权信息/ SDK初始化信息
     */
    abstract fun creditUser(
            context: Context,
            isAnchor: Boolean,
            userId: String,
            userName: String,
            response: DataResponse
    )

    /**
     * 查询主播歌单
     */
    abstract fun queryPlayList(
            context: Context,
            num: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            sort: String?,
            response: DataResponse
    )

    /**
     * 查询主播歌单升序
     */
    abstract fun musicSmallerThan(
            context: Context,
            musicId: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            response: DataResponse
    )

    /**
     * 查询主播歌单降序
     */
    abstract fun musicBiggerThan(
            context: Context,
            musicId: Int?,
            size: Int?,
            userId: String,
            roomId: String?,
            mediaAction: String,
            sort: String?,
            response: DataResponse
    )


    /**
     * 添加歌单，k歌/听歌
     */
    abstract fun addToPlayList(
            context: Context,
            userId: String,
            roomId: String?,
            musicNo: String,
            mediaAction: String,
            response: DataResponse
    )


    /**
     * 歌单删除
     */
    abstract fun deleteFromPlayList(
            context: Context,
            musicNo: String,
            userId: String,
            roomId: String?,
            mediaAction: String?,
            response: DataResponse
    )


    /**
     * 歌曲标签
     */
    abstract fun musicTags(
            context: Context,
            current: Int?,
            size: Int?,
            response: DataResponse
    )

    /**
     * 歌曲搜索
     */
    abstract fun searchMusicByTag(
            context: Context,
            current: Int?,
            size: Int?,
            tag: String,
            keyword: String,
            response: DataResponse
    )


    /**
     * 音乐点播/音乐播放资源获取
     */
    abstract fun resource(
            context: Context,
            musicNo: String,
            userId: String,
            userName: String,
            roomId: String?,
            mediaAction: String,
            response: DataResponse
    )


    /**
     * 歌曲推荐
     */
    abstract fun recommendMusic(

            context: Context,
            current: Int?,
            size: Int?,
            response: DataResponse
    )

    /**
     * 下载歌词
     */
    abstract fun downLoadLRC(
            context: Application,
            lrc: String,
            path: String,
            dataResponse: DataResponse
    )


    /**
     * 获取歌单等统计数量
     */
    abstract fun musicCount(context: Context, userId: String, dataResponse: DataResponse);


    //开放平台

    abstract fun memberLogin(context: Context,
                             memberName: String,
                             memberId: String,
                             societyName: String?,
                             societyId: String?,
                             headerUrl: String?,
                             gender: String?,
                             birthday: String?,
                             location: String?,
                             favoriteSinger: String?,
                             phone: String?,
                             dataResponse: DataResponse)


    abstract fun societyLogin(
            context: Context,
            societyName: String,
            societyId: String,
            dataResponse: DataResponse)


    abstract fun unbindingMember(
            context: Context,
            accessToken: String,
            memberOutId: String?,
            societyOutId: String?,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )


    abstract fun bindingMember(
            context: Context,
            accessToken: String,
            memberOutId: String?,
            societyOutId: String?,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )


    abstract fun deleteMember(
            context: Context,
            accessToken: String,
            memberOutId: String?,
            societyOutId: String?,
            memberId: String,
            dataResponse: DataResponse
    )

    abstract fun deleteSociety(
            context: Context,
            accessToken: String,
            memberOutId: String?,
            societyOutId: String?,
            societyId: String,
            dataResponse: DataResponse
    )
}