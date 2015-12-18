package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ruandongchuan on 15-11-25.
 */
public class ImageLoaderInfo {
    public final String url;
    public final ImageLoadingListener listener;
    public final ImageView view;
    public final ImageCache imageCache;
    public final ReentrantLock lock;
    public final AtomicBoolean paused;
    public final Object pauseLock;
    public ImageLoaderInfo(String url,ImageView view,ImageCache imageCache,
                           ReentrantLock lock,
                           AtomicBoolean paused,
                           Object pauseLock,
                           ImageLoadingListener listener){
        this.url = url;
        this.listener = listener;
        this.view = view;
        this.imageCache = imageCache;
        this.lock = lock;
        this.pauseLock = pauseLock;
        this.paused = paused;
    }
}
