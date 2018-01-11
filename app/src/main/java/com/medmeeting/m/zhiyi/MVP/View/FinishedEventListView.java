package com.medmeeting.m.zhiyi.MVP.View;

import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;

import java.util.List;

public interface FinishedEventListView {
    //显示加载页
    void showProgress2();
    //关闭加载页
    void hideProgress2();
    //加载新数据
    void newDatas2(List<VAppMyEvents> newsList);
    //添加更多数据
    void addDatas2(List<VAppMyEvents> addList);
    //显示加载失败
    void showLoadFailMsg2();
    //显示已加载所有数据
    void showLoadCompleteAllData2();
    //显示无数据
    void showNoData2();
}
