package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by ruandongchuan on 15-11-10.
 */
public class FileCache {
    private File cacheDir;
    private File imageDir;
    public FileCache(Context context){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cacheDir = context.getExternalCacheDir();
        }else {
            cacheDir = context.getCacheDir();
        }
        imageDir = new File(cacheDir,"cache-images");

    }

    public File getImageDir() {
        return imageDir;
    }

    public File getFile(String url){
        String filename = String.valueOf(url.hashCode());

        return new File(cacheDir,filename);
    }
    public File getFile(String url,String type){
        String filename = String.valueOf(url.hashCode());

        return new File(cacheDir,filename+type);
    }
    public void clear(){
        File[] files = cacheDir.listFiles();
        if (files == null){
            return;
        }
        for (File file:files){
            file.delete();
        }
    }
    public long getSize(){
        File[] files = cacheDir.listFiles();
        long size = 0;
        if (files == null){
            return size;
        }
        for (File file:files){
            size += file.length();
        }
        return size;
    }
}
