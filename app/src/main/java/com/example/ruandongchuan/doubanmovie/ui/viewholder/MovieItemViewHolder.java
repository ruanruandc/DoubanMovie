package com.example.ruandongchuan.doubanmovie.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class MovieItemViewHolder extends AbstractViewHolder {
    public ImageView iv_movie;
    public TextView tv_title;
    public TextView tv_score;
    public RatingBar rb_rating;
    public MovieItemViewHolder(View itemView) {
        super(itemView);
        iv_movie = (ImageView) itemView.findViewById(R.id.iv_movie);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        tv_score = (TextView) itemView.findViewById(R.id.tv_score);
        rb_rating = (RatingBar) itemView.findViewById(R.id.rb_rating);
    }
}
