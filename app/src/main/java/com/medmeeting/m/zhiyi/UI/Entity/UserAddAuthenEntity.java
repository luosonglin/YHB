package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 05/02/2018 5:15 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class UserAddAuthenEntity {

    /**
     * category : string
     * categoryName : string
     * company : string
     * department : string
     * diploma : string
     * email : string
     * entranceDate : string
     * identityPhoto : string
     * postion : string
     * title : string
     * userName : string
     * workPhoto : string
     * workPhotoType : string
     */

//    category (string, optional): 身份CODE ,
//    categoryName (string, optional): 身份名称 ,
//    company (string, optional): 单位/医院/学校 ,
//    department (string, optional): 部门/科室/专业 ,
//    diploma (string, optional): 学历 ,
//    email (string, optional): 电子邮箱 ,
//    entranceDate (string, optional): 入学年份 ,
//    identityPhoto (string, optional): 身份证图片 ,
//    postion (string, optional): 职务/职位 ,
//    title (string, optional): 职称 ,
//    userName (string, optional): 姓名 ,
//    workPhoto (string, optional): 工作证明图片 ,
//    workPhotoType (string, optional): 认证材料类型(工作证+身份证)

    private String category;
    private String categoryName;
    private String company;
    private String department;
    private String diploma;
    private String email;
    private String entranceDate;
    private String identityPhoto;
    private String postion;
    private String title;
    private String userName;
    private String workPhoto;
    private String workPhotoType;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(String entranceDate) {
        this.entranceDate = entranceDate;
    }

    public String getIdentityPhoto() {
        return identityPhoto;
    }

    public void setIdentityPhoto(String identityPhoto) {
        this.identityPhoto = identityPhoto;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkPhoto() {
        return workPhoto;
    }

    public void setWorkPhoto(String workPhoto) {
        this.workPhoto = workPhoto;
    }

    public String getWorkPhotoType() {
        return workPhotoType;
    }

    public void setWorkPhotoType(String workPhotoType) {
        this.workPhotoType = workPhotoType;
    }
}
