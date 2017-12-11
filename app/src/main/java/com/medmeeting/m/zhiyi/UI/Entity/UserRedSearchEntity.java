package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 11/12/2017 10:39 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class UserRedSearchEntity {

    /**
     * pageNum : 0
     * pageSize : 0
     * userName : 崧麟
     */

    private int pageNum;
    private int pageSize;
    private String userName;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
