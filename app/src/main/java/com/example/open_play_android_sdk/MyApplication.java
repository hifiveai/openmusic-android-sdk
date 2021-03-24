package com.example.open_play_android_sdk;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.hf.music.manager.HFPlayer;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化MultiDex
        MultiDex.install(this);

        HFPlayer.init(this)
                .setDebug(true)
                .setMaxBufferSize(200 * 1024)
                .apply();
    }
}
