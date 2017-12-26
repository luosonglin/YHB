package com.medmeeting.m.zhiyi.Data.API;


import com.medmeeting.m.zhiyi.UI.Entity.DoctorAuthentication;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VersionDto;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * API接口
 * 因为使用RxCache作为缓存策略 所以这里不需要写缓存信息
 */
public interface APIService {

    //获取七牛云存储的uploadToken
    @GET("/api/v1/qiniu/{bucketName}/")
    Observable<QiniuTokenDto> getQiniuToken(@Path("bucketName") String bucketName);

    //医师认证
    @PUT("/api/v1/userinfos/")
    Observable<DoctorAuthentication> Authentication(@QueryMap Map<String, Object> options);

    //修改头像
    @PUT("/api/v1/userinfos/avatar")
    Observable<MyInfoDto> updateAvatar(@QueryMap Map<String, Object> options);

    //GET /api/v1/android_version 获取Android version
    @GET("/api/v1/android_version")
    Observable<HttpResult<VersionDto>> getLatestAndroidVersion();

    //POST /api/v1/android_version/log 添加新的apk版本记录
    @POST("/api/v1/android_version/log")
    Observable<HttpResult> addLatestUpdateLog(@QueryMap Map<String, Object> options);

    ///api/v1/android_version/phoneInfo 手机信息
    @POST("/api/v1/android_version/phoneInfo")
    Observable<HttpResult> addNewPhoneInfo(@QueryMap Map<String, Object> options);
}