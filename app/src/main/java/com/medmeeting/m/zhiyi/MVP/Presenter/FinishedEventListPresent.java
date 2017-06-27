package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.FinishedEventListModel;
import com.medmeeting.m.zhiyi.MVP.View.FinishedEventListView;
import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;

import java.util.List;
import java.util.Map;

public class FinishedEventListPresent implements OnLoadDataListListener<List<FollowFinishedEvent>> {
    private FinishedEventListView mView;
    private FinishedEventListModel mModel;
    private boolean isjz;
    public FinishedEventListPresent(FinishedEventListView mView) {
        this.mView = mView;
        this.mModel=new FinishedEventListModel();
        mView.showProgress2();
    }

    public void LoadData(boolean isjz, Map<String, Object> map){
        this.isjz=isjz;
        mModel.LoadData(this, map);
    }

    @Override
    public void onSuccess(List<FollowFinishedEvent> data) {
        if(isjz){
            if(data.size()==0){
                mView.showLoadCompleteAllData2();
            }else{
                //新增自动加载的的数据
                mView.addDatas2(data);
            }
        }else{
            if(data.size()==0){
                mView.showNoData2();
            }else{
                mView.newDatas2(data);
            }
        }
        mView.hideProgress2();
    }

    @Override
    public void onFailure(Throwable e) {
        mView.showLoadFailMsg2();
    }
}
