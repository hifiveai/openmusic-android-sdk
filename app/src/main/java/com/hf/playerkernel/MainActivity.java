package com.hf.playerkernel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.hf.player.view.HFPlayer;
import com.hf.player.view.HFPlayerViewListener;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class MainActivity extends AppCompatActivity{
    private boolean flag;
    private AppCompatButton play;

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
        play =  findViewById(R.id.play);

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
//        HFLivePlayer.getInstance().showPlayer(MainActivity.this);
        play.setOnClickListener(view -> {
//            if(!flag){
//                HFLivePlayer.getInstance().showPlayer(MainActivity.this);
//                flag = true ;
//                play.setText("关闭播放器");
//            }else{
//                HFLivePlayer.getInstance().removePlayer();
//                flag = false;
//                play.setText("开启播放器");
//            }
            HFOpenMusic.getInstance().showOpenMusic(MainActivity.this);
            HFPlayer.getInstance().showPlayer(MainActivity.this);
            //初始化播放器UI
            HFPlayer.getInstance()
                    .setTitle("测试测试")
                    .setCover("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20190521%2F17%2F1558430156-SBswiePxFE.jpg&refer=http%3A%2F%2Fimage.biaobaiju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619162218&t=409c6be07cf495ccc4dcf3bc23f94028")
                    .playWithUrl("https://sharefs.yun.kugou.com/202103251442/489fd352b879416de9742d9c98797b6b/G197/M04/0E/18/ZYcBAF5x5-2AbFgmADaApn6O6Fw014.mp3")
                    .setListener(new HFPlayerViewListener() {
                        @Override
                        public void onClick() {
                            Log.e("HFPlayerViewListener", "onClick");
                        }

                        @Override
                        public void onPre() {
                            Log.e("HFPlayerViewListener", "onPre");
                        }

                        @Override
                        public void onPlayPause(boolean isPlaying) {
                            Log.e("HFPlayerViewListener", "onPlayPause");
                        }

                        @Override
                        public void onNext() {
                            Log.e("HFPlayerViewListener", "onNext");
                        }

                        @Override
                        public void onComplete() {
                            Log.e("HFPlayerViewListener", "onComplete");
                        }

                        @Override
                        public void onError() {
                            Log.e("HFPlayerViewListener", "onError");
                        }
                    });
        });
    }
}