package com.hfopenmusic.sdk.listener;

import com.hfopen.sdk.entity.MusicRecord;

public interface HFPlayMusicListener {
    void onPlayMusic(MusicRecord musicDetail,String url);

    void onStop();

    void onCloseOpenMusic();
}
