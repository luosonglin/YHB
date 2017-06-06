package com.medmeeting.m.zhiyi.MVP.View;


import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;

import java.util.List;

public interface BannerListView {

    //显示加载页
    void showProgress();
    //关闭加载页
    void hideProgress();
    //加载新数据
    void newDatas(List<BannerDto.BannersBean> newsList);
    //添加更多数据
    void addDatas(List<BannerDto.BannersBean> addList);
    //显示加载失败
    void showLoadFailMsg();
    //显示已加载所有数据
    void showLoadCompleteAllData();
    //显示无数据
    void showNoData();

}
