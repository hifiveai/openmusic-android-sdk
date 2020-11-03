package com.hifive.sdk.entity

import java.io.Serializable

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-07-09
 */
data class SdkInfo(
    val createTime: String,
    val icon: String,
    val name: String,
    val releaseVersion: String,
    val version: Int
) : Serializable


data class MusicInfo(
    val id: Int?,
    val albumName: String?,
    val bpm: Int?,
    val coverUrl: String?,
    val createTime: String?,
    val duration: Int?,
    val mediaAction: String?,
    val musicName: String?,
    val musicNo: String?,
    val musicSinger: String?,
    val price: Int?,
    val size: Int?,

    //自己添加的参数
    var isPlaying: Boolean?,   //标志是不是正在播放
    var isKing: Boolean?
) : Serializable


class AddSongBean : Serializable




class DeleteSongBean : Serializable

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



data class Token(
    val accessToken: String
)