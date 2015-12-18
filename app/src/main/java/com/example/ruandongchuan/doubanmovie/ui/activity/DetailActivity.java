package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.adapter.FragmentPagerAdapterImpl;
import com.example.ruandongchuan.doubanmovie.ui.fragment.CommentFragment;
import com.example.ruandongchuan.doubanmovie.ui.fragment.DetailFragment;
import com.example.ruandongchuan.doubanmovie.ui.fragment.ReviewFragment;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnCallBack;

import java.io.File;

public class DetailActivity extends AbstractSwipeActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String id,image;
    private Menu mMenu;
    private boolean isShare = false;
    private String share_text;
    private File share_image;
    private ImageView iv_title;
    private ImageLoader mImageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_detail);
        mTabLayout = (TabLayout) findViewById(R.id.tab_detail);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        mImageLoader = ImageLoader.getmInstance();
        mImageLoader.loadImage(image, iv_title);
        iv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ImageActivity.class);
                intent.putExtra("url", image);
                startActivity(intent);
            }
        });
        mViewPager.setOffscreenPageLimit(3);
        FragmentPagerAdapterImpl adapter = new FragmentPagerAdapterImpl(getSupportFragmentManager());
        DetailFragment fragment = DetailFragment.newInstance(id);
        CommentFragment commentFragment = CommentFragment.getInstance(id);
        fragment.setOnCallBack(new OnCallBack<Bundle>() {
            @Override
            public void callBack(Bundle bundle) {
                LogTool.i("here", "callback");
                share_text = bundle.getString("text");
                share_image = (File) bundle.getSerializable("image");
                isShare = true;
                onPrepareOptionsMenu(mMenu);
            }
        });
        adapter.addFragment(getString(R.string.introduction), fragment);
        adapter.addFragment(getString(R.string.comment), commentFragment);
        ReviewFragment reviewFragment = ReviewFragment.getInstance(id);
        adapter.addFragment(getString(R.string.review), reviewFragment);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (isShare){
            //分享
            getMenuInflater().inflate(R.menu.menu_detail,menu);
            MenuItem shareItem = menu.findItem(R.id.menu_share);
            ShareActionProvider shareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, share_text);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(share_image));
            shareActionProvider.setShareIntent(intent);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
