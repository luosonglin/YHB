package com.medmeeting.m.zhiyi.Data.APi;


import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpCodeDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpDto;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * API接口
 * 因为使用RxCache作为缓存策略 所以这里不需要写缓存信息
 */
public interface APIService {

    //test 获取banner
    @GET("api/v1/banner/meeting")
    Observable<HttpResult<BannerDto>> getBannerList();

    //获取验证码短信
    @GET("/api/v1/userinfos/{phone}")
    Observable<SignUpCodeDto> getPhoneCode(@Path("phone") String phone);

    //登录注册
    @POST("/api/v1/userinfos/")
    Observable<SignUpDto> LoginByPhoneCode(@QueryMap Map<String, Object> options);

    //热门会议
    @GET("/phoneEvent/getHotEvents")
    Observable<MeetingDto> getHotMeetings(@Query("pageNum")Integer pageNum, @Query("pageSize")Integer pageSize);
}
