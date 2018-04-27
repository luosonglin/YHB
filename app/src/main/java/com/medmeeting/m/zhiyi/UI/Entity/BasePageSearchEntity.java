package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 25/10/2017 3:57 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BasePageSearchEntity {

    /**
     * pageNum : 0
     * pageSize : 15
     */


    private int pageNum;
    private int pageSize;

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
}
