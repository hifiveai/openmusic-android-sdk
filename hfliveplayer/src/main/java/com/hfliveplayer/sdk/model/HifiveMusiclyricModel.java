package com.hfliveplayer.sdk.model;

import java.io.Serializable;

/**
 * 音乐歌曲歌词信息实体类
 *
 * @author huchao
 */
public class HifiveMusiclyricModel implements Serializable {
    private String dynamicUrl;//动听歌词
    private String staticUrl;//静态歌词

    public String getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(String dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }
}
