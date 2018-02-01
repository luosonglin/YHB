package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/01/2018 3:47 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserAddActivationEntity {

    /**
     * company : string
     * department : string
     * diploma : string
     * entranceDate : string
     * identityCode : string
     * identityTitle : string
     * name : string
     * postion : string
     * title : string
     */
    /*
    company (string, optional): 单位/医院/学校 ,
    department (string, optional): 部门/科室/专业 ,
    diploma (string, optional): 学历 ,
    entranceDate (string, optional): 入学年份 ,
    identityCode (string, optional): 身份code ,
    identityTitle (string, optional): 身份标题 ,
    name (string, optional): 姓名 ,
    postion (string, optional): 职务/职位 ,
    title (string, optional): 职称*/

    private String company;
    private String department;
    private String diploma;
    private String entranceDate;
    private String identityCode;
    private String identityTitle;
    private String name;
    private String postion;
    private String title;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(String entranceDate) {
        this.entranceDate = entranceDate;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getIdentityTitle() {
        return identityTitle;
    }

    public void setIdentityTitle(String identityTitle) {
        this.identityTitle = identityTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
