package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class LiveTicketDto {

    /**
     * amountSum : 0.01
     * extractStatus : null
     * payList : [{"orderId":"100196_20170719155450358_513","programId":100196,"buyerUserId":513,"amount":0.01,"tradeStatus":"SUCCESS","status":null,"createTime":1500450890000,"updateTime":null,"userPic":"http://oimas2nso.bkt.clouddn.com/2017-07-07_VsX4xI61.png","userName":"希特勒","nickName":"医宝341895","postion":null,"company":null,"department":"重症医学科"}]
     */

    private double amountSum;
    private String extractStatus; //extractStatus (string, optional): 提现状态（PENDING：待处理，ADOPT：通过，REJECT：驳回） ,
    private List<PayListBean> payList;

    public double getAmountSum() {
        return amountSum;
    }

    public void setAmountSum(double amountSum) {
        this.amountSum = amountSum;
    }

    public String getExtractStatus() {
        return extractStatus;
    }

    public void setExtractStatus(String extractStatus) {
        this.extractStatus = extractStatus;
    }

    public List<PayListBean> getPayList() {
        return payList;
    }

    public void setPayList(List<PayListBean> payList) {
        this.payList = payList;
    }

    public static class PayListBean {
        /**
         * orderId : 100196_20170719155450358_513
         * programId : 100196
         * buyerUserId : 513
         * amount : 0.01
         * tradeStatus : SUCCESS
         * status : null
         * createTime : 1500450890000
         * updateTime : null
         * userPic : http://oimas2nso.bkt.clouddn.com/2017-07-07_VsX4xI61.png
         * userName : 希特勒
         * nickName : 医宝341895
         * postion : null
         * company : null
         * department : 重症医学科
         */

        private String orderId;
        private int programId;
        private int buyerUserId;
        private double amount;
        private String tradeStatus;
        private Object status;
        private long createTime;
        private Object updateTime;
        private String userPic;
        private String userName;
        private String nickName;
        private Object postion;
        private Object company;
        private String department;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getProgramId() {
            return programId;
        }

        public void setProgramId(int programId) {
            this.programId = programId;
        }

        public int getBuyerUserId() {
            return buyerUserId;
        }

        public void setBuyerUserId(int buyerUserId) {
            this.buyerUserId = buyerUserId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
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

        public Object getPostion() {
            return postion;
        }

        public void setPostion(Object postion) {
            this.postion = postion;
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
    }
}
