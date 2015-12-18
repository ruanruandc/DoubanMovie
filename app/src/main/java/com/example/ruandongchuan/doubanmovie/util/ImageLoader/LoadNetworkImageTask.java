package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;

import com.example.ruandongchuan.doubanmovie.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ruandongchuan on 15-11-25.
 */
public class LoadNetworkImageTask implements Runnable {
    private String url;
    private ImageView  imageView;
    private ImageLoadingListener listener;
    private Bitmap bitmap;
    private Handler handler;
    private ImageLoaderInfo imageLoaderInfo;
    private MemoryCache memoryCache;
    private FileCache fileCache;
    public LoadNetworkImageTask(ImageLoaderInfo imageLoaderInfo,Handler handler){
        this.imageView = imageLoaderInfo.view;
        this.url = imageLoaderInfo.url;
        this.listener = imageLoaderInfo.listener;
        this.handler = handler;
        this.imageLoaderInfo = imageLoaderInfo;
        this.fileCache = imageLoaderInfo.imageCache.fileCache;
        this.memoryCache = imageLoaderInfo.imageCache.memoryCache;
    }
    @Override
    public void run() {
        waitIfPaused();
        ReentrantLock lock = imageLoaderInfo.lock;
        lock.lock();
        try {
            if (getBitmapFromFile()){
                ImageLoader.getmInstance().putMemoryCache(url, bitmap);
                handler.post(new DisplayImageTask(bitmap, imageLoaderInfo));
            }else if (getBitmapFromNetwork()){
                ImageLoader.getmInstance().putMemoryCache(url, bitmap);
                handler.post(new DisplayImageTask(bitmap, imageLoaderInfo));
            }
        }finally {
            lock.unlock();
        }

    }
    //dcl 休眠线程
    private void waitIfPaused(){
        AtomicBoolean pause = imageLoaderInfo.paused;
        if (pause.get()){
            synchronized (imageLoaderInfo.pauseLock){
                if (pause.get()){
                    try{
                        imageLoaderInfo.pauseLock.wait();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //从网络加载图片
    private boolean getBitmapFromNetwork(){
        try {
            URL mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setReadTimeout(20*1000);
            conn.setConnectTimeout(5 * 1000);
            InputStream inputStream = conn.getInputStream();
            File file = fileCache.getFile(url);
            Util.readStream(inputStream,new FileOutputStream(file));
            bitmap = Util.decodeBitmapFile(file,imageView.getWidth(),imageView.getHeight());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap != null;
    }
    private boolean getBitmapFromContent(){
        ContentResolver res = imageView.getContext().getContentResolver();
        Uri uri = Uri.parse(url);
        try {
            InputStream inputStream = res.openInputStream(uri);
            byte[] bytes = Util.readStream(inputStream);
            bitmap = Util.decodeBitmapByteArray(bytes,imageView.getWidth(),imageView.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap != null;
    }
    //从文件加载图片
    private boolean getBitmapFromFile(){
        File file = imageLoaderInfo.imageCache.fileCache.getFile(imageLoaderInfo.url);
        if (file ==null || !file.exists()){
            return false;
        }
        try {
            bitmap = Util.decodeBitmapFile(file, imageLoaderInfo.view.getWidth(), imageLoaderInfo.view.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap != null;
    }
}
