package com.medmeeting.m.zhiyi.MVP.Model;

import android.util.Log;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnLoadDataListListener;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;

import rx.Observer;

public class NewsListModel {
    private static final String TAG = NewsListModel.class.getSimpleName();

    public void LoadData(final OnLoadDataListListener listener, Integer pageNum, Integer pageSize){
        HttpData.getInstance().HttpDataGetNews(new Observer<BlogDto>() {
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
            public void onNext(BlogDto data) {
                listener.onSuccess(data.getBlog().getList());
                Log.e(TAG, "onNext");
            }
        }, pageNum, pageSize);
    }
}
