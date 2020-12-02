package com.hfliveplayer.sdk.model;

import java.io.Serializable;

/**
 * 音乐歌曲文件Url相关信息实体类
 *
 * @author huchao
 */
public class HifiveMusicFileModel implements Serializable {
    private String url;//编码
    private String ext;//名称
    private String expires;//编码
    private int size;//名称

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
