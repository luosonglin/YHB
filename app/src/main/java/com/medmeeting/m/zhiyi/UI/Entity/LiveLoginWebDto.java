package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveLoginWebDto {

    /**
     * extend : //programid
     * uuid : cb1b50c8-dae8-44a2-9b8e-da88b9f9a92e
     */

    private String extend;
    private String uuid;

    public LiveLoginWebDto(String extend, String uuid) {
        this.extend = extend;
        this.uuid = uuid;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
