package com.example.ruandongchuan.doubanmovie.util;

import android.os.Handler;
import android.os.Message;

import com.example.ruandongchuan.doubanmovie.DoubanApp;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.FileCache;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ruandongchuan on 15-11-17.
 */
public class DownLoadThread extends Thread {
    private OnCallBack<File> onCallBack;
    private FileCache fileCache;
    private String url;
    private File file;
    public DownLoadThread(String url,OnCallBack<File> onCallBack) {
        super();
        this.onCallBack = onCallBack;
        fileCache = DoubanApp.getmInstance().getmFileCache();
        this.url = url;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                onCallBack.callBack(file);
            }
        }
    };

    @Override
    public void run() {
        super.run();
        try {
            URL mUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            file = DoubanApp.getmInstance().getmFileCache().getFile(url,".jpg");
            Util.readStream(inputStream,new FileOutputStream(file));
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
