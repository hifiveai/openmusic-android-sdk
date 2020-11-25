package com.hfliveplayer.sdk.model;

import java.io.Serializable;

/**
 * 音乐歌曲专辑信息实体类
 *
 * @author huchao
 */
public class HifiveMusicAlbumModel implements Serializable {
    private int id;//专辑id
    private String code;//编码
    private String name;//名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
