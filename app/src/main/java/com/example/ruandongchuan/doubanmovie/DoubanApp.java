package com.example.ruandongchuan.doubanmovie;

import android.app.Application;

import com.example.ruandongchuan.doubanmovie.util.ImageLoader.FileCache;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ruandongchuan on 15-11-17.
 */
public class DoubanApp extends Application {
    private static DoubanApp mInstance;
    public ImageLoader mImageLoader;
    private int mScreenWidth, mScreenHeight;
    private FileCache mFileCache;

    public FileCache getmFileCache() {
        return mFileCache;
    }

    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public static DoubanApp getmInstance() {
        return mInstance;
    }

    public Gson getGson() {
        return new Gson();
    }
    public Gson getAnnotationGson(){
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mFileCache = new FileCache(this);
        mImageLoader = ImageLoader.getmInstance();
        mImageLoader.init(this);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    }
}
