package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.HttpResult2;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTagDto;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 直播API
 * http://hd.medmeeting.com
 */
public interface APILiveService {
    //获取直播分类标签
    @GET("/healife/vido/getVidoLabelList")
    Observable<HttpResult2<LiveTagDto>> getLiveTagList();

    //获取直播节目一栏-分页
    //全部
    @POST("/v1/liveProgram/page")
    Observable<HttpResult3<LiveDto>> getAllLiveList(@Body LiveSearchDto2 liveSearchDto);

    @POST("/v1/liveProgram/page")
    Observable<LiveDto> getLiveList(@Body LiveSearchDto liveSearchDto);
}
