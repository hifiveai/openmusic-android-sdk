package com.hifive.sdk.net

import com.hifive.sdk.entity.*
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
interface Api {


    @GET("/livestream/v1/company/getCompanySheetTagList")
    fun getCompanySheetTagList(): Flowable<BaseResp<ArrayList<SheetTagListItem>>>


    @FormUrlEncoded
    @POST("/livestream/v1/company/getCompanySheetList")
    fun getCompanySheetList(
            @Field("channelId") groupId: String?,
            @Field("language") language: String?,
            @Field("recoNum") recoNum: String?,
            @Field("type") type: String?,
            @Field("tagIdList") tagIdList: String?,
            @Field("field") field: String?,
            @Field("pageSize") pageSize: String?,
            @Field("page") page: String?
    ): Flowable<BaseResp<CompanySheetList>>

    @FormUrlEncoded
    @POST("/livestream/v1/company/getCompanySheetMusicList")
    fun getCompanySheetMusicList(
            @Field("sheetId") sheetId: String?,
            @Field("language") language: String?,
            @Field("field") field: String?,
            @Field("pageSize") pageSize: String?,
            @Field("page") page: String?
    ): Flowable<BaseResp<CompanySheetMusicList>>


    @GET("/livestream/v1/company/getCompanyChannelList")
    fun getCompanyChannelList(
    ): Flowable<BaseResp<ArrayList<CompanyChannelList>>>


    //开放平台


    @FormUrlEncoded
    @POST("/livestream/v1/member/memberLogin")
    fun token(
            @Field("sign") sign: String,
            @Field("appId") appId: String,
            @Field("memberName") memberName: String,
            @Field("memberId") memberId: String,
            @Field("sociatyName") sociatyName: String?,
            @Field("sociatyId") sociatyId: String?,
            @Field("deviceId") deviceId: String,
            @Field("timestamp") timestamp: String,
            @Field("headerUrl") headerUrl: String?,
            @Field("gender") gender: String?,
            @Field("birthday") birthday: String?,
            @Field("location") location: String?,
            @Field("favoriteSinger") favoriteSinger: String?,
            @Field("phone") phone: String?
    ): Flowable<BaseResp<Token>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/sociatyLogin")
    fun societyLogin(
            @Field("sign") sign: String,
            @Field("appId") appId: String,
            @Field("sociatyName") sociatyName: String,
            @Field("sociatyId") sociatyId: String,
            @Field("deviceId") deviceId: String?,
            @Field("timestamp") timestamp: String?
    ): Flowable<BaseResp<Token>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/unbundlingMember")
    fun unbendingMember(
            @Field("memberId") sign: String,
            @Field("sociatyId") appId: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/bindingMember")
    fun bind(
            @Field("memberId") sign: String,
            @Field("sociatyId") appId: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/deleteMember")
    fun delete(
            @Field("memberId") sign: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/deleteSociaty")
    fun deleteSociaty(
            @Field("sociatyId") sign: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/getMemberSheetList")
    fun getMemberSheetList(
            @Field("language") language: String,
            @Field("recoNum") recoNum: String,
            @Field("field") field: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/getMemberSheetMusicList")
    fun getMemberSheetMusicList(
            @Field("sheetId") sheetId: String,
            @Field("language") language: String,
            @Field("field") field: String,
            @Field("pageSize") pageSize: String,
            @Field("page") page: String

    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/getMusicDetail")
    fun getMusicDetail(
            @Field("musicId") musicId: String,
            @Field("language") language: String,
            @Field("mediaType") mediaType: String,
            @Field("field") field: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/saveMemberSheet")
    fun saveMemberSheet(
            @Field("sheetName") musicId: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/saveMemberSheetMusic")
    fun saveMemberSheetMusic(
            @Field("sheetId") sheetId: String,
            @Field("musicId") musicId: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/music/deleteMemberSheetMusic")
    fun deleteMemberSheetMusic(
            @Field("sheetId") sheetId: String,
            @Field("musicId") musicId: String
    ): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/member/record/updateMusicRecord")
    fun updateMusicRecord(
            @Field("recordId") recordId: String,
            @Field("duration") duration: String,
            @Field("mediaType") mediaType: String
    ): Flowable<BaseResp<Any>>


    @GET("/livestream/v1/search/getConfigList")
    fun getConfigList(): Flowable<BaseResp<Any>>


    @FormUrlEncoded
    @POST("/livestream/v1/search/getMusicList")
    fun getMusicList(
            @Field("searchId") searchId: String,
            @Field("keyword") keyword: String?,
            @Field("language") language: String?,
            @Field("field") field: String?,
            @Field("pageSize") pageSize: String?,
            @Field("page") page: String?
    ): Flowable<BaseResp<Any>>


    @GET("/livestream/v1/search/getSearchRecordList")
    fun getSearchRecordList(
            @Field("pageSize") pageSize: String?,
            @Field("page") page: String?
    ): Flowable<BaseResp<Any>>


    @GET("/livestream/v1/search/deleteSearchRecord")
    fun deleteSearchRecord(): Flowable<BaseResp<Any>>


}
