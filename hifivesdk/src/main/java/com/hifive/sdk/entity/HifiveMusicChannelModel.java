package com.hifive.sdk.entity;

import java.io.Serializable;

/**
 * 商户电台的实体类
 *
 * @author huchao
 */
public class HifiveMusicChannelModel implements Serializable {
    private String channelId;
    private String channelName;
    private String coverUrl;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
