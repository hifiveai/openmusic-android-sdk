package com.hfopenmusic.sdk.listener;

import com.hfopen.sdk.entity.MusicRecord;

public interface HFPlayMusicListener {
    /**
     * 音乐详情
     * @param musicDetail
     * @param url
     */
    void onPlayMusic(MusicRecord musicDetail,String url);

    /**
     * 播放列表删除所有歌曲
     */
    void onStop();

    /**
     * 关闭界面
     */
    void onCloseOpenMusic();
}
