package com.example.ruandongchuan.doubanmovie.ui.bean;

/**
 * Created by ruandongchuan on 15-12-10.
 */
public class ReviewBean extends BaseBean {
    private String name;
    private String img;
    private String title;
    private String time;
    private String rating;
    private String useful;
    private String review;
    private String link;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }
}
