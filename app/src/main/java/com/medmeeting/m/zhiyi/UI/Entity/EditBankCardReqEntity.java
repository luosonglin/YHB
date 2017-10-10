package com.medmeeting.m.zhiyi.UI.Entity;

public class EditBankCardReqEntity {

    /**
     * accountName : string
     * accountNumber : string
     * bankAddress : string
     * bankName : string
     * identityImage : string
     * identityNumber : string
     * mobilePhone : string
     * publicPrivateType : string
     * verCode : string
     */

    private String accountName;
    private String accountNumber;
    private String bankAddress;
    private String bankName;
    private String identityImage;
    private String identityNumber;
    private String mobilePhone;
    private String publicPrivateType;
    private String verCode;

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

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPublicPrivateType() {
        return publicPrivateType;
    }

    public void setPublicPrivateType(String publicPrivateType) {
        this.publicPrivateType = publicPrivateType;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }
}
