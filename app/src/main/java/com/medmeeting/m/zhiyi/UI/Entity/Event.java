package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/11/2017 1:06 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class Event {

    /**
     * id : 288
     * title : 宣武医院宣武医院
     * eventType : 5
     * subject : 5
     * eventDesc : 5
     * startDate : 5
     * endDate : 5
     * address : 5
     * contact : 5
     * contactPhone : 5
     * templateId : 1
     * status : 5
     * stateDate : 5
     * userId : 1
     * checkinDate : 5
     * registerEndDate : 5
     * creditType : 5
     * creditNum : 1
     * banner : http://on5q2lha8.bkt.clouddn.com/FkfWsmvSBfqmsvma6BqLp-bCu9r5
     * footContent : 5
     * hot : 5
     * news : 5
     * groom : 5
     * province : 1
     * city : 1
     * area : 2
     * hotOrder : 3
     * eventLogo : 5
     * onlineEvent : 5
     * sourceType :主办方创建SPONSOR,运营端创建ADMIN ,
     * eventProvince
     * eventCity
     */

    private int id;
    private String title;
    private String eventType;
    private String subject;
    private String eventDesc;
    private String startDate;
    private String endDate;
    private String address;
    private String contact;
    private String contactPhone;
    private int templateId;
    private String status;
    private String stateDate;
    private int userId;
    private String checkinDate;
    private String registerEndDate;
    private String creditType;
    private int creditNum;
    private String banner;
    private String footContent;
    private String hot;
    private String news;
    private String groom;
    private int province;
    private int city;
    private int area;
    private int hotOrder;
    private String eventLogo;
    private String onlineEvent;
    private String sourceType; //主办方创建SPONSOR 微站,  运营端创建ADMIN 新闻
    private String eventProvince;
    private String eventCity;

    public String getEventProvince() {
        return eventProvince;
    }

    public void setEventProvince(String eventProvince) {
        this.eventProvince = eventProvince;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStateDate() {
        return stateDate;
    }

    public void setStateDate(String stateDate) {
        this.stateDate = stateDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getRegisterEndDate() {
        return registerEndDate;
    }

    public void setRegisterEndDate(String registerEndDate) {
        this.registerEndDate = registerEndDate;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public int getCreditNum() {
        return creditNum;
    }

    public void setCreditNum(int creditNum) {
        this.creditNum = creditNum;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getFootContent() {
        return footContent;
    }

    public void setFootContent(String footContent) {
        this.footContent = footContent;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getGroom() {
        return groom;
    }

    public void setGroom(String groom) {
        this.groom = groom;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getHotOrder() {
        return hotOrder;
    }

    public void setHotOrder(int hotOrder) {
        this.hotOrder = hotOrder;
    }

    public String getEventLogo() {
        return eventLogo;
    }

    public void setEventLogo(String eventLogo) {
        this.eventLogo = eventLogo;
    }

    public String getOnlineEvent() {
        return onlineEvent;
    }

    public void setOnlineEvent(String onlineEvent) {
        this.onlineEvent = onlineEvent;
    }
}
