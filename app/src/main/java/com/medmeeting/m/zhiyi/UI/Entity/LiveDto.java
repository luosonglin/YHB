package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveDto {
    /**
     * id : 100003
     * roomId : 2
     * title : 王医师的直播首秀
     * coverPhoto :
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
     * status : A
     * createTime : null
     * updateTime : null
     * roomTitle : 医会小助手的直播间
     * userId : 145
     * userPic : http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png
     * labelIds : 1,2,3
     * labelNames : 循环,内分泌,神经
     * payCount : 0
     * des : null
     */

    private int id;
    private int roomId;
    private String title;
    private String coverPhoto;
    private Object streamPhoto;
    private long startTime;
    private long endTime;
    private String authorName;
    private String authorTitle;
    private String privacyType;
    private String chargeType;
    private Object price;
    private long firstOpenTime;
    private long realEndTime;
    private int onlineCount;
    private String liveStatus;
    private String status;
    private Object createTime;
    private Object updateTime;
    private String roomTitle;
    private int userId;
    private String userPic;
    private String labelIds;
    private String labelNames;
    private int payCount;
    private String des;

    public LiveDto(String title, String coverPhoto, long startTime, long endTime, String authorName, String authorTitle, String privacyType, String chargeType, Object price, String des) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.startTime = startTime;
        this.endTime = endTime;
        this.authorName = authorName;
        this.authorTitle = authorTitle;
        this.privacyType = privacyType;
        this.chargeType = chargeType;
        this.price = price;
        this.des = des;
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

    public Object getStreamPhoto() {
        return streamPhoto;
    }

    public void setStreamPhoto(Object streamPhoto) {
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

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public long getFirstOpenTime() {
        return firstOpenTime;
    }

    public void setFirstOpenTime(long firstOpenTime) {
        this.firstOpenTime = firstOpenTime;
    }

    public long getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(long realEndTime) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getLabelNames() {
        return labelNames;
    }

    public void setLabelNames(String labelNames) {
        this.labelNames = labelNames;
    }

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
