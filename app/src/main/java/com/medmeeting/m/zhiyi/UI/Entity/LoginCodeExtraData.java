package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 04/04/2018 4:20 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class LoginCodeExtraData {
    private boolean hasName;
    private boolean hasPwd;
    private String userName;
    private String nickName;
    private int userId;
    private Object roleList;
    private boolean newUser; //true 就是新用户

    public boolean isHasName() {
        return hasName;
    }

    public void setHasName(boolean hasName) {
        this.hasName = hasName;
    }

    public boolean isHasPwd() {
        return hasPwd;
    }

    public void setHasPwd(boolean hasPwd) {
        this.hasPwd = hasPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getRoleList() {
        return roleList;
    }

    public void setRoleList(Object roleList) {
        this.roleList = roleList;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}
