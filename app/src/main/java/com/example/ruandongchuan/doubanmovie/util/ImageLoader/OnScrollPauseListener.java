package com.example.ruandongchuan.doubanmovie.util.ImageLoader;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

/**
 * Created by ruandongchuan on 15-11-28.
 */
public class OnScrollPauseListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!ImageLoader.getmInstance().isSwipeLoadImage()) {
            switch (newState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    ImageLoader.getmInstance().resume();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    ImageLoader.getmInstance().pause();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    ImageLoader.getmInstance().pause();
                    break;
            }
        }
    }
}
