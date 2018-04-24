package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/03/2018 1:54 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class HttpResult6 {
    /**
     * status : success
     * msg : 上传到七牛云成功！
     * errorCode : 200
     * entity : null
     * data : null
     * total : null
     * pageNum : null
     * pageSize : null
     * totalPages : null
     * extra : {"absQiniuImgHash":"http://store-pic.medmeeting.com/FrJz1euOiSo6VQOUqY0Yhcz7u0E3","qiniuImgHash":"FrJz1euOiSo6VQOUqY0Yhcz7u0E3"}
     */

    private String status;
    private String msg;
    private String errorCode;
    private Object entity;
    private Object data;
    private Object total;
    private Object pageNum;
    private Object pageSize;
    private Object totalPages;
    private ExtraBean extra;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getTotal() {
        return total;
    }

    public void setTotal(Object total) {
        this.total = total;
    }

    public Object getPageNum() {
        return pageNum;
    }

    public void setPageNum(Object pageNum) {
        this.pageNum = pageNum;
    }

    public Object getPageSize() {
        return pageSize;
    }

    public void setPageSize(Object pageSize) {
        this.pageSize = pageSize;
    }

    public Object getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Object totalPages) {
        this.totalPages = totalPages;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public static class ExtraBean {
        /**
         * absQiniuImgHash : http://store-pic.medmeeting.com/FrJz1euOiSo6VQOUqY0Yhcz7u0E3
         * qiniuImgHash : FrJz1euOiSo6VQOUqY0Yhcz7u0E3
         */

        private String absQiniuImgHash;
        private String qiniuImgHash;

        public String getAbsQiniuImgHash() {
            return absQiniuImgHash;
        }

        public void setAbsQiniuImgHash(String absQiniuImgHash) {
            this.absQiniuImgHash = absQiniuImgHash;
        }

        public String getQiniuImgHash() {
            return qiniuImgHash;
        }

        public void setQiniuImgHash(String qiniuImgHash) {
            this.qiniuImgHash = qiniuImgHash;
        }
    }
}
