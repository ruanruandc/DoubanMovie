package com.example.ruandongchuan.doubanmovie.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.ruandongchuan.doubanmovie.util.LogTool;

/**
 * 滑动返回Activity
 * Created by ruandongchuan on 15-11-18.
 */
public abstract class AbstractSwipeActivity extends AbstractActivity {
    private float downX,downY,moveX,moveY,lastX;
    private boolean dispatch_flag,intercept_flag,touch_flag;
    private boolean isLayoutDispatch = false;
    private SwipeLayout mSwipeLayout;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeLayout = new SwipeLayout(this);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeLayout.replaceLayout(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float dx=0;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                moveX = downX;
                moveY = downY;
                lastX = downX;
                LogTool.i("activity-dispatch_DOWN", downX + "-" + downY);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                moveX = ev.getX();
                dx = moveX - lastX;
                lastX = moveX;
                LogTool.i("activity-dispatch_MOVE",moveX+ "-"+moveY);
                break;
            case MotionEvent.ACTION_CANCEL:
                LogTool.i("activity-dispatch_CANCE","");
                break;
        }
        dispatch_flag = super.dispatchTouchEvent(ev);
        LogTool.i("activity_dispatch_flag", String.valueOf(dispatch_flag));
        LogTool.i("is_layout_dispatch_flag", String.valueOf(isLayoutDispatch));
        return dispatch_flag;
    }

    class SwipeLayout extends FrameLayout{
        private float layout_downX,layout_downY,layout_moveX,layout_moveY,layout_lastX;
        private VelocityTracker mTracker;
        private Activity mActivity;
        //可移动View
        private View mContent;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void replaceLayout(Activity activity){
            setClickable(true);
            mActivity = activity;
            ViewGroup root = (ViewGroup) mActivity.getWindow().getDecorView();
            mContent = root.getChildAt(0);
            //将content与swipeLayout替换 包括LayoutParams
            //在Android5.0上，content的高度不再是屏幕高度，而是变成了Activity高度，比屏幕高度低一些，
            //如果this.addView(mContent),就会使用以前的params，这样content会像root一样比content高出一部分，导致底部空出一部分
            //所以我们要做的就是给content一个新的LayoutParams，Match_Parent那种，也就是下面的contentParams
            ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
            ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            root.removeView(mContent);
            this.addView(mContent,contentParams);
            root.addView(this, layoutParams);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            int action = ev.getAction();
            //dispatch_flag = super.dispatchTouchEvent(ev);
            LogTool.i("layout_dispatch_flag", String.valueOf(dispatch_flag));
            return super.dispatchTouchEvent(ev);
        }
        //触摸事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (mTracker == null) {
                mTracker = VelocityTracker.obtain();
            }
            mTracker.addMovement(event);
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    layout_downX = event.getX();
                    layout_downY = event.getY();
                    layout_moveX = layout_downX;
                    layout_moveY = layout_downY;
                    layout_lastX = layout_downX;
                    isLayoutDispatch = true;
                    LogTool.i("layout-onTouch_DOWN", layout_downX + "-" + layout_downY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    layout_moveY = event.getY();
                    layout_moveX = event.getX();
                    float dx = layout_moveX - layout_lastX;
                    if (mContent.getX() + dx < 0) {
                        mContent.setX(0);
                    } else {
                        mContent.setX(mContent.getX() + dx);
                    }
                    layout_lastX = layout_moveX;
                    isLayoutDispatch = true;
                    LogTool.i("layout-onTouch_MOVE",layout_moveX+ "-"+layout_moveY+"dx="+dx);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    isLayoutDispatch = false;
                    mTracker.computeCurrentVelocity(1000);
                    float vx = mTracker.getXVelocity();
                    //先判断移动速率决定是否结束
                    if (vx > screenWidth * 2.5){
                        LogTool.i("vx",vx+"");
                        if (vx > 0){
                            autoMoveFinish(vx);

                        }else {
                            autoMoveBack(vx);
                        }
                    }else {
                        //然后判断位置是否超过屏幕的二分之一决定是否结结束
                        if (mContent.getX() > screenWidth/2){
                            autoMoveFinish(0);
                        }else {
                            autoMoveBack(0);
                        }
                    }
                    LogTool.i("vx",vx+"");

                    if (mTracker != null){
                        mTracker.clear();
                        mTracker.recycle();
                        mTracker = null;
                    }
                    LogTool.i("layout-onTouch_CANCEL","");
                    break;
            }
            boolean flag = super.onTouchEvent(event);
            LogTool.i("layout_touch_flag", String.valueOf(flag));
            return flag;
        }
        //自动移动回原始位置 动画
        private void autoMoveBack(float vx){
            final float currentX = mContent.getX();
            TranslateAnimation animation = new TranslateAnimation(0,
                    -currentX,0,0);
            animation.setDuration(vx == 0?200:100);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContent.clearAnimation();
                    mContent.setX(0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setInterpolator(new DecelerateInterpolator());
            mContent.startAnimation(animation);
        }
        //自动移动至结束 动画
        private void autoMoveFinish(float vx){
            final float currentX = mContent.getX();
            TranslateAnimation animation = new TranslateAnimation(0,
                    screenWidth-currentX,0,0);
            animation.setDuration(vx == 0?200:100);
            long duration = (int)(200);
            animation.setDuration(duration);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContent.clearAnimation();
                    mContent.setX(screenWidth);
                    mActivity.finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setInterpolator(new DecelerateInterpolator());
            mContent.startAnimation(animation);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            int action = ev.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    layout_downX = ev.getX();
                    layout_downY = ev.getY();
                    layout_moveX = layout_downX;
                    layout_moveY = layout_downY;
                    layout_lastX = layout_downX;
                    LogTool.i("layout-Intercept_DOWN", layout_downX + "-" + layout_downY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    layout_moveY = ev.getY();
                    layout_moveX = ev.getX();
                    if (!dispatch_flag){
                        return true;
                    }
                    layout_lastX = layout_moveX;
                    LogTool.i("layout-Intercept_MOVE",layout_moveX+ "-"+layout_moveY);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    LogTool.i("layout-Intercept_CANCE","");
                    break;
            }
            intercept_flag = super.onInterceptTouchEvent(ev);
            LogTool.i("layout_Intercept_flag",String.valueOf(intercept_flag));
            return intercept_flag;
        }
    }

}
