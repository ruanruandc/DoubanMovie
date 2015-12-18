package com.example.ruandongchuan.doubanmovie.util;

import com.example.ruandongchuan.doubanmovie.ui.bean.CommentBean;
import com.example.ruandongchuan.doubanmovie.ui.bean.ReviewBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-12-9.
 */
public class HtmlParser {
    /**
     * 获取详细影评
     * @param src
     * @return
     */
    public static ReviewBean getReview(String src){
        Document doc = Jsoup.parse(src);
        ReviewBean bean = new ReviewBean();
        Element main_bd = doc.select("div.main-bd").first();
        bean.setReview(main_bd.html());
        return bean;
    }

    /**
     * 获取影评列表
     * @param src source
     * @return mData
     */
    public static List<ReviewBean> getReviewList(String src){
        List<ReviewBean> data = new ArrayList<>();
        Document doc = Jsoup.parse(src);
        Element content = doc.select("div.article").first();
        if (content == null){
            return data;
        }
        Elements reviews = content.select("div.review");
        for (Element review : reviews){
            ReviewBean bean = new ReviewBean();
            Element review_hd_avatar =  review.select("a.review-hd-avatar").first();
            bean.setName(review_hd_avatar.attr("title"));
            bean.setImg(review_hd_avatar.child(0).attr("src"));
            Element h3 = review.select("h3").first();
            Element a = h3.child(h3.children().size() - 1);
            bean.setTitle(a.text());
            bean.setLink(a.attr("href"));
            Element review_hd_info = review.select("div.review-hd-info").first();
            //review_hd_info.ownText()
            bean.setTime(review_hd_info.ownText());
            Element span = review_hd_info.select("span").first();
            String rating  = span.attr("class").substring(7);
            if (rating.equals("None0")){
                rating = "0";
            }
            bean.setRating(rating);
            Element review_short = review.select("div.review-short").first();
            Element review_short_span = review_short.select("span").first();
            bean.setReview(review_short_span.text());
            Element review_short_ft = review.select("div.review-short-ft").first();
            Element review_short_ft_span = review_short_ft.select("span").first();
            bean.setUseful(review_short_ft_span.text());
            data.add(bean);
        }
        return data;
    }

    /**
     * 获取短评的主体部分
     * @param src sourcce
     * @return body
     */
    public static String getCommentBody(String src){
        Document doc = Jsoup.parse(src);
        Element body = doc.select("div.article").first();
        if (body == null){
            return "";
        }
        return body.toString();
    }

    /**
     * 获取短评的标题
     * @param body body
     * @return title
     */
    public static String getTitle(String body){
        Document document = Jsoup.parse(body);
        Element title = document.select("span.fleft").first();
        return title.text();
    }

    /**
     * 获取短评评论列表
     * @param body body
     * @return comments
     */
    public static List<CommentBean> getComments(String body){
        List<CommentBean> data = new ArrayList<>();
        Document document = Jsoup.parse(body);
        Element list = document.select("div.mod-bd").first();
        if (list == null)
            return data;
        LogTool.i("div.mod-bd",list.toString());
        Elements comments = list.select("div.comment-item");
        LogTool.i("div.comment-item",comments.toString());
        if (comments == null){
            return data;
        }
        for (Element item : comments){
            CommentBean comment = new CommentBean();
            Element avatar = item.select("a").first();
            Element title = item.select("p").first();
            comment.setComment(title.text());
            Element img = avatar.select("img").first();
            comment.setImg(img.attr("src"));
            Element comment_info = item.select("span.comment-info").first();
            LogTool.i("span.comment-info",comment_info.toString());
            Elements span = comment_info.select("span");
            String rating = "";
            if (span.size() != 3){
                rating = "0";
            }else {
                LogTool.i("span", span.size() + "");
                rating = span.get(1).attr("class");
                rating = rating.substring(7,9);
                LogTool.i("span0", span.get(0).toString());
                LogTool.i("span1", span.get(1).toString()+"-"+rating);
                LogTool.i("span2", span.get(2).toString());
            }
            Element span_votes = item.select("span.votes").first();
            String vote = "";
            if (span_votes!=null){
                vote = item.select("span.votes").first().text();
            }
            String time = span.get(span.size()-1).text();
            comment.setTime(time);
            comment.setRating(rating);
            comment.setTitle(avatar.attr("title"));
            comment.setId(item.attr("mData-cid"));
            comment.setComment_vote(vote);
            data.add(comment);
        }
        return data;
    }
}
