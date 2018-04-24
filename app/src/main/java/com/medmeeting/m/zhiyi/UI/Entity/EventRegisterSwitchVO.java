package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/12/2017 1:22 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventRegisterSwitchVO {

    /**
     * eventId : 317
     * openEntrances : true
     * displayEntrances : true
     * registerResult : true
     * registerOkButtonName :
     * registerButtonName :
     */

    private int eventId;
    private boolean openEntrances;
    private boolean displayEntrances;
    private boolean registerResult;
    private String registerOkButtonName;
    private String registerButtonName;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isOpenEntrances() {
        return openEntrances;
    }

    public void setOpenEntrances(boolean openEntrances) {
        this.openEntrances = openEntrances;
    }

    public boolean isDisplayEntrances() {
        return displayEntrances;
    }

    public void setDisplayEntrances(boolean displayEntrances) {
        this.displayEntrances = displayEntrances;
    }

    public boolean isRegisterResult() {
        return registerResult;
    }

    public void setRegisterResult(boolean registerResult) {
        this.registerResult = registerResult;
    }

    public String getRegisterOkButtonName() {
        return registerOkButtonName;
    }

    public void setRegisterOkButtonName(String registerOkButtonName) {
        this.registerOkButtonName = registerOkButtonName;
    }

    public String getRegisterButtonName() {
        return registerButtonName;
    }

    public void setRegisterButtonName(String registerButtonName) {
        this.registerButtonName = registerButtonName;
    }
}
