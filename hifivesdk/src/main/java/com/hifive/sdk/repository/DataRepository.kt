package com.hifive.sdk.repository

import com.hifive.sdk.entity.*
import com.hifive.sdk.ext.convert
import com.hifive.sdk.net.LiveRetrofitFactory
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.Query


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
class DataRepository constructor() {

    fun baseLogin(
            Nickname: String?,
            Gender: String?,
            Birthday: String?,
            Location: String?,
            Education: String?,
            Profession: String?,
            IsOrganization: String?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?
    ): Flowable<LoginBean> {
        return LiveRetrofitFactory.api().baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre, "BaseLogin").convert()
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
            SheetId: String?,
            Language: Int?,
            Page: Int?,
            PageSize: Int?
    ): Flowable<SheetMusic> {
        return LiveRetrofitFactory.api().sheetMusic(SheetId, Language, Page, PageSize, "SheetMusic").convert()
    }


    fun searchMusic(TagIds: String?,
                    priceFromCent: Long?,
                    priceToCent: Long?,
                    BpmForm: Int?,
                    BpmTo: Int?,
                    DurationFrom: Int?,
                    DurationTo: Int?,
                    Keyword: String?,
                    Language: Int?,
                    Page: Int?,
                    PageSize: Int?
    ): Flowable<SearchMusic> {
        return LiveRetrofitFactory.api().searchMusic(TagIds, priceFromCent, priceToCent, BpmForm, BpmTo, DurationFrom, DurationTo, Keyword, Language, Page, PageSize, "SheetMusic").convert()
    }


    fun musicConfig(): Flowable<MusicConfig> {
        return LiveRetrofitFactory.api().musicConfig("MusicConfig").convert()
    }


    fun baseFavorite(Page: Int?,
                     PageSize: Int?
    ): Flowable<BaseFavorite> {
        return LiveRetrofitFactory.api().baseFavorite(Page, PageSize, "BaseFavorite").convert()
    }

    fun baseHot(StartTime: Long?,
                Duration: Int?,
                Page: Int?,
                PageSize: Int?
    ): Flowable<BaseHot> {
        return LiveRetrofitFactory.api().baseHot(StartTime, Duration, Page, PageSize, "BaseHot").convert()
    }


    fun trial(
            MusicId: String?
    ): Flowable<TrialMusic> {
        return LiveRetrofitFactory.api().trial(MusicId, "Trial").convert()
    }

    fun trafficHQListen(MusicId: String?,
                        AudioFormat: String?,
                        AudioRate: String?
    ): Flowable<TrafficHQListen> {
        return LiveRetrofitFactory.api().trafficHQListen(MusicId, AudioFormat, AudioRate, "TrafficHQListen").convert()
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

    fun orderAuthorization(CompanyName: String?,
                           ProjectName: String?,
                           Brand: String?,
                           Period: Int?,
                           Area: String?,
                           orderIds: String?
    ): Flowable<OrderAuthorization> {
        return LiveRetrofitFactory.api().orderAuthorization(CompanyName, ProjectName, Brand, Period, Area, orderIds, "OrderAuthorization").convert()
    }


    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?
    ): Flowable<TaskId> {
        return LiveRetrofitFactory.api().baseReport(Action, TargetId, Content, Location, "BaseReport").convert()
    }


    fun orderPublish(Action: String?,
                     OrderId: String?,
                     WorkId: String?
    ): Flowable<OrderPublish> {
        return LiveRetrofitFactory.api().orderPublish(Action, OrderId, WorkId, "OrderPublish").convert()
    }

}

