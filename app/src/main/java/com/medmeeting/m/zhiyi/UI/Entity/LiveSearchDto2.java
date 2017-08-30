package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * CURL -d 参数为空时使用的实体
 */
public class LiveSearchDto2 {
    private Integer pageNum;


    public LiveSearchDto2() {
    }

    public LiveSearchDto2(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
