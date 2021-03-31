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
 * 带列表版播放器
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

    /**
     * 显示播放器view
     * @param activity
     * @return
     */
    public HFOpenMusicPlayer showPlayer(FragmentActivity activity) {
        return showPlayer(activity,0,0);
    }

    /**
     * 显示播放器view
     * @param activity
     * @param marginTop
     * @param marginBottom
     * @return
     */
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
                        report();
                        HFOpenMusic.getInstance().playLastMusic();
                    }

                    @Override
                    public void onPlayPause(boolean isPlaying) {
                    }

                    @Override
                    public void onNext() {
                        report();
                        HFOpenMusic.getInstance().playNextMusic();
                    }

                    @Override
                    public void onComplete() {
                        report();
                        HFOpenMusic.getInstance().playNextMusic();
                    }

                    @Override
                    public void onError() {
                        report();
                        HFOpenMusic.getInstance().playNextMusic();
                    }
                });


        return null;
    }

    /**
     * 移除播放器
     */
    public void removePlayer() {
        HFPlayer.getInstance().removePlayer();
        HFOpenMusic.getInstance().closeOpenMusic();
    }

    /**
     *  设置音乐授权类型
     *  @param type
     *  @return
     */
    public HFOpenMusicPlayer setListenType(String type) {
        HFOpenMusic.getInstance().setListenType(type);
        return this;
    }

    /**
     * 显示OpenAPI播放列表
     * @param activity  activity
     */
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

    /**
     * 点击列表播放歌曲
     * @param musicDetail  歌曲详情
     * @param url  歌曲播放地址
     */
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


    /**
     * 数据上报
     */
    private void report(){
        try {
            if(musicId != null){
                int currentPosition = (int) HFPlayerApi.with().getCurrentPosition();
                HFOpenMusic.getInstance().reportListen(musicId,currentPosition,System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}