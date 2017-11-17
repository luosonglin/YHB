package com.medmeeting.m.zhiyi.UI.Entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 16/11/2017 7:44 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveLabel extends MultiItemEntity implements Serializable {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;

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

    public LiveLabel(int id, String labelName) {
        this(TYPE_MY_CHANNEL, id, labelName);
    }

    public LiveLabel(int type, int id, String labelName) {
        this.id = id;
        this.labelName = labelName;
        itemType = type;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
