package com.hifive.sdk.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商户歌单的实体类
 *
 * @author huchao
 */
public class HifiveMusicSheetModel implements Serializable {
    private long sheetId;//歌单id
    private String sheetName;//歌单名称
    private long musicTotal;//音乐总数
    private String describe;//歌单描述
    private int free;//是否免费
    private int price;//歌单价格（分）
    private int type;//歌单类型， 1：自定义歌单，0：系统歌单
    private List<HifiveMusicTagModel> tag;//标签
    private HifiveMusicImageModel cover;//封面

    public long getSheetId() {
        return sheetId;
    }

    public void setSheetId(long sheetId) {
        this.sheetId = sheetId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public long getMusicTotal() {
        return musicTotal;
    }

    public void setMusicTotal(long musicTotal) {
        this.musicTotal = musicTotal;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<HifiveMusicTagModel> getTag() {
        return tag;
    }

    public void setTag(List<HifiveMusicTagModel> tag) {
        this.tag = tag;
    }

    public HifiveMusicImageModel getCover() {
        return cover;
    }

    public void setCover(HifiveMusicImageModel cover) {
        this.cover = cover;
    }
}
