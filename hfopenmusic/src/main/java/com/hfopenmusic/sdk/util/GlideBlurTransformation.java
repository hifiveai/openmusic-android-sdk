package com.hfopenmusic.sdk.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.security.MessageDigest;

import io.reactivex.annotations.NonNull;

public class GlideBlurTransformation extends CenterCrop {
    private Context context;

    public GlideBlurTransformation(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        // 设置渲染的模糊程度, 25f是最大模糊度
        return GlideBlurBitmapUtil.instance().blurBitmap(context, toTransform, 1, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }


}