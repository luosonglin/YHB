package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * 用于直播API
 * Created by luosonglin on 17/6/19.
 */
public class HttpResult2<T> {

    /**
     * amtOrder : 0.0
     * list : [{"id":1,"labelName":"外科","searchCount":0,"sort":0,"useCount":0},{"id":2,"labelName":"儿科","searchCount":0,"sort":0,"useCount":0},{"id":3,"labelName":"妇产科","searchCount":0,"sort":0,"useCount":0},{"id":4,"labelName":"脑科","searchCount":0,"sort":0,"useCount":0},{"id":5,"labelName":"内科","searchCount":0,"sort":0,"useCount":0},{"id":6,"labelName":"外科","searchCount":0,"sort":0,"useCount":0},{"id":7,"labelName":"眼科","searchCount":0,"sort":0,"useCount":0},{"id":8,"labelName":"五官科","searchCount":0,"sort":0,"useCount":0},{"id":9,"labelName":"神经科","searchCount":0,"sort":0,"useCount":0}]
     * returnCode : 1
     * returnMsg : 成功！
     * status : success
     */

    private double amtOrder;
    private String returnCode;
    private String returnMsg;
    private String status;
    private List<T> list;

    public double getAmtOrder() {
        return amtOrder;
    }

    public void setAmtOrder(double amtOrder) {
        this.amtOrder = amtOrder;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
