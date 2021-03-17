package com.example.open_play_android_sdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;

import com.hfopenmusic.sdk.ui.player.HFLivePlayer;

public class MainActivity extends AppCompatActivity {
    private boolean flag;
    private AppCompatButton play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play =  findViewById(R.id.play);
        initView();
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