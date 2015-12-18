package com.example.ruandongchuan.doubanmovie.ui.bean;

import java.io.Serializable;

/**
 * Created by ruandongchuan on 15-11-12.
 */
public class BaseBean implements Serializable{
    protected int view_type;

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }
}
