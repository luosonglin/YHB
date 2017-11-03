package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 02/11/2017 6:30 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoSettlementEntity {
    private float actualAmount;
    private float feeAmount;
    private List<VideoPayUserEntity> payList;
    private float settlementAmount;
    private float totalAmount;

    public float getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(float actualAmount) {
        this.actualAmount = actualAmount;
    }

    public float getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(float feeAmount) {
        this.feeAmount = feeAmount;
    }

    public List<VideoPayUserEntity> getPayList() {
        return payList;
    }

    public void setPayList(List<VideoPayUserEntity> payList) {
        this.payList = payList;
    }

    public float getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(float settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
