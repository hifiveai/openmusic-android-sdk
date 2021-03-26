package com.hf.playerkernel;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.hf.playerkernel.manager.HFPlayerApi;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化MultiDex
        MultiDex.install(this);
    }
}
