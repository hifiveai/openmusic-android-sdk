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
    val record: List<Record>
)

data class MusicConfig(
        val prices: IntArray,
        val tagList: List<Tag>
)



data class ChannelSheet(
        val meta: Meta,
        val record: List<Record>
)

data class SheetMusic(
    val meta: Meta,
    val record: List<Record>
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


