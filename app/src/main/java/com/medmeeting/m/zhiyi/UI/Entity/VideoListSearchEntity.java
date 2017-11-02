package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 10:42 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoListSearchEntity {

    /**
     * keyword : string
     * labelId : 0
     * pageNum : 0
     * pageSize : 0
     * programId : 0
     * roomId : 0
     * roomNumber : string
     * videoUserId : 0
     */

    private String keyword;
    private Object labelId;
    private int pageNum;
    private int pageSize;
    private Object programId;
    private Object roomId;
    private String roomNumber;
    private Object videoUserId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Object getLabelId() {
        return labelId;
    }

    public void setLabelId(Object labelId) {
        this.labelId = labelId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Object getProgramId() {
        return programId;
    }

    public void setProgramId(Object programId) {
        this.programId = programId;
    }

    public Object getRoomId() {
        return roomId;
    }

    public void setRoomId(Object roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Object getVideoUserId() {
        return videoUserId;
    }

    public void setVideoUserId(Object videoUserId) {
        this.videoUserId = videoUserId;
    }
}
