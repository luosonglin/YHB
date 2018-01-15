package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 08/11/2017 10:48 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveProgramDateilsEntity {

    /**
     * id : 100172
     * roomId : 100128
     * title : 10.10铺好
     * coverPhoto : http://oikv7kt4d.bkt.clouddn.com/placeholder@1.5x.png
     * streamPhoto : 666666
     * startTime : 1510310700000
     * endTime : 1512902700000
     * authorName : 孔明灯
     * authorTitle : 教练
     * privacyType : public
     * chargeType : yes
     * price : 0.02
     * firstOpenTime : null
     * realEndTime : null
     * onlineCount : 0
     * countRatio : 1
     * countIncrement : 0
     * liveStatus : ready
     * des : 仉好
     * status : null
     * createTime : null
     * updateTime : null
     * roomUserId : 2920
     * userPic : defaultPic
     * roomNumber : 666666
     * rtmpPlayUrl : 666666
     * hlsPlayUrl : 666666
     * payFalg : 0
     * collectFlag : false
     * collectCount : 3
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
    private long createTime;
    private long updateTime;
    private int roomUserId;
    private String userPic;
    private String roomNumber;
    private String rtmpPlayUrl;
    private String hlsPlayUrl;
    private int payFalg;//是否购票 0:未购票，大于0:已购票 ,
    private boolean collectFlag;
    private int collectCount;
    private int countRatio; //人数放大比例
    private int countIncrement; //在线人数-固定增量



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

    public int getCountRatio() {
        return countRatio;
    }

    public void setCountRatio(int countRatio) {
        this.countRatio = countRatio;
    }

    public int getCountIncrement() {
        return countIncrement;
    }

    public void setCountIncrement(int countIncrement) {
        this.countIncrement = countIncrement;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getRoomUserId() {
        return roomUserId;
    }

    public void setRoomUserId(int roomUserId) {
        this.roomUserId = roomUserId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    public boolean isCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(boolean collectFlag) {
        this.collectFlag = collectFlag;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }
}
