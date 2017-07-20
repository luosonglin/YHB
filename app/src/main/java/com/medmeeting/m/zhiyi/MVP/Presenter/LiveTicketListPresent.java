package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.LiveTicketListModel;
import com.medmeeting.m.zhiyi.MVP.View.LiveTicketListView;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;

import java.util.List;

public class LiveTicketListPresent implements OnLoadDataListListener<List<LiveTicketDto.PayListBean>> {
    private LiveTicketListView mView;
    private LiveTicketListModel mModel;
    private boolean isjz;

    public LiveTicketListPresent(LiveTicketListView mView) {
        this.mView = mView;
        this.mModel = new LiveTicketListModel();
        mView.showProgress();
    }

    /**
     * 全部直播列表
     * @param isjz
     * @param programId
     */
    public void LoadData(boolean isjz, Integer programId) {
        this.isjz = isjz;
        mModel.LoadData(this, programId);
    }

    @Override
    public void onSuccess(List<LiveTicketDto.PayListBean> data) {
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
