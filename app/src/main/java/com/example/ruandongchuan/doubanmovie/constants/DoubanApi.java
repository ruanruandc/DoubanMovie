package com.example.ruandongchuan.doubanmovie.constants;

/**
 * Created by ruandongchuan on 15-11-10.
 */
public class DoubanApi {
    public static final String HTTP_HOST = "https://api.douban.com";
    public static final String BAIDU_API = "http://api.map.baidu.com/geocoder/v2/";

    public static final String APIKEY = "0b2933ef8c6011b5217b9bcea520351f";
    public static final String SERCET = "317feedcc3289fab";
    public static final String BAIDU_KEY = "VI40PNphGYC4yEQniUkQxAIs";
    public static String ACCESS_TOKEN = "";
    public static boolean isAuthed = false;
    public static void authComplete(String token){
        ACCESS_TOKEN = token;
        isAuthed = true;
    }
    public static void authClear(){
        ACCESS_TOKEN = "";
        isAuthed = false;
    }

    public static final String HTTP_IN_THEATERS = HTTP_HOST + "/v2/movie/in_theaters";
    public static final String HTTP_COMMING_SOON = HTTP_HOST + "/v2/movie/coming_soon";
    public static final String HTTP_SUBJECT = HTTP_HOST + "/v2/movie/subject/";
    public static final String HTTP_CELEBRITY = HTTP_HOST + "/v2/movie/celebrity/";
    public static final String HTTP_AUTH = "https://www.douban.com/service/auth2/auth?" +
            "client_id="+ APIKEY +"&" +
            "redirect_uri=http://baidu.com&" +
            "response_type=code&" +
            "scope=movie_basic_r,douban_basic_common,community_basic_photo,community_basic_user";
    public static final String HTTP_ACCESS_TOKEN = "https://www.douban.com/service/auth2/token";
    public static final String HTTP_TOP250 = HTTP_HOST + "/v2/movie/top250";
    public static final String HTTP_SEARCH = HTTP_HOST + "/v2/movie/search";
    public static final String HTTP_USBOX = HTTP_HOST + "/v2/movie/us_box";
    public static final String HTTP_USERINFO = HTTP_HOST + "/v2/user/~me";
    public static final String HTTP_COMMENT = "https://movie.douban.com/subject/";
    //正在上映
    public static final int GET_IN_THEATERS = 100;
    //即将上映
    public static final int GET_COMMING_SOON = 101;
    //电影条目信息
    public static final int GET_SUBJECT = 102;
    //获取Token
    public static final int POST_ACCESS_TOKEN = 103;
    //获取任务详情
    public static final int GET_CELEBRITY = 104;
    //获取Top250
    public static final int GET_TOP250 = 105;
    //获取搜索页面
    public static final int GET_SEARCH = 106;
    //获取北美票房榜
    public static final int GET_US_BOX = 107;
    //获取城市信息
    public static final int GET_GEOCODING = 108;
    //获取用户信息
    public static final int GET_USERINFO = 109;
    //获取短评
    public static final int GET_COMMENT = 110;
    //获取长评
    public static final int GET_REVIEW = 111;
    //获取长评详情
    public static final int GET_DETAIL_REVIEW = 112;
}
