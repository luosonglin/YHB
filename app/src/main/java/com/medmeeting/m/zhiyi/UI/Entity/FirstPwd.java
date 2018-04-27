package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 12/04/2018 3:52 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class FirstPwd {

    public FirstPwd(String password) {
        this.password = password;
    }

    /**
     * password : 123
     */

    private String password;


    private String inviteNumber;
    private String platformType="android";

    public String getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(String inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getPwd() {
        return password;
    }

    public void setPwd(String password) {
        this.password = password;
    }
}
