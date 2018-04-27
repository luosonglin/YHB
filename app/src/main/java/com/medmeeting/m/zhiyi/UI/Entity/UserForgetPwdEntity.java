package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/03/2018 1:35 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserForgetPwdEntity {


    /**
     * code : string
     * password : string
     * phone : string
     */

    private String code;
    private String password;
    private String phone;

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
