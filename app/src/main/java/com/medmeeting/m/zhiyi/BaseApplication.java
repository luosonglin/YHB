package com.medmeeting.m.zhiyi;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.OtherVIew.WelcomeActivity;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiaochao.lcrapiddeveloplibrary.Exception.core.Recovery;

import java.util.ArrayList;
import java.util.List;

//import com.vondear.rxtools.RxUtils;

public class BaseApplication extends MultiDexApplication {//Application {

    private static final String TAG = "BaseApplication";
    private static Context context;
    //记录当前栈里所有activity
    private List<Activity> activities = new ArrayList<Activity>();
    //记录需要一次性关闭的页面
    private List<Activity> activitys = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        Log.d(TAG, "医会宝2.0版本 Application is running~~~~~~~hahaha~~~~!");
        super.onCreate();
        context = this;
        instance = this;

        //初始化Leak内存泄露检测工具
//        LeakCanary.install(this);

        //工具类
//        RxUtils.init(this);

        SharedPreferencesMgr.init(this, "medmeeting");

        //初始化异常管理工具
        Recovery.getInstance()
                .debug(true)//关闭后 在错误统一管理页面不显示异常数据
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(WelcomeActivity.class)//恢复页面
//                .skip(H5PayActivity.class) //如果应用集成支付宝支付
                .init(this);

        UMShareAPI.get(this);

        //直播推流
        StreamingEnv.init(getApplicationContext());

        //融云
        //#define RIGHTRONGCLOUD_IM_APPKEY @"qd46yzrfq3lwf"//融云正式
        //#define RIGHTRONGCLOUD_IM_APPSECRET @"xVOEBe44fhL"//融云正式
        context = this;
        LiveKit.init(context, String.valueOf("qd46yzrfq3lwf"));//FakeServer.getAppKey()
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 应用实例
     */
    private static BaseApplication instance;

    /**
     * 获得实例
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 给临时Activitys添加activity
     */
    public void addTemActivity(Activity activity) {
        activitys.add(activity);
    }

    public void finishTemActivity(Activity activity) {
        if (activity != null) {
            this.activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 退出指定的Activity
     */
    public void exitActivitys() {
        for (Activity activity : activitys) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin(Constant.WeChat_AppID, "390b7bcd6e6e4f82441cebcdebccb223");
//        PlatformConfig.setWeixin("wx5b882abda749656d", "411f579410f6b81b875b2c2fbaa533f0");
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setQQZone("1105918131", "uNzl6dleoc80UQle");
        PlatformConfig.setSinaWeibo("188948618", "416592ff15fdad47403ad89e894d5fd4", "http://sns.whalecloud.com");
        PlatformConfig.setAlipay("2015111700822536");
    }
    /**
     * 应用签名：
     * 2d1f5af844ab43da48e5ec917713e2bc
     */




}

