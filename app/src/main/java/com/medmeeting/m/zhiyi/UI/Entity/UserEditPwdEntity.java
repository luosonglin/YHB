package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/03/2018 12:59 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserEditPwdEntity {


    /**
     * code : string
     * confirmPassword : string
     * password : string
     */

    private String code;
    private String confirmPassword;
    private String password;

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
}
