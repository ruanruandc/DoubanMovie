package com.example.ruandongchuan.doubanmovie.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ruandongchuan on 15-11-11.
 */
public class MovieBean extends BaseBean implements Serializable {
    private int reviews_count;
    private int wish_count;
    private int collect_count;
    private String douban_site;
    private String year;
    private InTheaterBean.SubjectBean.ImagesBean images;
    private String alt;
    private String id;
    private String mobile_url;
    private String title;
          //  "do_count": null,
           // "seasons_count": null,
    private String schedule_url;
     //       "episodes_count": null,
    private String[] genres;
    private String[] countries;
    private ArrayList<InTheaterBean.SubjectBean.CastBean> casts;
        //    "current_season": null,
    private String original_title;
    private String summary;
    private String subtype;
    private ArrayList<InTheaterBean.SubjectBean.CastBean> directors;
    private int comments_count;
    private int ratings_count;

    public ArrayList<InTheaterBean.SubjectBean.CastBean> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<InTheaterBean.SubjectBean.CastBean> directors) {
        this.directors = directors;
    }

    public ArrayList<InTheaterBean.SubjectBean.CastBean> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<InTheaterBean.SubjectBean.CastBean> casts) {
        this.casts = casts;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    public InTheaterBean.SubjectBean.ImagesBean getImages() {
        return images;
    }

    public void setImages(InTheaterBean.SubjectBean.ImagesBean images) {
        this.images = images;
    }


    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getDouban_site() {
        return douban_site;
    }

    public void setDouban_site(String douban_site) {
        this.douban_site = douban_site;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }
}
