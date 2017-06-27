package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.MyMeetingListModel;
import com.medmeeting.m.zhiyi.MVP.View.MeetingListView;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import java.util.List;
import java.util.Map;

public class MyMeetingListPresent implements OnLoadDataListListener<List<MeetingDto>> {
    private MeetingListView mView;
    private MyMeetingListModel mModel;
    private boolean isjz;
    public MyMeetingListPresent(MeetingListView mView) {
        this.mView = mView;
        this.mModel=new MyMeetingListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz, Map<String, Object> map){
        this.isjz=isjz;
        mModel.LoadData(this, map);
    }

    @Override
    public void onSuccess(List<MeetingDto> data) {
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
