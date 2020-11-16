package com.hifive.sdk.demo.model;

import java.io.Serializable;

/**
 * 歌单标签的实体类
 *
 * @author huchao
 */
public class HifiveMusicSheetTipsModel implements Serializable {
    private String name;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
