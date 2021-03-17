package com.hf.music.service

import android.os.Binder

class HFPlayBinder(private val service: PlayService) : Binder(){
    fun getService(): PlayService? {
        return service
    }
}