package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.CircleProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BrowserActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.circle_progress)
    CircleProgressView progressBar;

    private final Handler mHandler = new Handler();
    private String url, mTitle;
    private WebViewClientBase webViewClient = new WebViewClientBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        initToolBar();

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(Constant.EXTRA_URL);
            mTitle = intent.getStringExtra(Constant.EXTRA_TITLE);
        }
        setupWebView();
    }
    public void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle(mTitle);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        mToolbar.setNavigationOnClickListener(view -> finish());
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && mWebView.copyBackForwardList().getSize() > 0
                && !mWebView.getUrl().equals(mWebView.copyBackForwardList()
                .getItemAtIndex(0).getOriginalUrl())) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    public static void launch(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(Constant.EXTRA_URL, url);
        intent.putExtra(Constant.EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        progressBar.spin();
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // Technical settings
        webSettings.setAppCacheEnabled(false);  //是否使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);    //DOM Storage

        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog
                        .Builder(BrowserActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("确定", (dialog, which) -> result.confirm());
                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
//
//            public String GETUID;
//
//            @JavascriptInterface
//            public String getUserIdInWeb(final String string) {
//                try {
//                    // 解析js传递过来的json串
//                    JSONObject mJson = new JSONObject(string);
//                    GETUID = mJson.optString("GETUID");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Log.e(getLocalClassName(), "getUserIdInWeb" + userId + " " + GETUID + " " + Data.getUserToken());
////            return userId;
//                return Data.getUserToken();
//            }
        });
        mWebView.loadUrl(url);
    }


    public class WebViewClientBase extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.stopSpinning();
            mWebView.getSettings().setBlockNetworkImage(false);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            String errorHtml = "<html><body><h2>找不到网页</h2></body></html>";
            view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
        }
    }

//    /**
//     * 设置回退
//     *
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //退出web还是退出activity
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
//            mWebView.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        } else {
//            this.finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public class JSHook {
        @JavascriptInterface
        public void javaMethod(String p) {
            Log.e(getLocalClassName(), "JSHook.JavaMethod() called! + " + p);
            ToastUtils.show(BrowserActivity.this, "JSHook.JavaMethod() called! + " + p);
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

            Log.e(getLocalClassName(), "getUserIdInWeb" + Data.getUserId()+ " " + GETUID + " " + Data.getUserToken());
//            return userId;
            return Data.getUserToken();
        }

        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(getLocalClassName() + " WebView: ", str);
        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }


    @Override
    protected void onPause() {
        mWebView.reload();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

