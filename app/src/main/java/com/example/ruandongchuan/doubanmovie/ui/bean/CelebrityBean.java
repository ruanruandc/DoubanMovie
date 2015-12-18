package com.example.ruandongchuan.doubanmovie.ui.bean;

import java.util.ArrayList;

/**
 * Created by ruandongchuan on 15-11-12.
 */
public class CelebrityBean extends BaseBean{
    private String mobile_url;
    private String[] aka_en;
    private String name;
    private String name_en;
    private String gender;
    private InTheaterBean.SubjectBean.CastBean.AvatarBean avatars;
    private String alt;
    private String born_place;
    private String[] aka;
    private ArrayList<WorkItemBean> works;
    private String id;

    public ArrayList<WorkItemBean> getWorks() {
        return works;
    }

    public void setWorks(ArrayList<WorkItemBean> works) {
        this.works = works;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String[] getAka_en() {
        return aka_en;
    }

    public void setAka_en(String[] aka_en) {
        this.aka_en = aka_en;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public InTheaterBean.SubjectBean.CastBean.AvatarBean getAvatars() {
        return avatars;
    }

    public void setAvatars(InTheaterBean.SubjectBean.CastBean.AvatarBean avatars) {
        this.avatars = avatars;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getBorn_place() {
        return born_place;
    }

    public void setBorn_place(String born_place) {
        this.born_place = born_place;
    }

    public String[] getAka() {
        return aka;
    }

    public void setAka(String[] aka) {
        this.aka = aka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class WorkItemBean extends BaseBean{
        private String[] roles;
        private InTheaterBean.SubjectBean subject;

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }

        public InTheaterBean.SubjectBean getSubject() {
            return subject;
        }

        public void setSubject(InTheaterBean.SubjectBean subject) {
            this.subject = subject;
        }
    }
}
