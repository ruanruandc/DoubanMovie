package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;

import java.util.List;

/**
 * Created by ruandongchuan on 15-12-14.
 */
public class ReviewAdapter extends AbstractAdapter {
    public ReviewAdapter(List<BaseBean> mData){
        this.mData = mData;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        if (viewType == TYPE_HEADER){
            view = mItems.get(TYPE_HEADER);
            viewHolder = new HeaderHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getView_type() == TYPE_HEADER?
                TYPE_HEADER:TYPE_ITEM;
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder{
        public TextView tv_review;
        public ImageView iv_review;
        public TextView tv_name;
        public RatingBar rb_review;
        public HeaderHolder(View itemView) {
            super(itemView);
            tv_review = (TextView) itemView.findViewById(R.id.tv_review);
            iv_review = (ImageView) itemView.findViewById(R.id.iv_review);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            rb_review = (RatingBar) itemView.findViewById(R.id.rb_review);
        }
    }
}
