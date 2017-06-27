package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * 会议，分页
 */
public class PageInfoBean<T> {
    private boolean pageEnd;
    private int pages;
    private int total;
    private List<T> list;

    public boolean isPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(boolean pageEnd) {
        this.pageEnd = pageEnd;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
