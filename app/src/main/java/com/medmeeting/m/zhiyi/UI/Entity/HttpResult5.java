package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * 会议API
 * 交换名片时候有用
 */
public class HttpResult5<T> {

    /**
     * amtOrder : 0.0
     * entity : {"address":"上海","authenStatus":"C","birthday":276451200000,"city":"东城","confirmNumber":"708395","country":"中国","county":"东城区","department":"计算机与科学专业","email":"luosonglin@healife.com","hospital":"软件学院","id":7,"idCode":"441427199912125896","invoiceName":"123","mailAddress":"123","medical":"1","mobilePhone":"18817802295","myTemplateEmail":"","name":"罗崧麟","nickName":"松林","password":"123","province":"北京","sex":"男","stateDate":1480408829000,"status":"A","title":"学生","tokenId":"ec0467e0-b779-4450-ad00-81a0303cea21","userPic":"http://ono5ms5i0.bkt.clouddn.com/android_live_20170823171306787308","vailStatus":5,"zipCode":"251115"}
     * returnCode : success
     * returnMsg : 获取信息成功！
     * status : 200
     */

    private double amtOrder;
    private T entity;
    private String returnCode;
    private String returnMsg;
    private String status;

    public double getAmtOrder() {
        return amtOrder;
    }

    public void setAmtOrder(double amtOrder) {
        this.amtOrder = amtOrder;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
