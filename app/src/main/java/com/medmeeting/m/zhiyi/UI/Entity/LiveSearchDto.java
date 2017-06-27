package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveSearchDto {

    /**
     * keyword : string
     * labelIds : [0]
     * pageNum : 0
     * pageSize : 0
     * roomId : 0
     * roomNumber : string
     * roomUserId : 0
     * userId : 0
     */

    private String keyword;
//    private int pageNum;
//    private int pageSize;
//    private int roomId;
//    private String roomNumber;
//    private int roomUserId;
//    private int userId;
//    private List<Integer> labelIds;

    public LiveSearchDto() {
    }

    public LiveSearchDto(String keyword) {
        this.keyword = keyword;
    }

//    public LiveSearchDto(String keyword, int pageNum, int pageSize, int roomId, String roomNumber, int roomUserId, int userId, List<Integer> labelIds) {
//        this.keyword = keyword;
//        this.pageNum = pageNum;
//        this.pageSize = pageSize;
//        this.roomId = roomId;
//        this.roomNumber = roomNumber;
//        this.roomUserId = roomUserId;
//        this.userId = userId;
//        this.labelIds = labelIds;
//    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    public int getPageNum() {
//        return pageNum;
//    }
//
//    public void setPageNum(int pageNum) {
//        this.pageNum = pageNum;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public int getRoomId() {
//        return roomId;
//    }
//
//    public void setRoomId(int roomId) {
//        this.roomId = roomId;
//    }
//
//    public String getRoomNumber() {
//        return roomNumber;
//    }
//
//    public void setRoomNumber(String roomNumber) {
//        this.roomNumber = roomNumber;
//    }
//
//    public int getRoomUserId() {
//        return roomUserId;
//    }
//
//    public void setRoomUserId(int roomUserId) {
//        this.roomUserId = roomUserId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public List<Integer> getLabelIds() {
//        return labelIds;
//    }
//
//    public void setLabelIds(List<Integer> labelIds) {
//        this.labelIds = labelIds;
//    }
}
