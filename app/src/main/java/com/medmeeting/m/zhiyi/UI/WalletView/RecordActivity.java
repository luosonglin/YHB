package com.medmeeting.m.zhiyi.UI.WalletView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Widget.CircleProgressView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
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
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        mToolbar.setNavigationOnClickListener(view -> finish());
        mToolbar.setTitle(mTitle);
        mToolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.live_point));
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_record_all:
                    mWebView.loadUrl("http://webview.medmeeting.com/#/wallet/record-list");
                    break;
                case R.id.action_record_withdraw:
                    mWebView.loadUrl("http://webview.medmeeting.com/#/wallet/record-list?serviceType=EXTRACT");
                    break;
                case R.id.action_record_income:
                    mWebView.loadUrl("http://webview.medmeeting.com/#/wallet/record-list?serviceType=SETTLE");
                    break;
                case R.id.action_record_refund:
                    mWebView.loadUrl("http://webview.medmeeting.com/#/wallet/record-list?serviceType=REFOUND");
                    break;
            }
            return true;
        });
    }

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record_toolbar, menu);
        return true;
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
        Intent intent = new Intent(activity, RecordActivity.class);
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
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog
                        .Builder(RecordActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("确定", (dialog, which) -> result.confirm());
                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
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

