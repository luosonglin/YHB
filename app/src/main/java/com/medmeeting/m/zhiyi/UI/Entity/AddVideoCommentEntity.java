package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/10/2017 2:05 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class AddVideoCommentEntity {

    public AddVideoCommentEntity(String des) {
        this.des = des;
    }

    /**
     * des : string
     */

    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
