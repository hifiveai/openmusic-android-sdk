package com.hf.player.view

interface LifeCycleListener {
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}