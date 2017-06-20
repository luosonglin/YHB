package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;

import rx.Observer;

public class LiveListModel {
    private static final String TAG = LiveListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, LiveSearchDto2 liveSearchDto) {
        HttpData.getInstance().HttpDataGetAllLives(new Observer<LiveDto>() {
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
            public void onNext(LiveDto data) {
                listener.onSuccess(data.getData());
                Log.e(TAG, "onNext");
            }
        }, liveSearchDto);
    }
}
