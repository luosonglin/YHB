package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * —————————————————————–
 * Copyright (C) 2016，上海知醫有限公司， All rights reserved.
 * —————————————————————–
 * Created by luosonglin on 20/11/2016.
 * —————————————————————–
 * (Feature)
 * —————————————————————–
 */

public class DoctorAuthentication {

    /**
     * code : 200
     * data : {"mag":"success","user":{"id":7,"name":"露哟","nickName":"医宝123644","email":"iluosonglin@gmail.com","mobilePhone":"18817802222","company":"奥迪","department":"逗乐科","postion":"奥迪","hospital":"阿凡达","sex":"男","birthday":null,"title":null,"address":"水星","country":"中国","province":"11","city":"1101","zipCode":"251115","idCode":null,"status":"A","stateDate":1480408829000,"confirmNumber":"708395","phone":null,"userType":null,"userSource":null,"password":"","openId":"0","loginSource":null,"userPic":"http://wx1.sinaimg.cn/mw1024/a601622bly1fbdk9hstbmj20qo0qodja.jpg","authenStatus":"B","tokenId":"ec0467e0-b779-4450-ad00-81a0303cea21","county":"110101"}}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mag : success
         * user : {"id":7,"name":"露哟","nickName":"医宝123644","email":"iluosonglin@gmail.com","mobilePhone":"18817802222","company":"奥迪","department":"逗乐科","postion":"奥迪","hospital":"阿凡达","sex":"男","birthday":null,"title":null,"address":"水星","country":"中国","province":"11","city":"1101","zipCode":"251115","idCode":null,"status":"A","stateDate":1480408829000,"confirmNumber":"708395","phone":null,"userType":null,"userSource":null,"password":"","openId":"0","loginSource":null,"userPic":"http://wx1.sinaimg.cn/mw1024/a601622bly1fbdk9hstbmj20qo0qodja.jpg","authenStatus":"B","tokenId":"ec0467e0-b779-4450-ad00-81a0303cea21","county":"110101"}
         */

        private String mag;
        private UserBean user;

        public String getMag() {
            return mag;
        }

        public void setMag(String mag) {
            this.mag = mag;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 7
             * name : 露哟
             * nickName : 医宝123644
             * email : iluosonglin@gmail.com
             * mobilePhone : 18817802222
             * company : 奥迪
             * department : 逗乐科
             * postion : 奥迪
             * hospital : 阿凡达
             * sex : 男
             * birthday : null
             * title : null
             * address : 水星
             * country : 中国
             * province : 11
             * city : 1101
             * zipCode : 251115
             * idCode : null
             * status : A
             * stateDate : 1480408829000
             * confirmNumber : 708395
             * phone : null
             * userType : null
             * userSource : null
             * password :
             * openId : 0
             * loginSource : null
             * userPic : http://wx1.sinaimg.cn/mw1024/a601622bly1fbdk9hstbmj20qo0qodja.jpg
             * authenStatus : B
             * tokenId : ec0467e0-b779-4450-ad00-81a0303cea21
             * county : 110101
             */

            private int id;
            private String name;
            private String nickName;
            private String email;
            private String mobilePhone;
            private String company;
            private String department;
            private String postion;
            private String hospital;
            private String sex;
            private Object birthday;
            private Object title;
            private String address;
            private String country;
            private String province;
            private String city;
            private String zipCode;
            private Object idCode;
            private String status;
            private long stateDate;
            private String confirmNumber;
            private Object phone;
            private Object userType;
            private Object userSource;
            private String password;
            private String openId;
            private Object loginSource;
            private String userPic;
            private String authenStatus;
            private String tokenId;
            private String county;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
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

            public String getPostion() {
                return postion;
            }

            public void setPostion(String postion) {
                this.postion = postion;
            }

            public String getHospital() {
                return hospital;
            }

            public void setHospital(String hospital) {
                this.hospital = hospital;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public Object getBirthday() {
                return birthday;
            }

            public void setBirthday(Object birthday) {
                this.birthday = birthday;
            }

            public Object getTitle() {
                return title;
            }

            public void setTitle(Object title) {
                this.title = title;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getZipCode() {
                return zipCode;
            }

            public void setZipCode(String zipCode) {
                this.zipCode = zipCode;
            }

            public Object getIdCode() {
                return idCode;
            }

            public void setIdCode(Object idCode) {
                this.idCode = idCode;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getStateDate() {
                return stateDate;
            }

            public void setStateDate(long stateDate) {
                this.stateDate = stateDate;
            }

            public String getConfirmNumber() {
                return confirmNumber;
            }

            public void setConfirmNumber(String confirmNumber) {
                this.confirmNumber = confirmNumber;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public Object getUserType() {
                return userType;
            }

            public void setUserType(Object userType) {
                this.userType = userType;
            }

            public Object getUserSource() {
                return userSource;
            }

            public void setUserSource(Object userSource) {
                this.userSource = userSource;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getOpenId() {
                return openId;
            }

            public void setOpenId(String openId) {
                this.openId = openId;
            }

            public Object getLoginSource() {
                return loginSource;
            }

            public void setLoginSource(Object loginSource) {
                this.loginSource = loginSource;
            }

            public String getUserPic() {
                return userPic;
            }

            public void setUserPic(String userPic) {
                this.userPic = userPic;
            }

            public String getAuthenStatus() {
                return authenStatus;
            }

            public void setAuthenStatus(String authenStatus) {
                this.authenStatus = authenStatus;
            }

            public String getTokenId() {
                return tokenId;
            }

            public void setTokenId(String tokenId) {
                this.tokenId = tokenId;
            }

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }
        }
    }
}
