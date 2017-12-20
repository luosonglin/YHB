package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/11/2017 11:52 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class AdminEventActive {

    /**
     * id : 20
     * typeId : 20
     * createDate : 1500837526000
     * title : 四
     * banner : http://on5q2lha8.bkt.clouddn.com/FpLK2HgFO2un-f4sN78CSrmmCcQI
     * url :  tttttttttttt
     * type : active
     * sort : 2
     * pushPlace : HOME
     * sourceType : admin
     */

    private int id;
    private int typeId;
    private long createDate;
    private String title;
    private String banner;
    private String url;
    private String type;
    private int sort;
    private String pushPlace;
    private String sourceType;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getPushPlace() {
        return pushPlace;
    }

    public void setPushPlace(String pushPlace) {
        this.pushPlace = pushPlace;
    }
}
