package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruandongchuan on 15-11-27.
 */
public abstract class AbstractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_LOAD = 1;
    public static final int TYPE_HEADER = 2;
    public List<BaseBean> mData;
    public Map<Integer,View> mItems;
    public AbstractAdapter(){
        mItems = new HashMap<>();
        mData = new ArrayList<>();
    }
    //添加自定义View
    public void addCustomView(View view,int position,int tag){
        BaseBean bean = new BaseBean();
        if (tag >= 0){
            bean.setView_type(tag);
        }
        mItems.put(tag, view);
        mData.add(position, bean);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getView_type() == TYPE_LOAD?
                TYPE_LOAD:TYPE_ITEM;
    }


}
