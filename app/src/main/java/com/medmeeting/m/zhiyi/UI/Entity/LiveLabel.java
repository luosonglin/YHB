package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 16/11/2017 7:44 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveLabel {

    /**
     * id : 14
     * labelName : 风湿免疫
     * useCount : 12
     * searchCount : 0
     * sort : 14
     * iconUrl : http://onmtzwa3g.bkt.clouddn.com/%E9%A3%8E%E6%B9%BF%E5%85%8D%E7%96%AB.png
     */

    private int id;
    private String labelName;
    private int useCount;
    private int searchCount;
    private int sort;
    private String iconUrl;

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
