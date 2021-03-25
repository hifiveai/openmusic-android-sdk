package com.hf.player.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

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
        HFPlayerManager.getInstance().attach(activity)
        lifeListener?.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.d("======", "player_onStop")
        HFPlayerManager.getInstance().detach(activity)
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
        HFPlayerManager.getInstance().destory()
        lifeListener?.onDestroy()
        removeLifeListener()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        Log.e("======", "player_onMultiWindowModeChanged")
        super.onMultiWindowModeChanged(isInMultiWindowMode)
    }
}