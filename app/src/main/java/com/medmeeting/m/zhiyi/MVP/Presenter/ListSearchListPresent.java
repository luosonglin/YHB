package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.LiveSearchListModel;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;

import java.util.List;

public class ListSearchListPresent implements OnLoadDataListListener<List<LiveDto>> {
    private LiveListView mView;
    private LiveSearchListModel mModel;
    private boolean isjz;

    public ListSearchListPresent(LiveListView mView) {
        this.mView = mView;
        this.mModel = new LiveSearchListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, LiveSearchDto liveSearchDto) {
        this.isjz = isjz;
        mModel.LoadData(this, liveSearchDto);
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
