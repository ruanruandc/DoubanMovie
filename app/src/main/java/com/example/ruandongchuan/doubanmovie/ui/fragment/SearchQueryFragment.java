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
import com.example.ruandongchuan.doubanmovie.ui.adapter.OnShowAdapter;
import com.example.ruandongchuan.doubanmovie.ui.adapter.SearchAdapter;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-11-14.
 */
public class SearchQueryFragment extends AbstractFragment {
    private String query;
    private List<BaseBean> data;
    private SearchAdapter mAdapter;
    private String type;
    private LoadViewHolder footer;
    public static SearchQueryFragment getInstance(String type,String query){
        SearchQueryFragment fragment = new SearchQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString("query");
        type = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_base, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_base);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_base);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_base);
        mSwipeRefreshLayout.setEnabled(false);
        data = new ArrayList<>();
        BaseBean foot = new BaseBean();
        foot.setView_type(SearchAdapter.TYPE_FOOTER);
        data.add(foot);
        mAdapter = new SearchAdapter(getContext(),data);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setmListener(new OnShowAdapter.OnAdapterBackListener() {
            @Override
            public void OnClick(int position, int viewtype) {
                footer = mAdapter.getmLoadViewHolder();
                Util.loadAnima(footer.loading_layout, footer.tv_nomal);
                if (type.equals("q")) {
                    mHttpManager.getSearch(query, null, data.size() - 1, -1);
                }
                if (type.equals("tag")) {
                    mHttpManager.getSearch(null, query, data.size() - 1, -1);
                }
                Util.loadAnima(footer.tv_nomal, footer.loading_layout);
            }
        });
    }

    @Override
    void initData() {
        if (type.equals("q")) {
            mHttpManager.getSearch(query, null, -1, -1);
        }
        if (type.equals("tag")){
            mHttpManager.getSearch(null,query,-1,-1);
        }
    }

    @Override
    public void OnSuccess(String result, int tag) {
        if (result.isEmpty())
            return;
        LogTool.i("result" + String.valueOf(DoubanApi.isAuthed), result);
        Util.loadAnima(mProgressBar, mRecycleView);
        if (tag == DoubanApi.GET_SEARCH){
            InTheaterBean bean = mGson.fromJson(result,InTheaterBean.class);
            data.addAll(data.size()-1,bean.getSubjects());
            mAdapter.notifyDataSetChanged();
            /*if (footer!=null && bean.getTotal() <= mData.size()-1){
                footer.loading_layout.setVisibility(View.GONE);
                footer.tv_nomal.setVisibility(View.GONE);
                footer.tv_complete.setVisibility(View.VISIBLE);
                footer.tv_complete.setText("加载完毕");
            }*/
            if (footer != null){
                Util.loadAnima(footer.loading_layout, footer.tv_nomal);
            }
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
