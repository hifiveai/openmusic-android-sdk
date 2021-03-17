package com.hfopenplayer.sdk.util;

import android.util.Log;

public class LoggerUtils {
    private static final boolean isinput = true;// 用于判断是否输出日志 可以控制整个应用的输出 调试模式 true  上线模式 false

    //Android的Log等级通常有五类，按照日志级别由低到高分别是Verbose、Debug、Info、Warning、Error  等级逐步增大
    private final static int logLevel = Log.DEBUG; //日志级别，大于或者等于logLevel才会被打印

    /**
     * 输出故障的日志信息
     *
     * @param tag
     *            标记
     * @param msg
     *            详细描述
     */
    public static void d(String tag, String msg) {

        if (isinput) {
            if(logLevel <= Log.DEBUG) {
                Log.d(tag ,"日志信息:" + msg);
            }
        }
    }

    /**
     * 输出错误的日志信息
     *
     */
    public static void e(String tag, String msg) {

        if (isinput) {
            if(logLevel <= Log.DEBUG) {
                Log.e(tag ,"日志信息:" + msg);
            }
        }
    }

    /**
     * 输出程序的日志信息
     *
     */
    public static void i(String tag, String msg) {

        if (isinput) {
            if(logLevel <= Log.DEBUG) {
                Log.i(tag ,"日志信息:" + msg);
            }
        }
    }

    /**
     * 输出冗余的日志信息
     */
    public static void v(String tag, String msg) {

        if (isinput) {
            if(logLevel <= Log.DEBUG) {
                Log.v(tag ,"日志信息:" + msg);
            }
        }
    }

    /**
     * 输出警告的日志信息
     *
     */
    public static void w(String tag, String msg) {

        if (isinput) {
            if(logLevel <= Log.DEBUG) {
                Log.w(tag ,"日志信息:" + msg);
            }
        }
    }
}
