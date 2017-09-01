package com.medmeeting.m.zhiyi.UI.Entity;

public class VersionDto {

    /**
     * version : {"version":"2.0.0","isForce":0,"log":"1、新增直播模块","url":null,"createdAt":1504168037000}
     */

    private VersionBean version;

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class VersionBean {
        /**
         * version : 2.0.0
         * isForce : 0
         * log : 1、新增直播模块
         * url : null
         * createdAt : 1504168037000
         */

        private String version;
        private int isForce;
        private String log;
        private String url;
        private long createdAt;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getIsForce() {
            return isForce;
        }

        public void setIsForce(int isForce) {
            this.isForce = isForce;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }
}
