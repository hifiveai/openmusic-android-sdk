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





class MusicTag(val name: String?) : Serializable


data class SearchMusicInfo(
    val albumName: String,
    val duration: Int,
    val mediaAction: String,
    val musicName: String,
    val musicNo: String,
    val musicSinger: String,
    val coverUrl: String,

    //自己添加的参数
    var isPlayType: Boolean = false,
    var isPlaying: Boolean?,   //标志是不是正在播放
    var isKing: Boolean?,  //正在k歌
    var isExpend: Boolean? = false   //更多是不是扩展开的
) : Serializable


data class MusicResource(
    val accompanyUrl: String,
    val lyricUrl: String,
    val majorUrl: String,
    val outTradeNo: String,
    //自己添加的参数
    var type: String?,
    var musicNo: String?,   //播放的音乐编号
    var coverUrl: String
) : Serializable

data class RecommendMusic(
    val albumName: String,
    val dayTime: String,
    val mediaAction: String,
    val musicName: String,
    val musicNo: String,
    val musicSinger: String
) : Serializable

data class AddSongJson(
        val userId: String,
        val roomId: String?,
        val musicNo: String,
        val mediaAction: String
) : Serializable

data class MusicCount(
        val total: Int,
        val pnum: Int,
        val knum: Int
)


data class SheetTagListItem(
        val child: List<Child1>,
        val coverUrl: String,
        val pid: Int,
        val tagId: Int,
        val tagName: String
)

data class Child(
        val child: List<Any>,
        val coverUrl: String,
        val pid: Int,
        val tagId: Int,
        val tagName: String
)


data class Token(
        val accessToken: String
)


data class CompanySheetList(
        val currentPage: Int,
        val pageSize: Int,
        val records: List<Record>,
        val totalCount: Int,
        val totalPage: Int
)

data class Record(
        val cover: Cover,
        val describe: String,
        val free: Int,
        val music: List<Any>,
        val musicTotal: Int,
        val price: Int,
        val sheetId: Int,
        val sheetName: String,
        val tag: List<Tag>,
        val type: Int
)

data class Cover(
        val size: String,
        val url: String
)

data class Tag(
        val child: List<Child1>,
        val coverUrl: Any,
        val pid: Int,
        val tagId: Int,
        val tagName: String
)

data class Child1(
        val child: List<Any>,
        val coverUrl: Any,
        val pid: Int,
        val tagId: Int,
        val tagName: String
)


data class CompanySheetMusicList(
        val currentPage: Int,
        val pageSize: Int,
        val records: List<Any>,
        val totalCount: Int,
        val totalPage: Int
)

data class CompanyChannelList(
        val channelId: String,
        val channelName: String,
        val channelUrl: String
)


data class SheetList(
        val currentPage: Int,
        val pageSize: Int,
        val records: List<RecordInfo>,
        val totalCount: Int,
        val totalPage: Int
)

data class RecordInfo(
        val createTime: String,
        val sheetId: Int,
        val sheetName: String,
        val type: Int
)


data class SheetMusicList(
        val currentPage: Int,
        val pageSize: Int,
        val records: List<RecordInformation>,
        val totalCount: Int,
        val totalPage: Int
)

data class RecordInformation(
        val album: Album,
        val arranger: List<Any>,
        val artist: List<Artist>,
        val auditionBegin: Int,
        val auditionEnd: Int,
        val authType: Int,
        val author: List<Any>,
        val bpm: Int,
        val composer: List<Composer>,
        val cover: CoverInfo,
        val duration: Int,
        val forSale: Int,
        val majorVersion: String,
        val maker: List<Any>,
        val mastery: List<Any>,
        val musicId: String,
        val musicName: String,
        val price: Int,
        val tag: List<Any>,
        val version: List<Version>,
        val versionName: String,
        val waveUrl: Any
)

data class Album(
        val code: String,
        val id: Int,
        val name: String
)

data class Artist(
        val code: String,
        val icon: String,
        val id: Int,
        val name: String
)

data class Composer(
        val code: String,
        val icon: String,
        val id: Int,
        val name: String
)

data class CoverInfo(
        val size: String,
        val url: String
)

data class Version(
        val duration: Int,
        val free: Int,
        val majorVersion: Int,
        val musicId: String,
        val name: String,
        val price: Int
)