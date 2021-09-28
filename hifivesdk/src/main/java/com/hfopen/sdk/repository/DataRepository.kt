package com.hfopen.sdk.repository

import com.hfopen.sdk.entity.*
import com.hfopen.sdk.ext.convert
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.net.LiveRetrofitFactory
import io.reactivex.Flowable


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class DataRepository constructor() {

    fun baseLogin(
            Nickname: String,
            Gender: Int?,
            Birthday: Long?,
            Location: String?,
            Education: Int?,
            Profession: Int?,
            IsOrganization: Boolean?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?,
            Timestamp: String
    ): Flowable<LoginBean> {
        return LiveRetrofitFactory.api().baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre, "BaseLogin", HFOpenApi.APP_ID
                ?: "", Timestamp).convert()
    }

    fun channel(): Flowable<ArrayList<ChannelItem>> {
        return LiveRetrofitFactory.api().channel("Channel").convert()
    }

    fun channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?
    ): Flowable<ChannelSheet> {
        return LiveRetrofitFactory.api().channelSheet(GroupId, Language, RecoNum, Page, PageSize, "ChannelSheet").convert()
    }

    fun sheetMusic(
            SheetId: Long?,
            Language: Int?,
            Page: Int?,
            PageSize: Int?
    ): Flowable<MusicList> {
        return LiveRetrofitFactory.api().sheetMusic(SheetId, Language, Page, PageSize, "SheetMusic").convert()
    }


    fun searchMusic(TagIds: String?,
                    PriceFromCent: Long?,
                    PriceToCent: Long?,
                    BpmFrom: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    SearchFiled: String?,
                    SearchSmart: Int?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?
    ): Flowable<MusicList> {
        return LiveRetrofitFactory.api().searchMusic(TagIds, PriceFromCent, PriceToCent, BpmFrom, BpmTo, DurationFrom, DurationTo, Keyword, SearchFiled, SearchSmart, Language, Page, PageSize, "SearchMusic").convert()
    }


    fun musicConfig(): Flowable<MusicConfig> {
        return LiveRetrofitFactory.api().musicConfig("MusicConfig").convert()
    }


    fun baseFavorite(Page: Int?,
                     PageSize: Int?
    ): Flowable<MusicList> {
        return LiveRetrofitFactory.api().baseFavorite(Page, PageSize, "BaseFavorite").convert()
    }

    fun baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?
    ): Flowable<MusicList> {
        return LiveRetrofitFactory.api().baseHot(StartTime, Duration, Page, PageSize, "BaseHot").convert()
    }


    fun trial(
            MusicId: String?,
            Action: String?
    ): Flowable<TrialMusic> {
        return LiveRetrofitFactory.api().trial(MusicId, Action).convert()
    }


    fun trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?,
                        Action: String?
    ): Flowable<HQListen> {
        return LiveRetrofitFactory.api().trafficHQListen(MusicId, AudioFormat, AudioRate, Action).convert()
    }

    fun trafficListenMixed(MusicId: String?
    ): Flowable<TrafficListenMixed> {
        return LiveRetrofitFactory.api().trafficListenMixed(MusicId, "TrafficListenMixed").convert()
    }


    fun orderMusic(Subject: String?,
                   OrderId: String?,
                   Deadline: Int?,
                   Music: String?,
                   Language: Int?,
                   AudioFormat: String?,
                   AudioRate: String?,
                   TotalFee: Int?,
                   Remark: String?,
                   WorkId: String?
    ): Flowable<OrderMusic> {
        return LiveRetrofitFactory.api().orderMusic(Subject, OrderId, Deadline, Music, Language, AudioFormat, AudioRate, TotalFee, Remark, WorkId, "OrderMusic").convert()
    }


    fun orderDetail(
            OrderId: String?
    ): Flowable<OrderMusic> {
        return LiveRetrofitFactory.api().orderDetail(OrderId, "OrderDetail").convert()
    }


    fun createMemberSheet(SheetName: String?): Flowable<Any> {
        return LiveRetrofitFactory.api().createMemberSheet(SheetName, "CreateMemberSheet").convert()
    }


    fun deleteMemberSheet(SheetId: String?): Flowable<Any> {
        return LiveRetrofitFactory.api().deleteMemberSheet(SheetId, "DeleteMemberSheet").convert()
    }


    fun memberSheet(MemberOutId: String?, Page: Int?,
                    PageSize: Int?): Flowable<VipSheet> {
        return LiveRetrofitFactory.api().memberSheet(MemberOutId, Page, PageSize, "MemberSheet").convert()
    }


    fun memberSheetMusic(SheetId: String?, Page: Int?,
                         PageSize: Int?): Flowable<Any> {
        return LiveRetrofitFactory.api().memberSheetMusic(SheetId, Page, PageSize, "MemberSheetMusic").convert()
    }


    fun addMemberSheetMusic(SheetId: String?, MusicId: String?): Flowable<Any> {
        return LiveRetrofitFactory.api().addMemberSheetMusic(SheetId, MusicId, "AddMemberSheetMusic").convert()
    }

    fun removeMemberSheetMusic(SheetId: String?, MusicId: String?): Flowable<Any> {
        return LiveRetrofitFactory.api().removeMemberSheetMusic(SheetId, MusicId, "RemoveMemberSheetMusic").convert()
    }

    fun clearMemberSheetMusic(SheetId: String?): Flowable<Any> {
        return LiveRetrofitFactory.api().clearMemberSheetMusic(SheetId, "ClearMemberSheetMusic").convert()
    }


    fun orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           OrderIds: String?
    ): Flowable<OrderAuthorization> {
        return LiveRetrofitFactory.api().orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, OrderIds, "OrderAuthorization").convert()
    }


    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?
    ): Flowable<Any> {
        return LiveRetrofitFactory.api().baseReport(Action, TargetId, Content, Location, "BaseReport").convert()
    }


    fun orderPublish(OrderId: String?,
                     WorkId: String?
    ): Flowable<OrderPublish> {
        return LiveRetrofitFactory.api().orderPublish(OrderId, WorkId, "OrderPublish").convert()
    }


    fun report(MusicId: String,
               Duration: Int,
               Timestamp: Long,
               AudioFormat: String,
               AudioRate: String,
               Action: String?
    ): Flowable<Any> {
        return LiveRetrofitFactory.api().report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, Action).convert()
    }

}

