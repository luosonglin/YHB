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

    //会议订单页：
    public static final String URL_Meeting_Order = "http://mobile.medmeeting.com/#/wv/meeting/order";

    //消息
    public static final String URL_Message_Detail = "http://mobile.medmeeting.com/#/wv/message/";

    /**
     * 微信
     */
    //appID
//    public static final String WeChat_AppID = "wx7e6722fad8a0975c";
    public static final String WeChat_AppID = "wx3c3cd65767f5a563";
    //app secret
    public static final String WeChat_AppSecret = "ec0e6a8b17dc37681fd49161c4d69350";

    /**
     * 支付宝
     */
//    // 商户PID
//    public static final String PARTNER = "2088311846356487";
//    // 商户收款账号
//    public static final String SELLER = "zhifubao@healife.com";
//    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIoUMNT6sAOC05BU72JoQWbhmJhe9n917DtUrT8kU0nFGzeV+tmSSy+3bsbSbKUg9ngVpyXsfn4ouH33Ktx42H8TAHs3n39qaSAePKgB3o6pOV0vYnpVqnYB1UlecW/vbxv8XcvmcEOS1gE3OwcFh6NTzdgbr+rb+mLbAGlINfWNAgMBAAECgYBDY4FFoKegvwvkCB/g5kLtJDMmQkqJgJLvje8TvvXLLiCPa2pHH2gEfMDa1j3iBYlkqCSwlJBToCoSiDvp6Cy4cRtMKbTSNx00bhLWzpuropvSAH9EIsOJe+rCpOZox+DcIUOFS3TkXkXOKaAW9F2Onqr1nfK+1C9nXMPePOyLnQJBAPnuKnBgAYSxD8OeO2AIjF5iO93Ap1f9q8/L4bxcsw1WfKJlolFKxWC3nnuOrL9qe1DcKOPnKaJHA9XdFH7VNG8CQQCNbqL8HOh/9LhPe8TgwfUl4Bjz0WjQGq3qIQscy0o6JfkAHz+Po2zl+kTugtUguWnlhpFli/fT2d9yaVDj9cvDAkB5DuN/iwExRJJeLkaUPY/AJ9TXlHl6JWUTQa4VjtErpLi58ICu34i7UDVzo6gJD4qrn/gua8m+0KcK8Ar9ZEgBAkBFgan33P0mZU5vQZRwIOIpywh4SuIH5BS0i6i6be38xcypkrHaFabfHy/hR8sWWgkBFDFAhpk1NE3sHHX0kkehAkAud+wmYHchvQ2ME9yxNl5+LHsVRbEOscJIdbPTO95MrBufBfyFyIJ79SQvj/lb+ueEfyr+QUkJU5UxEH8rhhFM";
//    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKFDDU+rADgtOQVO9iaEFm4ZiYXvZ/dew7VK0/JFNJxRs3lfrZkksvt27G0mylIPZ4Facl7H5+KLh99yrceNh/EwB7N59/amkgHjyoAd6OqTldL2J6Vap2AdVJXnFv728b/F3L5nBDktYBNzsHBYejU83YG6/q2/pi2wBpSDX1jQIDAQAB";
//    private static final int SDK_PAY_FLAG = 1;
    // 商户PID
    public static final String PARTNER = "2088701741406183";
    // 商户收款账号
    public static final String SELLER = "pay@healife.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPrxYIZSe3irNL9ANd8MP5tUEro9Nglf97VTrsMJVRVRwgRfqZXbv0qBeU2l7CStywT4k/uEXrsuZrDWYFIW/UUROcR0PsVRpNa1llvO52Tk/Ug9sBLIf06ww/RIbV9hGhKr5wlFvjUWWEPkFmxfGW+Ji4eaeZzIRIOwJ5zQ43HSPlonjBtHWQGKiBSlSuatIASHQ1kiYC+uuWpaUMUn/+iKPr7B4lJsmsFy+1h2DOtWv2cAOn8P7oJdiJaBnJvK1Bonq61+jVbS/Ph9LAzgO+YvpZOECNqtV8Ak32WheL2ycWatj9ujL3sXmxlj1N8kpNTxJQaoKWd3Kz4TnuNkKHAgMBAAECggEATxkFug1ltebHGXL1P4vBdv/PJqUDQlKQ2GZtD4UXTjTaTPH5KOCvDdpTUQB91du8bdr7JXbeFwjfS+OOXBiyJWGUuB2LSNj4p9IG3rkJVVt7GvSTZawMFEoo7dNdnv/daKnNONCTXSp8Bjb3bgpeGswz1qoyF7q8v76seDzoLHImS9jYgpFygKQKUsfWOQ/JYvrrye9d+AZQNPMIq7s4lugPbSGozs69Oa9WmL4sYqwk3Pv6zvaFs28ZbEuW8iCHKggsSOjL0ANSz6ApDstzfnIi0aFgbVy1OzyCpz0L6tcO2cJJUwpuZz+yk0m2Ldqu0MQ7MI9YRlqmTJKK3F/ewQKBgQD3HC10Slz7WpqVrLuQnM7qWPffN5FtDkgm86h3Aa0oOTnY70MLUFyk8/NuR9DSFS3MX2o9wAePQutsCFkyhzP6qo0vh0mtI1XPalMIJo3QJWl5Q29DbWz7c7IpTiXJ2sKCzmLucS4WI1Lm7AWkPqkyhq97UUBFbOnSEjrFuu/2dwKBgQCU2mEh9dEBbLjKgTXDmBZcJVx7smksI18/6LeSyAvJ8LNhNZ4IdDSmdGn5FkKBfgVrf8ygHxt001vo3kn848RdFSL1OMU4U5iu893AENEjFFRc/3JDpv7sJ8cDAsbD9t5qkrLJvjvAD+qH6IQyxUj5fsO4vQsKRhmUQxJ/ZA9IcQKBgCnQuNhe5REbqeD+E9dK0bDSUU+ShadgrVD6Fl3dCMIvvNw60KuPc73uEhltSabjMpbM5puAbUcO2eu0FfP48RDP34hVamBEBHdO1LIiZAj+67Q7dw5/69s5HsLimH5p6FRePFuBB31ziMnO0r2SZOtJ41QI0cltvrtvnkZrdcPNAoGANkW1OqOc0+8nJTadOrZ+GJTKtpMjQ6OV/ABGxm7H1ZY8YQoil6R++d3iMZ6LGxmusjZDjnIpGZxim3JI+bq2AtZNC6nqRpI8TjW+OWQoTKkB5XKB3+bIdaQ/LHrbq+Gb0LGyHFqPAIUNFwaRBM3nwIgWponOt609cFBQM0BxThECgYEA0hTPCLwIeskLPtxDUFa+Z9ds+ul+CdLzlQ7mShCUAwOqkvh/bGvhMnXyx0mqzr3XgSy7yUjRJ6ABHjIt4z6jCcHblaaYQD8E66X/Z4Rdza6ZcvEy+FzDe+NRUJeQsbr4a9vHf9GfEBxI5VQXsdgqhMiDkgrKHOWcYm9n+3aMvjM=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj68WCGUnt4qzS/QDXfDD+bVBK6PTYJX/e1U67DCVUVUcIEX6mV279KgXlNpewkrcsE+JP7hF67Lmaw1mBSFv1FETnEdD7FUaTWtZZbzudk5P1IPbASyH9OsMP0SG1fYRoSq+cJRb41FlhD5BZsXxlviYuHmnmcyESDsCec0ONx0j5aJ4wbR1kBi";


    //web browser界面传递数据
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    String TITLE_SELECTED = "explore_title_selected";
    String TITLE_UNSELECTED = "explore_title_unselected";

    /**
     * 分享
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

    //新闻正文
    public static final String URL_BLOG_CONTENT = "http://mobile.medmeeting.com/#/new/article/android/";
}
