package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveAudienceDetailDto {

    /**
     * id : 100003
     * roomId : 100007
     * title : 王医师的直播首秀
     * coverPhoto : http://oikv7kt4d.bkt.clouddn.com/placeholder@1.5x.png
     * streamPhoto : null
     * startTime : 1495099328000
     * endTime : 1495099330000
     * authorName : 王二狗
     * authorTitle : 王1师
     * privacyType : public
     * chargeType : no
     * price : null
     * firstOpenTime : null
     * realEndTime : null
     * onlineCount : 0
     * liveStatus : ready
     * des : null
     * status : null
     * createTime : null
     * updateTime : null
     * rtmpPlayUrl : null
     * hlsPlayUrl : null
     * payFalg : 0
     */

    private int id;
    private int roomId;
    private String title;
    private String coverPhoto;
    private String streamPhoto;
    private long startTime;
    private long endTime;
    private String authorName;
    private String authorTitle;
    private String privacyType;
    private String chargeType;
    private float price;
    private Object firstOpenTime;
    private Object realEndTime;
    private int onlineCount;
    private String liveStatus;
    private String des;
    private Object status;
    private Object createTime;
    private Object updateTime;
    private String rtmpPlayUrl;
    private String hlsPlayUrl;
    private int payFalg;
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getStreamPhoto() {
        return streamPhoto;
    }

    public void setStreamPhoto(String streamPhoto) {
        this.streamPhoto = streamPhoto;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public void setAuthorTitle(String authorTitle) {
        this.authorTitle = authorTitle;
    }

    public String getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(String privacyType) {
        this.privacyType = privacyType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Object getFirstOpenTime() {
        return firstOpenTime;
    }

    public void setFirstOpenTime(Object firstOpenTime) {
        this.firstOpenTime = firstOpenTime;
    }

    public Object getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Object realEndTime) {
        this.realEndTime = realEndTime;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getRtmpPlayUrl() {
        return rtmpPlayUrl;
    }

    public void setRtmpPlayUrl(String rtmpPlayUrl) {
        this.rtmpPlayUrl = rtmpPlayUrl;
    }

    public String getHlsPlayUrl() {
        return hlsPlayUrl;
    }

    public void setHlsPlayUrl(String hlsPlayUrl) {
        this.hlsPlayUrl = hlsPlayUrl;
    }

    public int getPayFalg() {
        return payFalg;
    }

    public void setPayFalg(int payFalg) {
        this.payFalg = payFalg;
    }
}
