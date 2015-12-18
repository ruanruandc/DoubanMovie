package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.example.ruandongchuan.doubanmovie.constants.Setting;
import com.example.ruandongchuan.doubanmovie.util.LogTool;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ruandongchuan on 15-11-9.
 */
public class ImageLoader {
    //private ImageView imageView;
    //内存缓存
    //private MemoryCache memoryCache = new MemoryCache();
    //private FileCache fileCache;
    private ImageCache mImageCache;
    private volatile static ImageLoader mInstance;
    //网络请求线程池
    private Executor mExecutor;
    //解析bitmap线程池
    private Executor mDecodeExecutor;
    //分发任务线程池
    private Executor mDispatchExecutor;
    private Handler mHandler;

    private final AtomicBoolean mPaused = new AtomicBoolean(false);
    private final Object mPauseLock = new Object();
    //滑动时是否加载图片
    private boolean isSwipeLoadImage = true;
    //弱引用 重入锁
    private Map<String,ReentrantLock> mUrlLocks = new WeakHashMap<>();
    private ImageLoader(){

    }

    public boolean isSwipeLoadImage() {
        return isSwipeLoadImage;
    }

    public void setIsSwipeLoadImage(boolean isSwipeLoadImage) {
        this.isSwipeLoadImage = isSwipeLoadImage;
    }

    public synchronized void init(Context context){
        //fileCache = new FileCache(context);
        mImageCache = new ImageCache(context);
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
        //初始化三个线程池
        mExecutor = new ThreadPoolExecutor(3,3,0L, TimeUnit.MILLISECONDS,taskQueue,new DefaultThreadFactory(3,"thread-pool-"));
        mDecodeExecutor = new ThreadPoolExecutor(3,3,0L, TimeUnit.MILLISECONDS,taskQueue,new DefaultThreadFactory(3,"thread-pool-"));
        mDispatchExecutor = Executors.newCachedThreadPool(new DefaultThreadFactory(Thread.NORM_PRIORITY,"thread-pool-dispatch-"));
        mHandler = new Handler();
    }
    //单例模式获取实例
    public static ImageLoader getmInstance(){
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void putMemoryCache(String url,Bitmap bitmap){
        mImageCache.memoryCache.put(url, bitmap);
    }
    public void clearMemeoryCache(){
        mImageCache.memoryCache.clear();
    }
    public void clearFileCache(){
        mImageCache.fileCache.clear();
    }
    public long getFileCacheSize(){
        return mImageCache.getFileCache().getSize();
    }
    public String getMemoryCacheSize(){
        return mImageCache.getMemoryCacheSize();
    }
    //暂停线程池
    public void pause(){
        mPaused.set(true);
    }
    //恢复线程池
    public void resume(){
        mPaused.set(false);
        synchronized (mPauseLock){
            mPauseLock.notifyAll();
        }
    }

    public void loadImage(String url,ImageView imageView){
        loadImage(url, imageView, null);
        //Picasso.with(context).load(url).into(imageView);
    }
    public void loadImage(final String url,ImageView imageView,ImageLoadingListener listener){
        LogTool.i("url", url);
        //是否允许使用移动网络
        if (Setting.isMobileConn() && !Setting.isAllowMobile())
            return;
        imageView.setTag(url);
        //重入锁，防止任务重复执行
        ReentrantLock lock = getUrlLock(url);
        final ImageLoaderInfo info = new ImageLoaderInfo(url,imageView, mImageCache,
                lock, mPaused, mPauseLock,listener);
        //二级缓存 先从内存缓存中寻找图片
        Bitmap bitmap = mImageCache.memoryCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else {
            imageView.setImageBitmap(null);
            //内存缓存中没有则调用任务分发线程池
            mDispatchExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    File file = mImageCache.fileCache.getFile(url);
                    if (!file.exists()) {
                        //如果没有文件缓存 则调用网络线程获取图片
                        mExecutor.execute(new LoadNetworkImageTask(info, mHandler));
                    } else {
                        //从文件缓存中获取图片
                        mDecodeExecutor.execute(new LoadNetworkImageTask(info, mHandler));
                    }
                }
            });
        }
    }

    private boolean checkTag(String url,ImageView imageView){
        return url.equals(imageView.getTag());
    }


    public ReentrantLock getUrlLock(String url){
        //获取重入锁
        ReentrantLock lock = mUrlLocks.get(url);
        if (lock == null){
            lock = new ReentrantLock();
            mUrlLocks.put(url, lock);
        }
        return lock;
    }

    //线程工厂 作为线程池的参数
    public static class DefaultThreadFactory implements ThreadFactory{
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final int threadPriority;

        DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
            this.threadPriority = threadPriority;
            group = Thread.currentThread().getThreadGroup();
            namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            t.setPriority(threadPriority);
            return t;
        }
    }
}
