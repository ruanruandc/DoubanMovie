package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.adapter.FragmentPagerAdapterImpl;
import com.example.ruandongchuan.doubanmovie.ui.fragment.CelebrityFragment;

public class CelebrityActivity extends AbstractSwipeActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getIntent().getStringExtra("name");
        toolbar.setTitle(title == null?"":title);
        id = getIntent().getStringExtra("id");
        setSupportActionBar(toolbar);
        init();
    }
    public void init(){
        mViewPager = (ViewPager) findViewById(R.id.vp_man);
        mTabLayout = (TabLayout) findViewById(R.id.tab_man);
        mViewPager.setOffscreenPageLimit(4);
        FragmentPagerAdapterImpl adapter = new FragmentPagerAdapterImpl(getSupportFragmentManager());
        adapter.addFragment(getString(R.string.summary),CelebrityFragment.getInstance(id));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
