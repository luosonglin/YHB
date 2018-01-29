package com.medmeeting.m.zhiyi.Data.Retrofit;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.medmeeting.m.zhiyi.BuildConfig;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Constant.NetConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.R.attr.versionName;

/*
 * okHttp的配置
 * 其实完全可以直接写到RetrofitUtils  但是请求头比较重要 单独拿出来 方便你们扩展
 */
public class OkHttpUtils {

    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mOkHttpClientCookie;

    /**
     * 获取OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //添加拦截器
                    .addInterceptor(mTokenInterceptor)
//                    .addInterceptor(configInterceptor)
//                    .addInterceptor(responseInterceptor)

                    //添加日志拦截器
                    .addNetworkInterceptor(mHttpLoggingInterceptor)
                    //添加网络连接器
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

        }

        return mOkHttpClient;
    }

    /**
     * 获取Cookie OkHttpClient对象
     */
    public static OkHttpClient getCookieOkHttpClient() {

        if (null == mOkHttpClientCookie) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClientCookie = new OkHttpClient.Builder()
                    //添加拦截器
                    .addInterceptor(mCookieInterceptor)
//                    .addInterceptor(configInterceptor)
//                    .addInterceptor(responseInterceptor)

                    //添加日志拦截器
                    .addNetworkInterceptor(mHttpLoggingInterceptor)
                    //添加网络连接器
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

        }

        return mOkHttpClientCookie;
    }

    /**
     * 获取OkHttpClient对象，无header拦截器
     */
    public static OkHttpClient getOkHttpClientWithoutInterceptor() {

        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //添加日志拦截器
                    .addNetworkInterceptor(mHttpLoggingInterceptor)
                    //添加网络连接器
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        return mOkHttpClient;
    }

    /**
     * 云端响应头拦截器
     * 用于添加统一请求头  请按照自己的需求添加
     * 主要用于加密传输 和设备数据传输
     */
    private static final Interceptor mTokenInterceptor = chain -> {
        Request originalRequest = chain.request();
        Request authorised = null;
        authorised = originalRequest.newBuilder()
//                    authorised.header("FromSource", "1.0")
                //钱包测试用户token
//                    authorised.header("Authorization", "bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6IuWMu-WunTI2OTcwNCIsInVzZXJJZCI6IjEwMDEwMiIsInJvbGUiOiIxIiwiY3JlYXRlZCI6MTUwNzUxNzIwNDczOCwiZXhwIjoxNTA1ODE0MjM3NDQyLCJpc3MiOiJoZWFsaWZlIiwiYXVkIjoiMDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjYifQ.E9NVVakq5S71n-w_q4A707jjCLOjLNbLYhlfkOYk6kU")
                .header("Authorization", Data.getUserToken())
                .header("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "YiHuiBao", versionName, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID))
//                    .header("Cookie", Data.getSession())
                .build();
        return chain.proceed(authorised);
    };

    private static final Interceptor mCookieInterceptor = chain -> {
        Request originalRequest = chain.request();
        Request authorised = null;
        Log.e("wwww ",Data.getSession());
        authorised = originalRequest.newBuilder()
                .header("Authorization", Data.getUserToken())
                .header("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "YiHuiBao", versionName, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID))
                .header("Cookie", Data.getSession())
                .build();
        return chain.proceed(authorised);
    };

    /**
     * 日志拦截器
     */
    private static final Interceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    /**
     *
     */
    private static final Interceptor configInterceptor = chain -> {
        Request.Builder builder = chain.request().newBuilder();

        // add costom headers......

        if (chain.request().headers().get(NetConstants.ADD_COOKIE) != null) {
            Log.e("000 ", NetConstants.ADD_COOKIE + "");
            Log.e("000 ", Data.getSession() + "");
            builder.removeHeader(NetConstants.ADD_COOKIE);
            if (!TextUtils.isEmpty(Data.getSession())) { //Session管理
                builder.header("Cookie", Data.getSession());
            }
        }

        Request request = builder.build();
        if (BuildConfig.DEBUG) {
            Log.d("TAG", "request url : " + request.url());
        }
        return chain.proceed(request);
    };
    private static final Interceptor responseInterceptor = chain -> {
        Response response = chain.proceed(chain.request());
        //存入Session
        if (response.header("Set-Cookie") != null) {
            Data.setSession(response.header("Set-Cookie"));
            Log.e("TAG", response.body().source() + Data.getSession());
            Log.e("TAG", response.body().byteStream() + Data.getSession());
            Log.e("TAG", response.body().charStream() + Data.getSession());
            Log.e("TAG", response.body().contentType() + Data.getSession());
            Log.e("TAG", response.body().string() + Data.getSession());

            Data.setInputStream(response.body().byteStream());
        }
//            Log.e("res  ", response.body().bytes()+"");
        //刷新API调用时间
//            SessionManager.setLastApiCallTime(System.currentTimeMillis());

        return response;
    };
}
