package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class LiveDto {

    /**
     * status : success
     * msg : Success
     * errorCode : null
     * exp : null
     * entity : null
     * data : [{"id":100003,"roomId":2,"title":"王医师的直播首秀","coverPhoto":"","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王二狗","authorTitle":"王1师","privacyType":"public","chargeType":"no","price":null,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"循环,内分泌,神经","payCount":0},{"id":100005,"roomId":2,"title":"王医师的直播首秀","coverPhoto":"","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王二狗","authorTitle":"王一师","privacyType":"public","chargeType":"no","price":null,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"神经,循环,内分泌","payCount":0},{"id":100006,"roomId":2,"title":"主治医生王医师大讲堂","coverPhoto":"http://img14.3lian.com/201507/16/c39cc11642e914b698bca077d75bc0c3.jpg","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王医师","authorTitle":"主刀医生","privacyType":"public","chargeType":"yes","price":12.21,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"内分泌,内分泌,循环,神经,循环,神经","payCount":2}]
     * total : 3
     * pageNum : 1
     * pageSize : 15
     * totalPages : 1
     * recordsTotal : 3
     * recordsFiltered : 3
     * extra : null
     */

    private String status;
    private String msg;
    private Object errorCode;
    private Object exp;
    private Object entity;
    private int total;
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private int recordsTotal;
    private int recordsFiltered;
    private Object extra;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getExp() {
        return exp;
    }

    public void setExp(Object exp) {
        this.exp = exp;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
        private Object firstOpenTime;
        private Object realEndTime;
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
    }
}
