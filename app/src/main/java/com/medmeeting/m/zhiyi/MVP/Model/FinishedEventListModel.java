package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult4;

import java.util.Map;

import rx.Observer;

public class FinishedEventListModel {
    private static final String TAG = FinishedEventListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Map<String, Object> map){
        HttpData.getInstance().HttpDataGetMyFinishedMeeting(new Observer<HttpResult4<FollowFinishedEvent>>() {
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
            public void onNext(HttpResult4<FollowFinishedEvent> data) {
                listener.onSuccess(data.getPageInfo().getList());
                Log.e(TAG, "onNext");
            }
        }, map);
    }
}
