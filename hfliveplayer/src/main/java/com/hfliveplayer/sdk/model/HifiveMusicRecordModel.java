package com.hfliveplayer.sdk.model;

import java.io.Serializable;

/**
 * 音乐播放记录信息实体类
 *
 * @author huchao
 */
public class HifiveMusicRecordModel implements Serializable {
    private int recordId;// 播放记录Id
    private int duration;//时长
    private String mediaType;//播放记录类型 1-k歌；2-听歌

    public HifiveMusicRecordModel(int recordId, int duration, String mediaType) {
        this.recordId = recordId;
        this.duration = duration;
        this.mediaType = mediaType;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == null || o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        HifiveMusicRecordModel recordModel = (HifiveMusicRecordModel) o;
        return recordId == recordModel.getRecordId();
    }
}
