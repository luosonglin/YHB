package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 03/04/2018 10:04 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserOpenIdEntity {


    /**
     * qqOpenId : string
     * wxOpenId : string
     */

    private String qqOpenId;
    private String wxOpenId;

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
}
