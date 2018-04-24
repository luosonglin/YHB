package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;

import rx.Observer;

public class FinishedEventListModel {
    private static final String TAG = FinishedEventListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Integer type){
        HttpData.getInstance().HttpDataGetMyEvents(new Observer<HttpResult3<VAppMyEvents, Object>>() {
            @Override
            public void onCompleted() {

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
            public void onNext(HttpResult3<VAppMyEvents, Object> data) {
                listener.onSuccess(data.getData());
                Log.e(TAG, "onNext");
            }
        }, type);
    }
}
