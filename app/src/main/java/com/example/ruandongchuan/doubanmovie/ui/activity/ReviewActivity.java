package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.AbstractAdapter;
import com.example.ruandongchuan.doubanmovie.ui.adapter.ReviewAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.ReviewBean;
import com.example.ruandongchuan.doubanmovie.util.HtmlParser;
import com.example.ruandongchuan.doubanmovie.util.HttpManager;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AbstractActivity implements HttpManager.OnConnectListener{
    private String mUrl;
    private HttpManager mHttpManager;
    private RecyclerView mRecycleView;
    private ProgressBar mProgressBar;
    private ReviewAdapter mAdapter;
    private List<BaseBean> mData;
    private View mHeader;
    private ReviewAdapter.HeaderHolder mHeaderHolder;
    private ReviewBean mHeaderBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        mHeaderBean = (ReviewBean) intent.getSerializableExtra("bean");
        String title = mHeaderBean.getTitle();
        mUrl = mHeaderBean.getLink();
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initData();
        initView();
    }
    private void initData(){
        mHttpManager = new HttpManager(this);
        mData = new ArrayList<>();
        mAdapter = new ReviewAdapter(mData);
    }
    private void initView(){
        mRecycleView = (RecyclerView) findViewById(R.id.rv_review);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_review);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mHeader = LayoutInflater.from(this).inflate(R.layout.review_header,mRecycleView,false);
        mHeaderHolder = new ReviewAdapter.HeaderHolder(mHeader);
        //添加自定义header
        mAdapter.addCustomView(mHeader, 0, AbstractAdapter.TYPE_HEADER);
        mRecycleView.setAdapter(mAdapter);
        //获取影评详情
        mHttpManager.getString(mUrl, DoubanApi.GET_DETAIL_REVIEW);
        ImageLoader.getmInstance().loadImage(mHeaderBean.getImg(), mHeaderHolder.iv_review);
        mHeaderHolder.tv_name.setText(mHeaderBean.getName());
        int rating = Integer.parseInt(mHeaderBean.getRating());
        if (rating == 0){
            mHeaderHolder.rb_review.setVisibility(View.GONE);
        }else {
            mHeaderHolder.rb_review.setVisibility(View.VISIBLE);
            mHeaderHolder.rb_review.setRating(rating/10);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnSuccess(String result, int tag) {
        if (tag == DoubanApi.GET_DETAIL_REVIEW){
            ReviewBean bean = HtmlParser.getReview(result);
            mHeaderHolder.tv_review.setText(Html.fromHtml(bean.getReview()));
            //mHeaderHolder.tv_review.setText(result);
            //LogTool.i("bean","bean="+result);
            Util.loadAnima(mProgressBar, mRecycleView);
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
