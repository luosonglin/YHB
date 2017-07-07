package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * 直播-主播-直播房间
 */
public class LiveRoomDto {

    /**
     * id : 100018
     * userId : 145
     * number : 099828
     * title : xxxxx
     * coverPhoto : http://oikv7kt4d.bkt.clouddn.com/Rectangle%2012%20Copy%202.png
     * labelIds : 26
     * des : xxx
     * status : A
     * createTime : 1498109834000
     * updateTime : 1498109834000
     */

    private int id;
    private int userId;
    private String number;
    private String title;
    private String coverPhoto;
    private String labelIds;
    private String des;
    private String status;
    private long createTime;
    private long updateTime;

    public LiveRoomDto(String title, String coverPhoto, String labelIds, String des) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.labelIds = labelIds;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
