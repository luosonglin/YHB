package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;

import rx.Observer;

public class LiveTicketListModel {
    private static final String TAG = LiveTicketListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Integer programId) {
        HttpData.getInstance().HttpDataGetPayList(new Observer<HttpResult3<Object, LiveTicketDto>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //设置页面为加载错误
                listener.onFailure(e);
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<Object, LiveTicketDto> data) {
//                listener.onSuccess(data.getData());
                listener.onSuccess(data.getEntity().getPayList());
                Log.e(TAG, "onNext");
            }
        }, programId);
    }
}
