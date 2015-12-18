package com.example.ruandongchuan.doubanmovie.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.ruandongchuan.doubanmovie.util.LogTool;

/**
 * Created by ruandongchuan on 15-11-20.
 */
public class ZoomImageView extends ImageView{
    //模板矩阵 用于初始化
    private Matrix mMatrix;
    //当前矩阵
    private Matrix mCurrentMatrix;
    //拖动模式
    private static final int MODE_DRAG = 1;
    //缩放模式
    private static final int MODE_ZOOM = 2;
    //不可用模式
    private static final int MODE_UNABLE = 3;
    //当前模式
    private int mMode;
    //开始时手指两点之间的距离
    private float mStartDis;
    //按下的坐标
    private PointF down;
    //图片的宽度
    private float mImageWidth;
    //图片的高度
    private float mImageheight;

    private boolean isZoomToDrag = false;

    private GestureDetector mGestureDetector;
    private OnDoubleClickListener onDoubleClickListener;
    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mGestureDetector = new GestureDetector(getContext(),new DoubleClick(onDoubleClickListener));
        mMatrix = new Matrix();
        mCurrentMatrix = new Matrix();
        down = new PointF();
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mMatrix.set(getImageMatrix());
        mCurrentMatrix.set(getImageMatrix());
        mImageWidth = bm.getWidth();
        mImageheight = bm.getHeight();
        LogTool.i("bitmap", bm.getWidth() + "-" + bm.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction()&event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //设置拖动模式
                mMode = MODE_DRAG;
                isMatrixEnabled();
                down.set(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //复位
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_DRAG){
                    LogTool.i("mode","拖动模式");
                    if (isZoomToDrag){
                        down.set(event.getX(),event.getY());
                        isZoomToDrag = false;
                    }
                    setDragMatrix(event);

                }else if (mMode == MODE_ZOOM){
                    LogTool.i("mode","缩放模式"+distance(event));
                    setZoomMatrix(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mMode == MODE_UNABLE)
                    return true;
                mMode = MODE_ZOOM;
                mStartDis = distance(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mMode = MODE_DRAG;
                isZoomToDrag = true;
                LogTool.i("ACTION_POINTER_UP",event.findPointerIndex(event.getPointerId(1))+"-"+event.getX(1)+event.getY(1));
                break;
            default:
                break;
        }
        LogTool.i("mode", mMode + "");
        return mGestureDetector.onTouchEvent(event);
    }
    //拖动操作
    private void setDragMatrix(MotionEvent event){
        if (isZoomed()){
            float dx = event.getX() - down.x;
            float dy = event.getY() - down.y;
            if (Math.sqrt(dx*dx + dy*dy)> 10){
                mCurrentMatrix.set(getImageMatrix());
                float[] values = new float[9];
                mCurrentMatrix.getValues(values);
                mCurrentMatrix.postTranslate(dx, dy);
                setImageMatrix(mCurrentMatrix);
                down.x = event.getX();
                down.y = event.getY();
            }
        }
    }
    //计算偏移量dx
    private float calculateDx(float[] values,float dx){

        float width = getWidth();
        LogTool.i("width",values[Matrix.MSCALE_X]+"-"+mImageWidth);
        if (values[Matrix.MSCALE_X]*mImageWidth<width){
            return 0;
        }
        return dx;
    }

    private float calculateDy(float[] values,float dy){
        float height = getHeight();
        if (values[Matrix.MSCALE_Y]*mImageheight<height){
            return 0;
        }
        return dy;
    }

    //是否缩放过
    private boolean isZoomed(){
        float[] values = new float[9];
        mCurrentMatrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];
        mMatrix.getValues(values);
        LogTool.i("scale=",scale+"-"+values[Matrix.MSCALE_X]);
        return scale != values[Matrix.MSCALE_X];
    }
    //设置缩放
    private void setZoomMatrix(MotionEvent event){
        if (event.getPointerCount() <2 )
            return;
        float currentDis = distance(event);
        if (currentDis > 10f){
            float scale = currentDis/mStartDis;
            LogTool.i("scale",scale+"");
            mCurrentMatrix.set(getImageMatrix());
            mStartDis = currentDis;
            mCurrentMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mCurrentMatrix);
        }
    }
    private void doubleClick(){
        float scale = isZoomed()?1:5;
        setScaleType(isZoomed() ? ScaleType.FIT_CENTER : ScaleType.MATRIX);
        mCurrentMatrix.set(mMatrix);
        LogTool.i("doubleclick=", String.valueOf(isZoomed()) + getScaleType() + "");
        mCurrentMatrix.postScale(scale, scale);
        setImageMatrix(mCurrentMatrix);

    }
    //计算两点距离
    private float distance(MotionEvent event){
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
    //是否支持Matrix
    private void isMatrixEnabled(){
        if (getScaleType() != ScaleType.CENTER){
            setScaleType(ScaleType.MATRIX);
        }else {
            mMode = MODE_UNABLE;
        }
    }


    class DoubleClick extends GestureDetector.SimpleOnGestureListener{
        private final OnDoubleClickListener onDoubleClick;
        public DoubleClick(OnDoubleClickListener onDoubleClick){
            this.onDoubleClick = onDoubleClick;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogTool.i("here","double");
            doubleClick();
            if (onDoubleClick!=null) {

                onDoubleClick.onDoubleClick();
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
    public interface OnDoubleClickListener{
        void onDoubleClick();
    }
}
