package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by ruandongchuan on 15-11-25.
 */
public interface ImageLoadingListener {
    void onLoadCommplete(String url,ImageView view,Bitmap loadedImage);
}
