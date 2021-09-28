package com.hfopen.sdk.service.impl

import com.hfopen.sdk.entity.*
import com.hfopen.sdk.ext.convert
import com.hfopen.sdk.manager.HFOpenApi
import com.hfopen.sdk.net.LiveRetrofitFactory
import com.hfopen.sdk.repository.DataRepository
import com.hfopen.sdk.service.Service
import io.reactivex.Flowable


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class ServiceImpl constructor() : Service {

    private val dataRepository by lazy { DataRepository() }

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
        return dataRepository.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre, Timestamp)
    }


    fun channel(): Flowable<ArrayList<ChannelItem>> {
        return dataRepository.channel()
    }

    fun channelSheet(GroupId: String?,
                     Language: Int?,
                     RecoNum: Int?,
                     Page: Int?,
                     PageSize: Int?
    ): Flowable<ChannelSheet> {
        return dataRepository.channelSheet(GroupId, Language, RecoNum, Page, PageSize)

    }

    fun sheetMusic(
            SheetId: Long?,
            Language: Int?,
            Page: Int?,
            PageSize: Int?
    ): Flowable<MusicList> {
        return dataRepository.sheetMusic(SheetId, Language, Page, PageSize)
    }


    fun searchMusic(
            TagIds: String?,
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
        return dataRepository.searchMusic(TagIds, PriceFromCent, PriceToCent, BpmFrom, BpmTo, DurationFrom, DurationTo, Keyword, SearchFiled, SearchSmart, Language, Page, PageSize)
    }


    fun musicConfig(): Flowable<MusicConfig> {
        return dataRepository.musicConfig()
    }

    fun baseFavorite(Page: Int?,
                     PageSize: Int?
    ): Flowable<MusicList> {
        return dataRepository.baseFavorite(Page, PageSize)
    }

    fun baseHot(StartTime: Long,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?
    ): Flowable<MusicList> {
        return dataRepository.baseHot(StartTime, Duration, Page, PageSize)
    }


    fun trial(
            MusicId: String?,
            Action: String?
    ): Flowable<TrialMusic> {
        return dataRepository.trial(MusicId, Action)
    }

    fun hqListen(MusicId: String?,
                 AudioFormat: String?,
                 AudioRate: String?,
                 Action: String?
    ): Flowable<HQListen> {
        return dataRepository.trafficHQListen(MusicId, AudioFormat, AudioRate, Action)
    }

    fun trafficListenMixed(MusicId: String?
    ): Flowable<TrafficListenMixed> {
        return dataRepository.trafficListenMixed(MusicId)
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
        return dataRepository.orderMusic(Subject, OrderId, Deadline, Music, Language, AudioFormat, AudioRate, TotalFee, Remark, WorkId)
    }

    fun orderDetail(
            OrderId: String?
    ): Flowable<OrderMusic> {
        return dataRepository.orderDetail(OrderId)
    }

    fun orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           OrderIds: String?
    ): Flowable<OrderAuthorization> {
        return dataRepository.orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, OrderIds)
    }


    fun createMemberSheet(SheetName: String?): Flowable<Any> {
        return dataRepository.createMemberSheet(SheetName)
    }


    fun deleteMemberSheet(SheetId: String?): Flowable<Any> {
        return dataRepository.deleteMemberSheet(SheetId)
    }


    fun memberSheet(memberOutId: String?, Page: Int?,
                    PageSize: Int?): Flowable<VipSheet> {
        return dataRepository.memberSheet(memberOutId, Page, PageSize)
    }


    fun memberSheetMusic(SheetId: String?, Page: Int?,
                         PageSize: Int?): Flowable<Any> {
        return dataRepository.memberSheetMusic(SheetId, Page, PageSize)
    }

    fun addMemberSheetMusic(SheetId: String?, MusicId: String?): Flowable<Any> {
        return dataRepository.addMemberSheetMusic(SheetId, MusicId)
    }

     fun removeMemberSheetMusic(SheetId: String?, MusicId: String?): Flowable<Any> {
        return dataRepository.removeMemberSheetMusic(SheetId, MusicId)
    }


     fun clearMemberSheetMusic(SheetId: String?): Flowable<Any> {
        return dataRepository.clearMemberSheetMusic(SheetId)
    }






    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?
    ): Flowable<Any> {
        return dataRepository.baseReport(Action, TargetId, Content, Location)
    }

    fun orderPublish(OrderId: String?,
                     WorkId: String?
    ): Flowable<OrderPublish> {
        return dataRepository.orderPublish(OrderId, WorkId)
    }

    fun report(MusicId: String,
               Duration: Int,
               Timestamp: Long,
               AudioFormat: String,
               AudioRate: String,
               Action: String?
    ): Flowable<Any> {
        return dataRepository.report(MusicId, Duration, Timestamp, AudioFormat, AudioRate, Action)
    }
}