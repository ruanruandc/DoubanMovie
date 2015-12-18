package com.example.ruandongchuan.doubanmovie.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

/**
 * Created by ruandongchuan on 15-11-14.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private OnRecycleViewItemClick onRecycleViewItemClick;

    public void setOnRecycleViewItemClick(OnRecycleViewItemClick onRecycleViewItemClick) {
        this.onRecycleViewItemClick = onRecycleViewItemClick;
    }

    public AbstractViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onRecycleViewItemClick!=null) {
            onRecycleViewItemClick.OnItemClick(v, getPosition());
        }
    }
}
