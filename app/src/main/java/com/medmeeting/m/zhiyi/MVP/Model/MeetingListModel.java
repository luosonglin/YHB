package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import rx.Observer;

public class MeetingListModel {
    private static final String TAG = MeetingListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Integer pageNum, Integer pageSize){
        HttpData.getInstance().HttpDataGetHotMeetings(new Observer<MeetingDto>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //设置页面为加载错误
                listener.onFailure(e);
                Log.e(TAG, "onError: "+e.getMessage()
                        +"\n"+e.getCause()
                        +"\n"+e.getLocalizedMessage()
                        +"\n"+e.getStackTrace());
            }

            @Override
            public void onNext(MeetingDto data) {
                listener.onSuccess(data.getPageInfo().getList());
                Log.e(TAG, "onNext");
            }
        }, pageNum, pageSize);
    }
}
