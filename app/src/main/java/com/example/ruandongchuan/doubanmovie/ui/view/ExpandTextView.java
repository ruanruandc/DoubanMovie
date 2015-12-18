package com.example.ruandongchuan.doubanmovie.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.ruandongchuan.doubanmovie.util.LogTool;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class ExpandTextView extends TextView implements View.OnClickListener{
    private int maxLines;
    private boolean isExpand = false;

    public ExpandTextView(Context context) {
        super(context);
        //open();
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //open();
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        open();
    }
    public void open(){
        setEllipsize(TextUtils.TruncateAt.END);
        maxLines = getMaxLines();
        LogTool.i("max", maxLines + "");
        if (maxLines > 4){

        }
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isExpand){
            setMaxLines(maxLines);
        }else {
            setMaxLines(Integer.MAX_VALUE);
        }
        isExpand = !isExpand;
        invalidate();
        requestLayout();
    }
}
