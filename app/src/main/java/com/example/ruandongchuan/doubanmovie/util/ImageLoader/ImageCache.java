package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.content.Context;

/**
 * Created by ruandongchuan on 15-11-25.
 */
public class ImageCache {
    public MemoryCache memoryCache;
    public FileCache fileCache;
    public ImageCache(Context context){
        memoryCache = new MemoryCache();
        fileCache = new FileCache(context);
    }

    public FileCache getFileCache() {
        return fileCache;
    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public String getMemoryCacheSize(){
        return memoryCache.getMemorySize();
    }
    public int getMemoryCacheCount(){
        return memoryCache.getCount();
    }

}
