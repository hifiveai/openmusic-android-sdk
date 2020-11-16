package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 音乐歌单的实体类
 *
 * @author huchao
 */
public class HifiveMusicSheetModel implements Serializable {
    private long id;//歌单id
    private String name;//歌单名称
    private List<HifiveMusicSheetTipsModel> tips;//歌单标签
    private String imageUrl;//歌单背景图
    private String introduce;//歌单介绍

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HifiveMusicSheetTipsModel> getTips() {
        return tips;
    }

    public void setTips(List<HifiveMusicSheetTipsModel> tips) {
        this.tips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

}
