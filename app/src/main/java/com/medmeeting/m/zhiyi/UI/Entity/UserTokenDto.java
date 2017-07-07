package com.medmeeting.m.zhiyi.UI.Entity;

public class UserTokenDto {

    /**
     * accessToken : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6IuadvuaelyIsInVzZXJJZCI6IjciLCJjcmVhdGVkIjoxNDk5NDE4MzY5ODQ2LCJleHAiOjE0OTc3MTU0MDI1NTAsImlzcyI6ImhlYWxpZmUiLCJhdWQiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiJ9.1d0bnkTHWtOLhsDax-gKoSXK0QcEG_Uv_GjtVaHdGTI
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
