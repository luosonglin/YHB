package com.medmeeting.m.zhiyi.UI.Entity;

public class WalletPasswordDto {

    /**
     * newPwd : string
     * verCode : string
     */

    private String newPwd;
    private String verCode;

    public WalletPasswordDto(String newPwd, String verCode) {
        this.newPwd = newPwd;
        this.verCode = verCode;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }
}
