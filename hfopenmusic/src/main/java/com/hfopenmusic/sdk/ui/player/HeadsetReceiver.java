package com.hfopenmusic.sdk.ui.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

public class HeadsetReceiver extends BroadcastReceiver {

    public void registerReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        context.registerReceiver(this,intentFilter);
    }

    public void unRegisterReceiver(Context context){
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
//            //插入和拔出耳机会触发此广播
//            case Intent.ACTION_HEADSET_PLUG:
//                int state = intent.getIntExtra("state", 0);
//                Log.e("BroadcastReceiver","-------------"+state);
//                if (state == 1){
//                    //耳机已插入
//                } else if (state == 0){
//                    //耳机已拔出
//                }
//                break;
            case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                //耳机拔出时，可以暂停视频播放或做其他事情
                break;
            case Intent.ACTION_MEDIA_BUTTON:
                KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        break;
                }
                Log.e("BroadcastReceiver","-------------"+event.getKeyCode());
                break;
//            case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
//                int state2 = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
//                Log.e("BroadcastReceiver","-------------"+state2);
//                switch (state2) {
//                    case BluetoothProfile.STATE_CONNECTING:
//                        break;
//                    case BluetoothProfile.STATE_CONNECTED:
//                        break;
//                    case BluetoothHeadset.STATE_DISCONNECTED:
//                        break;
//                    case BluetoothHeadset.STATE_DISCONNECTING:
//                        break;
//                }
//                break;

        }
    }
}
