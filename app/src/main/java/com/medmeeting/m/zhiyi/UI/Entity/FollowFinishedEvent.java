package com.medmeeting.m.zhiyi.UI.Entity;

public class FollowFinishedEvent {
    /**
     * deleteFollowTime : 1483927996000
     * eventAddress :
     * eventBanner : ../../img/modelImgs/banner.jpg
     * eventId : 74
     * eventName : 第三届儿科交流大会
     * eventStartTime : 1482033600000
     * followTime : 1483927998000
     * isFollow : Y
     * userId : 7
     */

    private long deleteFollowTime;
    private String eventAddress;
    private String eventBanner;
    private int eventId;
    private String eventName;
    private long eventStartTime;
    private long followTime;
    private String isFollow;
    private int userId;

    public long getDeleteFollowTime() {
        return deleteFollowTime;
    }

    public void setDeleteFollowTime(long deleteFollowTime) {
        this.deleteFollowTime = deleteFollowTime;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventBanner() {
        return eventBanner;
    }

    public void setEventBanner(String eventBanner) {
        this.eventBanner = eventBanner;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(long eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public long getFollowTime() {
        return followTime;
    }

    public void setFollowTime(long followTime) {
        this.followTime = followTime;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
