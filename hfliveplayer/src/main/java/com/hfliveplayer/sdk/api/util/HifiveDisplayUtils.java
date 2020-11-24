package com.hfliveplayer.sdk.demo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;


import com.hfliveplayer.sdk.demo.model.HifiveMusicLyricDetailModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huchao
 * @date 2020/11/04
 * 像素转化工具
 */

public class HifiveDisplayUtils {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * dp转换成px
     */
    public static int dip2px(Context context, float dpVale) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }
    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 除数
     * @param value2 被除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static float div(double value1, double value2, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        //默认保留两位会有错误，这里设置保留小数点后4位
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    /**
     * 提供动态歌词获取歌词详细信息
     * @param content 歌词
     */
    public static List<HifiveMusicLyricDetailModel> getLyricDetailModels(String content) {
        Log.e("TASG","lyric=="+content);
        List<HifiveMusicLyricDetailModel> detailModels = new ArrayList<>();
        String[] lyric = content.split("\\n");
        String regex = "\\[(.*?)]";
        for (String s1 : lyric) {
            HifiveMusicLyricDetailModel detailModel = new HifiveMusicLyricDetailModel();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s1);
            while (matcher.find()) {
                long time = getStartTime(matcher.group(1));
                Log.e("TAGFG","time=="+time);
                if(time != -1) {
                    detailModel.setStartTime(getStartTime(matcher.group(1)));
                    detailModel.setContent(s1.replace("[" + matcher.group(1) + "]", "").trim());
                    if (!TextUtils.isEmpty(detailModel.getContent())) {
                        detailModels.add(detailModel);
                    }
                }
            }
        }
        return detailModels;
    }
    //将时间字符转为时间戳
    private static long getStartTime(String time) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss.SSS");
            Date date = simpleDateFormat.parse(time);
            return date.getTime()+TimeZone.getDefault().getRawOffset();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}