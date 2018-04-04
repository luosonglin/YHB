package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 04/04/2018 3:37 PM
 * @describe  ResponseData
 * @email iluosonglin@gmail.com
 * @org null
 */
public class HttpResult7<T, K, M> {
    private String status;
    private String msg;
    private String errorCode;
    private Object exp;
    private K entity;
    private int total;
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private int recordsTotal;
    private int recordsFiltered;
    private M extra;
    private List<T> data;


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

    public Object getExp() {
        return exp;
    }

    public void setExp(Object exp) {
        this.exp = exp;
    }

    public K getEntity() {
        return entity;
    }

    public void setEntity(K entity) {
        this.entity = entity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public M getExtra() {
        return extra;
    }

    public void setExtra(M extra) {
        this.extra = extra;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
