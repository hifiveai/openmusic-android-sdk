package com.hf.music.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;

import static android.content.Context.AUDIO_SERVICE;

public class AudioSoundManager  {


    private AudioManager mAudioManager;


    /**
     * 初始化操作
     * @param content           playService对象
     */
    public AudioSoundManager(@NonNull Context content) {
        mAudioManager = (AudioManager) content.getSystemService(AUDIO_SERVICE);
    }

    /**
     * 设置音量
     * @param volume
     */
    public void setVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 获取当前音量
     */
    public int getVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取最大音量
     */
    public int getMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * 切换到外放
     */
    public void changeToSpeaker(){
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(true);
    }


    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        mAudioManager.setSpeakerphoneOn(false);
    }





    /**
     * 切换到听筒
     */
    @SuppressLint("ObsoleteSdkInt")
    public void changeToReceiver(){
        mAudioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }


}
