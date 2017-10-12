package com.medmeeting.m.zhiyi.Data.HttpData;


import android.util.Log;

import com.medmeeting.m.zhiyi.Data.APi.APILiveService;
import com.medmeeting.m.zhiyi.Data.APi.APIMeetingService;
import com.medmeeting.m.zhiyi.Data.APi.APIService;
import com.medmeeting.m.zhiyi.Data.APi.CacheProviders;
import com.medmeeting.m.zhiyi.Data.Retrofit.ApiException;
import com.medmeeting.m.zhiyi.Data.Retrofit.RetrofitUtils;
import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.UI.Entity.DoctorAuthentication;
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult4;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult5;
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
import com.medmeeting.m.zhiyi.UI.Entity.LiveStream;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.PaymentStatus;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpCodeDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.TallageDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VersionDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordForgetDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordModifyDto;
import com.medmeeting.m.zhiyi.Util.FileUtil;

import java.io.File;
import java.util.Map;

import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/*
 *所有的请求数据的方法集中地
 * 根据MovieService的定义编写合适的方法
 */
public class HttpData extends RetrofitUtils {

    private static File cacheDirectory = FileUtil.getcacheDirectory();
    private static final CacheProviders providers = new RxCache.Builder()
            .persistence(cacheDirectory)
            .using(CacheProviders.class);

    protected static final APIService service = getRetrofit().create(APIService.class);
    protected static final APIMeetingService service_meeting = getMeetingRetrofit().create(APIMeetingService.class);
    protected static final APILiveService service_live = getLiveRetrofit().create(APILiveService.class);

    private static final String TAG = HttpData.class.getSimpleName();

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpData INSTANCE = new HttpData();
    }

    //获取单例
    public static HttpData getInstance() {
        Log.e("HttpData", "getInstance");
        return SingletonHolder.INSTANCE;
    }

//    //Get请求  视频列表
//    public void verfacationCodeGetCache(Observer<List<VideoListDto>> observer) {
//        Observable observable=service.getVideoList().map(new HttpResultFunc<List<VideoListDto>>());
//        Observable observableCahce=providers.getVideoList(observable,new DynamicKey("视频列表"),new EvictDynamicKey(false)).map(new HttpResultFuncCcche<List<VideoListDto>>());
//        setSubscribe(observableCahce,observer);
//    }
//
//    //post请求 学校列表
//    public void HttpDataToSchoolList(String type, int pageIndex, Observer<List<BookListDto>> observer){
//        Observable observable=service.getBookList(type,pageIndex).map(new HttpResultFunc<List<BookListDto>>());
//        Observable observableCahce=providers.getBookList(observable,new DynamicKey("书籍列表"+pageIndex+type),new EvictDynamicKey(false)).map(new HttpResultFuncCcche<List<BookListDto>>());
//        setSubscribe(observableCahce,observer);
//    }

    //get test banner
    public void HttpDataGetBanner(Observer<BannerDto> observer) {
        Observable observable = service.getBannerList().map(new HttpResultFunc<BannerDto>());
        setSubscribe(observable, observer);
//        Observable observableCache = providers.getBannerList(observable, new DynamicKey("banner测试"), new EvictDynamicKey(false)).map(new HttpResultFuncCcche<BannerDto>());
//        Log.e(TAG, "HttpDataGetBanner");
//        setSubscribe(observableCache, observer);
    }

    //update user avatar
    public void HttpDataUpdateAvatar(Observer<MyInfoDto> observer, Map<String, Object> map) {
        Observable observable = service.updateAvatar(map);
        setSubscribe(observable, observer);
    }

    //get test get phone code
    public void HttpDataGetPhoneCode(Observer<SignUpCodeDto> observer, String phone) {
        Observable observable = service.getPhoneCode(phone);
        setSubscribe(observable, observer);
    }

    //test login
    public void HttpDataLogin(Observer<SignUpDto> observer, Map<String, Object> map) {
        Observable observable = service.LoginByPhoneCode(map);
        setSubscribe(observable, observer);
    }

    public void HttpDataLoginByPassword(Observer<SignUpDto> observer, Map<String, Object> map) {
        Observable observable = service.LoginByPhonePassword(map);
        setSubscribe(observable, observer);
    }

    //个人中心
    public void HttpDataGetMyInfo(Observer<MyInfoDto> observer, Integer userId) {
        Observable observable = service.getMyInformation(userId);
        setSubscribe(observable, observer);
    }

    //我的参会 待参会
    public void HttpDataGetMyMeeting(Observer<HttpResult4<MeetingDto>> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getWaitEntryEvents(map);
        setSubscribe(observable, observer);
    }

    //我的参会 已结束
    public void HttpDataGetMyFinishedMeeting(Observer<HttpResult4<FollowFinishedEvent>> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getFollowFinishedEvents(map);
        setSubscribe(observable, observer);
    }

    //test get hot meetings
    public void HttpDataGetHotMeetings(Observer<HttpResult4<MeetingDto>> observer, Integer pageNum, Integer pageSize) {
        Observable observable = service_meeting.getHotMeetings(pageNum, pageSize);
        setSubscribe(observable, observer);
//        Observable observableCache = providers.getMeetingList(observable, new DynamicKey("热门会议"), new EvictDynamicKey(false));
//        setSubscribe(observableCache, observer);
        Log.e(TAG, "HttpDataGetHotMeetings: " + observable);
    }

    /**
     * 以下用于会议详情支付页
     */
    public void HttpDataGetEventStatus(Observer<HttpResult4> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getEventStatus(map);
        setSubscribe(observable, observer);
    }

    public void HttpDataGetPayInfo(Observer<HttpResult4> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getPayInfo(map);
        setSubscribe(observable, observer);
    }


    public void HttpDataCheckFollowEvent(Observer<HttpResult4> observer, Map<String, Object> map) {
        Observable observable = service_meeting.checkFollowEvent(map);
        setSubscribe(observable, observer);
    }

    public void HttpDataFollowEvent(Observer<HttpResult4> observer, Map<String, Object> map) {
        Observable observable = service_meeting.followEvent(map);
        setSubscribe(observable, observer);
    }

    /**
     * 以下用于会议报名页
     */
    public void HttpDataGetPaymentStatus(Observer<PaymentStatus> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getPaymentStatus(map);
        setSubscribe(observable, observer);
    }

    //test get recommending blog list
    public void HttpDataGetNews(Observer<BlogDto> observer, Integer pageNum, Integer pageSize) {
        Observable observable = service.getRecommendBlogList(pageNum, pageSize).map(new HttpResultFunc<BlogDto>());
//        Observable observableCache = providers.getRecommendNews(observable, new DynamicKey("推荐新闻帖子"), new EvictDynamicKey(false));
//        setSubscribe(observableCache, observer);
        setSubscribe(observable, observer);
    }

    //get live tags
    public void HttpDataGetLiveTags(Observer<HttpResult3<TagDto, Object>> observer) {
        Observable observable = service_live.getTags();
//        Observable observableCache = providers.getLiveTagList(observable, new DynamicKey("live标签"), new EvictDynamicKey(false));
        setSubscribe(observable, observer);
    }

    //test live list
    public void HttpDataGetAllLives(Observer<HttpResult3<LiveDto, Object>> observer, LiveSearchDto2 liveSearchDto) {
        Observable observable = service_live.getAllLiveList(liveSearchDto);
//        Observable observableCache = providers.getLiveList(observable, new DynamicKey("live直播列表"), new EvictDynamicKey(false));
        setSubscribe(observable, observer);
    }

    //get live detail
    public void HttpDataGetLiveDetail(Observer<LiveDetailDto> observer, Integer roomId) {
        Observable observable = service_live.getLiveDetail(roomId);
        setSubscribe(observable, observer);
    }

    public void HttpDataGetLiveDetailInfo(Observer<HttpResult3<Object, LiveDetailInfoDto>> observer, Integer roomId) {
        Observable observable = service_live.getLiveDetailInfo(roomId);
        setSubscribe(observable, observer);
    }

    //直播-主播-直播房间 GET /v1/anchor/liveRoom 获取主播房间列表
    public void HttpDataGetLiveRoom(Observer<HttpResult3<LiveRoomDto, Object>> observer) {
        Observable observable = service_live.getLiveRoom();
        setSubscribe(observable, observer);
    }

    //GET /v1/anchor/{roomId}/liveProgram 获取直播节目列表
    public void HttpDataGetLivePrograms(Observer<HttpResult3<LiveDto, Object>> observer, Integer roomId) {
        Observable observable = service_live.getLivePrograms(roomId);
        setSubscribe(observable, observer);
    }

    //search live
    public void HttpDataGetLives(Observer<HttpResult3<LiveDto, Object>> observer, LiveSearchDto liveSearchDto) {
        Observable observable = service_live.getLives(liveSearchDto);
        setSubscribe(observable, observer);
    }

    //live program detail 主播
    public void HttpDataGetLiveProgramDetail(Observer<HttpResult3<Object, LiveDto>> observer, Integer programId) {
        Observable observable = service_live.getLiveProgramDetail(programId);
        setSubscribe(observable, observer);
    }

    //live program detail 观众
    public void HttpDataGetLiveProgramAudienceDetail(Observer<HttpResult3<Object, LiveAudienceDetailDto>> observer, Integer programId) {
        Observable observable = service_live.getLiveProgramAudienceDetail(programId);
        setSubscribe(observable, observer);
    }

    //get live order
    public void HttpDataGetLiveOrder(Observer<HttpResult3<Object, LivePayDto>> observer, LiveOrderDto liveOrderDto) {
        Observable observable = service_live.getLiveOrder(liveOrderDto);
        setSubscribe(observable, observer);
    }

    //get qiniu token
    public void HttpDataGetQiniuToken(Observer<QiniuTokenDto> observer, String bucketName) {
        Observable observable = service.getQiniuToken(bucketName);
        setSubscribe(observable, observer);
    }

    //authentic
    public void HttpDataAuthentication(Observer<DoctorAuthentication> observer, Map<String, Object> options) {
        Observable observable = service.Authentication(options);
        setSubscribe(observable, observer);
    }

    //add live room
    public void HttpDataAddLiveRoom(Observer<HttpResult3> observer, LiveRoomDto liveRoomDto) {
        Observable observable = service_live.addLiveRoom(liveRoomDto);
        setSubscribe(observable, observer);
    }

    //update live room
    public void HttpDataUpdateLiveRoom(Observer<HttpResult3> observer, LiveRoomDto liveRoomDto) {
        Observable observable = service_live.updateLiveRoom(liveRoomDto);
        setSubscribe(observable, observer);
    }

    //delete live room
    public void HttpDataDeleteLiveRoom(Observer<HttpResult3> observer, Integer roomId) {
        Observable observable = service_live.deleteLiveRoom(roomId);
        setSubscribe(observable, observer);
    }

    //add live program
    public void HttpDataAddLiveProgram(Observer<HttpResult3> observer, Integer roomId, LiveDto liveDto) {
        Observable observable = service_live.addLiveProgram(roomId, liveDto);
        setSubscribe(observable, observer);
    }

    //update live program
    public void HttpDataUpdataLiveProgram(Observer<HttpResult3> observer, LiveDto liveDto) {
        Observable observable = service_live.updateLiveProgram(liveDto);
        setSubscribe(observable, observer);
    }

    //get user token
    public void HttpDataGetToken(Observer<HttpResult3<Object, UserTokenDto>> observer, Integer userId) {
        Observable observable = service_live.getUserToken(userId);
        setSubscribe(observable, observer);
    }

    //get my pay live list
    public void HttpDataGetMyPayLives(Observer<HttpResult3<LiveDto, Object>> observer) {
        Observable observable = service_live.getMyPayLives();
        setSubscribe(observable, observer);
    }

    //login web
    public void HttpDataLoginWeb(Observer<HttpResult3> observer, LiveLoginWebDto liveLoginWebDto) {
        Observable observable = service_live.loginWeb(liveLoginWebDto);
        setSubscribe(observable, observer);
    }

    //get live stream
    public void HttpDataGetLiveStream(Observer<HttpResult3<Object, LiveStream>> observer, Integer programId) {
        Observable observable = service_live.getLiveStream(programId);
        setSubscribe(observable, observer);
    }

    //get pay list
    public void HttpDataGetPayList(Observer<HttpResult3<Object, LiveTicketDto>> observer, Integer programId) {
        Observable observable = service_live.getPayList(programId);
        setSubscribe(observable, observer);
    }

    //extract
    public void HttpDataExtract(Observer<HttpResult3> observer, Integer programId) {
        Observable observable = service_live.extract(programId);
        setSubscribe(observable, observer);
    }

    //delete live program
    public void HttpDataDeleteProgram(Observer<HttpResult3> observer, Integer programId) {
        Observable observable = service_live.deleteProgram(programId);
        setSubscribe(observable, observer);
    }

    //get user im
    public void HttpDataGetUserIm(Observer<HttpResult3<Object, RCUserDto>> observer) {
        Observable observable = service_live.getUserIm();
        setSubscribe(observable, observer);
    }

    //close live program
    public void HttpDataCloseProgram(Observer<HttpResult3> observer, Integer programId) {
        Observable observable = service_live.closeProgram(programId);
        setSubscribe(observable, observer);
    }

    //get program detail
    public void HttpDataGetProgramDetail(Observer<HttpResult3<Object, LiveDto>> observer, Integer programId) {
        Observable observable = service_live.getLiveProgramDetail(programId);
        setSubscribe(observable, observer);
    }

    //get UserInfoByConfirmNum
    public void HttpDataGetUserInfoByConfirmNum(Observer<HttpResult5<UserInfoDto>> observer, Map<String, Object> map) {
        Observable observable = service_meeting.getUserInfoByConfirmNum(map);
        setSubscribe(observable, observer);
    }

    //get latest android version
    public void HttpDataGetLatestAndroidVersion(Observer<HttpResult<VersionDto>> observer) {
        Observable observable = service.getLatestAndroidVersion();
        setSubscribe(observable, observer);
    }

    //update live order status
    public void HttpDataUpdateLiveOrderStatus(Observer<HttpResult3<Object, Object>> observer, String tradeId) {
        Observable observable = service_live.updateOrderStatus(tradeId);
        setSubscribe(observable, observer);
    }

    //add update log
    public void HttpDataAddUpdataLog(Observer<HttpResult> observer, Map<String, Object> map) {
        Observable observable = service.addLatestUpdateLog(map);
        setSubscribe(observable, observer);
    }

    //add phone info
    public void HttpDataAddNewPhoneInfo(Observer<HttpResult> observer, Map<String, Object> map) {
        Observable observable = service.addNewPhoneInfo(map);
        setSubscribe(observable, observer);
    }

    public void HttpDataGetWalletInfo(Observer<HttpResult3<Object, WalletInfoDto>> observer) {
        Observable observable = service_live.getWalletInfo();
        setSubscribe(observable, observer);
    }

    public void HttpDataGetWalletAccount(Observer<HttpResult3<WalletAccountDto, Object>> observer) {
        Observable observable = service_live.getWalletAccount();
        setSubscribe(observable, observer);
    }

    public void HttpDataSetWalletPassword(Observer<HttpResult3> observer, WalletPasswordDto walletPasswordDto) {
        Observable observable = service_live.setWalletPassword(walletPasswordDto);
        setSubscribe(observable, observer);
    }

    public void HttpDataModifyWalletPassword(Observer<HttpResult3> observer, WalletPasswordModifyDto walletPasswordModifyDto) {
        Observable observable = service_live.modifyWalletPassword(walletPasswordModifyDto);
        setSubscribe(observable, observer);
    }

    public void HttpDataForgetWalletPassword(Observer<HttpResult3> observer, WalletPasswordForgetDto walletPasswordForgetDto) {
        Observable observable = service_live.forgetWalletPassword(walletPasswordForgetDto);
        setSubscribe(observable, observer);
    }

    public void HttpDataSetBankCard(Observer<HttpResult3> observer, EditBankCardReqEntity editBankCardReqEntity) {
        Observable observable = service_live.setBankCard(editBankCardReqEntity);
        setSubscribe(observable, observer);
    }

    public void HttpDataSetAlipay(Observer<HttpResult3> observer, EditAlipayReqEntity editAlipayReqEntity) {
        Observable observable = service_live.setAlipay(editAlipayReqEntity);
        setSubscribe(observable, observer);
    }

    public void HttpDataGetTallage(Observer<HttpResult3<Object, TallageDto>> observer, double num) {
        Observable observable = service_live.getTallage(num);
        setSubscribe(observable, observer);
    }

    public void HttpDataGetAuthMessage(Observer<HttpResult3> observer) {
        Observable observable = service_live.getAuthMessage();
        setSubscribe(observable, observer);
    }


    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        Log.e("HttpData", "setSubscribe");
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {

            if (httpResult.getCode() != 200) {
                throw new ApiException(httpResult);
            }

            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理RxCacha的结果
     */
    private class HttpResultFuncCcche<T> implements Func1<Reply<T>, T> {

        @Override
        public T call(Reply<T> httpResult) {
            return httpResult.getData();
        }
    }

    public class getInstance {
    }
}
