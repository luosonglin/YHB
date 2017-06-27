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
}
