package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;

import rx.Observer;

public class LiveDetailListModel {
    private static final String TAG = LiveDetailListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Integer roomId){
        HttpData.getInstance().HttpDataGetLiveDetail(new Observer<LiveDetailDto>() {
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
            public void onNext(LiveDetailDto data) {
                listener.onSuccess(data.getEntity().getProgramList());
                Log.e(TAG, "onNext");
            }
        }, roomId);
    }
}
