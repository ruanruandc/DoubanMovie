package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 内存缓存
 * Created by ruandongchuan on 15-11-9.
 */
public class MemoryCache {
    private Map<String,SoftReference<Bitmap>> cache =
            Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
    private LruCache<String,Bitmap> mMemoryCache;
    public MemoryCache(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //使用最大内存的16分之一
        final int cacheSize = maxMemory/16;
        LogTool.i("cachesize",cacheSize+"");
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };

    }
    public Bitmap get(String id){
        return mMemoryCache.get(id);
    }
    public void put(String id,Bitmap bitmap){
        mMemoryCache.put(id,bitmap);
    }

    public int getCount(){
        return mMemoryCache.size();
    }

    public String getMemorySize(){
        Map<String,Bitmap> map = mMemoryCache.snapshot();
        long size = 0;
        for (Map.Entry<String,Bitmap> entry :map.entrySet()){
            size += entry.getValue().getByteCount();
        }
        Log.i("count=",mMemoryCache.createCount()+"-"+mMemoryCache.hitCount()+"-"+mMemoryCache.putCount()
        +"---"+map.size());
        return Util.getRealSize(size);
    }

    public void clear(){
        cache.clear();
    }
}
