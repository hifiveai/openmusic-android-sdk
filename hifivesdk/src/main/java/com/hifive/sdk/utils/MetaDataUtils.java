package com.hifive.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class MetaDataUtils {

    /**
     * appliction MetaData读取
     */
    public static String getApplicationMetaData(Context context, String key) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);

            return info.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * activity MetaData读取
     */
    public static String getActivityMetaData(Activity context, String key) {
        ActivityInfo info;
        try {
            info = context.getPackageManager().getActivityInfo(context.getComponentName(),
                    PackageManager.GET_META_DATA);
            return info.metaData.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
