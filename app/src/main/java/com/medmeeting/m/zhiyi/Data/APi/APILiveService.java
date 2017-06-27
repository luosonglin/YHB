package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 直播API
 * http://hd.medmeeting.com
 */
public interface APILiveService {

    //获取直播节目一栏-分页
    //全部
    @POST("/v1/liveProgram/page")
    Observable<HttpResult3<LiveDto>> getAllLiveList(@Body LiveSearchDto2 liveSearchDto);

//    @POST("/v1/liveProgram/page")
//    Observable<LiveDto> getLiveList(@Body LiveSearchDto liveSearchDto);

    //获取直播房间详情，包括直播节目列表
    @GET("/v1/open/room/{roomId}")
    Observable<LiveDetailDto> getLiveDetail(@Path("roomId") Integer roomId);

    //获取主播房间列表
    @GET("/v1/anchor/liveRoom")
    Observable<HttpResult3<LiveRoomDto>> getLiveRoom();

    //GET /v1/anchor/{roomId}/liveProgram 获取直播节目列表
    @GET("/v1/anchor/{roomId}/liveProgram")
    Observable<HttpResult3<LiveDto>> getLivePrograms(@Path("roomId") Integer roomId);   //LiveDto需换

    //直播-公开-直播标签房间节目 GET /v1/open/liveLabel 获取直播标签列表 获取直播分类标签
    @GET("v1/open/liveLabel?limit=100")
    Observable<HttpResult3<TagDto>> getTags();

    //搜索
    @POST("v1/liveProgram/page")
    Observable<HttpResult3<LiveDto>> getLives(@Body LiveSearchDto liveSearchDto);
}
