package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.graphics.Bitmap;

/**
 * Created by ruandongchuan on 15-11-25.
 */
public class DisplayImageTask implements Runnable {
    private Bitmap bitmap;
    private ImageLoaderInfo imageLoaderInfo;
    public DisplayImageTask(Bitmap bitmap,ImageLoaderInfo imageLoaderInfo){
        this.bitmap = bitmap;
        this.imageLoaderInfo = imageLoaderInfo;
    }
    @Override
    public void run() {
        if (imageLoaderInfo.url.equals(imageLoaderInfo.view.getTag())){
            imageLoaderInfo.view.setImageBitmap(bitmap);
            if (imageLoaderInfo.listener!=null) {
                imageLoaderInfo.listener.onLoadCommplete(
                        imageLoaderInfo.url, imageLoaderInfo.view, bitmap);
            }
        }
    }
}
