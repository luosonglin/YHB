package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.MeetingListModel;
import com.medmeeting.m.zhiyi.MVP.View.MeetingListView;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import java.util.List;

public class MeetingListPresent implements OnLoadDataListListener<List<MeetingDto.PageInfoBean.ListBean>> {
    private MeetingListView mView;
    private MeetingListModel mModel;
    private boolean isjz;
    public MeetingListPresent(MeetingListView mView) {
        this.mView = mView;
        this.mModel=new MeetingListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, int pageNum, int pageSize){
        this.isjz=isjz;
        mModel.LoadData(this, pageNum, pageSize);
    }

    @Override
    public void onSuccess(List<MeetingDto.PageInfoBean.ListBean> data) {
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

