package com.hifive.sdk.demo.model;

import java.io.Serializable;

/**
 * 音乐歌曲歌词时间戳详细信息实体类
 *
 * @author huchao
 */
public class HifiveMusicLyricDetailModel implements Serializable {
    private long startTime;//开始时间
    private String content;//歌词内容

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
