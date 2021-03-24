package com.example.open_play_android_sdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import com.hfopenmusic.sdk.player.HFLivePlayer;
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
        HFLivePlayer.getInstance().showPlayer(MainActivity.this);
        play.setOnClickListener(view -> {
            if(!flag){
                HFLivePlayer.getInstance().showPlayer(MainActivity.this);
                flag = true ;
                play.setText("关闭播放器");
            }else{
                HFLivePlayer.getInstance().removePlayer();
                flag = false;
                play.setText("开启播放器");
            }
        });
    }
}