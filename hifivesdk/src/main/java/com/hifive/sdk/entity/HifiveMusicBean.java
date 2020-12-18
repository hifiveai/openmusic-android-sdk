package com.hifive.sdk.entity;

import java.util.List;


/**
 * 体类
 *
 * @author huchao
 */
public class HifiveMusicBean<T>{
    private int totalPage;
    private boolean isRecommand;
    private List<T> records;

    public int getTotalPage() {
        return totalPage;
    }

    public boolean isRecommand() {
        return isRecommand;
    }

    public List<T> getRecords() {
        return records;
    }
}
