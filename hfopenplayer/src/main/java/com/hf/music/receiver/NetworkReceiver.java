package com.hf.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hf.music.manager.HFPlayer;
import com.hf.music.utils.MusicLogUtils;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();//获取网络状态
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                MusicLogUtils.i("网络已连接");
//                HFPlayer.with().playPause();
            } else {
                MusicLogUtils.i("网络已断开");
//                HFPlayer.with().stop();
            }
//            isNetWork = !isNetWork;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
