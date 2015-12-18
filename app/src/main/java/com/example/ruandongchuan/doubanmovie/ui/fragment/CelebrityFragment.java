package com.example.ruandongchuan.doubanmovie.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.DoubanApi;
import com.example.ruandongchuan.doubanmovie.ui.adapter.CelebrityAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.CelebrityBean;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

/**
 * Created by ruandongchuan on 15-11-12.
 */
public class CelebrityFragment extends AbstractFragment {
    private String id;
    private CelebrityAdapter mAdapter;
    //private List<BaseBean> mData;
    public static CelebrityFragment getInstance(String id){
        CelebrityFragment fragment = new CelebrityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        //isCreated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_celebrity,container,false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_celebrity);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_celebrity);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CelebrityAdapter(getContext(), mData);
        mRecycleView.setAdapter(mAdapter);
        return view;
    }

    @Override
    void initData() {
        mHttpManager.getCelebrity(id);
    }

    @Override
    public void OnSuccess(String result, int tag) {
        LogTool.i("result" + String.valueOf(DoubanApi.isAuthed), result);
        Util.loadAnima(mProgressBar,mRecycleView);
        if (tag == DoubanApi.GET_CELEBRITY){
            CelebrityBean bean = mGson.fromJson(result,CelebrityBean.class);
            bean.setView_type(CelebrityAdapter.TYPE_HEADER);
            mData.add(bean);
            mData.addAll(bean.getWorks());
            mAdapter.notifyDataSetChanged();
            LogTool.i("info",bean.getBorn_place());
        }
    }

    @Override
    public void OnError(int tag) {

    }
}
