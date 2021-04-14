package com.hf.player.view

interface HFPlayerViewListener {
    fun onClick()
    fun onExpanded()
    fun onFold()
    fun onPre()
    fun onPlayPause(isPlaying : Boolean )
    fun onNext()
    fun onComplete()
    fun onError()
}