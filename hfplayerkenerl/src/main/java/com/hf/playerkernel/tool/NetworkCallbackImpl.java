package com.hf.playerkernel.tool;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.hf.playerkernel.config.MusicPlayAction;
import com.hf.playerkernel.manager.HFPlayerApi;
import com.hf.playerkernel.utils.MusicLogUtils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        MusicLogUtils.i("网络已连接");

    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        MusicLogUtils.i("网络已断开");
        if(HFPlayerApi.with() != null){
            HFPlayerApi.with().sendMessage(MusicPlayAction.STATE_PAUSE);
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if(HFPlayerApi.with() != null){
                HFPlayerApi.with().sendMessage(MusicPlayAction.STATE_PLAYING);
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                MusicLogUtils.i("wifi已连接");
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                MusicLogUtils.i("数据流量已经连接");
            } else {
                MusicLogUtils.i("其他网络");
            }
        }
    }

}
