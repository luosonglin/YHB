package com.medmeeting.m.zhiyi.MVP.Presenter;

import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.MVP.Model.TagListModel;
import com.medmeeting.m.zhiyi.MVP.View.TagListView;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;

import java.util.List;

public class TagListPresent implements OnLoadDataListListener<List<TagDto>> {
    private TagListView mView;
    private TagListModel mModel;
    private boolean isjz;
    public TagListPresent(TagListView mView) {
        this.mView = mView;
        this.mModel=new TagListModel();
        mView.showProgress();
    }

    public void LoadData(boolean isjz){
        this.isjz=isjz;
        mModel.LoadData(this);
    }

    @Override
    public void onSuccess(List<TagDto> data) {
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

