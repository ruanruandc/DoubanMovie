package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.activity.ReviewActivity;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.ReviewBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.AbstractViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

import java.util.List;

/**
 * Created by ruandongchuan on 15-12-10.
 */
public class ReviewListAdapter extends AbstractAdapter {
    public ReviewListAdapter(List<BaseBean> data){
        this.mData = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        if (viewType == TYPE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
            holder = new ReviewHolder(view);
        }
        if (viewType == TYPE_LOAD){
            view = mItems.get(TYPE_LOAD);
            holder = new LoadViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM){
            ReviewHolder reviewHolder = (ReviewHolder) holder;
            final ReviewBean bean = (ReviewBean) mData.get(position);
            reviewHolder.tv_name.setText(bean.getName());
            ImageLoader.getmInstance().loadImage(bean.getImg(), reviewHolder.iv_comment);
            reviewHolder.tv_title.setText(bean.getTitle());
            reviewHolder.tv_time.setText(bean.getTime());
            reviewHolder.tv_comment.setText(Html.fromHtml(bean.getReview()));
            reviewHolder.tv_vote.setText(bean.getUseful());
            int rating = Integer.parseInt(bean.getRating());
            if (rating == 0){
                reviewHolder.rb_comment.setVisibility(View.GONE);
            }else {
                reviewHolder.rb_comment.setVisibility(View.VISIBLE);
                reviewHolder.rb_comment.setRating(rating/10);
            }
            reviewHolder.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(v.getContext(), ReviewActivity.class);
                    intent.putExtra("bean",bean);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    static class ReviewHolder extends AbstractViewHolder {
        public ImageView iv_comment;
        public TextView tv_title;
        public TextView tv_comment;
        public TextView tv_time;
        public TextView tv_vote;
        public TextView tv_name;
        public RatingBar rb_comment;
        public ReviewHolder(View itemView) {
            super(itemView);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_vote = (TextView) itemView.findViewById(R.id.tv_vote);
            rb_comment = (RatingBar) itemView.findViewById(R.id.rb_comment);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
