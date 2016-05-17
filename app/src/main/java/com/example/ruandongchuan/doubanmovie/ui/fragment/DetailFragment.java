package com.example.ruandongchuan.doubanmovie.ui.fragment;


import android.app.Fragment;
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
import com.example.ruandongchuan.doubanmovie.ui.adapter.DetailAdapter;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.MovieBean;
import com.example.ruandongchuan.doubanmovie.util.DownLoadThread;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnCallBack;
import com.example.ruandongchuan.doubanmovie.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends AbstractFragment {
    private String id;
    private DetailAdapter mAdapter;
    private List<BaseBean> data;
    private OnCallBack<Bundle> onCallBack;

    public void setOnCallBack(OnCallBack<Bundle> onCallBack) {
        this.onCallBack = onCallBack;
    }

    public DetailFragment(){
        super();
    }
    public static DetailFragment newInstance(String id){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_detail);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_detail);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        data = new ArrayList<>();
        mAdapter = new DetailAdapter(getContext(),data);
        mRecycleView.setAdapter(mAdapter);
        //tv_test.open();
        return view;
    }

    @Override
    void initData() {
        mHttpManager.getSubject(id);
        LogTool.i("here", id);
    }

    @Override
    public void OnSuccess(String result, int tag) {
        LogTool.i("result"+String.valueOf(DoubanApi.isAuthed), result);
        if (!isAdded())
            return;
        if (result.isEmpty())
            return;
        if (tag == DoubanApi.GET_SUBJECT){
            //onFragmentCallBack.callBack(null);
            final MovieBean bean = mGson.fromJson(result, MovieBean.class);
            DownLoadThread thread = new DownLoadThread(bean.getImages().getLarge(), new OnCallBack<File>() {
                @Override
                public void callBack(File file) {
                    if (isAdded()){
                        Bundle bundle = new Bundle();
                        bundle.putString("text", "《" + bean.getTitle() + "》" +
                                getString(R.string.from_douban_movie) + bean.getAlt());
                        bundle.putSerializable("image", file);
                        if (onCallBack != null) {
                            onCallBack.callBack(bundle);
                        }
                    }
                }
            });
            thread.start();
            bean.setView_type(DetailAdapter.TYPE_HEADER);
            data.add(bean);
            List<InTheaterBean.SubjectBean.CastBean> items =
                    bean.getCasts();
            List<InTheaterBean.SubjectBean.CastBean> directors =
                    bean.getDirectors();
            for (InTheaterBean.SubjectBean.CastBean director: directors
                 ) {
                director.setName(director.getName()+getString(R.string.director));
            }
            data.addAll(directors);
            data.addAll(items);
            mAdapter.notifyDataSetChanged();
            Util.loadAnima(mProgressBar, mRecycleView);
        }
    }

    @Override
    public void OnError(int tag) {

    }
}

