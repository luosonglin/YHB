package com.medmeeting.m.zhiyi.UI.MeetingView;

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
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MeetingDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BridgeWebView mWebView;
    private static final String TAG = MeetingDetailActivity.class.getSimpleName();

    private static final String URL = "file:///android_asset/test.html";
    private static final String URL_MeetingDetail = "http://wap.medmeeting.com/#!/mw/index/";//http://wap.medmeeting.com/#!/mw/index/:eventId/0
    private static String userAgent;
    private String version;
    private String eventId;
    private String eventTitle;
    private String userId = "7";
    private String openId= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        mWebView = (BridgeWebView) findViewById(R.id.WebView);

        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        eventTitle = getIntent().getExtras().getString("eventTitle");
        toolbarTitle.setText(eventTitle);
        toolbarTitle.setTextColor(Color.BLACK);
        toolbarTitle.setFocusable(true);

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

        eventId = getIntent().getExtras().getString("eventId");

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

        if (openId != null) {
            mWebView.loadUrl(URL_MeetingDetail + eventId + "/" + openId);
            Log.e(TAG, URL_MeetingDetail + eventId + "/" + openId);
        } else {
            mWebView.loadUrl(URL_MeetingDetail + eventId + "/0");
            Log.e(TAG, URL_MeetingDetail + eventId + "/0");
        }

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
}
