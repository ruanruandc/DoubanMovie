package com.example.ruandongchuan.doubanmovie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.ruandongchuan.doubanmovie.DoubanApp;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.constants.Setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruandongchuan on 15-11-10.
 */
public class HttpManager {
    private OnConnectListener onConnectListener;
    private String access_token;
    private final String apikey = "apikey="+DoubanApi.APIKEY;
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36";
    public HttpManager(OnConnectListener onConnectListener){
        this.onConnectListener = onConnectListener;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.getData().getString("result");
            int tag = msg.what;
            if (result == null || result.isEmpty()){
                onConnectListener.OnError(tag);
            }else {
                onConnectListener.OnSuccess(result,
                        msg.what);
            }
        }
    };

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * 获取影评
     * @param subject 电影id
     * @param start 开始索引
     * @param limit 数量
     */
    public void getReview(String subject,String start,String limit){
        String url = DoubanApi.HTTP_COMMENT + subject + "/reviews?";
        Map<String,String> map = new HashMap<>();
        map.put("start",start);
        map.put("limit",limit);
        String args = getParams(map);
        doGet(url + args,false,true,DoubanApi.GET_REVIEW);
    }

    /**
     * 获取短评
     * @param subject 电影id
     * @param start 开始索引
     * @param limit 数量
     * @param sortType 排序方式
     */
    public void getComment(String subject,String start,String limit,String sortType){
        String url = DoubanApi.HTTP_COMMENT + subject + "/comments?";
        Map<String,String> map = new HashMap<>();
        map.put("start",start);
        map.put("limit",limit);
        map.put("sortType", sortType);
        String args = getParams(map);
        doGet(url + args, false, true, DoubanApi.GET_COMMENT);
    }
    /**
     * 用户完整版信息
     */
    public void getUserInfo(){
        doGet(DoubanApi.HTTP_USERINFO,true,false,DoubanApi.GET_USERINFO);
    }

    /**
     * 获取定位城市
     * @param la 纬度
     * @param lo 经度
     */
    public void getGeocoding(String la,String lo){
        Map<String,String> params = new HashMap<>();
        params.put("ak",DoubanApi.BAIDU_KEY);
        params.put("output","json");
        params.put("location",la+","+lo);
        doGet(DoubanApi.BAIDU_API +"?"+getParams(params),DoubanApi.GET_GEOCODING);
    }
    /**
     * 正在热映
     * @param city 城市
     */
    public void getInTheaters(String city){
        String url = DoubanApi.HTTP_IN_THEATERS;
        String args = "?city=" + city+"&"+apikey;
        doGet(url+args,DoubanApi.GET_IN_THEATERS);
    }

    /**
     * 即将上映
     * @param start 开始
     * @param count 数量
     */
    public void getComingSoon(int start,int count){
        String url = DoubanApi.HTTP_COMMING_SOON;
        String args = "?start="+ start + "&count=" + count;
        doGet(url + args + "&"+apikey,DoubanApi.GET_COMMING_SOON);
    }

    /**
     * 电影条目搜索
     * @param q
     * @param tag
     * @param start
     * @param count
     */
    public void getSearch(String q,String tag,int start,int count){
        Map<String,String> params = new HashMap<>();
        if (q!=null  && !q.isEmpty()) {
            params.put("q", q);
        }
        if (tag!=null && !tag.isEmpty()) {
            params.put("tag", tag);
        }
        if (start >= 0) {
            params.put("start", "" + start);
        }
        if (count > 0){
            params.put("count",count+"");
        }
        doGet(DoubanApi.HTTP_SEARCH +"?"+ getParams(params)+"&"+apikey,DoubanApi.GET_SEARCH);
    }

    /**
     * 电影条目信息
     * @param id id
     */
    public void getSubject(String id){
        doGet(DoubanApi.HTTP_SUBJECT + id+"?"+apikey, DoubanApi.GET_SUBJECT);
    }

    /**
     * 影人条目信息
     * @param id id
     */
    public void getCelebrity(String id){
        doGet(DoubanApi.HTTP_CELEBRITY + id+"?"+apikey,DoubanApi.GET_CELEBRITY);
    }

    /**
     * Top250
     * @param start
     * @param count
     */
    public void getTop250(int start,int count){
        String args = "?start="+ start + "&count=" + count;
        doGet(DoubanApi.HTTP_TOP250 + args+"&"+apikey,DoubanApi.GET_TOP250);
    }

    /**
     * 北美票房榜
     */
    public void getUsBox(){
        doGet(DoubanApi.HTTP_USBOX+"?"+apikey,DoubanApi.GET_US_BOX);
    }

    /**
     * 获取登录token
     * @param code
     */
    public void postAccessToken(String code){
        Map<String,String> params = new HashMap<>();
        params.put("client_id",DoubanApi.APIKEY);
        params.put("client_secret",DoubanApi.SERCET);
        params.put("redirect_uri","http://baidu.com");
        params.put("grant_type","authorization_code");
        params.put("code",code);
        doPost(DoubanApi.HTTP_ACCESS_TOKEN, params, DoubanApi.POST_ACCESS_TOKEN);
    }

    /**
     * 获取页面html
     * @param url 地址
     * @param tag 标志
     */
    public void getString(String url,int tag){
        doGet(url,false,false,tag);
    }
    private void doGet(String murl,int tag){
        doGet(murl, false,false, tag);
    }

    private void doGet(String mUrl, final boolean isNeedToken, final boolean isNeedUa,final int mTag){
        final String url = mUrl;
        final int tag = mTag;
        if (vaifyNetwork(tag))
            return;
        LogTool.i("murl", mUrl);
        new Thread(){
            @Override
            public void run() {
                super.run();
                String result = get(url,isNeedToken,isNeedUa);
                Message msg = new Message();
                msg.what = mTag;
                Bundle bundle = new Bundle();
                if (DoubanApi.isAuthed) {
                    bundle.putString("result",result);
                }else {
                    bundle.putString("result",result);
                }
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void doPost(String mUrl, final Map<String,String> params,final int mTag){
        final String url = mUrl;
        final int tag = mTag;
        if (vaifyNetwork(tag))
            return;
        new Thread(){
            @Override
            public void run() {
                super.run();
                String result = post(url, params);
                Message msg = new Message();
                msg.what = mTag;
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }
    private String get(String murl){
        return get(murl,false,false);
    }

    /**
     * get请求
     * @param mUrl
     * @param isNeedToken
     * @param isNeedUa
     * @return
     */
    private String get(String mUrl,boolean isNeedToken,boolean isNeedUa){
        String result = "";
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (isNeedToken && DoubanApi.isAuthed){
                conn.setRequestProperty("Authorization",
                        "Bearer "+ DoubanApi.ACCESS_TOKEN);
            }
            if (isNeedUa) {
                conn.setRequestProperty("User-Agent",
                        USER_AGENT);
            }
            conn.connect();
            InputStream is = conn.getInputStream();
            result = readIs(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String post(String mUrl,Map<String,String> params){
        String result = "";
        String post = "";
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (params!=null){
                post = getParams(params);
                LogTool.i("params", post);
            }
            conn.getOutputStream().write(post.getBytes("UTF-8"));
            InputStream is = conn.getInputStream();
            result = readIs(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    private String readIs(InputStream is) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(is,"UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer();
        String tempLine = null;
        while ((tempLine = reader.readLine()) != null){
            sb.append(tempLine);
            sb.append("\r\n");
        }
        is.close();
        inputStreamReader.close();
        reader.close();
        return sb.toString();
    }
    //组装http get参数
    public String getParams(Map<String,String> params){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> entry : params.entrySet()){
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    //检测网络是否可用
    public boolean vaifyNetwork(int tag){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) DoubanApp.getmInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()){
            onConnectListener.OnError(tag);
            Toast.makeText(DoubanApp.getmInstance(),"network vailed",Toast.LENGTH_SHORT).show();
            return true;
        }
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifi = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobile = networkInfo.isConnected();
        Setting.setIsMobileConn(isMobile);
        //Setting.setIsMobileConn(isMobile);
        if (isMobile && !Setting.isAllowMobile()){
            onConnectListener.OnError(tag);
            Toast.makeText(DoubanApp.getmInstance(),"network vailed",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    public interface OnConnectListener{
        void OnSuccess(String result,int tag);
        void OnError(int tag);
    }
}
