package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/03/2018 9:59 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class HospitalInfo {


    /**
     * hsId : 1069
     * hsProvince : null
     * hsCity : null
     * hsName : 北京市丰台区广济医院
     * hsAddress :
     * selectCount : 1
     */

    private int hsId;
    private String hsProvince;
    private String hsCity;
    private String hsName;
    private String hsAddress;
    private int selectCount;

    public int getHsId() {
        return hsId;
    }

    public void setHsId(int hsId) {
        this.hsId = hsId;
    }

    public String getHsProvince() {
        return hsProvince;
    }

    public void setHsProvince(String hsProvince) {
        this.hsProvince = hsProvince;
    }

    public String getHsCity() {
        return hsCity;
    }

    public void setHsCity(String hsCity) {
        this.hsCity = hsCity;
    }

    public String getHsName() {
        return hsName;
    }

    public void setHsName(String hsName) {
        this.hsName = hsName;
    }

    public String getHsAddress() {
        return hsAddress;
    }

    public void setHsAddress(String hsAddress) {
        this.hsAddress = hsAddress;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }
}
