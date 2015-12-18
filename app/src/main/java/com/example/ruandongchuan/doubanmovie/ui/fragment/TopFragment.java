package com.example.ruandongchuan.doubanmovie.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.OnShowAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.OnScrollPauseListener;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends AbstractFragment {
    private OnShowAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private View loadView;
    private TextView tv_complete;
    private TextView tv_nomal;
    private LinearLayout loading_layout;
    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        initView(view);
        return view;
    }

    @Override
    void initData() {
        addLoad(mData.size());
        initLoadView();
        mHttpManager.getTop250(0, 20);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHttpManager.getTop250(0, 20);
                isRefresh = true;
            }
        });
    }
    private void initView(View view){
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_base);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_base);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_base);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mData.get(position).getView_type() == OnShowAdapter.TYPE_LOAD ?
                        mGridLayoutManager.getSpanCount() : 1;
            }
        });
        mRecycleView.setLayoutManager(mGridLayoutManager);
        mData = new ArrayList<>();
        mAdapter = new OnShowAdapter(getContext(), mData);
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.title_item,null);
        tv_complete = (TextView) loadView.findViewById(R.id.tv_complete);
        tv_nomal = (TextView) loadView.findViewById(R.id.tv_nomal);
        loading_layout = (LinearLayout) loadView.findViewById(R.id.loading_layout);
        tv_nomal.setText(getString(R.string.click_to_load_more));
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setOnScrollListener(new OnScrollPauseListener());
    }
    private void initLoadView(){
        tv_complete.setVisibility(View.GONE);
        tv_nomal.setVisibility(View.VISIBLE);
        tv_nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_nomal.setVisibility(View.GONE);
                loading_layout.setVisibility(View.VISIBLE);
                mHttpManager.getTop250(mData.size() - 1, 20);
            }
        });
    }

    private void addLoad(int position){
        mAdapter.addCustomView(loadView, position, OnShowAdapter.TYPE_LOAD);
    }

    @Override
    public void OnSuccess(String result, int tag) {
        if (result.isEmpty())
            return;
        mSwipeRefreshLayout.setRefreshing(false);
        LogTool.i("result" + String.valueOf(DoubanApi.isAuthed), result);
        if (tag == DoubanApi.GET_TOP250){
            tv_nomal.setVisibility(View.VISIBLE);
            loading_layout.setVisibility(View.GONE);
            if (isRefresh){
                mData.clear();
                addLoad(mData.size());
                isRefresh = false;
            }
            InTheaterBean bean = mGson.fromJson(result, InTheaterBean.class);
            LogTool.i("title", bean.getTitle());
            mData.addAll(mData.size() - 1, bean.getSubjects());
            mAdapter.notifyDataSetChanged();
            Util.loadAnima(mProgressBar, mRecycleView);
        }
    }

    @Override
    public void OnError(int tag) {
        if (tag == DoubanApi.GET_TOP250){
            tv_nomal.setVisibility(View.VISIBLE);
            loading_layout.setVisibility(View.GONE);
        }
    }
}
