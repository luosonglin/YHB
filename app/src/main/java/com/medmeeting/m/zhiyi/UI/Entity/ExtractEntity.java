package com.medmeeting.m.zhiyi.UI.Entity;

public class ExtractEntity {

    /**
     * amount : 0
     * extractNumber : string
     * extractPassword : string
     * extractType : string
     * userId : 0
     * walletId : 0
     * withdrawType : string
     */

    private double amount;
    private String extractNumber;
    private String extractPassword;
    private String extractType;
    private int userId;
    private int walletId;
    private String withdrawType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getExtractNumber() {
        return extractNumber;
    }

    public void setExtractNumber(String extractNumber) {
        this.extractNumber = extractNumber;
    }

    public String getExtractPassword() {
        return extractPassword;
    }

    public void setExtractPassword(String extractPassword) {
        this.extractPassword = extractPassword;
    }

    public String getExtractType() {
        return extractType;
    }

    public void setExtractType(String extractType) {
        this.extractType = extractType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }
}
