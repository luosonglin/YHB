package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/11/2017 1:31 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class AccessToken {

    /**
     * accessToken : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6IuadvuaelyIsInVzZXJJZCI6IjciLCJyb2xlIjoiIiwiY3JlYXRlZCI6MTUxMTMyNzE0NTM2NiwiZXhwIjoxNTA5NjI0MTc4MDcwLCJpc3MiOiJoZWFsaWZlIiwiYXVkIjoiMDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjYifQ.HM7GK4EZV7HzIvh0LjBETpcqqC-XY3TXwPyfScqtRH0
     * tokenType : bearer
     * expiresIn : 2592000
     */

    private String accessToken;
    private String tokenType;
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
