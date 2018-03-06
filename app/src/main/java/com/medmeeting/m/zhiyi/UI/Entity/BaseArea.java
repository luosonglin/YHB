package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 06/03/2018 10:18 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class BaseArea {

    /**
     * baseAreaid : 11
     * name : 北京
     * parentid : 0
     * vieworder : 1
     */

    private int baseAreaid;
    private String name;
    private int parentid;
    private int vieworder;

    public int getBaseAreaid() {
        return baseAreaid;
    }

    public void setBaseAreaid(int baseAreaid) {
        this.baseAreaid = baseAreaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getVieworder() {
        return vieworder;
    }

    public void setVieworder(int vieworder) {
        this.vieworder = vieworder;
    }
}
