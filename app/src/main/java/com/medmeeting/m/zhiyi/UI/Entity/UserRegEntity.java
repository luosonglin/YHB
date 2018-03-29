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
    private String confirmPassword;
    private String password;
    private String phone;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
