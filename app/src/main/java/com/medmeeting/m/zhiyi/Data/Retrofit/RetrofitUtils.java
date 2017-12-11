package com.medmeeting.m.zhiyi.Data.Retrofit;

import com.medmeeting.m.zhiyi.Constant.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *封装一个retrofit集成0kHttp3的抽象基类
 */
public abstract class RetrofitUtils {

    private static Retrofit mRetrofit;
    private static Retrofit mRetrofitMeeting;
    private static Retrofit mRetrofitLive;
    private static Retrofit mRetrofitCookie;

    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mOkHttpClientLive;
    private static OkHttpClient mOkHttpClientCookie;

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    protected static Retrofit getRetrofit() {

        if (null == mRetrofit) {

            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpUtils.getOkHttpClient();
            }

            //Retrofit2后使用build设计模式
            mRetrofit = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(Constant.API_SERVER + "/")
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置使用okhttp网络请求
                    .client(mOkHttpClient)
                    .build();
        }

        return mRetrofit;
    }

    /**
     * 会议Retrofit对象
     *
     * @return
     */
    protected static Retrofit getMeetingRetrofit() {

        if (null == mRetrofitMeeting) {

            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpUtils.getOkHttpClientWithoutInterceptor();
            }

            //Retrofit2后使用build设计模式
            mRetrofitMeeting = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(Constant.API_SERVER_MEETING + "/")
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置使用okhttp网络请求
                    .client(mOkHttpClient)
                    .build();
        }

        return mRetrofitMeeting;
    }


    /**
     * 短信Retrofit对象 Cookie
     *
     * @return
     */
    protected static Retrofit getCookieRetrofit() {

        if (null == mOkHttpClientCookie) {

            if (null == mOkHttpClientCookie) {
                mOkHttpClientCookie = OkHttpUtils.getCookieOkHttpClient();
            }
            mRetrofitCookie = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(Constant.API_SERVER_LIVE + "/")
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置使用okhttp网络请求
                    .client(mOkHttpClientCookie)
                    .build();
        }

        return mRetrofitCookie;
    }



    /**
     * 直播Retrofit对象
     *
     * @return
     */
    protected static Retrofit getLiveRetrofit() {

        if (null == mOkHttpClientLive) {

            if (null == mOkHttpClientLive) {
                mOkHttpClientLive = OkHttpUtils.getOkHttpClient();
            }

            //Retrofit2后使用build设计模式
//            mRetrofitLive = new Retrofit.Builder()
//                    //设置服务器路径
//                    .baseUrl(Constant.API_SERVER_LIVE + "/")
//                    //添加转化库，默认是Gson
//                    .addConverterFactory(GsonConverterFactory.create())
//                    //添加回调库，采用RxJava
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    //设置使用okhttp网络请求
//                    .client(mOkHttpClientLive)
//                    .build();

            mRetrofitLive = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(Constant.API_SERVER_LIVE_TEST + "/")
//                    .baseUrl(Constant.API_SERVER_LIVE + "/")
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置使用okhttp网络请求
                    .client(mOkHttpClientLive)
                    .build();
        }

        return mRetrofitLive;
    }
}
