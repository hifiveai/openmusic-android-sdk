package com.hifive.sdk.controller

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
     * 获取商户歌单标签列表
     */
    abstract fun getCompanySheetTagList(
            context: Context,
            response: DataResponse
    )

    /**
     * 获取商户歌单列表
     */
    abstract fun getCompanySheetList(
            context: Context,
            groupId: String?,
            language: String?,
            recoNum: String?,
            type: String?,
            tagIdList: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    )

    /**
     * 获取商户歌单歌曲列表
     */
    abstract fun getCompanySheetMusicList(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            response: DataResponse
    )


    /**
     * 获取商户电台列表
     */
    abstract fun getCompanyChannelList(
            context: Context,
            response: DataResponse
    )









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
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )


    abstract fun bindingMember(
            context: Context,
            memberId: String,
            societyId: String,
            dataResponse: DataResponse
    )


    abstract fun deleteMember(
            context: Context,
            memberId: String,
            dataResponse: DataResponse
    )

    abstract fun deleteSociety(
            context: Context,
            societyId: String,
            dataResponse: DataResponse
    )
}