package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/10/2017 2:56 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class UserRedEntity {

    /**
     * userId : 7
     * name : 罗崧麟
     * nickName : 松林
     * department : 计算机与科学专业
     * postion : null
     * hospital : 软件学院
     * company : null
     * userPic : http://ww2.sinaimg.cn/mw1024/a601622bgw1f8awv4alasj20hs0bdmy6.jpg
     * roomList : [{"id":100125,"userId":7,"number":"851889","title":"哈哈哈(ಡωಡ)hiahiahia","coverPhoto":"http://ono5ms5i0.bkt.clouddn.com/android_live_20170715175944131385","labelIds":"1,2,3","des":"哈哈哈","sort":0,"status":"A","createTime":1500112806000,"updateTime":1500112806000},{"id":100123,"userId":7,"number":"427045","title":"测试","coverPhoto":"http://ono5ms5i0.bkt.clouddn.com/android_live_20170715153543973690","labelIds":"29,30,31","des":"","sort":0,"status":"A","createTime":1500104213000,"updateTime":1500104213000}]
     */

    private int userId;
    private String name;
    private String nickName;
    private String department;
    private Object postion;
    private String hospital;
    private Object company;
    private String userPic;
    private List<LiveRoomDto> roomList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Object getPostion() {
        return postion;
    }

    public void setPostion(Object postion) {
        this.postion = postion;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public List<LiveRoomDto> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<LiveRoomDto> roomList) {
        this.roomList = roomList;
    }
}
