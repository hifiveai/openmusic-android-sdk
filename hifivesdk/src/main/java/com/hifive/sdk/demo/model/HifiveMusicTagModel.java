package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 歌曲标签的实体类
 *
 * @author huchao
 */
public class HifiveMusicTagModel implements Serializable {
    private String tagName;
    private long tagId;
    private List<HifiveMusicTagModel> child;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public List<HifiveMusicTagModel> getChild() {
        return child;
    }

    public void setChild(List<HifiveMusicTagModel> child) {
        this.child = child;
    }
}
