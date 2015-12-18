package com.example.ruandongchuan.doubanmovie.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class LoadViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_nomal;
    public TextView tv_complete;
    public LinearLayout loading_layout;
    public LoadViewHolder(View itemView) {
        super(itemView);
        tv_nomal = (TextView) itemView.findViewById(R.id.tv_nomal);
        tv_complete = (TextView) itemView.findViewById(R.id.tv_complete);
        loading_layout = (LinearLayout) itemView.findViewById(R.id.loading_layout);
    }
}
