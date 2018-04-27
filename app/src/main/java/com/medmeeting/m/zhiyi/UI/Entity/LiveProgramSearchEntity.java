package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 10/11/2017 8:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveProgramSearchEntity {

    /**
     * keyword : string
     * labelIds : [0]
     * pageNum : 0
     * pageSize : 0
     * roomId : 0
     * roomNumber : string
     * roomUserId : 0
     */

    private String keyword;
    private int pageNum;
    private int pageSize;
    private int roomId;
    private String roomNumber;
    private int roomUserId;
    private List<Integer> labelIds;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomUserId() {
        return roomUserId;
    }

    public void setRoomUserId(int roomUserId) {
        this.roomUserId = roomUserId;
    }

    public List<Integer> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Integer> labelIds) {
        this.labelIds = labelIds;
    }
}

