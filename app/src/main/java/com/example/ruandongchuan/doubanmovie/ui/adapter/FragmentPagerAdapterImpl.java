package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class FragmentPagerAdapterImpl extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mTitles = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public FragmentPagerAdapterImpl(FragmentManager mFragmentManager) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
    }

    public void addFragment(String title,Fragment fragment){
        mFragments.add(fragment);
        mTitles.add(title);

    }
    //通过tag获取到Fragment
    public Fragment getActiveFragment(ViewPager container,int positon){
        String name = makeFragmentName(container.getId(),positon);
        return mFragmentManager.findFragmentByTag(name);
    }
    private String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }



    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
