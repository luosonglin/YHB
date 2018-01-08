package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 08/01/2018 10:42 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class UserAuthorEntity {

    /**
     * department : string
     * hospital : string
     * name : string
     * title : string
     */

    private String department;
    private String hospital;
    private String name;
    private String title;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
