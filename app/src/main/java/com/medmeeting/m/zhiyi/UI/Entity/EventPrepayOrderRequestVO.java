package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 21/12/2017 10:42 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventPrepayOrderRequestVO {


    /**
     * orderId : 378_20171220181404554_482
     * paymentChannel : alipay
     * platformType : APP
     */

    private String orderId;
    private String paymentChannel;
    private String platformType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
}
