package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/03/2018 7:15 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class Version {

    /**
     * id : 5
     * version : 2.1.0
     * isForce : false
     * log : 程序员哥哥正在加紧开发中，无需升级
     * url : medmeeting.com
     * createdAt : 1510324213000
     */

    private int id;
    private String version;
    private boolean isForce;
    private String log;
    private String url;
    private long createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isIsForce() {
        return isForce;
    }

    public void setIsForce(boolean isForce) {
        this.isForce = isForce;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
