package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.util.HttpManager;
import com.example.ruandongchuan.doubanmovie.util.LogTool;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AbstractActivity implements HttpManager.OnConnectListener {
    private WebView mWebview;
    private HttpManager mHttpManager;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public void onBackPressed() {
        //重写返回按键响应
        //返回webview的上一页
        if (mWebview.canGoBack()) {
            mWebview.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void init(){
        mHttpManager = new HttpManager(this);
        mWebview = (WebView) findViewById(R.id.webview_auth);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mWebview.loadUrl(DoubanApi.HTTP_AUTH);
        //在webview内部打开网页
        //根据url获取code
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("code=")) {
                    int index = url.indexOf("=");
                    String code = url.substring(index + 1, url.length());
                    LogTool.i("code", url);
                    mProgressDialog.show();
                    mHttpManager.postAccessToken(code);
                } else {
                    mWebview.loadUrl(url);
                }
                return true;
            }
        });
        //获取webview加载网页的进度
        mWebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100)
                    mProgressBar.setVisibility(View.GONE);
                else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }

            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.is_loading));
    }

    @Override
    public void OnSuccess(String result, int tag){
        LogTool.i("result",result);
        //json解析数据
        if (tag == DoubanApi.POST_ACCESS_TOKEN){
            try {
                //把登录token保存到本地
                JSONObject jsonObject = new JSONObject(result);
                String access_token = jsonObject.getString("access_token");
                String douban_user_id = jsonObject.getString("douban_user_id");
                String douban_user_name = jsonObject.getString("douban_user_name");
                String expires_in = jsonObject.getString("expires_in");
                String refresh_token = jsonObject.getString("refresh_token");
                DoubanApi.authComplete(access_token);
                mSharedPreferences = getSharedPreferences("Token",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("access_token",access_token);
                editor.putString("douban_user_id",douban_user_id);
                editor.putString("douban_user_name", douban_user_name);
                editor.putString("expires_in", expires_in);
                editor.putString("refresh_token", refresh_token);
                editor.apply();
                LogTool.i("access_token", access_token);
                mHttpManager.getUserInfo();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (tag == DoubanApi.GET_USERINFO){
            try {
                //把用户信息保存的本地
                JSONObject jsonObject = new JSONObject(result);
                String image = jsonObject.getString("large_avatar");
                String created = jsonObject.getString("created");
                String uid = jsonObject.getString("uid");
                String loc_name = jsonObject.getString("loc_name");
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("image",image);
                editor.putString("created",created);
                editor.putString("uid",uid);
                editor.putString("loc_name",loc_name);
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
