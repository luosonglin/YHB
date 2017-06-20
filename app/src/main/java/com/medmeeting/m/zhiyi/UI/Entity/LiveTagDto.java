package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * 直播标签API
 */
public class LiveTagDto {

    /**
     * id : 1
     * labelName : 外科
     * searchCount : 0
     * sort : 0
     * useCount : 0
     */

    private int id;
    private String labelName;
    private int searchCount;
    private int sort;
    private int useCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
}
