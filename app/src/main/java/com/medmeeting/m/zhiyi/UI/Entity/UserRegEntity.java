package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/03/2018 10:32 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserRegEntity {

    /**
     * code : string
     * confirmPassword : string
     * password : string
     * phone : string
     */

    private String code;
    private String password;
    private String phone;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
