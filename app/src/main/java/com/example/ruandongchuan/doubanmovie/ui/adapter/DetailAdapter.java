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
import com.example.ruandongchuan.doubanmovie.ui.activity.CelebrityActivity;
import com.example.ruandongchuan.doubanmovie.ui.bean.BaseBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.InTheaterBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.MovieBean;
import com.example.ruandongchuan.doubanmovie.ui.view.ExpandTextView;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.Util;
import com.example.ruandongchuan.doubanmovie.util.interfaces.OnRecycleViewItemClick;

import java.util.List;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BaseBean> data;
    private ImageLoader mImageLoader;
    public static final int TYPE_ITEM = 100;
    public static final int TYPE_HEADER = 101;
    private Context mContext;
    public DetailAdapter(Context context,List<BaseBean> data){
        this.data = data;
        mImageLoader = ImageLoader.getmInstance();
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getView_type() == TYPE_HEADER ?
                TYPE_HEADER:TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType){
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_detail_header,
                        parent,false);
                holder = new HeaderHolder(view);
                break;
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_detail_item,
                        parent,false);
                holder = new ItemHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER){
            HeaderHolder headerHolder = (HeaderHolder) holder;
            MovieBean bean = (MovieBean)data.get(position);
            //mImageLoader.loadImage(bean.getImages().getLarge(), headerHolder.iv_icon);
            headerHolder.tv_detail_score.setText(bean.getRatings_count() + "人评价");
            headerHolder.tv_detail_summary.setText(bean.getSummary());
            headerHolder.tv_detail_time.setText(bean.getYear());
            headerHolder.tv_detail_type.setText(Util.arrayToString(bean.getGenres()));
            headerHolder.tv_detail_country.setText(Util.arrayToString(bean.getCountries()));
        }
        if (getItemViewType(position) == TYPE_ITEM){
            ItemHolder itemHolder = (ItemHolder) holder;
            final InTheaterBean.SubjectBean.CastBean bean = (InTheaterBean.SubjectBean.CastBean) data.get(position);
            if (bean.getAvatars()!=null) {
                mImageLoader.loadImage(Setting.getLogoUrl(bean.getAvatars()), itemHolder.iv_item_man);
            }
            itemHolder.tv_item_name.setText(bean.getName());

            itemHolder.setOnRecycleViewItemClick(new OnRecycleViewItemClick() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(mContext,CelebrityActivity.class);
                    intent.putExtra("name",bean.getName());
                    intent.putExtra("id",bean.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HeaderHolder extends RecyclerView.ViewHolder{
        public ImageView iv_icon;
        public TextView tv_detail_score;
        public TextView tv_detail_time;
        public TextView tv_detail_type;
        public TextView tv_detail_country;
        public ExpandTextView tv_detail_summary;
        //public Button btn_buyer;
        public HeaderHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_detail_score = (TextView) itemView.findViewById(R.id.tv_detail_score);
            tv_detail_time = (TextView) itemView.findViewById(R.id.tv_detail_time);
            tv_detail_type = (TextView) itemView.findViewById(R.id.tv_detail_type);
            tv_detail_country = (TextView) itemView.findViewById(R.id.tv_detail_country);
            tv_detail_summary = (ExpandTextView) itemView.findViewById(R.id.tv_detail_summary);
            //btn_buyer = (Button) itemView.findViewById(R.id.btn_buyer);
        }
    }
    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView iv_item_man;
        public TextView tv_item_name;
        private OnRecycleViewItemClick onRecycleViewItemClick;
        public ItemHolder(View itemView) {
            super(itemView);
            iv_item_man = (ImageView) itemView.findViewById(R.id.iv_item_man);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            itemView.setOnClickListener(this);
        }

        public void setOnRecycleViewItemClick(OnRecycleViewItemClick onRecycleViewItemClick) {
            this.onRecycleViewItemClick = onRecycleViewItemClick;
        }

        @Override
        public void onClick(View v) {
            onRecycleViewItemClick.OnItemClick(v,getPosition());
        }
    }
    /*public interfaces OnRecycleViewItemClick{
        public void OnItemClick(View v,int position);
    }*/
}
