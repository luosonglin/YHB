package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult4;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 会议API
 */
public interface APIMeetingService {
    //热门会议
    @GET("/phoneEvent/getHotEvents")
    Observable<HttpResult4<MeetingDto>> getHotMeetings(@Query("pageNum")Integer pageNum, @Query("pageSize")Integer pageSize);

    //待参会
    @GET("/phoneEvent/getWaitEntryEvents")
    Observable<HttpResult4<MeetingDto>> getWaitEntryEvents(@QueryMap Map<String, Object> map);

    //已结束
    @GET("/phoneEvent/getFollowEvents")
    Observable<HttpResult4<FollowFinishedEvent>> getFollowFinishedEvents(@QueryMap Map<String, Object> map);

    //查询手机是否绑定
    @GET("/mobile/v1/checkRegisterPhone")
    Observable<HttpResult4> checkRegisterPhone(@QueryMap Map<String, Object> map);//?userId=21

    //判断会议状态
    @GET("/cRegiests/eventStatus")
    Observable<HttpResult4> getEventStatus(@QueryMap Map<String, Object> map);//?userId=21&eventId=172&type=regist

    //获取关注状态
    @GET("/phoneEvent/checkFollowEvent")
    Observable<HttpResult4> checkFollowEvent(@QueryMap Map<String, Object> map);//?userId=21&eventId=172

    //关注会议 关注? "N": "Y"
    @GET("/phoneEvent/followEvent")
    Observable<HttpResult4> followEvent(@QueryMap Map<String, Object> map);//?userId=21&eventId=172&follow=Y
}
