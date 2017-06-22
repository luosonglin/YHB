package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.LiveDetailListModel;
import com.medmeeting.m.zhiyi.MVP.View.LiveDetailListView;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;

import java.util.List;

public class LiveDetailListPresent implements OnLoadDataListListener<List<LiveDetailDto.EntityBean.ProgramListBean>> {
    private LiveDetailListView mView;
    private LiveDetailListModel mModel;
    private boolean isjz;
    private Integer roomId;
    public LiveDetailListPresent(LiveDetailListView mView) {
        this.mView = mView;
        this.mModel=new LiveDetailListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, Integer roomId){
        this.isjz=isjz;
        mModel.LoadData(this, roomId);
    }

    @Override
    public void onSuccess(List<LiveDetailDto.EntityBean.ProgramListBean> data) {
        if(isjz){
            if(data.size()==0){
                mView.showLoadCompleteAllData();
            }else{
                //新增自动加载的的数据
                mView.addDatas(data);
            }
        }else{
            if(data.size()==0){
                mView.showNoData();
            }else{
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
