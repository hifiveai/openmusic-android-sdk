package com.hf.test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hf.player.view.HFPlayer;
import com.hf.player.view.HFPlayerViewListener;
import com.hf.test.R;
import com.hfopen.sdk.entity.MusicRecord;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.hfopenmusic.sdk.listener.HFPlayMusicListener;
import com.hfopenmusic.sdk.util.HifiveDisplayUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class MainActivity extends AppCompatActivity {
    private boolean flag;

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
        int type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            //            HFPlayer.getInstance().removePlayer();
            HFOpenMusic.getInstance().closeOpenMusic();
            HFPlayer.getInstance().showPlayer(MainActivity.this)
                    .setListener(new HFPlayerViewListener() {
                        @Override
                        public void onClick() {
                            play(null, null);
                        }

                        @Override
                        public void onPre() {
                            Toast.makeText(MainActivity.this, "上一首", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPlayPause(boolean isPlaying) {
                            Toast.makeText(MainActivity.this, isPlaying? "暂停" : "播放" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext() {
                            Toast.makeText(MainActivity.this, "下一首", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Toast.makeText(MainActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(MainActivity.this, "播放错误", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (type == 2) {
            HFOpenMusic.getInstance().closeOpenMusic();
            if (flag) {
                HFOpenMusic.getInstance().closeOpenMusic();
                flag = false;
            } else {
                showMusic(false);
            }
        } else if (type == 3) {
            //            HFPlayer.getInstance().removePlayer();
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
        }

    }

    private void play(MusicRecord musicDetail, String url) {
        if (musicDetail != null) {
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle(musicDetail.getMusicName())
                    .setMajorVersion(musicDetail.getVersion().get(0).getMajorVersion())
                    .setCover(musicDetail.getCover().get(0).getUrl())
                    .playWithUrl(url);
        } else {
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle("测试测试")
                    .setMajorVersion(true)
                    .setCover("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20190521%2F17%2F1558430156-SBswiePxFE.jpg&refer=http%3A%2F%2Fimage.biaobaiju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619162218&t=409c6be07cf495ccc4dcf3bc23f94028")
                    .playWithUrl("https://static-test.hifiveai.open.hifiveai.com/WaterMark/hifive/KPM/KPM/mp3_320/KPM_KPM_0948/KPM_KPM_0948_01601.mp3?param=ns5eoi3vWyU_4dWGiR9zWi6PmGfyMU2lOCQX63jrsygAQN2uTQq6PyANuRnJkNm8B6QIaU_0eNQ1fT9wuxPQ78RrIQ16FTkMq2CXJgnaBtgG6cYjUyJ6Eez88bjggmubieX8K-RJ0Af-2xSpSPV8KQ&sign=61b1e03016e685b259b965bc79c691d8&t=1616756630");
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
                            Toast.makeText(MainActivity.this, "歌曲:"+musicDetail.getMusicName(), Toast.LENGTH_SHORT).show();
                            Log.e("HFPlayMusicListener", url);
                        }

                    }

                    @Override
                    public void onStop() {
                        HFPlayer.getInstance().stopPlay();
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        HFPlayer.getInstance().destory();
//        HFOpenMusic.getInstance().closeOpenMusic();
//    }

}