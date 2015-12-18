package com.example.ruandongchuan.doubanmovie.constants;

import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;

/**
 * Created by ruandongchuan on 15-12-2.
 */
public class Setting {
    //是否允许使用移动网络
    private static boolean IS_ALLOW_MOBILE = false;
    //移动网络是否连接
    private static boolean IS_MOBILE_CONN = false;
    //图片质量
    private static int IMAGE_QUALITY = 0;
    //头像质量
    private static int LOGO_QUALITY = 0;
    //自动
    public static final int QUALITY_AUTO = 0;
    //高
    public static final int QUALITY_HIGH = 1;
    //中
    public static final int QUALITY_MID = 2;
    //低
    public static final int QUALITY_LOW = 3;

    public static void setImageQuality(int imageQuality) {
        Setting.IMAGE_QUALITY = imageQuality;
    }

    public static void setLogoQuality(int logoQuality) {
        Setting.LOGO_QUALITY = logoQuality;
    }

    public static int getImageQuality() {
        return IMAGE_QUALITY;
    }

    public static int getLogoQuality() {
        return LOGO_QUALITY;
    }

    public static void setIsAllowMobile(boolean bl){
        IS_ALLOW_MOBILE = bl;
    }
    public static boolean isAllowMobile(){
        return IS_ALLOW_MOBILE;
    }

    public static boolean isMobileConn() {
        return IS_MOBILE_CONN;
    }

    public static void setIsMobileConn(boolean isMobileConn) {
        Setting.IS_MOBILE_CONN = isMobileConn;
    }

    /**
     * 根据当前设置自动获取图片质量
     * @param bean
     * @return
     */
    public static String getImageUrl(InTheaterBean.SubjectBean.ImagesBean bean){
        String url = "";
        switch (IMAGE_QUALITY){
            case QUALITY_AUTO:
                if (isMobileConn()){
                    url = bean.getSmall();
                }else {
                    url = bean.getLarge();
                }
                break;
            case QUALITY_HIGH:
                url = bean.getLarge();
                break;
            case QUALITY_MID:
                url = bean.getMedium();
                break;
            case QUALITY_LOW:
                url = bean.getSmall();
                break;
        }
        return url;
    }
    /**
     * 根据当前设置自动获取头像质量
     * @param bean
     * @return
     */
    public static String getLogoUrl(InTheaterBean.SubjectBean.ImagesBean bean){
        String url = "";
        switch (LOGO_QUALITY){
            case QUALITY_AUTO:
                if (isMobileConn()){
                    url = bean.getSmall();
                }else {
                    url = bean.getLarge();
                }
                break;
            case QUALITY_HIGH:
                url = bean.getLarge();
                break;
            case QUALITY_MID:
                url = bean.getMedium();
                break;
            case QUALITY_LOW:
                url = bean.getSmall();
                break;
        }
        return url;
    }
}
