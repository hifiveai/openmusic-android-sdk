package com.hfopen.sdk.service.impl

import com.hfopen.sdk.entity.*
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
            Nickname: String?,
            Gender: Int?,
            Birthday: Long?,
            Location: String?,
            Education: Int?,
            Profession: Int?,
            IsOrganization: Boolean?,
            Reserve: String?,
            FavoriteSinger: String?,
            FavoriteGenre: String?
    ): Flowable<LoginBean> {
        return dataRepository.baseLogin(Nickname, Gender, Birthday, Location, Education, Profession, IsOrganization, Reserve, FavoriteSinger, FavoriteGenre)
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
    ): Flowable<SheetMusic> {
        return dataRepository.sheetMusic(SheetId, Language, Page, PageSize)
    }


    fun searchMusic(
            TagIds: String?,
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
        return dataRepository.searchMusic(TagIds, priceFromCent, priceToCent, BpmForm, BpmTo, DurationFrom, DurationTo, Keyword, Language, Page, PageSize)
    }


    fun musicConfig(): Flowable<MusicConfig> {
        return dataRepository.musicConfig()
    }

    fun baseFavorite(Page: Int?,
                     PageSize: Int?
    ): Flowable<BaseFavorite> {
        return dataRepository.baseFavorite(Page, PageSize)
    }

    fun baseHot( StartTime: Long,
                 Duration: Int?,
                 Page: Int?,
                 PageSize: Int?
    ): Flowable<BaseHot> {
        return dataRepository.baseHot(StartTime,Duration,Page, PageSize)
    }


    fun trial(
            MusicId: String?,
            Action :String?
    ): Flowable<TrialMusic> {
        return dataRepository.trial(MusicId,Action)
    }

    fun hqListen( MusicId: String?,
                         AudioFormat: String?,
                         AudioRate: String?,
                         Action :String?
    ): Flowable<TrafficHQListen> {
        return dataRepository.trafficHQListen(MusicId,AudioFormat,AudioRate,Action)
    }

    fun trafficListenMixed( MusicId: String?
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
        return dataRepository.orderMusic(Subject,OrderId,Deadline,Music,Language,AudioFormat,AudioRate,TotalFee,Remark,WorkId)
    }

    fun orderDetail(
            OrderId: String?
    ): Flowable<OrderMusic> {
        return dataRepository.orderDetail(OrderId)
    }

    fun orderAuthorization( CompanyName: String?,
                            ProjectName: String?,
                            Brand: String?,
                            Period: Int?,
                            Area: String?,
                            orderIds: String?
    ): Flowable<OrderAuthorization> {
        return dataRepository.orderAuthorization(CompanyName,ProjectName,Brand,Period,Area,orderIds)
    }

    fun baseReport(Action: Int?,
                   TargetId: String?,
                   Content: String?,
                   Location: String?
    ): Flowable<Any> {
        return dataRepository.baseReport(Action,TargetId, Content, Location)
    }

    fun orderPublish(Action: String?,
                     OrderId: String?,
                     WorkId: String?
    ): Flowable<OrderPublish> {
        return dataRepository.orderPublish(Action,OrderId, WorkId)
    }

    fun report(musicId: String?,
               duration: Long,
               timestamp: Long,
               audioFormat: String?,
               audioRate: String?,
               Action :String?
    ): Flowable<Any> {
        return dataRepository.report(musicId, duration, timestamp, audioFormat,audioRate, Action)
    }
}