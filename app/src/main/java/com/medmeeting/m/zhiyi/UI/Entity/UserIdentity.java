package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/01/2018 4:04 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserIdentity {

    /**
     * identityId : 1
     * code : ASSOCIATION
     * title : 医疗协会
     * icon : http://onmtzwa3g.bkt.clouddn.com/iconGroup%209@1.5x.png
     * des : 医疗协会、社会团体、组织联盟成员
     * level : 2
     */

    private int identityId;
    private String code;
    private String title;
    private String icon;
    private String des;
    private int level;

    public int getIdentityId() {
        return identityId;
    }

    public void setIdentityId(int identityId) {
        this.identityId = identityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
