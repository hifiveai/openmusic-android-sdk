package com.hfopenmusic.sdk.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.hfopenmusic.sdk.listener.LifeCycleListener
import com.hfopenmusic.sdk.player.HFLivePlayer

/**
 * 空白Fragment实现生命周期管理
 */

class LifeFragment: Fragment() {

    private var lifeListener: LifeCycleListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun addLifeListener(listener: LifeCycleListener?) {
        this.lifeListener = listener
    }

    fun removeLifeListener() {
        lifeListener = null
    }


    override fun onStart() {
        super.onStart()
        Log.d("======", "player_onStart")
        HFLivePlayer.getInstance().attach(activity)
        lifeListener?.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.d("======", "player_onStop")
        HFLivePlayer.getInstance().detach(activity)
        lifeListener?.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("======", "player_onResume")
        lifeListener?.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("======", "player_onPause")
        lifeListener?.onPause()
    }

    override fun onDestroy() {
        Log.e("======", "player_onDestroy")
        super.onDestroy()
        HFLivePlayer.getInstance().destory()
        lifeListener?.onDestroy()
        removeLifeListener()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        Log.e("======", "player_onMultiWindowModeChanged")
        super.onMultiWindowModeChanged(isInMultiWindowMode)
    }
}