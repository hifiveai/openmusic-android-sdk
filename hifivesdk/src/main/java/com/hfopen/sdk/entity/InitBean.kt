package com.hfopen.sdk.entity

import java.io.Serializable

/**
 * lsh 2021年3月1日13:44:07
 */
data class LoginBean(
    val token: String
)


data class ChannelItem(
    val coverUrl: String,
    val groupId: String,
    val groupName: String
)


data class TrialMusic(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String,
    val waveUrl: String

)


data class TrafficHQListen(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String
)

data class TrafficListenMixed(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String,
    val waveUrl: String
)


data class SearchMusic(
    val meta: Meta,
    val record: List<MusicRecord>
)

data class MusicConfig(
        val prices: List<Int>,
        val tagList: List<Tag>
)


data class BaseFavorite(
    val meta: Meta,
    val record: List<MusicRecord>
)

data class BaseHot(
        val meta: Meta,
        val record: List<MusicRecord>
)


data class OrderMusic(
    val HForderId: String,
    val createTime: String,
    val deadline: String,
    val music: List<MusicRecord>,
    val orderId: String,
    val subject: String,
    val totalFee: Int
)

data class OrderAuthorization(
    val fileUrl: List<String>
)

data class OrderPublish(
    val orderId: String,
    val workId: String
)


data class ChannelSheet(
        val meta: Meta,
        val record: List<Record>
)

data class SheetMusic(
    val meta: Meta,
    val record: List<MusicRecord>
)


data class Meta(
        val currentPage: Int,
        val totalCount: Int
)

data class Record(
        val cover: List<Cover>?,
        val describe: String,
        val free: Int,
        val music: List<MusicRecord>?,
        val musicTotal: Int,
        val price: Int,
        val sheetId: Int,
        val sheetName: String,
        val tag: List<Tag>?,
        val type: Int
) : Serializable

data class MusicRecord(
        val albumId: String,
        val albumName: String,
        val arranger: List<Desc>?,
        val artist: List<Desc>?,
        val auditionBegin: Int,
        val auditionEnd: Int,
        val author: List<Author>?,
        val bpm: Int,
        val composer: List<Composer>?,
        val cover: List<Cover>?,
        val duration: Int,
        val musicId: String,
        val musicName: String,
        val tag: List<Tag>?,
        val version: List<Version>?
)

data class Cover(
        val size: String,
        val url: String
)

//data class Music(
//        val albumId: String,
//        val albumName: String,
//        val arranger: List<Desc>,
//        val artist: List<Desc>,
//        val auditionBegin: Int,
//        val auditionEnd: Int,
//        val author: List<Author>,
//        val bpm: Int,
//        val composer: List<Composer>,
//        val cover: List<Cover>,
//        val duration: Int,
//        val musicId: String,
//        val musicName: String,
//        val tag: List<Tag>,
//        val version: List<Version>
//)


data class Desc(
        val avatar: String,
        val code: String,
        val name: String
)

data class Author(
        val avatar: String,
        val code: String,
        val name: String
)

data class Composer(
        val avatar: String,
        val code: String,
        val name: String
)

data class Tag(
        val child: List<Tag>,
        val tagId: Int,
        val tagName: String
)

data class Version(
        val auditionBegin: Int,
        val auditionEnd: Int,
        val duration: Int,
        val free: Int,
        val majorVersion: Boolean,
        val musicId: String,
        val name: String,
        val price: Int
)


