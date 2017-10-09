package com.medmeeting.m.zhiyi.UI.Entity;

public class WalletAccountDto {

    /**
     * id : 4
     * userId : 100102
     * accountType : BANK
     * accountName : 12312 
     * accountNumber : 1221231
     * publicPrivateType : PUBLIC
     * identityNumber : null
     * bankName : 中国农业银行
     * bankAddress : null
     * mobilePhone : null
     * createTime : 1506656850000
     * updateTime : 1506741244000
     * identityImage : null
     */

    private int id;
    private int userId;
    private String accountType;
    private String accountName;
    private String accountNumber;
    private String publicPrivateType;
    private String identityNumber;
    private String bankName;
    private String bankAddress;
    private String mobilePhone;
    private long createTime;
    private long updateTime;
    private String identityImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPublicPrivateType() {
        return publicPrivateType;
    }

    public void setPublicPrivateType(String publicPrivateType) {
        this.publicPrivateType = publicPrivateType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }
}
