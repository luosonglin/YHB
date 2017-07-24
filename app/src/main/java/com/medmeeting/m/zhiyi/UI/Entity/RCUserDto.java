package com.medmeeting.m.zhiyi.UI.Entity;

public class RCUserDto {

    /**
     * userId : 7
     * name : 罗崧麟
     * token : w/gOK/9lWKAOHuzYUnkMSTUz3xKqIEg3HLRSgu04W5/2WCNQB6XiQjQA+gO51BWs6POuJFmtWF0=
     * portraitUri : http://ww2.sinaimg.cn/mw1024/a601622bgw1f8awv4alasj20hs0bdmy6.jpg
     * createTime : 1496655304000
     */

    private String userId;
    private String name;
    private String token;
    private String portraitUri;
    private long createTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
