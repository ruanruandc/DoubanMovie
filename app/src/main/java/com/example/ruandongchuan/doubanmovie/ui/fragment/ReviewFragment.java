package com.example.ruandongchuan.doubanmovie.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.AbstractAdapter;
import com.example.ruandongchuan.doubanmovie.ui.adapter.ReviewListAdapter;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.bean.ReviewBean;
import com.example.ruandongchuan.doubanmovie.util.HtmlParser;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.List;

/**
 * Created by ruandongchuan on 15-12-10.
 */
public class ReviewFragment extends AbstractFragment {
    private String id;
    private ReviewListAdapter mAdapter;
    private LoadViewHolder load_view;
    public static ReviewFragment getInstance(String id){
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base,container,false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_base);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_base);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_base);
        return view;
    }

    @Override
    void initData() {
        mHttpManager.getReview(id, "0", "20");
        mSwipeRefreshLayout.setEnabled(false);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ReviewListAdapter(mData);
        View load = LayoutInflater.from(getContext()).inflate(R.layout.title_item, mRecycleView,false);
        load_view = new LoadViewHolder(load);
        initLoadView();
        mAdapter.addCustomView(load, 0, AbstractAdapter.TYPE_LOAD);
        mRecycleView.setAdapter(mAdapter);
    }

    private void initLoadView(){
        load_view.tv_nomal.setText(getString(R.string.click_to_load_more));
        load_view.tv_nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.loadAnima(load_view.tv_nomal,load_view.loading_layout);
                mHttpManager.getReview(id,String.valueOf(mData.size()-1),"20");
            }
        });
    }

    @Override
    public void OnSuccess(String result, int tag) {
        LogTool.i("result", result);
        if (tag == DoubanApi.GET_REVIEW){
            Util.loadAnima(load_view.loading_layout,load_view.tv_nomal);
            Util.loadAnima(mProgressBar, mRecycleView);
            List<ReviewBean> data = HtmlParser.getReviewList(result);
            this.mData.addAll(this.mData.size()-1,data);
            mAdapter.notifyDataSetChanged();
            StringBuilder sb = new StringBuilder();
            for (ReviewBean bean : data){
                sb.append(bean.getRating());
                sb.append(bean.getLink());
                sb.append("\n\r");
            }
            LogTool.i("sb",sb.toString());
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
