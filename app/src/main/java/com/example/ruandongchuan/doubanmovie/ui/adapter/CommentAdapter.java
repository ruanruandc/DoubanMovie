package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.CommentBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.AbstractViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;

import java.util.List;

/**
 * Created by ruandongchuan on 15-12-9.
 */
public class CommentAdapter extends AbstractAdapter {
    public CommentAdapter(List<BaseBean> data){
        this.mData = data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        if (viewType == TYPE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                    parent,false);
            holder = new CommentHolder(view);
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
            CommentBean bean = (CommentBean) mData.get(position);
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.tv_comment.setText(bean.getComment());
            commentHolder.tv_time.setText(bean.getTime());
            commentHolder.tv_title.setText(bean.getTitle());
            commentHolder.tv_vote.setText(bean.getComment_vote());
            ImageLoader.getmInstance().loadImage(bean.getImg(),commentHolder.iv_comment);
            int rating = Integer.parseInt(bean.getRating());
            if (rating == 0){
                commentHolder.rb_comment.setVisibility(View.GONE);
            }else {
                commentHolder.rb_comment.setVisibility(View.VISIBLE);
                commentHolder.rb_comment.setRating(rating/10);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    static class CommentHolder extends AbstractViewHolder {
        public ImageView iv_comment;
        public TextView tv_title;
        public TextView tv_comment;
        public TextView tv_time;
        public TextView tv_vote;
        public RatingBar rb_comment;

        public CommentHolder(View itemView) {
            super(itemView);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_vote = (TextView) itemView.findViewById(R.id.tv_vote);
            rb_comment = (RatingBar) itemView.findViewById(R.id.rb_comment);
        }
        //public TextView tv_title;
    }
}
