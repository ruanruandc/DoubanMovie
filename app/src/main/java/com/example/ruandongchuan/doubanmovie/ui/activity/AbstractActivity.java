package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ruandongchuan.doubanmovie.R;

/**
 * Created by ruandongchuan on 15-12-11.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否使用夜间模式
        SharedPreferences preferences = getSharedPreferences("setting",MODE_PRIVATE);
        boolean isDark = preferences.getBoolean("isDark",false);
        if (isDark){
            setTheme(R.style.AppTheme_Dark);
        }
    }
}
