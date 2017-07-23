package com.medmeeting.m.zhiyi.Data.APi;


import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.UI.Entity.DoctorAuthentication;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpCodeDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpDto;

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

    //test 获取banner
    @GET("api/v1/banner/meeting")
    Observable<HttpResult<BannerDto>> getBannerList();

    //获取验证码短信
    @GET("/api/v1/userinfos/{phone}")
    Observable<SignUpCodeDto> getPhoneCode(@Path("phone") String phone);

    //登录注册
    @POST("/api/v1/userinfos/")
    Observable<SignUpDto> LoginByPhoneCode(@QueryMap Map<String, Object> options);

    // 推荐新闻帖子
    @GET("/api/v1/blogs/recommend/{pageNum}/{pageSize}")
    Observable<HttpResult<BlogDto>> getRecommendBlogList(@Path("pageNum") Integer pageNum, @Path("pageSize") Integer pageSize);

    //个人重心
    @GET("/api/v1/app/{user_id}")
    Observable<MyInfoDto> getMyInformation(@Path("user_id") Integer userId);

    //获取七牛云存储的uploadToken
    @GET("/api/v1/qiniu/{bucketName}/")
    Observable<QiniuTokenDto> getQiniuToken(@Path("bucketName") String bucketName);

    //医师认证
    @PUT("/api/v1/userinfos/")
    Observable<DoctorAuthentication> Authentication(@QueryMap Map<String, Object> options);

}
