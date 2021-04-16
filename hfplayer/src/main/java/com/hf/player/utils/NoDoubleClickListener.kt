package com.hf.player.utils

import android.view.View

abstract class NoDoubleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    protected abstract fun onNoDoubleClick(v: View?)
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    companion object {
        const val MIN_CLICK_DELAY_TIME = 1000 //这里设置不能超过多长时间
    }
}