package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 26/10/2017 1:28 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class UserCollect {

    /**
     * serviceId : 5
     * serviceType :   PROG：直播节目，VIDEO：视频，EVENT：会议，BLOG：新闻
     */

    private int serviceId;
    private String serviceType;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
