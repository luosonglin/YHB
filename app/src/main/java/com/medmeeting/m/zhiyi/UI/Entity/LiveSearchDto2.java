package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * CURL -d 参数为空时使用的实体
 */
public class LiveSearchDto2 {
    private Integer pageSize;


    public LiveSearchDto2() {
    }

    public LiveSearchDto2(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
