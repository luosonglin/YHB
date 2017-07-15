package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.MyLiveProgramListModel;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;

import java.util.List;

public class MyLiveProgramListPresent implements OnLoadDataListListener<List<LiveDto>> {
    private LiveListView mView;
    private MyLiveProgramListModel mModel;
    private boolean isjz;

    public MyLiveProgramListPresent(LiveListView mView) {
        this.mView = mView;
        this.mModel = new MyLiveProgramListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, Integer roomId) {
        this.isjz = isjz;
        mModel.LoadData(this, roomId);
    }

    @Override
    public void onSuccess(List<LiveDto> data) {
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
