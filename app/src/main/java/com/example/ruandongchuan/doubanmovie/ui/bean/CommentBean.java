package com.example.ruandongchuan.doubanmovie.ui.bean;

/**
 * Created by ruandongchuan on 15-12-9.
 */
public class CommentBean extends BaseBean{
    private String id;
    private String title;
    private String img;
    private String comment;
    private String comment_vote;
    private String time;
    private String rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_vote() {
        return comment_vote;
    }

    public void setComment_vote(String comment_vote) {
        this.comment_vote = comment_vote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
