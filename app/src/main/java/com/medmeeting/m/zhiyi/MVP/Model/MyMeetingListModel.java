package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult4;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import java.util.Map;

import rx.Observer;

public class MyMeetingListModel {

    private static final String TAG = MyMeetingListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Map<String, Object> map){
        HttpData.getInstance().HttpDataGetMyMeeting(new Observer<HttpResult4<MeetingDto>>() {
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
            public void onNext(HttpResult4<MeetingDto> data) {
                listener.onSuccess(data.getPageInfo().getList());
                Log.e(TAG, "onNext");
            }
        }, map);
    }
}
