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


data class HQListen(
        val expires: Long?,
        val fileSize: Int?,
        val fileUrl: String?,
        val musicId: String?
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
        val music: List<OrderDetail>,
        val orderId: String,
        val subject: String,
        val totalFee: Int
)

data class OrderDetail(
        val musicId: String,
        val fileUrl: String,
        val expires: Long,
        val fileSize: Int

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
        val sheetId: Long,
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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MusicRecord

        if (albumId != other.albumId) return false
        if (albumName != other.albumName) return false
        if (arranger != other.arranger) return false
        if (artist != other.artist) return false
        if (auditionBegin != other.auditionBegin) return false
        if (auditionEnd != other.auditionEnd) return false
        if (author != other.author) return false
        if (bpm != other.bpm) return false
        if (composer != other.composer) return false
        if (cover != other.cover) return false
        if (duration != other.duration) return false
        if (musicId != other.musicId) return false
        if (musicName != other.musicName) return false
        if (tag != other.tag) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = albumId.hashCode()
        result = 31 * result + albumName.hashCode()
        result = 31 * result + (arranger?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + auditionBegin
        result = 31 * result + auditionEnd
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + bpm
        result = 31 * result + (composer?.hashCode() ?: 0)
        result = 31 * result + (cover?.hashCode() ?: 0)
        result = 31 * result + duration
        result = 31 * result + musicId.hashCode()
        result = 31 * result + musicName.hashCode()
        result = 31 * result + (tag?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        return result
    }
}

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


