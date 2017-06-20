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
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTagDto;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpCodeDto;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpDto;
import com.medmeeting.m.zhiyi.Util.FileUtil;

import java.io.File;
import java.util.Map;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
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

//    protected static final APIService service_meeting = getMeetingRetrofit().create(APIService.class);

    protected static final APIService service = getRetrofit().create(APIService.class);
    protected static final APIMeetingService service_meeting = getMeetingRetrofit().create(APIMeetingService.class);
    protected static final APILiveService service_live = getLiveRetrofit().create(APILiveService.class);

    private static final String TAG = HttpData.class.getSimpleName();

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpData INSTANCE = new HttpData();
    }

    //获取单例
    public static HttpData getInstance(){
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
        Observable observableCache = providers.getBannerList(observable, new DynamicKey("banner测试"), new EvictDynamicKey(false)).map(new HttpResultFuncCcche<BannerDto>());
        Log.e(TAG, "HttpDataGetBanner");
        setSubscribe(observableCache, observer);
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

    //test get hot meetings
    public void HttpDataGetHotMeetings(Observer<MeetingDto> observer, Integer pageNum, Integer pageSize){
        Observable observable = service_meeting.getHotMeetings(pageNum, pageSize);
        setSubscribe(observable, observer);
//        Observable observableCache = providers.getMeetingList(observable, new DynamicKey("热门会议"), new EvictDynamicKey(false));
//        setSubscribe(observableCache, observer);
        Log.e(TAG, "HttpDataGetHotMeetings: " + observable);
    }

    //test get recommending blog list
    public void HttpDataGetNews(Observer<BlogDto> observer, Integer pageNum, Integer pageSize) {
        Observable observable = service.getRecommendBlogList(pageNum, pageSize).map(new HttpResultFunc<BlogDto>());
//        Observable observableCache = providers.getRecommendNews(observable, new DynamicKey("推荐新闻帖子"), new EvictDynamicKey(false));
//        setSubscribe(observableCache, observer);
        setSubscribe(observable, observer);
    }

    //get live tags
    public void HttpDataGetLiveTags(Observer<LiveTagDto> observer) {
        Observable observable = service_live.getLiveTagList();
        Observable observableCache = providers.getLiveTagList(observable, new DynamicKey("live标签"), new EvictDynamicKey(false));
        setSubscribe(observableCache, observer);
    }

    //test live list
    public void HttpDataGetAllLives(Observer<LiveDto> observer, LiveSearchDto2 liveSearchDto) {
        Observable observable = service_live.getAllLiveList(liveSearchDto);
//        Observable observableCache = providers.getLiveList(observable, new DynamicKey("live直播列表"), new EvictDynamicKey(false));
        setSubscribe(observable, observer);
    }

    /**
     * 插入观察者
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
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private  class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

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
    private  class HttpResultFuncCcche<T> implements Func1<Reply<T>, T> {

        @Override
        public T call(Reply<T> httpResult) {
            return httpResult.getData();
        }
    }
}
