package com.hf.player.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object GlideUtil {

    @JvmStatic
     fun getBitmap(context : Context, url : String) : Bitmap?{
        var bitmap: Bitmap  ?= null
        Glide.with(context).asBitmap().load(url).into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {}

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                bitmap = resource
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
            }
        })
        return bitmap
    }
}