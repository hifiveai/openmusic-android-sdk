package com.hf.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.open_play_android_sdk.R;
import com.hf.openplayer.HFOpenMusicPlayer;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hfopen.sdk.common.HFOpenCallback;
import com.hfopen.sdk.manager.HFOpenApi;
import com.hfopen.sdk.rx.BaseException;
import com.hfopenmusic.sdk.HFOpenMusic;
import com.tencent.bugly.crashreport.CrashReport;

public class LoginActivity extends AppCompatActivity {
    private AppCompatButton play3;
    private String secretKey, appId;
    private String memberId;
    private boolean flag;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CrashReport.initCrashReport(getApplicationContext(), "2e151e6755", false);
        init();
        initView();
    }

    private void init() {
        secretKey = "59b1aff189b3474398";
        appId = "3faeec81030444e98acf6af9ba32752a";
        memberId = "hifivetest";
        HFOpenApi.registerApp(getApplication(), appId, secretKey, memberId);
        HFOpenApi.configCallBack(new HFOpenCallback() {
            @Override
            public void onError(BaseException exception) {
                HFOpenMusic.getInstance().showToast(LoginActivity.this, exception.getMsg());
            }

            @Override
            public void onSuccess() {

            }
        });

        SPUtils.put(this, SPUtils.appId, appId);
        SPUtils.put(this, SPUtils.secretKey, secretKey);
        flag = true;
        Toast.makeText(LoginActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        play3 = findViewById(R.id.play3);
        play3.setOnClickListener(v -> {
            if (!flag) {
                Toast.makeText(LoginActivity.this, "请先初始化SDK", Toast.LENGTH_SHORT).show();
                return;
            }
            initOpenPlayer();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        });

    }

    private void initPlayer() {
        HFPlayerApi.init(getApplication())
                .setDebug(true)
                .setMaxBufferSize(ConsData.MaxBufferSize)
                .setUseCache(ConsData.UseCache)
                .setReconnect(ConsData.Reconnect)
                .setNotificationSwitch(true)
                .apply();
    }

    private void initOpenPlayer() {
        Log.d(ConsData.TAG, "musicType:" + ConsData.musicType.toString());

        HFOpenMusicPlayer.getInstance()
                .registerApp(getApplication(), appId, secretKey, memberId)
                .setDebug(true)
                .setMaxBufferSize(ConsData.MaxBufferSize)
                .setUseCache(ConsData.UseCache)
                .setReconnect(ConsData.Reconnect)
                .setNotificationSwitch(true)
                .setListenType(ConsData.musicType.toString())
                .apply();
    }

}
