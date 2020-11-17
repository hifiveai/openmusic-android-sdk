package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 用户歌单的实体类
 *
 * @author huchao
 */
public class HifiveMusicUserSheetModel implements Serializable {
    private String sheetId;//歌单id
    private String type;//歌单名称
    private String sheetName;//歌单背景图
    private String createTime;//歌单介绍

    public HifiveMusicUserSheetModel(String sheetId, String sheetName) {
        this.sheetId = sheetId;
        this.sheetName = sheetName;
    }
    public HifiveMusicUserSheetModel() {

    }
    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
