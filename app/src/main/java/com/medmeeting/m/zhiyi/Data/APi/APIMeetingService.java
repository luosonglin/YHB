package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 会议API
 */
public interface APIMeetingService {
    //热门会议
    @GET("/phoneEvent/getHotEvents")
    Observable<MeetingDto> getHotMeetings(@Query("pageNum")Integer pageNum, @Query("pageSize")Integer pageSize);
}
