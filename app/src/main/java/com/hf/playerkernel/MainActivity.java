package com.hf.playerkernel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hf.player.view.HFPlayer;
import com.hf.player.view.HFPlayerViewListener;
import com.hfopen.sdk.entity.HQListen;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.hfopenmusic.sdk.listener.HFPlayMusicListener;
import com.hfopenmusic.sdk.util.HifiveDisplayUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class MainActivity extends AppCompatActivity {
    private boolean flag;
    private AppCompatButton play, play2, play3;

    /**
     * 权限组
     */
    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        play2 = findViewById(R.id.play2);
        play3 = findViewById(R.id.play3);

        requestPermission();
        initView();
    }

    @SuppressLint("CheckResult")
    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissionsGroup)
                .subscribe(aBoolean -> {

                });
    }

    private void initView() {
        play.setOnClickListener(v -> {
            HFPlayer.getInstance().removePlayer();
            HFOpenMusic.getInstance().closeOpenMusic();
            HFPlayer.getInstance().showPlayer(MainActivity.this)
                    .setListener(new HFPlayerViewListener() {
                        @Override
                        public void onClick() {
                            play(null, null);
                        }

                        @Override
                        public void onPre() {

                        }

                        @Override
                        public void onPlayPause(boolean isPlaying) {

                        }

                        @Override
                        public void onNext() {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

        });


        play2.setOnClickListener(v -> {
            HFPlayer.getInstance().removePlayer();
            HFOpenMusic.getInstance().closeOpenMusic();
            if (flag) {
                HFOpenMusic.getInstance().closeOpenMusic();
                flag = false;
            } else {
                showMusic(false);
            }
        });

        play3.setOnClickListener(view -> {
            HFPlayer.getInstance().removePlayer();
            HFOpenMusic.getInstance().closeOpenMusic();
            flag = false;
            HFPlayer.getInstance().showPlayer(MainActivity.this)
                    .setListener(new HFPlayerViewListener() {
                        @Override
                        public void onClick() {
                            Log.e("HFPlayerViewListener", "onClick");
                            if (flag) {
                                HFOpenMusic.getInstance().closeOpenMusic();
                                HFPlayer.getInstance().updateViewY(0);
                                flag = false;
                            } else {
                                showMusic(true);
                            }
                        }

                        @Override
                        public void onPre() {
                            Log.e("HFPlayerViewListener", "onPre");
                            HFOpenMusic.getInstance().playLastMusic();
                        }

                        @Override
                        public void onPlayPause(boolean isPlaying) {
                            Log.e("HFPlayerViewListener", "onPlayPause");
                        }

                        @Override
                        public void onNext() {
                            Log.e("HFPlayerViewListener", "onNext");
                            HFOpenMusic.getInstance().playNextMusic();
                        }

                        @Override
                        public void onComplete() {
                            Log.e("HFPlayerViewListener", "onComplete");
                            HFOpenMusic.getInstance().playNextMusic();
                        }

                        @Override
                        public void onError() {
                            Log.e("HFPlayerViewListener", "onError");
                            HFOpenMusic.getInstance().playNextMusic();
                        }
                    });
        });
    }

    private void play(MusicRecord musicDetail, String url) {
        if (musicDetail != null) {
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle(musicDetail.getMusicName())
                    .setCover(musicDetail.getCover().get(0).getUrl())
                    .playWithUrl(url);
        } else {
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle("测试测试")
                    .setCover("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20190521%2F17%2F1558430156-SBswiePxFE.jpg&refer=http%3A%2F%2Fimage.biaobaiju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619162218&t=409c6be07cf495ccc4dcf3bc23f94028")
                    .playWithUrl("https://sharefs.yun.kugou.com/202103251442/489fd352b879416de9742d9c98797b6b/G197/M04/0E/18/ZYcBAF5x5-2AbFgmADaApn6O6Fw014.mp3");
        }
    }

    private void showMusic(boolean showplayer) {
        flag = true;
        HFOpenMusic.getInstance()
                .setPlayListen(new HFPlayMusicListener() {
                    @Override
                    public void onPlayMusic(MusicRecord musicDetail, String url) {
                        if (showplayer) {
                            play(musicDetail, url);
                        } else {
                            Log.e("HFPlayMusicListener", url);
                        }

                    }

                    @Override
                    public void onCloseOpenMusic() {
                        HFPlayer.getInstance().updateViewY(0);
                        flag = false;
                    }
                })
                .showOpenMusic(MainActivity.this);
        if (showplayer) {
            HFPlayer.getInstance().updateViewY(HifiveDisplayUtils.getPlayerHeight(this));
        }
    }
}