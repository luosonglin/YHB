package com.medmeeting.m.zhiyi.UI.Entity;

public class TagDto {

    /**
     * id : 2
     * labelName : 内分泌
     * useCount : 3
     * searchCount : 56
     * sort : 2
     */

    private int id;
    private String labelName;
    private int useCount;
    private int searchCount;
    private int sort;
    private String iconUrl;
    private String labelType;

    public TagDto(String labelName, String iconUrl) {
        this.labelName = labelName;
        this.iconUrl = iconUrl;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

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

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
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
}
