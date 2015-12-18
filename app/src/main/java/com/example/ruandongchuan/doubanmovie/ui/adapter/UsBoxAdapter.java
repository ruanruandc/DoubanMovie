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
import com.example.ruandongchuan.doubanmovie.ui.bean.UsBoxBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.MovieItemViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

import java.util.List;

/**
 * Created by ruandongchuan on 15-11-17.
 */
public class UsBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_ITEM = 100;
    private List<BaseBean> data;
    private Context mContext;
    public UsBoxAdapter(Context mContext,List<BaseBean> data){
        this.data = data;
        this.mContext = mContext;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType){
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,
                        parent,false);
                viewHolder =  new MovieItemViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position)==TYPE_ITEM){
            final UsBoxBean.SubjectsBean bean = (UsBoxBean.SubjectsBean) data.get(position);
            MovieItemViewHolder itemViewHolder = (MovieItemViewHolder) holder;
            itemViewHolder.tv_title.setText(bean.getSubject().getTitle());
            itemViewHolder.rb_rating.setRating(bean.getSubject().getRating().getAverage()/2);
            itemViewHolder.tv_score.setText(bean.getSubject().getRating().getAverage()+"");
            itemViewHolder.tv_title.setSelected(true);
            ImageLoader.getmInstance().loadImage(Setting.getImageUrl(bean.getSubject().getImages()),
                    itemViewHolder.iv_movie);
            itemViewHolder.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id",bean.getSubject().getId());
                    intent.putExtra("title",bean.getSubject().getTitle());
                    intent.putExtra("image",bean.getSubject().getImages().getLarge());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
