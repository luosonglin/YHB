package com.medmeeting.m.zhiyi.UI.Entity;

import com.google.gson.annotations.SerializedName;

public class LiveWechatPayDto {

    /**
     * timeStamp : 1499137355
     * package : Sign=WXPay
     * appid : wx7e6722fad8a0975c
     * sign : C49A0FEC8FCC1A3FE2020783CC0E9B2B
     * partnerid : 1427222502
     * prepayid : wx20170704110235eb674476c30374197038
     * noncestr : er8nxaq4snnb4lvwboo20ymq19ibukmm
     */
    private String timeStamp;
    @SerializedName("package")
    private String packageX;
    private String appid;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }
}
