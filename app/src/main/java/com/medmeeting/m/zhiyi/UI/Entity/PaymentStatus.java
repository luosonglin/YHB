package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class PaymentStatus {

    /**
     * payStatus : [{"alipay":true,"line":false,"wechat":true}]
     * line : {"bank":{"bankNum":"","tel":"","bankTitle":""}}
     */

    private LineBean line;
    private List<PayStatusBean> payStatus;

    public LineBean getLine() {
        return line;
    }

    public void setLine(LineBean line) {
        this.line = line;
    }

    public List<PayStatusBean> getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(List<PayStatusBean> payStatus) {
        this.payStatus = payStatus;
    }

    public static class LineBean {
        /**
         * bank : {"bankNum":"","tel":"","bankTitle":""}
         */

        private BankBean bank;

        public BankBean getBank() {
            return bank;
        }

        public void setBank(BankBean bank) {
            this.bank = bank;
        }

        public static class BankBean {
            /**
             * bankNum :
             * tel :
             * bankTitle :
             */

            private String bankNum;
            private String tel;
            private String bankTitle;

            public String getBankNum() {
                return bankNum;
            }

            public void setBankNum(String bankNum) {
                this.bankNum = bankNum;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getBankTitle() {
                return bankTitle;
            }

            public void setBankTitle(String bankTitle) {
                this.bankTitle = bankTitle;
            }
        }
    }

    public static class PayStatusBean {
        /**
         * alipay : true
         * line : false
         * wechat : true
         */

        private boolean alipay;
        private boolean line;
        private boolean wechat;

        public boolean isAlipay() {
            return alipay;
        }

        public void setAlipay(boolean alipay) {
            this.alipay = alipay;
        }

        public boolean isLine() {
            return line;
        }

        public void setLine(boolean line) {
            this.line = line;
        }

        public boolean isWechat() {
            return wechat;
        }

        public void setWechat(boolean wechat) {
            this.wechat = wechat;
        }
    }
}
