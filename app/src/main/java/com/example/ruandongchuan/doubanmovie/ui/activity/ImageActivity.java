package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.view.ZoomImageView;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;

public class ImageActivity extends AbstractActivity {
    private ZoomImageView iv_image;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv_image = (ZoomImageView) findViewById(R.id.iv_image);
        url = getIntent().getStringExtra("url");
        ImageLoader.getmInstance().loadImage(url,
                iv_image, null);
        iv_image.setOnDoubleClickListener(new ZoomImageView.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                finish();
            }
        });

    }



}
