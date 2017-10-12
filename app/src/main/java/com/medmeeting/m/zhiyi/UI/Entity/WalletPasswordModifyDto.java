package com.medmeeting.m.zhiyi.UI.Entity;

public class WalletPasswordModifyDto {


    /**
     * newPwd : string
     * oldPwd : string
     */


    private String newPwd;
    private String oldPwd;


    public WalletPasswordModifyDto(String newPwd, String oldPwd) {
        this.newPwd = newPwd;
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }
}
