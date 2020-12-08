package com.hfliveplayer.sdk.listener;
/**
 * 歌曲播放监听
 * @author huchao
 */
public interface HifivePlayListener {
    void playError();//播放出错
    void playCompletion();//播放完成
}
