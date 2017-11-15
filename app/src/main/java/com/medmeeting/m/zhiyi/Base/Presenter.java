package com.medmeeting.m.zhiyi.Base;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
