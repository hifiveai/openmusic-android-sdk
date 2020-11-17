package com.hifive.sdk.demo.model;

import java.io.Serializable;
/**
 * 音乐版本的实体
 *
 * @author huchao
 */
public class HifiveMusicVersionModel implements Serializable {
    private String musicId;//音乐id
    private String name;//版本名称
    private int majorVersion;//是否为主版本，1：是 0：否
    private int free;//1：免费0：收费
    private int price;//价格
    private int duration;//时长（秒）
    private int auditionBegin;//推荐试听开始时间
    private int auditionEnd;//推荐试听结束时间

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
}
