package com.medmeeting.m.zhiyi.Constant;

/**
 * Created by Administrator on 2016/7/1.
 */
public class Constant {

    public static String ERROR_TITLE = "网络连接异常";
    public static String ERROR_CONTEXT = "请检查网络后重试";
    public static String ERROR_BUTTON = "重试";

    public static String EMPTY_TITLE = "没有找到数据";
    public static String EMPTY_CONTEXT = "换个条件试试吧";

    public static String EMPTY_TITLE2 = "没有数据";
    public static String EMPTY_CONTEXT2 = "输入条件试试吧";

    //服务器路径
    public static final String API_SERVER = "http://medmeeting.com:8080";
    public static final String API_SERVER_MEETING = "http://www.medmeeting.com";
    public static final String API_SERVER_LIVE_TEST = "http://106.14.0.72:8088";
    public static final String API_SERVER_LIVE = "http://api.medmeeting.com";

    public static final String FIRST_OPEN = "first_open";

    //会议
    public static final String URL_microWebsiteDetail = "http://mobile.medmeeting.com/?#/mw/"; //微站
    public static final String URL_Meeting_Detail = "http://mobile.medmeeting.com/?#/wv/meeting/detail/";
    public static final String URL_Meeting_Enrol = "http://mobile.medmeeting.com/?#/m/reg/sign/";

//    public static final String URL_microWebsiteDetail = "http://192.168.199.147/#/mw/";
//    public static final String URL_Meeting_Detail = "http://192.168.199.147/#/wv/meeting/detail/";
//    public static final String URL_Meeting_Enrol = "http://192.168.199.147/#/m/reg/sign/";

    //消息
    public static final String URL_Message_Detail = "http://mobile.medmeeting.com/#/wv/message/";

    //微信appID
    public static final String WeChat_AppID = "wx7e6722fad8a0975c";

    //web browser界面传递数据
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    String TITLE_SELECTED = "explore_title_selected";
    String TITLE_UNSELECTED = "explore_title_unselected";

    /**
     *  分享
     */
    //新闻视频分享页面：
    public static final String Share_News_Video = "http://mobile.medmeeting.com/?#/new/video/share/";

    //新闻文章分享页面：
    public static final String Share_News_Article = "http://mobile.medmeeting.com/?#/new/article/share/";

    //红V直播分享页面：
    public static final String Share_Live_Red_Vip = "http://mobile.medmeeting.com/?#/live/person/";
    //直播房间分享页面：
    public static final String Share_Live_Room = "http://mobile.medmeeting.com/?#/live/room/";
    //直播分享页面：
    public static final String Share_Live = "http://mobile.medmeeting.com/?#/live/show/share/";
    //回播分享页面：
    public static final String Share_Video = "http://mobile.medmeeting.com/?#/live/video/share/";

    //微站首页分享：
    public static final String Share_Meeting_Index = "http://mobile.medmeeting.com/?#/mw/";
}
