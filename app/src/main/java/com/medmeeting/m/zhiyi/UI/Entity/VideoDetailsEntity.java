package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 6:35 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailsEntity {

    /**
     * videoId : 4
     * userId : 7905
     * programId : null
     * title : 直播9261
     * coverPhoto : http://onmtzwa3g.bkt.clouddn.com/Rectangle%2010%20Copy%203@3x.png
     * timeSecond : 658
     * authorName : 习老大
     * authorTitle : 总统
     * labelIds : 12,16
     * chargeType : yes
     * price : 10
     * url : null
     * des : null
     * playCount : 0
     * videoStatus : null
     * status : null
     * createTime : 1506505120000
     * userPic : http://oimas2nso.bkt.clouddn.com/2017-09-26_7u2JLcIe.png
     * payFlag : false
     * collectCount : 0
     * collectFlag : false
     * roomId:12124
     * healifeFlag:false
     */

    private int videoId;
    private int userId;
    private Integer programId;
    private String title;
    private String coverPhoto;
    private int timeSecond;
    private String authorName;
    private String authorTitle;
    private String labelIds;
    private String chargeType;
    private float price;
    private String url;
    private String des;
    private int playCount;
    private String videoStatus;
    private String status;
    private long createTime;
    private String userPic;
    private boolean payFlag;
    private int collectCount;
    private boolean collectFlag;
    private Integer roomId;
    private boolean healifeFlag;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public boolean isHealifeFlag() {
        return healifeFlag;
    }

    public void setHealifeFlag(boolean healifeFlag) {
        this.healifeFlag = healifeFlag;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
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

    public int getTimeSecond() {
        return timeSecond;
    }

    public void setTimeSecond(int timeSecond) {
        this.timeSecond = timeSecond;
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

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(String videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public boolean isPayFlag() {
        return payFlag;
    }

    public void setPayFlag(boolean payFlag) {
        this.payFlag = payFlag;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public boolean isCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(boolean collectFlag) {
        this.collectFlag = collectFlag;
    }
}
