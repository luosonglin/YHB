package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.NewsListModel;
import com.medmeeting.m.zhiyi.MVP.View.NewsListView;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;

import java.util.List;

public class NewsListPresent implements OnLoadDataListListener<List<BlogDto.BlogBean.ListBean>> {
    private NewsListView mView;
    private NewsListModel mModel;
    private boolean isjz;

    public NewsListPresent(NewsListView mView) {
        this.mView = mView;
        this.mModel = new NewsListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, Integer pageNum, Integer pageSize) {
        this.isjz = isjz;
        mModel.LoadData(this, pageNum, pageSize);
    }

    @Override
    public void onSuccess(List<BlogDto.BlogBean.ListBean> data) {
        if (isjz) {
            if (data.size() == 0) {
                mView.showLoadCompleteAllData();
            } else {
                //新增自动加载的的数据
                mView.addDatas(data);
            }
        } else {
            if (data.size() == 0) {
                mView.showNoData();
            } else {
                mView.newDatas(data);
            }
        }
        mView.hideProgress();
    }

    @Override
    public void onFailure(Throwable e) {
        mView.showLoadFailMsg();
    }
}
