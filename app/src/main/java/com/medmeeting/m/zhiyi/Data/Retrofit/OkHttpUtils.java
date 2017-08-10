package com.medmeeting.m.zhiyi.Data.Retrofit;

import com.medmeeting.m.zhiyi.Constant.Data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/*
 * okHttp的配置
 * 其实完全可以直接写到RetrofitUtils  但是请求头比较重要 单独拿出来 方便你们扩展
 */
public class OkHttpUtils {

    private static OkHttpClient mOkHttpClient;

    /**
     * 获取OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //添加拦截器
                    .addInterceptor(mTokenInterceptor)
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
    private static final Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = null;
            authorised = originalRequest.newBuilder()
//                    .header("FromSource", "1.0")
//                    .header("Authorization", "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6IuadvuaelyIsInVzZXJJZCI6IjciLCJjcmVhdGVkIjoxNDk5MTY1MzMzNDA3LCJleHAiOjE0OTc0NjIzNjYxMTEsImlzcyI6ImhlYWxpZmUiLCJhdWQiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiJ9.vbT88lTmgegWm_Nu8EBWE92WwoXoTFFQs841wONO1KA")
                    .header("Authorization", Data.getUserToken())
                    .build();
            return chain.proceed(authorised);
        }
    };

    /**
     * 日志拦截器
     */
    private static final Interceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
}
