package com.medmeeting.m.zhiyi.Data.API;

import com.medmeeting.m.zhiyi.UI.Entity.AccessToken;
import com.medmeeting.m.zhiyi.UI.Entity.AddVideoCommentEntity;
import com.medmeeting.m.zhiyi.UI.Entity.AdminEventActive;
import com.medmeeting.m.zhiyi.UI.Entity.BaseArea;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.BlogComment;
import com.medmeeting.m.zhiyi.UI.Entity.BlogVideoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.CollectType;
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.EditVideoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.EventBanner;
import com.medmeeting.m.zhiyi.UI.Entity.EventPrepayOrderRequestVO;
import com.medmeeting.m.zhiyi.UI.Entity.EventRegisterSwitchVO;
import com.medmeeting.m.zhiyi.UI.Entity.ExtractEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HospitalInfo;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.UI.Entity.IndexLabel;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAndVideoPayDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveLoginWebDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveProListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.LiveProgramDateilsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSettlementEntity;
import com.medmeeting.m.zhiyi.UI.Entity.LiveStream;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.UI.Entity.PaySettlement;
import com.medmeeting.m.zhiyi.UI.Entity.PushUserMessage;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.TallageDto;
import com.medmeeting.m.zhiyi.UI.Entity.UnifiedOrderResult;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddActivationEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddAuthenEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserAuthenRecord;
import com.medmeeting.m.zhiyi.UI.Entity.UserAuthorEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.UI.Entity.UserEditEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserEditPwdEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserGetInfoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserIdentity;
import com.medmeeting.m.zhiyi.UI.Entity.UserInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserRegEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;
import com.medmeeting.m.zhiyi.UI.Entity.Version;
import com.medmeeting.m.zhiyi.UI.Entity.VideoComment;
import com.medmeeting.m.zhiyi.UI.Entity.VideoCommentUserEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoInfo;
import com.medmeeting.m.zhiyi.UI.Entity.VideoInfoUserEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoSettlementEntity;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordForgetDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordModifyDto;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    @GET("v1/open/liveLabel?limit=1000")
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
    @GET("/v1/open/program/{programId}")
    Observable<HttpResult3<Object, LiveAudienceDetailDto>> getLiveProgramAudienceDetail(@Path("programId") Integer programId);

    //直播门票下单
    @POST("/v1/liveProgram/livePay")
    Observable<HttpResult3<Object, LiveAndVideoPayDto>> getLiveOrder(@Body LiveOrderDto liveOrderDto);

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

    //GET /v1/open/program/{programId} 获取直播节目详情
    @GET("/v1/open/program/{programId}")
    Observable<HttpResult3<Object, LiveProgramDateilsEntity>> getOpenProgramDetail(@Path("programId") Integer programId);

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


    ////////**********************
    ////////**********************
    ////////**********************
    ////////**********************
    //POST /v1/anchor/livePay/{programId}/settlement 新增直播节目结算
    @POST("/v1/anchor/livePay/{programId}/settlement")
    Observable<HttpResult3> postLiveSettlement(@Path("programId") Integer programId);

    /**
     * 回播
     */
    //直播-公开-直播标签房间节目 GET /v1/open/liveLabel 获取直播标签列表 获取直播分类标签
    @GET("v1/open/liveLabel")
    Observable<HttpResult3<TagDto, Object>> getVideoTags(@QueryMap Map<String, Object> map);

    //POST /v1/open/video/page 获取视频列表-分页 主页 相关视频页
    @POST("/v1/open/video/page")
    Observable<HttpResult3<VideoListEntity, Object>> getVideos(@Body VideoListSearchEntity videoListSearchEntity);

    //GET /v1/open/video/details/{videoId} 获取视频详情（观众）
    @GET("/v1/open/video/details/{videoId}")
    Observable<HttpResult3<Object, VideoDetailsEntity>> getVideoDetail(@Path("videoId") Integer videoId);

    //GET /v1/auchor/video/details/{videoId} 获取视频详情(主播自己的)
    @GET("/v1/auchor/video/details/{videoId}")
    Observable<HttpResult3<Object, VideoInfo>> getVideoDetail2(@Path("videoId") Integer videoId);

    //POST /v1/open/video/comment/page/{videoId} 获取视频评论一览
    @POST("/v1/open/video/comment/page/{videoId}")
    Observable<HttpResult3<VideoCommentUserEntity, Object>> getVideoComments(@Path("videoId") Integer videoId, @Body BasePageSearchEntity basePageSearchEntity);

    //POST /v1/video/comment/add/{videoId} 视频评论-新增
    @POST("/v1/video/comment/add/{videoId}")
    Observable<HttpResult3<Object, VideoComment>> addComment(@Path("videoId") Integer videoId, @Body AddVideoCommentEntity content);

    //POST /v1/users/collect/add 新增我的收藏记录
    @POST("/v1/users/collect/add")
    Observable<HttpResult3> collect(@Body UserCollect userCollect);

    //POST /v1/video/user/collect 我的收藏-视频
    @POST("/v1/video/user/collect")
    Observable<HttpResult3<VideoListEntity, Object>> getCollect(@Body BasePageSearchEntity basePageSearchEntity);

    //GET /v1/open/userRed/details/{userId} 获取红V主页信息
    @GET("/v1/open/userRed/details/{userId}")
    Observable<HttpResult3<Object, UserRedEntity>> getUserRedRoom(@Path("userId") Integer userId);

    // /v1/auchor/video/page 获取我的视频一览（分页）
    @POST("/v1/auchor/video/page")
    Observable<HttpResult3<VideoInfoUserEntity, Object>> getMyVideo(@Body BasePageSearchEntity basePageSearchEntity);

    //DELETE /v1/auchor/video/del/{videoId} 删除视频信息
    @DELETE("/v1/auchor/video/del/{videoId}")
    Observable<HttpResult3> deleteVideo(@Path("videoId") Integer videoId);

    //PUT /v1/auchor/video/edit 更新视频信息
    @PUT("/v1/auchor/video/edit")
    Observable<HttpResult3> updateVideo(@Body EditVideoEntity entity);

    //GET /v1/auchor/video/settlement/details/{videoId} 获取视频门票结算信息
    @GET("/v1/auchor/video/settlement/details/{videoId}")
    Observable<HttpResult3<Object, VideoSettlementEntity>> getSettlementDetail(@Path("videoId") Integer videoId);

    //GET /v1/auchor/video/settlement/list/{videoId} 获取视频门票结算列表
    @GET("/v1/auchor/video/settlement/list/{videoId}")
    Observable<HttpResult3<PaySettlement, Object>> getSettlementList(@Path("videoId") Integer videoId);

    //POST /v1/auchor/video/settlement/add/{videoId} 提交视频门票结算申请
    @POST("/v1/auchor/video/settlement/add/{videoId}")
    Observable<HttpResult3> addSettlement(@Path("videoId") Integer videoId);

    //POST /v1/video/pay 视频门票下单
    @POST("/v1/video/pay")
    Observable<HttpResult3<Object, LiveAndVideoPayDto>> payVideo(@Body VideoOrderDto videoOrderDto);

    //POST /v1/open/video/editPlayCount/{videoId} 更新视频播放次数+1
    @POST("/v1/open/video/editPlayCount/{videoId}")
    Observable<HttpResult3> editPlayCount(@Path("videoId") Integer videoId);

    //POST /v1/liveProgram/user/collect 我的收藏-直播节目
    @POST("/v1/liveProgram/user/collect")
    Observable<HttpResult3<LiveDto, Object>> getLiveCollect(@Body BasePageSearchEntity basePageSearchEntity);

    /**
     * 直播结算
     */
    //GET /v1/anchor/livePay/{programId}/settlement 获取直播节目的结算信息
    @GET("/v1/anchor/livePay/{programId}/settlement")
    Observable<HttpResult3<Object, LiveSettlementEntity>> getLiveSettlement(@Path("programId") Integer programId);


    //POST /v1/liveProgram/page 获取直播节目一栏-分页
    @POST("/v1/liveProgram/page")
    Observable<HttpResult3<LiveDto, Object>> getProgramLists(@Body LiveSearchDto basePageSearchEntity);

    //POST /v1/liveProgram/user/order 我的订单-直播节目
    @POST("/v1/liveProgram/user/order")
    Observable<HttpResult3<LiveDto, Object>> getMyPayLive(@Body BasePageSearchEntity basePageSearchEntity);

    //POST /v1/video/user/order 我的订单-视频
    @POST("/v1/video/user/order")
    Observable<HttpResult3<VideoListEntity, Object>> getMyPayVideo(@Body BasePageSearchEntity basePageSearchEntity);

    /**
     * 用户
     */
    //GET /v1/oauth/token/pwd 2.0新版本手机号+密码登录接口
    @GET("/v1/oauth/token/pwd")
    Observable<HttpResult3<Object, AccessToken>> loginByPwd(@QueryMap Map<String, Object> map);

    //GET /v1/users/app/info APP-获取个人信息
    @GET("/v1/users/app/info")
    Observable<HttpResult3<Object, UserInfoDto>> getUserInfo();

    //GET /v1/token/imageCode/read 读取图形验证码
    @GET("/v1/token/imageCode/read")
    Observable<ResponseBody> getImageCode(@QueryMap Map<String, Object> map);

    //GET /v1/token/code 2.0 App登录注册之前获取短信验证码接口
//    @Headers(NetConstants.ADD_COOKIE)
    @GET("/v1/token/code")
    Observable<HttpResult3> getCode(@QueryMap Map<String, Object> map);

    //GET /v1/oauth/token 2.0新版本手机号+验证码登录接口
    @GET("/v1/oauth/token")
    Observable<HttpResult3<Object, AccessToken>> loginByCode(@QueryMap Map<String, Object> map);

//    //GET /v1/msgs/codes 发送验证码接口
//    @GET("/v1/msgs/codes")
//    Observable<HttpResult3>

    //POST /v1/users/app/user/author APP-提交实名认证信息
    @POST("/v1/users/app/user/author")
    Observable<HttpResult3> authorization(@Body UserAuthorEntity userAuthorEntity);


    /**
     * 新闻
     */
    ///v1/app/Common/A/getLabels 标签模块_获取栏目标签接口
    @GET("/v1/app/Common/A/getLocalLabel")
    Observable<HttpResult3<Object, IndexLabel>> getLabels();

    //GET /v1/app/Home/E/findLabelBlogList 首页模块_获取首页相关标签新闻列表
    @GET("/v1/app/Home/E/findLabelBlogList")
    Observable<HttpResult3<Blog, Object>> findLabelBlogs(@QueryMap Map<String, Object> map);

//    //GET /v1/app/Home/A/getHomeBannerList 首页模块_获取首页banner接口
//    @GET("/v1/app/Home/A/getHomeBannerList")
//    Observable<HttpResult3<AdminEventActive, Object>> getHomeBannerList();

    //GET /v1/app/Home/B/greatEventList 首页模块_首页会议精选_查看会议精选列表
    @GET("/v1/app/Home/B/greatEventList")
    Observable<HttpResult3<Event, Object>> greatEventList();

    //GET /v1/app/Home/M/selectVideoLive 首页模块_查看直播列表
    @GET("/v1/app/Home/M/selectVideoLive")
    Observable<HttpResult3<LiveProListEntity, Object>> selectVideoLive();

    //GET /v1/app/Home/D/findGenBlogList 首页模块_首页普通新闻_查看会新闻列表
    @GET("/v1/app/Home/D/findGenBlogList")
    Observable<HttpResult3<Blog, Object>> findGenBlogList(@QueryMap Map<String, Object> map);

    /**
     * 新闻
     *
     * @param map
     * @return
     */
    //GET /v1/app/Home/I/getPicNews 首页模块_获取图文新闻
    @GET("/v1/app/Home/I/getPicNews")
    Observable<HttpResult3<Object, Blog>> getPicNews(@QueryMap Map<String, Object> map);

    //GET /v1/app/Home/H/getNewsCommentList 首页模块_获取该新闻的评论列表
    @GET("/v1/app/Home/H/getNewsCommentList")
    Observable<HttpResult3<BlogComment, Object>> getNewsCommentList(@QueryMap Map<String, Object> map);

    //POST /v1/app/Home/K/insertComment 首页模块_添加相关新闻评论
    @POST("/v1/app/Home/K/insertComment")
    Observable<HttpResult3> insertComment(@Body BlogComment blogComment);

    //POST /v1/app/Home/L/insertCollection 首页模块_收藏指定新闻
    @POST("/v1/app/Home/L/insertCollection")
    Observable<HttpResult3> insertCollection(@QueryMap Map<String, Object> map);

    //GET /v1/app/Home/N/selectCollectList 首页模块_查看收藏新闻列表
    @GET("/v1/app/Home/N/selectCollectList")
    Observable<HttpResult3<Blog, Object>> selectCollectList(@QueryMap Map<String, Object> map);

    //GET /v1/app/Home/J/getVideoNews 首页模块_获取视频新闻
    @GET("/v1/app/Home/J/getVideoNews")
    Observable<HttpResult3<Object, BlogVideoEntity>> getVideoNews(@QueryMap Map<String, Object> map);

    /**
     * 会议
     */
    //GET /v1/app/Event/B/getEventCollect 会议模块_获取用户会议收藏状态
    @GET("/v1/app/Event/B/getEventCollect")
    Observable<HttpResult3<Object, CollectType>> getEventCollect(@QueryMap Map<String, Object> map);

    //GET /v1/app/Event/C/getEventCollectList 会议模块_获取收藏列表
    @GET("/v1/app/Event/C/getEventCollectList")
    Observable<HttpResult3<Event, Object>> getEventCollectList(@QueryMap Map<String, Object> map);

    //GET /v1/app/Event/A/getAllEventList 会议模块_获取会议列表
    @GET("/v1/app/Event/A/getAllEventList")
    Observable<HttpResult3<Event, Object>> getAllEventList(@QueryMap Map<String, Object> map);

    //GET /v1/website/events/banners App获取banner轮播信息
    @GET("/v1/website/events/banners")
    Observable<HttpResult<EventBanner>> getMeetingBanner();

    //GET /v1/eventRegister/events/{eventId}/registerSwitch App获取会议报名开关信息接口
    @GET("/v1/eventRegister/events/{eventId}/registerSwitch")
    Observable<HttpResult3<Object, EventRegisterSwitchVO>> registerSwitch(@Path("eventId") Integer eventId);

//GET /v1/eventRegister/app/myEvents/{type} App获取我的待参会与已结束会议列表
    @GET("/v1/eventRegister/app/myEvents/{type}")
    Observable<HttpResult3<VAppMyEvents, Object>> getMyEvents(@Path("type") Integer type);


    /**
     * 搜索
     */
    //POST /v1/open/user/getRedUser 检索红V用户分页
    @POST("/v1/open/user/getRedUser")
    Observable<HttpResult3<UserRedEntity, Object>> getRedUser(@Body UserRedSearchEntity userRedEntity);

    //GET /v1/app/Home/G/findBlogList 首页模块_获取指定新闻列表
    @GET("/v1/app/Home/G/findBlogList")
    Observable<HttpResult3<Blog, Object>> findBlogList(@QueryMap Map<String, Object> map);

    //GET /v1/app/Home/F/findAllContentEventList 首页模块_获取会议搜索列表
    @GET("/v1/app/Home/F/findAllContentEventList")
    Observable<HttpResult3<Event, Object>> findAllContentEventList(@QueryMap Map<String, Object> map);

    /**
     * Banner
     */
    //GET /v1/app/Home/A/getHomeBannerList 首页模块_获取首页/会议banner接口
    @GET("/v1/app/Home/A/getHomeBannerList")
    Observable<HttpResult3<AdminEventActive, Object>> getHomeBannerList(@Query("type") String type);

    /**
     * 消息
     */
    //GET /v1/app/Home/O/selectAllPushList 首页模块_查看发送消息列表
    @GET("/v1/app/Home/O/selectAllPushList")
    Observable<HttpResult3<PushUserMessage, Object>> selectAllPushList(@QueryMap Map<String, Object> map);

    //DELETE /v1/app/Home/Q/deletePush 首页模块_查看发送消息列表
    @DELETE("/v1/app/Home/Q/deletePush")
    Observable<HttpResult3> deletePush(@Query("newsId") Integer newsId);


    /**
     * 会议支付
     */
    //POST /v1/events/{eventId}/orders/payType 微站中切换支付方式接口
    @POST("/v1/events/{eventId}/orders/payType")
    Observable<HttpResult3<Object, UnifiedOrderResult>> getPayType(@Body EventPrepayOrderRequestVO eventPrepayOrderRequestVO, @Path("eventId") Integer eventId);


    //GET /v1/website/events/{eventId}/shareInfo App获取会议分享信息
    @GET("/v1/website/events/{eventId}/shareInfo")
    Observable<HttpResult3<Object, Event>> getMeetingInfo(@Path("eventId") Integer eventId);

    /**
     * 用户模块
     */
//    POST /v1/users/activation/add 用户激活-提交激活身份信息
    @POST("/v1/users/activation/add")
    Observable<HttpResult3> activate(@Body UserAddActivationEntity userAddActivationEntity);

    //GET /v1/open/user/identity/authentication 用户身份-获取认证身份列
    @GET("/v1/open/user/identity/authentication")
    Observable<HttpResult3<UserIdentity, Object>> getAuthenticationTypes();

    //GET /v1/users/app/info APP-获取个人信息
    @GET("/v1/users/app/info")
    Observable<HttpResult3<Object, UserGetInfoEntity>> getUserInfo2();

    //POST /v1/users/authentication/add 用户认证-提交认证信息
    @POST("/v1/users/authentication/add")
    Observable<HttpResult3> authorize(@Body UserAddAuthenEntity userAddAuthenEntity);

    //GET /v1/users/authentication/last 用户认证-获取上一次认证记录信息
    @GET("/v1/users/authentication/last")
    Observable<HttpResult3<Object, UserAuthenRecord>> getLastAuthentizeStatus();

    //GET /v1/app/hospital/A/getHospitalDetail 获取医院名称
    @GET("/v1/app/hospital/A/getHospitalDetail")
    Observable<HttpResult3<HospitalInfo, Object>> getHospitalInfo(@Query("pojo") String word);

    //GET /v1/open/app/area/{parentId} 获取地区列表信息
    @GET("/v1/open/app/area/{parentId}")
    Observable<HttpResult3<BaseArea, Object>> getArea(@Path("parentId") Integer areaId);

    //PUT /v1/users/app/edit APP-更新用户信息
    @PUT("/v1/users/app/edit")
    Observable<HttpResult3> editUserInfo(@Body UserEditEntity userEditEntity);


    /**
     * android版本号
     */
    //GET /v1/open/app/version/android 获取android版本号
    @GET("/v1/open/app/version/android")
    Observable<HttpResult3<Object, Version>> getAndroidVersion();

    /**
     * 文件上传
     */
    //POST /v1/files 普通文件上传，通过file文件的形式
    @Multipart
    @POST("/v1/files")
    Observable<HttpResult6> uploadFile(@Part MultipartBody.Part file, @Part("description") RequestBody description);

    /**
     * 快速注册
     */
    //POST /v1/app/fast/reg APP-快速注册
    @POST("/v1/app/fast/reg")
    Observable<HttpResult3<Object, AccessToken>> fastSignUp(@Body UserRegEntity userRegEntity);

    /**
     * 用户标签
     */
    //    POST /v1/users/label/edit 用户标签-更新
    @POST("/v1/users/label/edit")
    Observable<HttpResult3> userSubject(@Path("labelIds") String labelIds);


    //PUT /v1/users/app/user/editPwd APP-用户忘记密码
    Observable<HttpResult3> editPwd(@Body UserEditPwdEntity userEditPwdEntity);
}
