package com.example.ruandongchuan.doubanmovie.ui.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ruandongchuan on 15-11-17.
 */
public class UsBoxBean extends BaseBean {
    private String title;
    private String date;
    private ArrayList<SubjectsBean> subjects;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectsBean extends BaseBean{
        private int box;
        @Expose
        @SerializedName ("new")
        private boolean new_type;
        private int rank;
        private InTheaterBean.SubjectBean subject;

        public int getBox() {
            return box;
        }

        public void setBox(int box) {
            this.box = box;
        }

        public boolean isNew_type() {
            return new_type;
        }

        public void setNew_type(boolean new_type) {
            this.new_type = new_type;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public InTheaterBean.SubjectBean getSubject() {
            return subject;
        }

        public void setSubject(InTheaterBean.SubjectBean subject) {
            this.subject = subject;
        }
    }
}
