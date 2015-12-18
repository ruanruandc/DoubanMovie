package com.example.ruandongchuan.doubanmovie.util;

import android.util.Log;

import com.example.ruandongchuan.doubanmovie.BuildConfig;

/**
 * Created by ruandongchuan on 15-12-11.
 */
public class LogTool {
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOS = 5;
    public static int LOG_LEVEL = WARN;
    public static final boolean IS_DEBUG = BuildConfig.DEBUG;
    public static void e(String tag, String msg){
        if (LOG_LEVEL >= ERROR || IS_DEBUG){
            Log.e(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (LOG_LEVEL >= WARN || IS_DEBUG){
            Log.w(tag, msg);
        }
    }
    public static void i(String tag, String msg){
        Log.i("debug",String.valueOf(BuildConfig.DEBUG));
        if (LOG_LEVEL >= INFO || IS_DEBUG){
            Log.i(tag,msg);
        }
    }
    public static void d(String tag, String msg){
        if (LOG_LEVEL >= DEBUG || IS_DEBUG){
            Log.d(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (LOG_LEVEL >= VERBOS || IS_DEBUG){
            Log.v(tag, msg);
        }
    }
}
