package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/11/2017 2:24 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EditVideoEntity {

    /**
     * chargeType : string
     * coverPhoto : string
     * des : string
     * price : 1.0
     * privacyType : string
     * title : string
     * videoId : 0
     */

    private String chargeType;
    private String coverPhoto;
    private String des;
    private double price;
    private String privacyType;
    private String title;
    private int videoId;

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(String privacyType) {
        this.privacyType = privacyType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
