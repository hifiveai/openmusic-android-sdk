package com.hf.openplayer;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.hf.player.view.HFPlayer;
import com.hf.player.view.HFPlayerViewListener;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.hfopenmusic.sdk.listener.HFPlayMusicListener;
import com.hfopenmusic.sdk.util.HifiveDisplayUtils;


/**
 * 播放器UI
 */
public class HFOpenMusicPlayer {
    private static volatile HFOpenMusicPlayer mInstance;
    private boolean flag;
    private String musicId;

    private HFOpenMusicPlayer() {

    }
    public static HFOpenMusicPlayer getInstance() {
        if (mInstance == null) {
            synchronized (HFOpenMusicPlayer.class) {
                if (mInstance == null) {
                    mInstance = new HFOpenMusicPlayer();
                }
            }
        }
        return mInstance;
    }

    //添加播放器view
    public HFOpenMusicPlayer showPlayer(FragmentActivity activity) {
        return showPlayer(activity,0,0);
    }

    //添加播放器view
    public HFOpenMusicPlayer showPlayer(FragmentActivity activity, int marginTop, int marginBottom) {
        HFPlayer.getInstance().showPlayer(activity,marginTop,marginBottom)
                .setListener(new HFPlayerViewListener() {
                    @Override
                    public void onClick() {
                        Log.e("HFPlayerViewListener", "onClick");
                        if (flag) {
                            HFOpenMusic.getInstance().closeOpenMusic();
                            HFPlayer.getInstance().setMarginBottom(0);
                            flag = false;
                        } else {
                            showMusic(activity);
                        }
                    }

                    @Override
                    public void onPre() {
                        try {
                            if(musicId != null){
                                int currentPosition = (int) HFPlayerApi.with().getCurrentPosition();
                                HFOpenMusic.getInstance().reportListen(musicId,currentPosition,System.currentTimeMillis());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HFOpenMusic.getInstance().playLastMusic();
                    }

                    @Override
                    public void onPlayPause(boolean isPlaying) {
                    }

                    @Override
                    public void onNext() {
                        try {
                            if(musicId != null){
                                int currentPosition = (int) HFPlayerApi.with().getCurrentPosition();
                                HFOpenMusic.getInstance().reportListen(musicId,currentPosition,System.currentTimeMillis());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HFOpenMusic.getInstance().playNextMusic();
                    }

                    @Override
                    public void onComplete() {
                        try {
                            if(musicId != null){
                                int currentPosition = (int) HFPlayerApi.with().getCurrentPosition();
                                HFOpenMusic.getInstance().reportListen(musicId,currentPosition,System.currentTimeMillis());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HFOpenMusic.getInstance().playNextMusic();
                    }

                    @Override
                    public void onError() {
                        HFOpenMusic.getInstance().playNextMusic();
                    }
                });


        return null;
    }

    //设置音乐授权类型
    public HFOpenMusicPlayer setListenType(String type) {
        HFOpenMusic.getInstance().setListenType(type);
        return this;
    }

    private void showMusic(FragmentActivity activity) {
        flag = true;
        HFOpenMusic.getInstance()
                .setPlayListen(new HFPlayMusicListener() {
                    @Override
                    public void onPlayMusic(MusicRecord musicDetail, String url) {
                        play(musicDetail, url);
                    }

                    @Override
                    public void onStop() {
                        HFPlayer.getInstance().stopPlay();
                    }

                    @Override
                    public void onCloseOpenMusic() {
                        HFPlayer.getInstance().setMarginBottom(0);
                        flag = false;
                    }
                })
                .showOpenMusic(activity);
        HFPlayer.getInstance().setMarginBottom(HifiveDisplayUtils.getPlayerHeight(activity));
    }

    private void play(MusicRecord musicDetail, String url) {
        if (musicDetail != null) {
            musicId = musicDetail.getMusicId();

            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle(musicDetail.getMusicName())
                    .setMajorVersion(musicDetail.getVersion().get(0).getMajorVersion())
                    .setCover(musicDetail.getCover().get(0).getUrl())
                    .playWithUrl(url);
        }
    }



}