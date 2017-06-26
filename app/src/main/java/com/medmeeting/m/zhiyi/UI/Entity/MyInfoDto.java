package com.medmeeting.m.zhiyi.UI.Entity;

public class MyInfoDto {

    /**
     * code : 200
     * data : {"msg":"success","user":{"id":7,"name":"罗崧麟","nickName":"松林","email":"luosonglin@healife.com","mobilePhone":"18817802295","company":null,"department":"计算机与科学专业","postion":null,"hospital":"软件学院","sex":"男","birthday":276451200000,"title":"学生","address":"上海","country":"中国","province":"11","city":"1101","zipCode":"251115","idCode":"441427199912125896","status":"A","stateDate":1480408829000,"confirmNumber":"708395","phone":null,"userType":null,"userSource":null,"password":"490","openId":null,"loginSource":null,"userPic":"http://ww2.sinaimg.cn/mw1024/a601622bgw1f8awv4alasj20hs0bdmy6.jpg","authenStatus":"C","tokenId":"ec0467e0-b779-4450-ad00-81a0303cea21","county":"110101","qqOpenId":null}}
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
         * msg : success
         * user : {"id":7,"name":"罗崧麟","nickName":"松林","email":"luosonglin@healife.com","mobilePhone":"18817802295","company":null,"department":"计算机与科学专业","postion":null,"hospital":"软件学院","sex":"男","birthday":276451200000,"title":"学生","address":"上海","country":"中国","province":"11","city":"1101","zipCode":"251115","idCode":"441427199912125896","status":"A","stateDate":1480408829000,"confirmNumber":"708395","phone":null,"userType":null,"userSource":null,"password":"490","openId":null,"loginSource":null,"userPic":"http://ww2.sinaimg.cn/mw1024/a601622bgw1f8awv4alasj20hs0bdmy6.jpg","authenStatus":"C","tokenId":"ec0467e0-b779-4450-ad00-81a0303cea21","county":"110101","qqOpenId":null}
         */

        private String msg;
        private UserBean user;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
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
             * name : 罗崧麟
             * nickName : 松林
             * email : luosonglin@healife.com
             * mobilePhone : 18817802295
             * company : null
             * department : 计算机与科学专业
             * postion : null
             * hospital : 软件学院
             * sex : 男
             * birthday : 276451200000
             * title : 学生
             * address : 上海
             * country : 中国
             * province : 11
             * city : 1101
             * zipCode : 251115
             * idCode : 441427199912125896
             * status : A
             * stateDate : 1480408829000
             * confirmNumber : 708395
             * phone : null
             * userType : null
             * userSource : null
             * password : 490
             * openId : null
             * loginSource : null
             * userPic : http://ww2.sinaimg.cn/mw1024/a601622bgw1f8awv4alasj20hs0bdmy6.jpg
             * authenStatus : C
             * tokenId : ec0467e0-b779-4450-ad00-81a0303cea21
             * county : 110101
             * qqOpenId : null
             */

            private int id;
            private String name;
            private String nickName;
            private String email;
            private String mobilePhone;
            private Object company;
            private String department;
            private Object postion;
            private String hospital;
            private String sex;
            private long birthday;
            private String title;
            private String address;
            private String country;
            private String province;
            private String city;
            private String zipCode;
            private String idCode;
            private String status;
            private long stateDate;
            private String confirmNumber;
            private Object phone;
            private Object userType;
            private Object userSource;
            private String password;
            private Object openId;
            private Object loginSource;
            private String userPic;
            private String authenStatus;
            private String tokenId;
            private String county;
            private Object qqOpenId;

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

            public Object getCompany() {
                return company;
            }

            public void setCompany(Object company) {
                this.company = company;
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

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
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

            public String getIdCode() {
                return idCode;
            }

            public void setIdCode(String idCode) {
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

            public Object getOpenId() {
                return openId;
            }

            public void setOpenId(Object openId) {
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

            public Object getQqOpenId() {
                return qqOpenId;
            }

            public void setQqOpenId(Object qqOpenId) {
                this.qqOpenId = qqOpenId;
            }
        }
    }
}
