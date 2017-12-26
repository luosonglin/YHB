package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class LiveSettlementEntity {

    /**
     * totalAmount : 256
     * feeAmount : 51.2
     * actualAmount : 204.8
     * settlementFlag : true
     * payList : [{"orderId":"100175_20171013111537497_269","programId":100175,"buyerUserId":269,"amount":156,"tradeStatus":"FINISHED","status":null,"createTime":1507864537000,"updateTime":null,"userPic":"https://wx.qlogo.cn/mmopen/GPyw0pGicibl65wQBtRrpLdHxTYx3WAKrQicvLLG36yqtKvb9p6RibylLMSiaPG5xFdeuK7O3bYyF3Yw14bxzWVkSJibfsOGiaWic6nib/0","userName":"观众2222","nickName":"刘云","postion":"外科","company":null,"department":"","mobilePhone":null},{"orderId":"100175_20171013111433512_181","programId":100175,"buyerUserId":181,"amount":100,"tradeStatus":"FINISHED","status":null,"createTime":1507864473000,"updateTime":null,"userPic":"https://q.qlogo.cn/qqapp/1105801178/2DDA378B1615DCFB5814EAB494F5CDFA/100","userName":"观众2221","nickName":"淡定","postion":"胸部肿瘤科","company":null,"department":"","mobilePhone":null}]
     */

    private double totalAmount;
    private double feeAmount;
    private double actualAmount;
    private boolean settlementFlag;
    private List<PayListBean> payList;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public boolean isSettlementFlag() {
        return settlementFlag;
    }

    public void setSettlementFlag(boolean settlementFlag) {
        this.settlementFlag = settlementFlag;
    }

    public List<PayListBean> getPayList() {
        return payList;
    }

    public void setPayList(List<PayListBean> payList) {
        this.payList = payList;
    }

    public static class PayListBean {
        /**
         * orderId : 100175_20171013111537497_269
         * programId : 100175
         * buyerUserId : 269
         * amount : 156
         * tradeStatus : FINISHED
         * status : null
         * createTime : 1507864537000
         * updateTime : null
         * userPic : https://wx.qlogo.cn/mmopen/GPyw0pGicibl65wQBtRrpLdHxTYx3WAKrQicvLLG36yqtKvb9p6RibylLMSiaPG5xFdeuK7O3bYyF3Yw14bxzWVkSJibfsOGiaWic6nib/0
         * userName : 观众2222
         * nickName : 刘云
         * postion : 外科
         * company : null
         * department :
         * mobilePhone : null
         */

        private String orderId;
        private int programId;
        private int buyerUserId;
        private double amount;
        private String tradeStatus;
        private String status;
        private long createTime;
        private long updateTime;
        private String userPic;
        private String userName;
        private String nickName;
        private String postion;
        private String company;
        private String department;
        private String mobilePhone;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
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

        public String getPostion() {
            return postion;
        }

        public void setPostion(String postion) {
            this.postion = postion;
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

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }
    }
}
