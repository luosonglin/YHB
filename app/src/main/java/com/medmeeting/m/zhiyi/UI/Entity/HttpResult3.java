package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class HttpResult3<T> {

    /**
     * status : success
     * msg : Success
     * errorCode : null
     * exp : null
     * entity : null
     * data : [{"id":100003,"roomId":2,"title":"王医师的直播首秀","coverPhoto":"","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王二狗","authorTitle":"王1师","privacyType":"public","chargeType":"no","price":null,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"循环,内分泌,神经","payCount":0},{"id":100005,"roomId":2,"title":"王医师的直播首秀","coverPhoto":"","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王二狗","authorTitle":"王一师","privacyType":"public","chargeType":"no","price":null,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"循环,内分泌,神经","payCount":0},{"id":100006,"roomId":2,"title":"主治医生王医师大讲堂","coverPhoto":"http://img14.3lian.com/201507/16/c39cc11642e914b698bca077d75bc0c3.jpg","streamPhoto":null,"startTime":1495099328000,"endTime":1495099330000,"authorName":"王医师","authorTitle":"主刀医生","privacyType":"public","chargeType":"yes","price":12.21,"firstOpenTime":null,"realEndTime":null,"onlineCount":0,"liveStatus":"ready","status":"A","createTime":null,"updateTime":null,"roomTitle":"医会小助手的直播间","userId":145,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-03-30_0soZ0Vxe.png","labelIds":"1,2,3","labelNames":"内分泌,循环,神经,循环,神经,内分泌","payCount":2}]
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
    private List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
