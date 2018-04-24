package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 18/01/2018 4:50 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class VideoInfo {

    /**
     * videoId : 464
     * userId : 2186
     * roomId : 100521
     * programId : 100856
     * title : 直播小韩国
     * coverPhoto : http://oimas2nso.bkt.clouddn.com/2018-01-17_hwmTEkUL.png
     * timeSecond : 4558
     * authorName : 小韩
     * authorTitle : 主任
     * labelIds : 7,5,10
     * chargeType : yes
     * price : 0.01
     * url : http://onmtzwa3g.bkt.clouddn.com/recordings/z1.yihuibao-test.yihuibao-test_20180117153117955_100856/1516166400_1516178852.m3u8
     * des : 坏账损失
     * playCount : 9
     * shareApp : 0
     * shareWechat : 0
     * videoStatus : ADOPT
     * status : A
     * createTime : 1516166520000
     */

    private int videoId;
    private int userId;
    private int roomId;
    private int programId;
    private String title;
    private String coverPhoto;
    private int timeSecond;
    private String authorName;
    private String authorTitle;
    private String labelIds;
    private String chargeType;
    private double price;
    private String url;
    private String des;
    private int playCount;
    private int shareApp;
    private int shareWechat;
    private String videoStatus;
    private String status;
    private long createTime;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public int getShareApp() {
        return shareApp;
    }

    public void setShareApp(int shareApp) {
        this.shareApp = shareApp;
    }

    public int getShareWechat() {
        return shareWechat;
    }

    public void setShareWechat(int shareWechat) {
        this.shareWechat = shareWechat;
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
}
