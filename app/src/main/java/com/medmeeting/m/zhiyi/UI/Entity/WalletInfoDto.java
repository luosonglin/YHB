package com.medmeeting.m.zhiyi.UI.Entity;

public class WalletInfoDto {

    /**
     * userId : 100102
     * balance : 33630.66
     * password : ***
     * updateTime : 1506760451000
     */

    private int userId;
    private double balance;
    private String password;
    private long updateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
