package com.hfliveplayer.sdk.listener

interface LifeCycleListener {
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}