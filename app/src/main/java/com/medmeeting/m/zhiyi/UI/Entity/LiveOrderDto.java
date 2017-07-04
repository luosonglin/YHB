package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveOrderDto {

    /**
     * openId :
     * paymentChannel : ALIPAY
     * platformType : APP
     * programId : 100010
     */

    private String openId;
    private String paymentChannel;
    private String platformType;
    private int programId;

    public LiveOrderDto(String openId, String paymentChannel, String platformType, int programId) {
        this.openId = openId;
        this.paymentChannel = paymentChannel;
        this.platformType = platformType;
        this.programId = programId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }
}
