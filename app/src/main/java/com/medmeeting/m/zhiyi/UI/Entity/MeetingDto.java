package com.medmeeting.m.zhiyi.UI.Entity;

public class MeetingDto {

    /**
     * address : 上海龙之梦万丽酒店
     * banner : 57cb691c-a4ae-4482-914d-ff04265dc1e7.jpg
     * endDate : 1512302400000
     * eventType : 3
     * hot :
     * id : 218
     * startDate : 1512086400000
     * title : 2017第二届全球精准医疗（中国）峰会
     */

    private String address;
    private String banner;
    private long endDate;
    private String eventType;
    private String hot;
    private int id;
    private long startDate;
    private String title;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
