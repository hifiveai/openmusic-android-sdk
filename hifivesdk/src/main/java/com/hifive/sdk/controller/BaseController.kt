package com.hifive.sdk.controller

import android.content.Context
import com.hifive.sdk.hInterface.DataResponse
import com.hifive.sdk.hInterface.DownLoadResponse
import com.hifive.sdk.manager.HFLiveApi
import com.hifive.sdk.rx.BaseException
import com.hifive.sdk.service.Service
import com.hifive.sdk.service.impl.ServiceImpl
import com.hifive.sdk.utils.NetWorkUtils


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
abstract class BaseController {

    val mService by lazy { ServiceImpl() }

    fun checkNetWork(context: Context, info: DataResponse): Boolean {
        if (NetWorkUtils.isNetWorkAvailable(context)) {
            return true
        }
        //向开发者抛出errorMsg,交给开发者处理
        HFLiveApi.callbacks?.onError(BaseException(10001,"网络错误"))
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
     * 获取商户歌单全部歌曲列表
     */
    abstract fun getCompanySheetMusicAll(
            context: Context,
            sheetId: String?,
            language: String?,
            field: String?,
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

    abstract fun getMemberSheetList(
            context: Context,
            page: String?,
            pageSize: String?,
            dataResponse: DataResponse
    )


    abstract fun getMemberSheetMusicList(
            context: Context,
            sheetId: String,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )


    abstract fun getMusicDetail(
            context: Context,
            musicId: String, language: String?, mediaType: String,
            audioFormat: String?, audioRate: String?, field: String?,
            dataResponse: DataResponse
    )


    abstract fun saveMemberSheet(
            context: Context,
            sheetName: String,
            dataResponse: DataResponse
    )

    abstract fun saveMemberSheetMusic(
            context: Context,
            sheetId: String,
            musicId: String,
            dataResponse: DataResponse
    )


    abstract fun deleteMemberSheetMusic(
            context: Context,
            sheetId: String,
            musicId: String,
            dataResponse: DataResponse
    )


    abstract fun updateMusicRecord(
            context: Context,
            recordId: String,
            duration: String,
            mediaType: String,
            dataResponse: DataResponse
    )


    abstract fun getConfigList(
            context: Context,
            dataResponse: DataResponse
    )


    abstract fun getMusicList(
            context: Context,
            searchId: String,
            keyword: String?,
            language: String?,
            field: String?,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )

    abstract fun getSearchRecordList(
            context: Context,
            pageSize: String?,
            page: String?,
            dataResponse: DataResponse
    )


    abstract fun deleteSearchRecord(
            context: Context,
            dataResponse: DataResponse
    )


    abstract fun getMemberSheetMusicAll(
            context: Context,
            sheetId: String,
            language: String?,
            field: String?,
            dataResponse: DataResponse
    )
}