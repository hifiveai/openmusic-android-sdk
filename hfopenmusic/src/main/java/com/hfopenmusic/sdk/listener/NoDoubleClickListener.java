package com.hfopenmusic.sdk.listener;

import android.view.View;

public abstract class NoDoubleClickListener implements View.OnClickListener {


    public static final int MIN_CLICK_DELAY_TIME = 500;//这里设置不能超过多长时间
    private long lastClickTime = 0;

    protected abstract void onNoDoubleClick(View v);
    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}