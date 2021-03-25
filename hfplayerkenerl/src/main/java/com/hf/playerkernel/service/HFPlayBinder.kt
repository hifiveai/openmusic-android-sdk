package com.hf.playerkernel.service

import android.os.Binder

class HFPlayBinder(private val service: PlayService) : Binder(){
    fun getService(): PlayService? {
        return service
    }
}