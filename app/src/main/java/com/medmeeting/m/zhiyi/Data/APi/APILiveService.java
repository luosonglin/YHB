package com.medmeeting.m.zhiyi.Data.APi;

import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.ExtractEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveLoginWebDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LivePayDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSettlementEntity;
import com.medmeeting.m.zhiyi.UI.Entity.LiveStream;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.TallageDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordForgetDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordModifyDto;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
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

    //GET /v1/anchor/liveRoom/{roomId} 获取直播房间详情
    @GET("/v1/anchor/liveRoom/{roomId}")
    Observable<HttpResult3<Object, LiveDetailInfoDto>> getLiveDetailInfo(@Path("roomId") Integer roomId);

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

    //PUT /v1/anchor/{roomId}/liveProgram 更新直播节目信息
    @PUT("/v1/anchor/{roomId}/liveProgram")
    Observable<HttpResult3> updateLiveProgram(@Body LiveDto liveDto);

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

    //PUT /v1/anchor/liveRoom 更新直播房间信息
    @PUT("/v1/anchor/liveRoom")
    Observable<HttpResult3> updateLiveRoom(@Body LiveRoomDto liveRoomDto);

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

    //DELETE /v1/anchor/{roomId}/liveProgram/{programId} 删除直播节目
    @DELETE("/v1/anchor/{roomId}/liveProgram/{programId}")
    Observable<HttpResult3> deleteProgram(@Path("programId") Integer programId);

    //GET /v1/userIm 获取用户IM信息
    @GET("/v1/userIm")
    Observable<HttpResult3<Object, RCUserDto>> getUserIm();

    //GET /v1/anchor/{roomId}/liveProgram/close/{programId} 关闭直播
    @GET("/v1/anchor/{roomId}/liveProgram/close/{programId}")
    Observable<HttpResult3> closeProgram(@Path("programId") Integer programId);

    //GET /v1/anchor/{roomId}/liveProgram/{programId} 获取直播节目详情
    @GET("/v1/anchor/{roomId}/liveProgram/{programId}")
    Observable<HttpResult3<Object, LiveDto>> getProgramDetail(@Path("programId") Integer programId);

    /**
     * 财务
     */
    //此API无可奈何，因后端无法收到支付宝回调
    //PUT /v1/payment/record/tradeStatus/{tradeId} 更新订单流水的交易状态
    @PUT("/v1/payment/record/tradeStatus/{tradeId}")
    Observable<HttpResult3<Object, Object>> updateOrderStatus(@Path("tradeId") String tradeId);

    //GET /v1/user/wallet/info 获取钱包信息
    @GET("/v1/user/wallet/info")
    Observable<HttpResult3<Object, WalletInfoDto>> getWalletInfo();

    //GET /v1/user/wallet/account 获取提现账户列表
    @GET("/v1/user/wallet/account")
    Observable<HttpResult3<WalletAccountDto, Object>> getWalletAccount();

    //POST /v1/user/wallet/info/password 钱包密码-第一次提交
    @POST("/v1/user/wallet/info/password")
    Observable<HttpResult3> setWalletPassword(@Body WalletPasswordDto walletPasswordDto);

    //PUT /v1/user/wallet/info/password 钱包密码-更新密码
    @PUT("/v1/user/wallet/info/password")
    Observable<HttpResult3> modifyWalletPassword(@Body WalletPasswordModifyDto walletPasswordDto);


    //GET /v1/users/authMessages 根据用户token发送验证码消息接口
    @GET("/v1/users/authMessages")
    Observable<HttpResult3> getAuthMessage();

    //PUT /v1/user/wallet/info/forgotPwd 钱包密码-忘记密码
    @PUT("/v1/user/wallet/info/forgotPwd")
    Observable<HttpResult3> forgetWalletPassword(@Body WalletPasswordForgetDto walletPasswordForgetDto);

    //PUT /v1/user/wallet/info/bankCard 更新-银行卡信息
    @PUT("/v1/user/wallet/info/bankCard")
    Observable<HttpResult3> setBankCard(@Body EditBankCardReqEntity editBankCardReqEntity);

    //PUT /v1/user/wallet/info/alipay 更新-支付宝信息
    @PUT("/v1/user/wallet/info/alipay")
    Observable<HttpResult3> setAlipay(@Body EditAlipayReqEntity editAlipayReqEntity);

    //GET /v1/user/wallet/backTallage/{tallageNum} 返回税及实际到账接口
    @GET("/v1/user/wallet/backTallage/{tallageNum}")
    Observable<HttpResult3<Object, TallageDto>> getTallage(@Path("tallageNum") double tallageNum);

    //POST /v1/user/wallet/finance 财务_提现
    @POST("/v1/user/wallet/finance")
    Observable<HttpResult3> withdraw(@Body ExtractEntity extractEntity);

    //GET /v1/anchor/livePay/{programId}/settlement 获取直播节目的结算信息
    @GET("/v1/anchor/livePay/{programId}/settlement")
    Observable<HttpResult3<Object, LiveSettlementEntity>> getLiveSettlement(@Path("programId") Integer programId);

    //POST /v1/anchor/livePay/{programId}/settlement 新增直播节目结算
    @POST("/v1/anchor/livePay/{programId}/settlement")
    Observable<HttpResult3> postLiveSettlement(@Path("programId") Integer programId);

    /**
     * 回播
     */
    //直播-公开-直播标签房间节目 GET /v1/open/liveLabel 获取直播标签列表 获取直播分类标签
    @GET("v1/open/liveLabel")
    Observable<HttpResult3<TagDto, Object>> getVideoTags(@QueryMap Map<String, Integer> map);

    //POST /v1/open/video/page 获取视频列表-分页
    @POST("/v1/open/video/page")
    Observable<HttpResult3<VideoListEntity, Object>> getVideos(@Body VideoListSearchEntity videoListSearchEntity);

    //GET /v1/open/video/details/{videoId} 获取视频详情
    @GET("/v1/open/video/details/{videoId}")
    Observable<HttpResult3<Object, VideoDetailsEntity>> getVideoDetail(@Path("videoId") Integer videoId);
}
