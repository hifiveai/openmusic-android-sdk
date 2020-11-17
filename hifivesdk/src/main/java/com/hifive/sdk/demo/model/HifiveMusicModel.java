package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 音乐歌曲的实体类
 *
 * @author huchao
 */
public class HifiveMusicModel implements Serializable {
    private String musicId;//歌曲id
    private String musicName;//歌曲名称
    private String albumId;//专辑id
    private String albumName;//专辑名
    private int duration;//时长
    private List<HifiveMusicAuthorModel> artist;//表演者
    private List<HifiveMusicAuthorModel> author;//作词者
    private List<HifiveMusicAuthorModel> composer;//作曲者
    private List<HifiveMusicAuthorModel> arranger;//编曲者
    private List<HifiveMusicImageModel> cover;//编曲曲者

    private List<HifiveMusicTagModel> tag;//标签
    private String introduce;//歌曲介绍
    private int auditionBegin;//推荐试听开始时间
    private int auditionEnd;//推荐试听结束时间
    private int bpm;
    private List<HifiveMusicVersionModel> version;
    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<HifiveMusicTagModel> getTag() {
        return tag;
    }

    public void setTag(List<HifiveMusicTagModel> tag) {
        this.tag = tag;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public long getAuditionBegin() {
        return auditionBegin;
    }

    public void setAuditionBegin(int auditionBegin) {
        this.auditionBegin = auditionBegin;
    }

    public long getAuditionEnd() {
        return auditionEnd;
    }

    public void setAuditionEnd(int auditionEnd) {
        this.auditionEnd = auditionEnd;
    }

    public List<HifiveMusicVersionModel> getVersion() {
        return version;
    }

    public void setVersion(List<HifiveMusicVersionModel> version) {
        this.version = version;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public List<HifiveMusicAuthorModel> getArtist() {
        return artist;
    }

    public void setArtist(List<HifiveMusicAuthorModel> artist) {
        this.artist = artist;
    }

    public List<HifiveMusicAuthorModel> getAuthor() {
        return author;
    }

    public void setAuthor(List<HifiveMusicAuthorModel> author) {
        this.author = author;
    }

    public List<HifiveMusicAuthorModel> getComposer() {
        return composer;
    }

    public void setComposer(List<HifiveMusicAuthorModel> composer) {
        this.composer = composer;
    }

    public List<HifiveMusicAuthorModel> getArranger() {
        return arranger;
    }

    public void setArranger(List<HifiveMusicAuthorModel> arranger) {
        this.arranger = arranger;
    }

    public List<HifiveMusicImageModel> getCover() {
        return cover;
    }

    public void setCover(List<HifiveMusicImageModel> cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == null || o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        HifiveMusicModel musicModel = (HifiveMusicModel) o;
        return musicId.equals(musicModel.getMusicId());
    }


}
