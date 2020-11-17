package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 商户电台的实体类
 *
 * @author huchao
 */
public class HifiveMusicChannelModel implements Serializable {
    private String groupId;
    private String groupName;
    private String coverUrl;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
