package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 10/01/2018 4:59 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class VAppMyEvents {


    /**
     * eventId : 378
     * registerId : 21
     * registerStatus : ok
     * title : 会议2
     * banner : http://on5q2lha8.bkt.clouddn.com/Fi-TCeFgHXOLIHQXIibJguJkltKT
     * stateDate : 1513856211000
     * endDate : 1514554566000
     * createTime : 1513764610000
     * status : A
     * address : 亚太企业大楼
     */

    private int eventId;
    private int registerId;
    private String registerStatus;
    private String title;
    private String banner;
    private long stateDate;
    private long endDate;
    private long createTime;
    private String status;
    private String address;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
        this.registerId = registerId;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getStateDate() {
        return stateDate;
    }

    public void setStateDate(long stateDate) {
        this.stateDate = stateDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
