package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.Setting;
import com.example.ruandongchuan.doubanmovie.ui.activity.DetailActivity;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.MovieItemViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

import java.util.List;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class OnShowAdapter extends AbstractAdapter {
    //private List<InTheaterBean.SubjectBean> mData;
    private ImageLoader mImageLoader;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_TITLE = 2;
    public static final int TYPE_LOAD = 3;
    private Context mContext;
    public OnShowAdapter(Context mContext,List<BaseBean> data){
        this.mData = data;
        mImageLoader = ImageLoader.getmInstance();
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mData.get(position).getView_type();
        switch (type){
            case TYPE_TITLE:
            case TYPE_LOAD:
                break;
            default:
                type = TYPE_ITEM;
                break;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        //根据不同的ViewType，返回不同的ViewHolder
        switch (viewType){
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,
                        parent,false);
                viewHolder = new MovieItemViewHolder(view);
                break;
            case TYPE_TITLE:
                view = mItems.get(TYPE_TITLE);
                viewHolder = new LoadViewHolder(view);
                break;
            case TYPE_LOAD:
                view = mItems.get(TYPE_LOAD);
                viewHolder = new LoadViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //数据绑定
        if (holder.getItemViewType() == TYPE_ITEM) {
            MovieItemViewHolder itemViewHolder = (MovieItemViewHolder) holder;
            final InTheaterBean.SubjectBean bean = (InTheaterBean.SubjectBean) mData.get(position);
            mImageLoader.loadImage(Setting.getImageUrl(bean.getImages()), itemViewHolder.iv_movie);
            itemViewHolder.tv_title.setText(bean.getTitle());
            itemViewHolder.tv_title.setSelected(true);
            if (Util.isZero(bean.getRating().getAverage())) {
                itemViewHolder.tv_score.setText(mContext.getString(R.string.have_not_score));
                itemViewHolder.rb_rating.setVisibility(View.GONE);
            }else {
                itemViewHolder.tv_score.setText(String.valueOf(bean.getRating().getAverage()));
                itemViewHolder.rb_rating.setRating(bean.getRating().getAverage()/2);
                itemViewHolder.rb_rating.setVisibility(View.VISIBLE);
            }
            itemViewHolder.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id",bean.getId());
                    intent.putExtra("title",bean.getTitle());
                    intent.putExtra("image",bean.getImages().getLarge());
                    LogTool.i("memory=", mImageLoader.getMemoryCacheSize());
                    mContext.startActivity(intent);
                }
            });
        }
        if (holder.getItemViewType() == TYPE_TITLE ){

        }
        if (holder.getItemViewType() == TYPE_LOAD){
        }
    }

    public interface OnAdapterBackListener{
        void OnClick(int position,int type);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
