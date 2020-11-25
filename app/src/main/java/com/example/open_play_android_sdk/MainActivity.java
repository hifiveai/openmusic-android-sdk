package com.example.open_play_android_sdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Toast;

import com.hfliveplayer.sdk.ui.player.HFLivePlayer;
import com.hifive.sdk.hInterface.DataResponse;
import com.hifive.sdk.manager.HFLiveApi;

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
        play.setOnClickListener(view -> {
            if(!flag){
                HFLivePlayer.getInstance().add(MainActivity.this);
                flag = true ;
                play.setText("关闭播放器");
            }else{
                HFLivePlayer.getInstance().remove();
                flag = false;
                play.setText("开启播放器");
            }
        });
    }
    @Override
    protected void onStart() {
        HFLivePlayer.getInstance().attach(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        HFLivePlayer.getInstance().destory();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        HFLivePlayer.getInstance().detach(this);
        super.onStop();
    }
}