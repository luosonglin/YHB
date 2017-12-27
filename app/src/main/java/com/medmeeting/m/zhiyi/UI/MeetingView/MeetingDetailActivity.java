package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.CollectType;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.EventRegisterSwitchVO;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;

public class MeetingDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BridgeWebView mWebView;
    private TextView a;
    private static final String TAG = MeetingDetailActivity.class.getSimpleName();

    private String URL;
    private static String userAgent;
    private String version;
    private Integer eventId;
    private String userId;
    private String openId = null;

    private Map<String, Object> checkRegisterPhoneOptions = new HashMap<>();
    private Map<String, Object> getEventStatusOptions = new HashMap<>();
    private Map<String, Object> checkFollowEventOptions = new HashMap<>();
    private Map<String, Object> followEventOptions = new HashMap<>();

    private boolean isFollowEvent = false;
    private String status = "0";

    private EventRegisterSwitchVO eventRegisterSwitchVO = null;

    private String eventTitle;
    private String sourceType;
    private String photo;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (BridgeWebView) findViewById(R.id.WebView);
        a = (TextView) findViewById(R.id.a);

        eventId = getIntent().getExtras().getInt("eventId");
        try {
            userId = DBUtils.get(MeetingDetailActivity.this, "userId");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        initToolbar();

        eventTitle = getIntent().getExtras().getString("eventTitle");
        sourceType = getIntent().getExtras().getString("eventTitle");
        photo = getIntent().getExtras().getString("eventTitle");
        description = getIntent().getExtras().getString("eventTitle");


        HttpData.getInstance().HttpDataGetMeetingInfo(new Observer<HttpResult3<Object, Event>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MeetingDetailActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, Event> data) {
                eventTitle = data.getEntity().getTitle();
                sourceType = data.getEntity().getSourceType();
                photo = data.getEntity().getBanner();
                description = data.getEntity().getEventDesc();

                initShare(savedInstanceState, photo, description);

                initWebView();
            }
        }, eventId);


    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出web还是退出activity
                if (mWebView.canGoBack()) {
                    mWebView.goBack(); //goBack()表示返回WebView的上一页面
                } else {
//                    onBackPressed();
                    finish();
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        ShareBoardConfig config = new ShareBoardConfig();
                        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                        mShareAction.open(config);
                        break;
                    case R.id.action_collect:
                        collectService(true);
                        break;
                    case R.id.action_collect_no:
                        collectService(false);
                        break;
//                    case R.id.action_enroll:
//                        if (eventRegisterSwitchVO.isOpenEntrances()) {  // 是否开启报名入口
//                            Intent i = new Intent(MeetingDetailActivity.this, MeetingEnrolActivity.class);
//                            i.putExtra("title", "报名");
//                            i.putExtra("eventId", eventId);
//                            i.putExtra("eventTitle", getIntent().getExtras().getString("eventTitle"));
//                            startActivity(i);
//                        } else {
//                            ToastUtils.show(MeetingDetailActivity.this, "暂未开启报名入口");
//                        }
//                        break;
//                    case R.id.action_order:
//                        Intent i2 = new Intent(MeetingDetailActivity.this, MeetingOrderActivity.class);
//                        i2.putExtra("title", "订单详情");
//                        i2.putExtra("eventId", eventId);
//                        i2.putExtra("eventTitle", getIntent().getExtras().getString("eventTitle"));
//                        startActivity(i2);
//                        break;
                }
                return true;
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("eventId", eventId);
        HttpData.getInstance().HttpDataGetEventCollect(new Observer<HttpResult3<Object, CollectType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MeetingDetailActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, CollectType> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MeetingDetailActivity.this, data.getMsg());
                    return;
                }
                isFollowEvent = data.getEntity().isCollectType();
            }
        }, map);
    }

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_share).setVisible(true);

        if (isFollowEvent) {
            menu.findItem(R.id.action_collect).setVisible(true);
            menu.findItem(R.id.action_collect_no).setVisible(false);
        } else {
            menu.findItem(R.id.action_collect).setVisible(false);
            menu.findItem(R.id.action_collect_no).setVisible(true);
        }

//        if (eventRegisterSwitchVO != null) {
//            if (eventRegisterSwitchVO.isDisplayEntrances()) {   //是否显示报名按钮
//                menu.findItem(R.id.action_enroll).setVisible(true);
//            } else {
//                menu.findItem(R.id.action_enroll).setVisible(false);
//            }
//
//            if (eventRegisterSwitchVO.isRegisterResult()) { //是否已报名
//                menu.findItem(R.id.action_order).setVisible(true);
//            } else {
//                menu.findItem(R.id.action_order).setVisible(false);
//            }
//        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 初始化WebView配置
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        // BindUserInfo settings
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);    //设置webview支持javascript
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true); //和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setSavePassword(true);    //设置webview保存密码
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //支持缩放

        // Technical settings
        settings.setAppCacheEnabled(false);  //是否使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);    //DOM Storage

        settings.setSupportMultipleWindows(true);
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);

        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }

        if (userAgent == null) {
            userAgent = mWebView.getSettings().getUserAgentString() + "; yihuibao_a Version/" + version;
        }
        settings.setUserAgentString(userAgent);//设置用户代理
        Log.e(TAG, userAgent);

        //哈哈哈哈哈哈哈
//        if (getIntent().getStringExtra("sourceType") == null) {
//            ToastUtils.show(MeetingDetailActivity.this, "该会议没有sourceType字段，找文戈！");
//            finish();
//            return;
//        }
        switch (sourceType) {     //主办方创建SPONSOR 微站,  运营端创建ADMIN 新闻,
            case "SPONSOR":
                URL = Constant.URL_microWebsiteDetail + eventId;
                break;
            case "ADMIN": //会议新闻
                URL = Constant.URL_Meeting_Detail + eventId;
                break;
        }
        mWebView.loadUrl(URL);
        Log.e(TAG, URL);

        mWebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                Log.d(TAG, "－－－－－－setWebChromeClient ");
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候WebView打开，为false则系统浏览器或第三方浏览器打开。
                //如果要下载页面中的游戏或者继续点击网页中的链接进入下一个网页的话，重写此方法下，不然就会跳到手机自带的浏览器了，而不继续在你这个webview里面展现了
//                return super.shouldOverrideUrlLoading(view, url);
                Log.d(TAG, " url:" + url);
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //想在页面开始加载时有操作，在这添加
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //想在页面加载结束时有操作，在这添加
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //想在收到错误信息的时候，执行一些操作，走此方法
                super.onReceivedError(view, request, error);
            }

        });
    }

    /**
     * 设置回退
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //退出web还是退出activity
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        } else {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    String web_event_id = null;

    public class JSHook {
        @JavascriptInterface
        public void javaMethod(String p) {
            Log.e(TAG, "JSHook.JavaMethod() called! + " + p);
            ToastUtils.show(MeetingDetailActivity.this, "JSHook.JavaMethod() called! + " + p);
        }


        public String GETUID;

        @JavascriptInterface
        public String getUserIdInWeb(final String string) {
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(string);
                GETUID = mJson.optString("GETUID");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "getUserIdInWeb" + userId + " " + GETUID + " " + Data.getUserToken().substring(7));
            return Data.getUserToken().substring(7);
        }

        @JavascriptInterface
        public void pay(String info) {  //点击"支付订单"按钮时候调用
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(info);
                web_event_id = mJson.optString("EVENT_ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(" EVENT_ID", web_event_id); //E/ PAY: alipay|378|378_20171221131135754_7

            Intent i = new Intent(MeetingDetailActivity.this, MeetingEnrolActivity.class);
            i.putExtra("title", "报名");
            i.putExtra("eventId", Integer.parseInt(web_event_id));
            i.putExtra("eventTitle", eventTitle);
            startActivity(i);
        }


        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(TAG + " WebView: ", str);
        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }

    public void initShare(Bundle savedInstanceState, final String photo, final String desc) {
        //qq微信新浪授权防杀死, 在onCreate中再设置一次回调
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        });

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(MeetingDetailActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

//                        UMWeb web = new UMWeb(Constant.Share_Meeting_Index + eventId);
                        UMWeb web = new UMWeb(URL);
                        web.setTitle(eventTitle);//标题
                        if (photo != null) {
                            web.setThumb(new UMImage(MeetingDetailActivity.this, photo));  //缩略图
                        } else {
                            web.setThumb(new UMImage(MeetingDetailActivity.this, R.mipmap.meeting_bg));
                        }
                        web.setDescription(desc);//描述
                        new ShareAction(MeetingDetailActivity.this)
                                .withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();

//                        new ShareAction(MeetingDetailActivity.this)
//                                .withTitle(eventTitle)
//                                .withText(eventTitle)
//                                .withTargetUrl(URL)
//                                //此处更改为了Silvia
////                                .withTitle("2017上海交通大学附属第六人民医院耳鼻咽喉头颈外科学术论坛")
////                                .withText("2017-08-07至2017-08-19" +"     \n     "+ "让青春舞动智慧的翅膀")
////                                .withTargetUrl("http://wap.medmeeting.com/#!/mw/index/268/0")
////                                .withMedia(new UMImage(MeetingDetailActivity.this, R.mipmap.silvia_3))
//                                .setPlatform(share_media)
//                                .setCallback(mShareListener)
//                                .share();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(MeetingDetailActivity.this, " 分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MeetingDetailActivity.this, " 分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MeetingDetailActivity.this, " 分享已取消", Toast.LENGTH_SHORT).show();
        }
    };

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<MeetingDetailActivity> mActivity;

        private CustomShareListener(MeetingDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), " 收藏成功", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE) {
                    Toast.makeText(mActivity.get(), " 分享成功", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(mActivity.get(), "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.e(TAG, "umeng throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), " 分享已取消", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //qq微信新浪授权防杀死
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    /**
     * 收藏API
     *
     * @param oldCollected
     */
    private void collectService(boolean oldCollected) {
        UserCollect userCollect = new UserCollect();
        userCollect.setServiceId(eventId);
        userCollect.setServiceType("EVENT");
        HttpData.getInstance().HttpDataCollect(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MeetingDetailActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(MeetingDetailActivity.this, httpResult3.getMsg());
                    return;
                }
                if (oldCollected) {     //老状态是 已收藏
                    ToastUtils.show(MeetingDetailActivity.this, "取消收藏");
                    isFollowEvent = false;
                    invalidateOptionsMenu(); //重新绘制menu
                } else {
                    ToastUtils.show(MeetingDetailActivity.this, "收藏成功");
                    isFollowEvent = true;
                    invalidateOptionsMenu(); //重新绘制menu
                }
            }
        }, userCollect);
    }

}
