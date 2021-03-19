package com.hf.music.inter;


import com.hf.music.model.AudioBean;

/**
 * 播放进度监听器
 */
public interface HFPlayerEventListener {

    /**
     * 切换歌曲
     * 主要是切换歌曲的时候需要及时刷新界面信息
     */
    void onChange(AudioBean music);

    /**
     * 播放状态改变
     */
    void onPlayStateChanged(int state);

    /**
     * 更新进度
     * 主要是播放音乐或者拖动进度条时，需要更新进度
     */
    void onProgressUpdate(int progress,int duration);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);

}
