package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * 会议API
 */
public class HttpResult4<T> {
    private double amtOrder;
    private PageInfoBean<T> pageInfo;
    private String returnCode;
    private String returnMsg;
    private String status;

    public double getAmtOrder() {
        return amtOrder;
    }

    public void setAmtOrder(double amtOrder) {
        this.amtOrder = amtOrder;
    }

    public PageInfoBean<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean<T> pageInfo) {
        this.pageInfo = pageInfo;
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
