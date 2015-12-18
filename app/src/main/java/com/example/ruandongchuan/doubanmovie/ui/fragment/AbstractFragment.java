package com.example.ruandongchuan.doubanmovie.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ruandongchuan.doubanmovie.DoubanApp;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.AbstractViewHolder;
import com.example.ruandongchuan.doubanmovie.util.HttpManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public abstract class AbstractFragment extends Fragment implements HttpManager.OnConnectListener {
    public HttpManager mHttpManager;
    protected RecyclerView mRecycleView;
    protected ProgressBar mProgressBar;
    protected List<BaseBean> mData;
    protected FloatingActionButton mFloatingActionButton;
    public Gson mGson;
    protected boolean isCreated = false;
    protected boolean isRefresh = false;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    public AbstractFragment(){
        mHttpManager = new HttpManager(this);
        mGson = DoubanApp.getmInstance().getGson();
        mData = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated){
            return;
        }
        if (isVisibleToUser){
            initData();
            isCreated = false;
        }
    }
    public void showFloatingActionButton(){
        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setOnClickListener(new AbstractViewHolder(mRecycleView) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                mRecycleView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecycleView.smoothScrollToPosition(0);
                    }
                });
            }
        });
    }
    abstract void initData();
}
