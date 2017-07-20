package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveLoginWebDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LivePayDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveStream;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserTokenDto;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 直播API
 * http://hd.medmeeting.com
 */
public interface APILiveService {

    ///v1/token/{userId} 根据用户id获取token，为了兼容
    @GET("/v1/token/{userId}")
    Observable<HttpResult3<Object, UserTokenDto>> getUserToken(@Path("userId") Integer userId);

    //获取直播节目一栏-分页
    //全部
    @POST("/v1/liveProgram/page")
    Observable<HttpResult3<LiveDto, Object>> getAllLiveList(@Body LiveSearchDto2 liveSearchDto);

//    @POST("/v1/liveProgram/page")
//    Observable<LiveDto> getLiveList(@Body LiveSearchDto liveSearchDto);

    //获取直播房间详情，包括直播节目列表
    @GET("/v1/open/room/{roomId}")
    Observable<LiveDetailDto> getLiveDetail(@Path("roomId") Integer roomId);

    //获取主播房间列表
    @GET("/v1/anchor/liveRoom")
    Observable<HttpResult3<LiveRoomDto, Object>> getLiveRoom();

    //GET /v1/anchor/{roomId}/liveProgram 获取直播节目列表
    @GET("/v1/anchor/{roomId}/liveProgram")
    Observable<HttpResult3<LiveDto, Object>> getLivePrograms(@Path("roomId") Integer roomId);   //LiveDto需换

    //直播-公开-直播标签房间节目 GET /v1/open/liveLabel 获取直播标签列表 获取直播分类标签
    @GET("v1/open/liveLabel?limit=100")
    Observable<HttpResult3<TagDto, Object>> getTags();

    //搜索
    @POST("v1/liveProgram/page")
    Observable<HttpResult3<LiveDto, Object>> getLives(@Body LiveSearchDto liveSearchDto);

    //获取直播节目详情(主播)
    @GET("v1/anchor/{roomId}/liveProgram/{programId}")
    Observable<HttpResult3<Object, LiveDto>> getLiveProgramDetail(@Path("programId") Integer programId);

    //GET /v1/anchor/{roomId}/liveProgram/open/{programId} 开启直播，返回推流信息
    @GET("/v1/anchor/{roomId}/liveProgram/open/{programId}")
    Observable<HttpResult3<Object, LiveStream>> getLiveStream(@Path("programId") Integer programId);

    //获取直播节目详情(观众)
    @GET("v1/open/program/{programId}")
    Observable<HttpResult3<Object, LiveAudienceDetailDto>> getLiveProgramAudienceDetail(@Path("programId") Integer programId);

    //直播门票下单
    @POST("/v1/liveProgram/livePay")
    Observable<HttpResult3<Object, LivePayDto>> getLiveOrder(@Body LiveOrderDto liveOrderDto);

    //POST /v1/anchor/liveRoom 新增直播房间信息
    @POST("/v1/anchor/liveRoom")
    Observable<HttpResult3> addLiveRoom(@Body LiveRoomDto liveRoomDto);

    //DELETE /v1/anchor/liveRoom/{roomId} 删除直播房间
    @DELETE("/v1/anchor/liveRoom/{roomId}")
    Observable<HttpResult3> deleteLiveRoom(@Path("roomId") Integer roomId);

    //POST /v1/anchor/{roomId}/liveProgram 新增直播节目
    @POST("/v1/anchor/{roomId}/liveProgram")
    Observable<HttpResult3> addLiveProgram(@Path("roomId") Integer roomId, @Body LiveDto liveDto);

    //GET /v1/liveProgram/myPay 获取已购门票的直播列表
    @GET("/v1/liveProgram/myPay")
    Observable<HttpResult3<LiveDto, Object>> getMyPayLives();

    //POST /v1/open/user/scanCodeLogin 移动端确认登陆, [extend]字段传递:直播节目ID
    @POST("/v1/open/user/scanCodeLogin")
    Observable<HttpResult3> loginWeb(@Body LiveLoginWebDto liveLoginWebDto);

    //GET /v1/anchor/livePay/{programId}/list 获取直播节目的已购门票记录
    @GET("/v1/anchor/livePay/{programId}/list")
    Observable<HttpResult3<Object, LiveTicketDto>> getPayList(@Path("programId") Integer programId);

    //POST /v1/anchor/livePay/{programId}/extract 新增 节目门票 提现申请
    @POST("/v1/anchor/livePay/{programId}/extract")
    Observable<HttpResult3> extract(@Path("programId") Integer programId);
}
