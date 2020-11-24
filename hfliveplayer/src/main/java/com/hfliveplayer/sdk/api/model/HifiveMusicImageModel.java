package com.hfliveplayer.sdk.demo.model;

import java.io.Serializable;

/**
 * 音乐歌曲封面实体类
 *
 * @author huchao
 */
public class HifiveMusicImageModel implements Serializable {
    private String url;//名称
    private String size;//编号

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
