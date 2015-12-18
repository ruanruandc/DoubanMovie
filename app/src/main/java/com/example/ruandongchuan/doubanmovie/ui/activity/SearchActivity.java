package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.fragment.SearchQueryFragment;

/**
 * Created by ruandongchuan on 15-11-14.
 */
public class SearchActivity extends AbstractViewPagerActivity {
    @Override
    public void init() {
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        String query;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }else {
            query = getIntent().getStringExtra("query");
        }
        toolbar.setTitle(getString(R.string.search)+"\"" + query + "\"");
        setSupportActionBar(toolbar);
        mAdapter.addFragment(getString(R.string.name), SearchQueryFragment.getInstance("q", query));
        mAdapter.addFragment(getString(R.string.tag), SearchQueryFragment.getInstance("tag",query));
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
