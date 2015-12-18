package com.example.ruandongchuan.doubanmovie.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.CommentAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.CommentBean;
import com.example.ruandongchuan.doubanmovie.util.HtmlParser;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-12-4.
 */
public class CommentFragment extends AbstractFragment {
    private TextView tv_nomal;
    private LinearLayout mLoadingLayout;
    private String id;
    private CommentAdapter mAdapter;
    private View load_view;
    public static CommentFragment getInstance(String id){
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            Bundle bundle = getArguments();
            id = bundle.getString("id");
        }
    }
    public CommentFragment(){}

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
        mSwipeRefreshLayout.setEnabled(false);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mAdapter = new CommentAdapter(mData);
        load_view = LayoutInflater.from(getContext()).inflate(R.layout.title_item, mRecycleView,false);
        tv_nomal = (TextView) load_view.findViewById(R.id.tv_nomal);
        mLoadingLayout = (LinearLayout) load_view.findViewById(R.id.loading_layout);
        mAdapter.addCustomView(load_view, mData.size() > 0 ? mData.size() - 1 : 0, CommentAdapter.TYPE_LOAD);
        tv_nomal.setText(getString(R.string.click_to_load_more));
        tv_nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_nomal.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                mHttpManager.getComment(id, mData.size()-1 + "", "20", "new_score");
            }
        });

        mRecycleView.setAdapter(mAdapter);
        mHttpManager.getComment(id,"0","20","new_score");
    }

    @Override
    public void OnSuccess(String result, int tag) {
        Log.i("result",result);
        if (tag == DoubanApi.GET_COMMENT) {
            mLoadingLayout.setVisibility(View.GONE);
            tv_nomal.setVisibility(View.VISIBLE);
            Util.loadAnima(mProgressBar, mRecycleView);
            String body = HtmlParser.getCommentBody(result);
            List<CommentBean> data = HtmlParser.getComments(body);
            StringBuilder sb = new StringBuilder();
            for (CommentBean bean : data) {
                sb.append(bean.getRating());
                sb.append("\n\r");
            }
            this.mData.addAll(this.mData.size()-1,data);
            mAdapter.notifyDataSetChanged();
            //textView.setText(sb.toString());
            Log.i("sb", sb.toString());
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
