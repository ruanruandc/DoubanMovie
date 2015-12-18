package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.adapter.FragmentPagerAdapterImpl;

public abstract class AbstractViewPagerActivity extends AbstractSwipeActivity {
    protected ViewPager mViewPager;
    protected TabLayout mTabLayout;
    protected FragmentPagerAdapterImpl mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_view_pager);
        mViewPager = (ViewPager) findViewById(R.id.vp_base);
        mTabLayout = (TabLayout) findViewById(R.id.tab_base);
        mAdapter = new FragmentPagerAdapterImpl(getSupportFragmentManager());
        init();
    }
    public abstract void init();
}
