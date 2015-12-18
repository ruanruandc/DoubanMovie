package com.example.ruandongchuan.doubanmovie.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.example.ruandongchuan.doubanmovie.util.HttpManager;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.OnScrollPauseListener;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnCallBack;
import com.example.ruandongchuan.doubanmovie.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnShowFragment extends AbstractFragment implements HttpManager.OnConnectListener{
    private OnShowAdapter mAdapter;
    private GridLayoutManager mManager;
    private int title_position;
    private String city;
    private boolean isRefresh = false;
    private String la,lo;
    private OnCallBack<String> onGetCity;
    private final Gson gson = new Gson();
    private View loadView;
    private TextView tv_complete;
    private TextView tv_nomal;
    private LinearLayout loading_layout;
    public OnShowFragment() {
        // Required empty public constructor
    }

    public void setOnGetCity(OnCallBack<String> onGetCity) {
        this.onGetCity = onGetCity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            city =savedInstanceState.getString("city");
        }
        if (city == null || TextUtils.isEmpty(city)){
            city = getString(R.string.default_location);
        }
        if (getArguments() != null){
            la = getArguments().getString("la");
            lo = getArguments().getString("lo");
        }
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.title_item,null);
        isCreated = false;
        LogTool.i("fragment", "oncreate");
    }

    public static OnShowFragment getInstance(String la,String lo){
        OnShowFragment fragment = new OnShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("la", la);
        bundle.putString("lo", lo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        mHttpManager = new HttpManager(this);
        LogTool.i("Onshow",this.toString()+"-view="+view.toString());
        mData = new ArrayList<>();
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_base);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_base);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_base);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_base);
        initView();
        initData();
        initLoadView();
        return view;
    }
    private void initLoadView(){
        //holder = new TitleViewHolder(loadView);
        tv_complete = (TextView) loadView.findViewById(R.id.tv_complete);
        tv_nomal = (TextView) loadView.findViewById(R.id.tv_nomal);
        loading_layout = (LinearLayout) loadView.findViewById(R.id.loading_layout);
        tv_complete.setVisibility(View.GONE);
        tv_nomal.setVisibility(View.VISIBLE);
        tv_nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_nomal.setVisibility(View.GONE);
                loading_layout.setVisibility(View.VISIBLE);
                mHttpManager.getComingSoon(0, 20);
            }
        });
    }
    private void initView(){
        //showFloatingActionButton();
        mSwipeRefreshLayout.setEnabled(false);
        mRecycleView.setHasFixedSize(true);
        mAdapter = new OnShowAdapter(getContext(), mData);
        mRecycleView.setAdapter(mAdapter);
        mManager = new GridLayoutManager(getContext(),3);
        mRecycleView.setLayoutManager(mManager);
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = 1;
                switch (mAdapter.getItemViewType(position)) {
                    case OnShowAdapter.TYPE_TITLE:
                    case OnShowAdapter.TYPE_LOAD:
                        size = mManager.getSpanCount();
                        break;
                }
                return size;
            }
        });
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setOnScrollListener(new OnScrollPauseListener());
    }
    public void initData(){
        mHttpManager.getGeocoding(la, lo);
    }

    public void update(String la,String lo){
        this.la = la;
        this.lo = lo;
        mHttpManager.getGeocoding(la, lo);
        tv_complete.setVisibility(View.GONE);
        tv_nomal.setVisibility(View.VISIBLE);
        isRefresh = true;
        Util.loadAnima(mRecycleView, mProgressBar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("city",city);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void OnSuccess(String result, int tag) {
        LogTool.i("result"+String.valueOf(DoubanApi.isAuthed),result);
        if (result.isEmpty())
            return;
        //正在上映
        if (tag == DoubanApi.GET_IN_THEATERS){
            InTheaterBean bean = gson.fromJson(result, InTheaterBean.class);
            if (isRefresh){
                mData.clear();
                mAdapter.notifyDataSetChanged();
                isRefresh = false;
            }
            mData.addAll(bean.getSubjects());
            mAdapter.addCustomView(loadView, mData.size(), OnShowAdapter.TYPE_TITLE);
            title_position = mData.size();
            mAdapter.notifyDataSetChanged();
            Util.loadAnima(mProgressBar, mRecycleView);
            LogTool.i(tag+"",bean.getTitle());
        }
        //即将上映
        if (tag == DoubanApi.GET_COMMING_SOON){
            loading_layout.setVisibility(View.GONE);
            tv_complete.setVisibility(View.VISIBLE);
            InTheaterBean bean = gson.fromJson(result, InTheaterBean.class);
            mData.addAll(bean.getSubjects());
            mAdapter.notifyDataSetChanged();
            mRecycleView.post(new Runnable() {
                @Override
                public void run() {
                    mRecycleView.smoothScrollToPosition(title_position);
                }
            });

        }
        //根据经纬度获取城市信息
        if (tag == DoubanApi.GET_GEOCODING){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonresult= jsonObject.getJSONObject("result");
                JSONObject addressComponent = jsonresult.getJSONObject("addressComponent");
                city = addressComponent.getString("city").replaceAll("市", "");
            } catch (JSONException e) {
                e.printStackTrace();
                city = getString(R.string.default_location);
            }
            if (city.isEmpty()){
                city = getString(R.string.default_location);
            }
            if (onGetCity!=null) {
                onGetCity.callBack(city);
            }
            mHttpManager.getInTheaters(city);
        }
    }

    @Override
    public void OnError(int tag) {
        if (tag == DoubanApi.GET_GEOCODING){
            city = getString(R.string.default_location);
            mHttpManager.getInTheaters(city);
        }
    }
}
