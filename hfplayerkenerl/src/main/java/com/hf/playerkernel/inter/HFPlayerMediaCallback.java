package com.hf.playerkernel.inter;

/**
 * 锁屏/通话栏事件
 */
public interface HFPlayerMediaCallback {
    /**
     * 切换歌曲
     * 主要是切换歌曲的时候需要及时刷新界面信息
     */
    void onPre();

    /**
     * 播放状态改变
     */
    void playPause();

    void onNext();

}
