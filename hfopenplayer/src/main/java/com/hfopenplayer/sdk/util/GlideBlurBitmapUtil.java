package com.hfopenplayer.sdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Function:实现高斯模糊工具类
 */
public class GlideBlurBitmapUtil {
    /**
     * 图片缩放比例(即模糊度);
     * 缩放比例越大，模糊度越高
     */
    private static final int BITMAP_SCALE = 16;


    private static GlideBlurBitmapUtil sInstance;
    private GlideBlurBitmapUtil() {
    }
    public static GlideBlurBitmapUtil instance() {
        if (sInstance == null) {
            synchronized (GlideBlurBitmapUtil.class) {
                if (sInstance == null) {
                    sInstance = new GlideBlurBitmapUtil();
                }
            }
        }
        return sInstance;
    }
    /**
     * @param context   上下文对象
     * @param image     需要模糊的图片
     * @param     blurRadius 图片模糊的值
     * @param outWidth  输入出的宽度
     * @param outHeight 输出的高度
     * @return 模糊处理后的Bitmap
     */
    public Bitmap blurBitmap(Context context, Bitmap image, float blurRadius, int outWidth, int outHeight) {
        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, outWidth/BITMAP_SCALE, outHeight/BITMAP_SCALE, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度
        //其中blurRadius为模糊处理的虚化程度，不断对该数值的增大，会造成CPU的紧张，通过简单的多次使用，默认最大为25。当然越小的话对CPU负担越不重。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript.setRadius(blurRadius);
        }
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

}