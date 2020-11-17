package com.hifive.sdk.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 音乐歌曲作者实体类
 *
 * @author huchao
 */
public class HifiveMusicAuthorModel implements Serializable {
    private String name;//名称
    private String code;//编号
    private String avatar;//头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
