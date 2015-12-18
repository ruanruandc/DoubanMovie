package com.example.ruandongchuan.doubanmovie.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.ruandongchuan.doubanmovie.util.LogTool;

/**
 * Created by ruandongchuan on 15-11-16.
 */
public class SwipeViewPager extends ViewPager {
    private float downX,downY,moveX,moveY,lastX,lastY;
    private boolean isIntercept = false;
    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        LogTool.i("onPageScrolled", position + "-" + offset + "-" + offsetPixels);
        if (position == 0 && offset == 0){
            isIntercept = true;
        }else {
            isIntercept = false;
        }
    }

    //拦截touch事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                moveX = downX;
                moveY = downY;
                lastX = downX;
                lastY = downY;
                LogTool.i("dispatch_ACTION_DOWN",downX+ "-"+downY);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                moveX = ev.getX();
                float dx = moveX-lastX;
                float dy = moveY-lastY;
                lastX = moveX;
                lastY = moveY;
                //当向右滑动超过30像素并且水平移动距离大于垂直移动距离时对事件拦截
                if (dx > 30) {
                    if (dy == 0f || Math.abs(dx / dy) > 1) {
                        if (isIntercept) {
                            return false;
                        }
                    }
                }
                LogTool.i("dispatch_ACTION_MOVE",moveX+ "-"+moveY+"-dx="+dx);
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
