package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 音乐歌曲详情的实体类
 *
 * @author huchao
 */
public class HifiveMusicDetailModel implements Serializable {
    private String musicId;//歌曲id
    private String musicName;//歌曲名称
    private HifiveMusicAlbumModel album;
    private HifiveMusicImageModel cover;//封面图
    private List<HifiveMusicAuthorModel> artist;//表演者
    private List<HifiveMusicAuthorModel> author;//作词者
    private List<HifiveMusicAuthorModel> composer;//作曲者
    private List<HifiveMusicAuthorModel> arranger;//编曲者
    private List<HifiveMusicAuthorModel> mastery;//制作人
    private List<HifiveMusicAuthorModel> maker;//MV制作人
    private List<HifiveMusicTagModel> tag;//标签
    private int isMajor;//是否是主版本1：是，0：不是
    private int duration;//时长
    private int auditionBegin;//推荐试听开始时间
    private int auditionEnd;//推荐试听结束时间
    private int bpm;//节拍数
    private int price;//价格
    private String waveUrl;//波形图
    private int forSale;//是否销售，1：是 0：否
    private int authType;//音乐授权类型0:K歌、1:播放、2:自定义
    private HifiveMusicFileModel file;
    private int recordId;//播放记录ID
    private HifiveMusiclyricModel lyric;
    private String mvUrl;
    private String intro;
    private int status;//音乐上下架状态0:下架、1:上架
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

    public HifiveMusicAlbumModel getAlbum() {
        return album;
    }

    public void setAlbum(HifiveMusicAlbumModel album) {
        this.album = album;
    }

    public HifiveMusicImageModel getCover() {
        return cover;
    }

    public void setCover(HifiveMusicImageModel cover) {
        this.cover = cover;
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

    public List<HifiveMusicAuthorModel> getMastery() {
        return mastery;
    }

    public void setMastery(List<HifiveMusicAuthorModel> mastery) {
        this.mastery = mastery;
    }

    public List<HifiveMusicAuthorModel> getMaker() {
        return maker;
    }

    public void setMaker(List<HifiveMusicAuthorModel> maker) {
        this.maker = maker;
    }

    public List<HifiveMusicTagModel> getTag() {
        return tag;
    }

    public void setTag(List<HifiveMusicTagModel> tag) {
        this.tag = tag;
    }

    public int getIsMajor() {
        return isMajor;
    }

    public void setIsMajor(int isMajor) {
        this.isMajor = isMajor;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAuditionBegin() {
        return auditionBegin;
    }

    public void setAuditionBegin(int auditionBegin) {
        this.auditionBegin = auditionBegin;
    }

    public int getAuditionEnd() {
        return auditionEnd;
    }

    public void setAuditionEnd(int auditionEnd) {
        this.auditionEnd = auditionEnd;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getWaveUrl() {
        return waveUrl;
    }

    public void setWaveUrl(String waveUrl) {
        this.waveUrl = waveUrl;
    }

    public int getForSale() {
        return forSale;
    }

    public void setForSale(int forSale) {
        this.forSale = forSale;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public HifiveMusicFileModel getFile() {
        return file;
    }

    public void setFile(HifiveMusicFileModel file) {
        this.file = file;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public HifiveMusiclyricModel getLyric() {
        return lyric;
    }

    public void setLyric(HifiveMusiclyricModel lyric) {
        this.lyric = lyric;
    }

    public String getMvUrl() {
        return mvUrl;
    }

    public void setMvUrl(String mvUrl) {
        this.mvUrl = mvUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HifiveMusicVersionModel> getVersion() {
        return version;
    }

    public void setVersion(List<HifiveMusicVersionModel> version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == null || o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        HifiveMusicDetailModel musicModel = (HifiveMusicDetailModel) o;
        return musicId.equals(musicModel.getMusicId());
    }


}
