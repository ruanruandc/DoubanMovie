package com.example.ruandongchuan.doubanmovie.util;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.ruandongchuan.doubanmovie.DoubanApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Created by ruandongchuan on 15-11-9.
 */
public class Util {
    /**
     * 计算缩放大小
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int width = options.outWidth;
        final int height = options.outHeight;
        if (reqWidth == 0 ){
            reqWidth = DoubanApp.getmInstance().getmScreenWidth();
        }
        if (reqHeight == 0){
            reqHeight = DoubanApp.getmInstance().getmScreenHeight();
        }
        LogTool.i("Bounds","outwidth="+width+"-"+"outheight="+height);
        int inSampleSize = 1;
        if (width > reqWidth || height> reqHeight){
            final int halfWidth = width/2;
            final int halfHeight = height/2;
            while ((halfHeight/inSampleSize)>reqHeight &&
                    (halfWidth/inSampleSize)>reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    /**
     * 解析bitmap
     */

    public static Bitmap decodeBitmapByteArray(byte[] bytes,int reqWidth,int reqHeight){
        LogTool.i("Bounds","width="+reqWidth+"-"+"height="+reqHeight);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        LogTool.i("insampleSize=",options.inSampleSize+"");
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap decodeBitmapFile(File file,int reqWidth,int reqHeight) throws FileNotFoundException {
        LogTool.i("Bounds","width="+reqWidth+"-"+"height="+reqHeight);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String pathname = file.getPath() + file.getName();
        LogTool.i("pathname", pathname);
        BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        LogTool.i("insampleSize=",options.inSampleSize+"");
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeStream(new FileInputStream(file), null, options);
    }

    public static void readStream(InputStream is,OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer))!= -1){
            os.write(buffer,0,len);
        }
        is.close();
        os.close();
    }

    public static byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1){
            baos.write(buffer,0,len);
        }
        baos.close();
        is.close();
        return baos.toByteArray();
    }

    /**
     * 字符数组转为字符串 用/分割
     * @param array
     * @return
     */
    public static String arrayToString(String[] array){
        int len = array.length;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<len;i++){
            sb.append(array[i]);
            sb.append("/");
        }
        return sb.length()==0?"":sb.substring(0, sb.length() - 1);
    }

    public static boolean isZero(float score){
        final int temp = (int) score;
        return temp == 0;
    }

    /**
     * 将字节转换为相应的大小
     * @param size
     * @return
     */
    public static String getRealSize(long size){
        int count = 0;
        double realsize = size;
        String type = "BKMGTP";
        while (realsize>1024){
            realsize = realsize/1024.00;
            count++;
        }
        BigDecimal bigDecimal = new BigDecimal(realsize);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP)
                .doubleValue()+String.valueOf(type.charAt(count))+(count==0?"":"B");
    }

    /**
     * 渐变动画
     * @param outView
     * @param inView
     */
    public static void loadAnima(final View outView,View inView){
        if (outView == null || inView == null){
            LogTool.i("null","true");
            return;
        }
        if (!outView.isShown())
            return;
        int time = 200;
        inView.setAlpha(0f);
        inView.setVisibility(View.VISIBLE);
        inView.animate()
                .alpha(1f)
                .setDuration(time)
                .setListener(null);
        outView.animate()
                .alpha(0f)
                .setDuration(time)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        outView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}
