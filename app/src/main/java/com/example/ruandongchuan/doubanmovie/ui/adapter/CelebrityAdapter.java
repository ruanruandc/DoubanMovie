package com.example.ruandongchuan.doubanmovie.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.Setting;
import com.example.ruandongchuan.doubanmovie.ui.activity.DetailActivity;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.CelebrityBean;
import com.example.ruandongchuan.doubanmovie.ui.viewholder.AbstractViewHolder;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

import java.util.List;

/**
 * Created by ruandongchuan on 15-11-13.
 */
public class CelebrityAdapter extends RecyclerView.Adapter<ViewHolder>{
    public static final int TYPE_HEADER = 100;
    public static final int TYPE_ITEM = 101;
    private List<BaseBean> data;
    private ImageLoader mImageLoader;
    private Context mContext;
    public CelebrityAdapter(Context mContext,List<BaseBean> data) {
        super();
        this.data = data;
        mImageLoader = ImageLoader.getmInstance();
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getView_type() == TYPE_HEADER ?
                TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = null;
        switch (viewType){
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_celebrity_header,
                        parent,false);
                viewHolder = new CelebrityHeadHolder(view);
                break;
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_celebrity_item,
                        parent,false);
                viewHolder = new CelebrityItemHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //数据绑定
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                CelebrityBean bean = (CelebrityBean) data.get(position);
                CelebrityHeadHolder headHolder = (CelebrityHeadHolder) holder;
                headHolder.tv_celebrity_born_place.setText(bean.getBorn_place());
                headHolder.tv_celebrity_name.setText(bean.getName());
                headHolder.tv_celebrity_gender.setText(bean.getGender());
                mImageLoader.loadImage(bean.getAvatars().getMedium(), headHolder.iv_celebrity);
                break;
            case TYPE_ITEM:
                final CelebrityBean.WorkItemBean workItemBean = (CelebrityBean.WorkItemBean) data.get(position);
                CelebrityItemHolder celebrityItem = (CelebrityItemHolder) holder;
                celebrityItem.tv_item_name.setText(workItemBean.getSubject().getTitle());
                String rating = workItemBean.getSubject().getRating() == null?
                        mContext.getString(R.string.have_not_score):workItemBean.getSubject().getRating().getAverage()+"";
                celebrityItem.tv_item_rating.setText(rating);
                mImageLoader.loadImage(Setting.getLogoUrl(workItemBean.getSubject().getImages()),
                        celebrityItem.iv_item_celebrity);
                celebrityItem.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                    @Override
                    public void OnItemClick(View v, int position) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("id",workItemBean.getSubject().getId());
                        intent.putExtra("title",workItemBean.getSubject().getTitle());
                        intent.putExtra("image",workItemBean.getSubject().getImages().getLarge());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }
    public class CelebrityHeadHolder extends RecyclerView.ViewHolder{
        public ImageView iv_celebrity;
        public TextView tv_celebrity_name;
        public TextView tv_celebrity_born_place;
        public TextView tv_celebrity_gender;
        public CelebrityHeadHolder(View itemView) {
            super(itemView);
            iv_celebrity = (ImageView) itemView.findViewById(R.id.iv_celebrity);
            tv_celebrity_name = (TextView) itemView.findViewById(R.id.tv_celebrity_name);
            tv_celebrity_born_place = (TextView) itemView.findViewById(R.id.tv_celebrity_born_place);
            tv_celebrity_gender = (TextView) itemView.findViewById(R.id.tv_celebrity_gender);
        }
    }
    public class CelebrityItemHolder extends AbstractViewHolder{
        public ImageView iv_item_celebrity;
        public TextView tv_item_name;
        public TextView tv_item_rating;

        public CelebrityItemHolder(View itemView) {
            super(itemView);
            iv_item_celebrity = (ImageView) itemView.findViewById(R.id.iv_item_celebrity);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            tv_item_rating = (TextView) itemView.findViewById(R.id.tv_item_rating);
        }


    }
}
