package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("JavaDoc")
public class LiveInvitationLetterActivity extends AppCompatActivity {

    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.WebView)
    BridgeWebView WebView;
    @Bind(R.id.a)
    TextView a;
    @Bind(R.id.content_live_invitation_letter)
    RelativeLayout contentLiveInvitationLetter;

    private static final String TAG = LiveInvitationLetterActivity.class.getSimpleName();

    private static final String URL = "file:///android_asset/test.html";
    private static final String URL_MeetingDetail = "http://wap.medmeeting.com/#!/mw/index/";//http://wap.medmeeting.com/#!/mw/index/:eventId/0
//    private static final String URL_Live = "http://wap.medmeeting.com/#!/live/invite/";
    private static final String URL_Live = "http://wap.medmeeting.com/#!/live/room/";
    private static String userAgent;
    private String version;

    private String eventId;
    private String eventTitle;
    private String userId;
    private String openId;
    private int liveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_invitation_letter);
        ButterKnife.bind(this);

        initToolbar();

        initWebView();

//        initShare();
//
//        //qq微信新浪授权防杀死, 在onCreate中再设置一次回调
//        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform, int action) {
//
//            }
//        });
    }

    private void initToolbar() {
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化WebView配置
     */
    private void initWebView() {
        WebSettings settings = WebView.getSettings();

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
        WebView.setLongClickable(true);
        WebView.setScrollbarFadingEnabled(true);
        WebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebView.setDrawingCacheEnabled(true);

        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }

        if (userAgent == null) {

            PackageManager pm = LiveInvitationLetterActivity.this.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = pm.getPackageInfo(LiveInvitationLetterActivity.this.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            version = pi.versionName;
            userAgent = WebView.getSettings().getUserAgentString() + "; yihuibao_a Version/" + version;
        }
        settings.setUserAgentString(userAgent);//设置用户代理
        Log.e(TAG, userAgent);

//        if (openId != null) {
//            WebView.loadUrl(URL_MeetingDetail + eventId + "/" + openId);
//            Log.e(TAG, URL_MeetingDetail + eventId + "/" + openId);
//        } else {
//            WebView.loadUrl(URL_MeetingDetail + eventId + "/0");
//            Log.e(TAG, URL_MeetingDetail + eventId + "/0");
//        }

//        WebView.loadUrl(URL);
//        liveId = getIntent().getExtras().getInt("liveId");
        liveId = getIntent().getExtras().getInt("roomId");
//        WebView.loadUrl(URL_Live + liveId);
        WebView.loadUrl(URL_Live + liveId);
        Log.e(TAG, URL_Live + liveId);

        WebView.addJavascriptInterface(new LiveInvitationLetterActivity.JSHook(), "SetAndroidJavaScriptBridge");
        WebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
                Log.d(TAG, "－－－－－－setWebChromeClient ");
//                CreditActivity.this.onReceivedTitle(view, title);
            }
        });
        WebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && WebView.canGoBack()) {
            WebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        } else {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class JSHook {
        @JavascriptInterface
        public void javaMethod(String p) {
            Log.e(TAG, "JSHook.JavaMethod() called! + " + p);
            ToastUtils.show(LiveInvitationLetterActivity.this, "JSHook.JavaMethod() called! + " + p);
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
            Log.e(TAG, "getUserIdInWeb" + userId+" "+GETUID);
            return userId;
        }

        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(TAG + " WebView: ", str);
        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }
//
//    @OnClick({R.id.confirm, R.id.a})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.confirm:
////                startActivity(new Intent(LiveInvitationLetterActivity.this, LiveMyLiveActivity.class));
//                break;
//            case R.id.a:
//                ShareBoardConfig config = new ShareBoardConfig();
//                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
//                mShareAction.open(config);
//                break;
//        }
//    }
//
//    private void initShare() {
//        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }
//
//        mShareListener = new CustomShareListener(this);
//        /*增加自定义按钮的分享面板*/
//        mShareAction = new ShareAction(LiveInvitationLetterActivity.this)
//                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
//                .setShareboardclickCallback(new ShareBoardlistener() {
//                    @Override
//                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//
//                        new ShareAction(LiveInvitationLetterActivity.this)
//                                .withTitle("医会宝第一主播")
//                                .withText("关于投资雄安概念的策略分享")
//                                .withTargetUrl("http://www.medmeeting.com")
//                                .withMedia(new UMImage(LiveInvitationLetterActivity.this, R.mipmap.ic_launcher))
//                                .setPlatform(share_media)
//                                .setCallback(mShareListener)
//                                .share();
//                    }
//                });
//    }
//
//    private UMShareListener mShareListener;
//    private ShareAction mShareAction;
//
//    private static class CustomShareListener implements UMShareListener {
//
//        private WeakReference<LiveInvitationLetterActivity> mActivity;
//
//        private CustomShareListener(LiveInvitationLetterActivity activity) {
//            mActivity = new WeakReference(activity);
//        }
//
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
//            } else {
//                if (platform != SHARE_MEDIA.MORE) {
//                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            if (platform != SHARE_MEDIA.MORE) {
//                Toast.makeText(mActivity.get(), platform + "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                if (t != null) {
//                    com.umeng.socialize.utils.Log.e(TAG, "umeng throw:" + t.getMessage());
//                }
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //qq微信新浪授权防杀死
//        UMShareAPI.get(this).onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
//        UMShareAPI.get(this).release();
//    }
//
//    /**
//     * 屏幕横竖屏切换时避免出现window leak的问题
//     */
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mShareAction.close();
//    }
}

