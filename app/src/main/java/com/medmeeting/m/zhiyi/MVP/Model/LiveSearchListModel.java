package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;

import rx.Observer;

public class LiveSearchListModel {
    private static final String TAG = LiveSearchListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, LiveSearchDto liveSearchDto) {
        HttpData.getInstance().HttpDataGetLives(new Observer<HttpResult3<LiveDto, Object>>() {
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
            public void onNext(HttpResult3<LiveDto, Object> data) {
                listener.onSuccess(data.getData());
                Log.e(TAG, "onNext");
            }
        }, liveSearchDto);
    }
}
