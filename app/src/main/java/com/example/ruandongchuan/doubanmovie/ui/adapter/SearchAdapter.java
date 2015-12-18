package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.Setting;
import com.example.ruandongchuan.doubanmovie.ui.activity.DetailActivity;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.AbstractViewHolder;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.LoadViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;

import java.util.List;

/**
 * Created by ruandongchuan on 15-11-14.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 100;
    public static final int TYPE_FOOTER = 101;
    private List<BaseBean> data;
    private Context mContext;
    private ImageLoader mImageLoader;
    private LoadViewHolder mLoadViewHolder;
    private OnShowAdapter.OnAdapterBackListener mListener;
    public SearchAdapter(Context mContext,List<BaseBean> data){
        this.data = data;
        this.mContext = mContext;
        mImageLoader = ImageLoader.getmInstance();
    }

    public void setmListener(OnShowAdapter.OnAdapterBackListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getView_type() == TYPE_FOOTER?
                TYPE_FOOTER:TYPE_ITEM;
    }

    public LoadViewHolder getmLoadViewHolder() {
        return mLoadViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                        parent, false);
                holder = new SearchItemHolder(view);
                break;
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item,
                        parent,false);
                holder = new LoadViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_ITEM){
            final InTheaterBean.SubjectBean bean = (InTheaterBean.SubjectBean) data.get(position);
            SearchItemHolder itemHolder = (SearchItemHolder) holder;
            itemHolder.tv_name.setText(bean.getTitle());
            itemHolder.tv_name_en.setText(bean.getOriginal_title());
            itemHolder.tv_rating.setText(bean.getRating().getAverage()+"");
            mImageLoader.loadImage(Setting.getImageUrl(bean.getImages()), itemHolder.iv_icon, null);
            itemHolder.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", bean.getId());
                    intent.putExtra("title", bean.getTitle());
                    intent.putExtra("image",bean.getImages().getLarge());
                    mContext.startActivity(intent);
                }
            });
        }
        if (getItemViewType(position)== TYPE_FOOTER){
            LoadViewHolder loadViewHolder = (LoadViewHolder) holder;
            loadViewHolder.tv_nomal.setText(mContext.getString(R.string.click_to_load_more));
            loadViewHolder.tv_nomal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnClick(position, getItemViewType(position));
                }
            });
            mLoadViewHolder = loadViewHolder;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SearchItemHolder extends AbstractViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_name_en;
        public TextView tv_rating;
        public SearchItemHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_name_en = (TextView) itemView.findViewById(R.id.tv_name_en);
            tv_rating = (TextView) itemView.findViewById(R.id.tv_rating);
        }
    }
}
