package com.hfliveplayer.sdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * 歌曲标签的实体类
 *
 * @author huchao
 */
public class HifiveMusicTagModel implements Serializable {
    private int tagId;//标签id
    private int pid;//父标签Id
    private String tagName;//标签名称
    private String coverUrl;//标签图
    private List<HifiveMusicTagModel> child;
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<HifiveMusicTagModel> getChild() {
        return child;
    }

    public void setChild(List<HifiveMusicTagModel> child) {
        this.child = child;
    }
}
