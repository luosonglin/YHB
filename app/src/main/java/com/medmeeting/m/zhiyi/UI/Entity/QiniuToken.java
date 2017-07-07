package com.medmeeting.m.zhiyi.UI.Entity;

public class QiniuToken {


    /**
     * code : 200
     * data : {"msg":"success","uploadToken":"r0_GHWBaOY4cbuUfQHAOsj0KoGAo_648Xc1SYCfe:Z1q7HJWcp2I-XMffScAkF72q-Es=:eyJzY29wZSI6ImFuZHJvaWQiLCJkZWFkbGluZSI6MTQ5MDk1MDMyNH0="}
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
         * uploadToken : r0_GHWBaOY4cbuUfQHAOsj0KoGAo_648Xc1SYCfe:Z1q7HJWcp2I-XMffScAkF72q-Es=:eyJzY29wZSI6ImFuZHJvaWQiLCJkZWFkbGluZSI6MTQ5MDk1MDMyNH0=
         */

        private String msg;
        private String uploadToken;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUploadToken() {
            return uploadToken;
        }

        public void setUploadToken(String uploadToken) {
            this.uploadToken = uploadToken;
        }
    }
}
