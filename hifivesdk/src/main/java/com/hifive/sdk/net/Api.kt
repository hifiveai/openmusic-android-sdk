package com.hifive.sdk.net

import com.hifive.sdk.entity.*
import com.hifive.sdk.protocol.BaseResp
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
interface Api {


    /**                        音乐电台列表                            */
    /**
     * 电台列表
     * 此接口返回使用者拥有的全部已上架电台。通过电台ID可获取此电台下的已上架歌单。
     */
    @GET("/")
    fun channel(@Query("X-HF-Action") Action: String?): Flowable<BaseResp<ArrayList<ChannelItem>>>

    /**
     * 电台获取歌单列表
     * 通过电台ID可获取此电台下全部已上架的歌单列表，列表信息包括歌单名、歌单封面、歌曲数量、歌单描述、歌单推荐音乐等信息。
     */
    @GET("/")
    fun channelSheet(@Query("GroupId") GroupId: String?,
                     @Query("Language") Language: Int?,
                     @Query("RecoNum") RecoNum: Int?,
                     @Query("Page") Page: Int?,
                     @Query("PageSize") PageSize: Int?,
                     @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<ChannelSheet>>

    /**
     * 电台获取歌单列表
     * 通过电台ID可获取此电台下全部已上架的歌单列表，列表信息包括歌单名、歌单封面、歌曲数量、歌单描述、歌单推荐音乐等信息。
     */
    @GET("/")
    fun sheetMusic(@Query("SheetId") SheetId: String?,
                   @Query("Language") Language: Int?,
                   @Query("Page") Page: Int?,
                   @Query("PageSize") PageSize: Int?,
                   @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<SheetMusic>>


    /**                        音乐搜索                           */
    /**
     * 组合搜索
     * 此接口提供歌曲名、艺人、标签名称、标签ID、BPM区间、时长区间等多维度的搜索功能，使用到的标签ID、歌曲价格等信息可以在音乐配置信息接口中获取
     */
    @GET("/")
    fun searchMusic(@Query("TagIds") TagIds: String?,
                    @Query("priceFromCent") priceFromCent: Long?,
                    @Query("priceToCent") priceToCent: Long?,
                    @Query("BpmForm") BpmForm: Int?,
                    @Query("BpmTo") BpmTo: Int?,
                    @Query("DurationFrom") DurationFrom: Int?,
                    @Query("DurationTo") DurationTo: Int?,
                    @Query("Keyword") Keyword: String?,
                    @Query("Language") Language: Int?,
                    @Query("Page") Page: Int?,
                    @Query("PageSize") PageSize: Int?,
                    @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<SearchMusic>>


    /**
     * 电台列表
     * 此接口返回使用者拥有的全部已上架电台。通过电台ID可获取此电台下的已上架歌单。
     */
    @GET("/")
    fun musicConfig(@Query("X-HF-Action") Action: String?): Flowable<BaseResp<Any>>


    /**                        音乐推荐                            */
    /**
     * 猜你喜欢
     */
    @GET("/")
    fun baseFavorite(@Query("Page") Page: Int?,
                     @Query("PageSize") PageSize: Int?,
                     @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<Any>>

    /**
     * 热门推荐
     */
    @GET("/")
    fun baseHot(@Query("StartTime") StartTime: Long?,
                @Query("Duration") Duration: Int?,
                @Query("Page") Page: Int?,
                @Query("PageSize") PageSize: Int?,
                @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<Any>>


    /**                        音乐播放                             */
    /**
     * 歌曲试听
     */
    @GET("/")
    fun trial(@Query("MusicId") MusicId: String?,
              @Query("X-HF-Action") Action: String?): Flowable<BaseResp<TrialMusic>>


    /**
     * 获取音乐HQ播放信息
     */
    @GET("/")
    fun trafficHQListen(@Query("MusicId") MusicId: String?,
                        @Query("AudioFormat") AudioFormat: String?,
                        @Query("AudioRate") AudioRate: String?,
                        @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<TrafficHQListen>>

    /**
     * 获取音乐混音播放信息
     */
    @GET("/")
    fun trafficListenMixed(@Query("MusicId") MusicId: String?,
                           @Query("X-HF-Action") Action: String?): Flowable<BaseResp<TrafficListenMixed>>


    /**                        音乐售卖                            */
    /**
     * 购买音乐
     */
    @FormUrlEncoded
    @POST("/")
    fun orderMusic(@Field("Subject") Subject: String?,
                   @Field("OrderId") OrderId: Long?,
                   @Field("Deadline") Deadline: Int?,
                   @Field("Music") Music: String?,
                   @Field("Language") Language: Int?,
                   @Field("AudioFormat") AudioFormat: String?,
                   @Field("AudioRate") AudioRate: String?,
                   @Field("TotalFee") TotalFee: Int?,
                   @Field("Remark") Remark: String?,
                   @Field("WorkId") WorkId: String?,
                   @Field("X-HF-Action") Action: String?
    ): Flowable<BaseResp<Any>>

    /**
     * 查询订单
     */
    @GET("/")
    fun orderDetail(@Query("OrderId") OrderId: String?,
                    @Query("X-HF-Action") Action: String?): Flowable<BaseResp<Any>>

    /**
     * 下载授权书
     */
    @GET("/")
    fun orderAuthorization(@Query("CompanyName") CompanyName: String?,
                           @Query("ProjectName") ProjectName: String?,
                           @Query("Brand") Brand: String?,
                           @Query("Period") Period: Int?,
                           @Query("Area") Area: String?,
                           @Query("orderIds") orderIds: String?,
                           @Query("X-HF-Action") Action: String?
    ): Flowable<BaseResp<Any>>


    /**                        数据上报                            */
    /**
     * 获取token
     */
    @FormUrlEncoded
    @POST("/")
    fun baseLogin(@Field("Nickname") Nickname: String?,
                  @Field("Gender") Gender: String?,
                  @Field("Birthday") Birthday: String?,
                  @Field("Location") Location: String?,
                  @Field("Education") Education: String?,
                  @Field("Profession") Profession: String?,
                  @Field("IsOrganization") IsOrganization: String?,
                  @Field("Reserve") Reserve: String?,
                  @Field("FavoriteSinger") FavoriteSinger: String?,
                  @Field("FavoriteGenre") FavoriteGenre: String?,
                  @Field("X-HF-Action") Action: String?
    ): Flowable<BaseResp<LoginBean>>

    /**
     * 行为采集
     */
    @FormUrlEncoded
    @POST("/")
    fun baseReport(@Field("Action") Action: Int?,
                   @Field("TargetId") TargetId: String?,
                   @Field("Content") Content: String?,
                   @Field("Location") Location: Int?,
                   @Field("X-HF-Action") Actions: String?
    ): Flowable<BaseResp<Any>>

    /**
     * 发布作品
     */
    @FormUrlEncoded
    @POST("/")
    fun orderPublish(@Field("Action") Action: Int?,
                     @Field("OrderId") OrderId: String?,
                     @Field("WorkId") WorkId: String?,
                     @Field("X-HF-Action") Actions: String?
    ): Flowable<BaseResp<Any>>


}
