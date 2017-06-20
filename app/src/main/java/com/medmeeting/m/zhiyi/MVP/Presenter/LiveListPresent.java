package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.LiveListModel;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;

import java.util.List;

public class LiveListPresent implements OnLoadDataListListener<List<LiveDto.DataBean>> {
    private LiveListView mView;
    private LiveListModel mModel;
    private boolean isjz;

    public LiveListPresent(LiveListView mView) {
        this.mView = mView;
        this.mModel = new LiveListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, LiveSearchDto2 liveSearchDto) {
        this.isjz = isjz;
        mModel.LoadData(this, liveSearchDto);
    }

    @Override
    public void onSuccess(List<LiveDto.DataBean> data) {
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
