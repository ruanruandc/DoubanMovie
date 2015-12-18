package com.example.ruandongchuan.doubanmovie.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.UsBoxAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.UsBoxBean;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.OnScrollPauseListener;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-11-17.
 */
public class UsBoxFragment extends AbstractFragment {
    private List<BaseBean> data;
    private GridLayoutManager mGridLayoutManager;
    private UsBoxAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base,container,false);
        initView(view);
        return view;
    }

    @Override
    void initData() {
        mHttpManager.getUsBox();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHttpManager.getUsBox();
            }
        });
    }
    private void initView(View view){
        data = new ArrayList<>();
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_base);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_base);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_base);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getContext(),3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        mRecycleView.setLayoutManager(mGridLayoutManager);
        mAdapter = new UsBoxAdapter(getContext(),data);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setOnScrollListener(new OnScrollPauseListener());
    }

    @Override
    public void OnSuccess(String result, int tag) {
        if (result.isEmpty())
            return;
        mSwipeRefreshLayout.setRefreshing(false);
        Util.loadAnima(mProgressBar, mRecycleView);
        LogTool.i("result" + String.valueOf(DoubanApi.isAuthed), result);
        if (tag == DoubanApi.GET_US_BOX) {
            UsBoxBean bean = mGson.fromJson(result, UsBoxBean.class);
            LogTool.i("bean",bean.getTitle());
            data.clear();
            data.addAll(bean.getSubjects());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
