package com.hfliveplayer.sdk.model;

import java.io.Serializable;

/**
 * 搜索历史记录的实体类
 *
 * @author huchao
 */
public class HifiveMusicSearchrModel implements Serializable {

    private int searchId;//搜索id
    private String createTime;//搜索时间
    private String keyword;//搜索关键字

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
