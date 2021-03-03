package com.hifive.sdk.entity

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
    val groupName: String,
    val taskId: String
)


data class TrialMusic(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String,
    val waveUrl: String,
    val taskId: String

)


data class TrafficHQListen(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String,
    val taskId: String
)

data class TrafficListenMixed(
    val expires: Long,
    val fileSize: Int,
    val fileUrl: String,
    val musicId: String,
    val waveUrl: String,
    val taskId: String
)


data class SearchMusic(
    val meta: Meta,
    val record: List<Record>,
    val taskId: String
)

data class MusicConfig(
        val prices: List<Int>,
        val tagList: List<Tag>,
        val taskId: String
)


data class BaseFavorite(
    val meta: Meta,
    val record: List<Record>,
    val taskId: String
)

data class BaseHot(
        val meta: Meta,
        val record: List<Record>,
        val taskId: String
)


data class OrderMusic(
    val HForderId: String,
    val createTime: String,
    val deadline: String,
    val music: List<Music>,
    val orderId: String,
    val subject: String,
    val totalFee: Int,
    val taskId: String
)

data class OrderAuthorization(
    val fileUrl: List<String>,
    val taskId: String
)

data class OrderPublish(
    val orderId: String,
    val workId: String,
    val taskId: String
)

data class TaskId(
        val taskId: String
)


data class ChannelSheet(
        val meta: Meta,
        val record: List<Record>,
        val taskId: String
)

data class SheetMusic(
    val meta: Meta,
    val record: List<Record>,
    val taskId: String
)


data class Meta(
        val currentPage: Int,
        val totalCount: Int
)

data class Record(
        val cover: List<Cover>,
        val describe: String,
        val free: Int,
        val music: List<Music>,
        val musicTotal: Int,
        val price: Int,
        val sheetId: Int,
        val sheetName: String,
        val tag: List<Tag>,
        val type: Int
)

data class Cover(
        val size: Any,
        val url: String
)

data class Music(
        val albumId: String,
        val albumName: String,
        val arranger: List<Any>,
        val artist: List<Artist>,
        val auditionBegin: Int,
        val auditionEnd: Int,
        val author: List<Author>,
        val bpm: Int,
        val composer: List<Composer>,
        val cover: List<Cover>,
        val duration: Int,
        val musicId: String,
        val musicName: String,
        val tag: List<Tag>,
        val version: List<Version>
)


data class Artist(
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
        val child: List<Child>,
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

data class Child(
        val child: List<Any>,
        val tagId: Int,
        val tagName: String
)


