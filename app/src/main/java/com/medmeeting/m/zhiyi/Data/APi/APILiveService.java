package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.HttpResult2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTagDto;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 直播API
 * http://hd.medmeeting.com
 */
public interface APILiveService {
    //获取直播分类标签
    @GET("/healife/vido/getVidoLabelList")
    Observable<HttpResult2<LiveTagDto>> getLiveTagList();
}
