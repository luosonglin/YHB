package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult4;
import com.medmeeting.m.zhiyi.UI.Entity.PaymentStatus;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

public class MeetingEnrolActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.WebView)
    BridgeWebView WebView;
    @Bind(R.id.content_meeting_enrol)
    RelativeLayout contentMeetingEnrol;

    private static final String TAG = MeetingEnrolActivity.class.getSimpleName();

    private static final String URL = "file:///android_asset/test.html";
    private String URL_MeetingDetail;// = "http://wap.medmeeting.com/#!/reg/";//http://wap.medmeeting.com/#!/reg/:eventId
    private static String userAgent;
    private String url;
    private Integer eventId;
    private String eventTitle;
    private String userId;
    private String version;
    private String title;
    private Boolean AlipayDisplay;
    private Boolean WechatDisplay;
    private Boolean OffLineDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_enrol);
        ButterKnife.bind(this);

        initMeetingData();

        initToolbar();

        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initMeetingData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        eventId = bundle.getInt("eventId");
        URL_MeetingDetail = bundle.getString("url");
        title = bundle.getString("title");
        eventTitle = bundle.getString("eventTitle");

        try {
            userId = DBUtils.get(MeetingEnrolActivity.this, "userId");
            String openId = DBUtils.get(MeetingEnrolActivity.this, "openId");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }


        PackageManager pm = MeetingEnrolActivity.this.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(MeetingEnrolActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pi.versionName;
    }

    private void initToolbar() {
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(Color.BLACK);
        toolbarTitle.setFocusable(true);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

//
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                WebView.goBack();
////                finish();
//
////                testAlipay();
////                payV2();
////                Log.e(TAG, "getUserIdInWeb userId:" + userId + " eventId:" + eventId + " PAY:" + PAY);
//            }
//        });
    }


    /**
     * 初始化WebView配置
     */
    private void initWebView() {
//        WebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
            userAgent = WebView.getSettings().getUserAgentString() + "; yihuibao_a Version/" + version;
        }
        settings.setUserAgentString(userAgent);//设置用户代理
        Log.e(TAG, userAgent);

        /*
        export function PushAppMsg (obj, next) {
            // APP通信事件
            function setupWebViewJavascriptBridge (callback) {
            if (window.WebViewJavascriptBridge) {
                return callback(window.WebViewJavascriptBridge)
            }
            if (window.WVJBCallbacks) {
                return window.WVJBCallbacks.push(callback)
            }
            window.WVJBCallbacks = [callback]
            var WVJBIframe = document.createElement('iframe')
            WVJBIframe.style.display = 'none'
            WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__'
            document.documentElement.appendChild(WVJBIframe)
            setTimeout(function () {
                document.documentElement.removeChild(WVJBIframe)
            }, 0)
            }
            setupWebViewJavascriptBridge((bridge) => {
                    bridge.callHandler('CALLAPP', obj, (response) => {
                            next(response)
                    })
            })
        }
        */

        WebView.loadUrl(URL_MeetingDetail + eventId);
        Log.e(TAG, URL_MeetingDetail + eventId);
//        WebView.loadUrl(URL);


        WebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
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
        /*WebView.registerHandler("CALLAPP", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack(userId);//"submitFromWeb exe, response data from Java" );

            }
        });*/
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
    public String PAY;
    public String type;
    public String paymentId;

    public class JSHook {
        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(TAG + " WebView: ", str);
        }

        @JavascriptInterface
        public String getUserIdInWeb(final String string) {
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(string);
                PAY = mJson.optString("PAY");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "getUserIdInWeb userId:" + userId + " eventId:" + eventId + " PAY:" + PAY );

            return userId;
        }

        @JavascriptInterface
        public void pay(String info) {  //点击"支付订单"按钮时候调用
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(info);
                info = mJson.optString("PAY");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "pay() userId:" + userId + " eventId:" + eventId + " PAY:" + info);

            type = info.split("/")[1];
            paymentId = info.split("/")[0];
            Log.e(TAG, "pay() type: " + type + " paymentId: "+paymentId);

            showAcademicPopupWindow(type, paymentId);

        }


        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }

    /**
     * academic筛选之弹出窗
     */
    private PopupWindow academicPopupWindow;

    private void showAcademicPopupWindow(String type, final String paymentId) {

        if (AlipayDisplay == null) {
            Map<String, Object> options = new HashMap<>();
            options.put("eventId", eventId);
            options.put("type", type);//register hotel
            HttpData.getInstance().HttpDataGetPaymentStatus(new Observer<PaymentStatus>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.getMessage());
                    ToastUtils.show(MeetingEnrolActivity.this, e.getMessage());
                }

                @Override
                public void onNext(PaymentStatus paymentStatus) {
                    if (paymentStatus.getPayStatus().get(0).isAlipay()) {
                        AlipayDisplay = true;
                    } else {
                        AlipayDisplay = false;
                    }

                    if (paymentStatus.getPayStatus().get(0).isWechat()) {
                        WechatDisplay = true;
                    } else {
                        WechatDisplay = false;
                    }

                    if (paymentStatus.getPayStatus().get(0).isLine()) {
                        OffLineDisplay = true;
                    } else {
                        OffLineDisplay = false;
                    }

                    initPopupwindow();
                }
            }, options);
        }

        initPopupwindow();

    }

    private void initPopupwindow() {

        View academicPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_pay_type_meeting, null);
        academicPopupWindow = new PopupWindow(academicPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView a = (TextView) academicPopupwindowView.findViewById(R.id.alipay);
        final TextView b = (TextView) academicPopupwindowView.findViewById(R.id.pay_offline);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayInfo(v, paymentId, "alipay");
                academicPopupWindow.dismiss();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ToastUtils.show(MeetingEnrolActivity.this, "http://www.medmeeting.com/phoneEvent/confirmPayType?paymentId="+paymentId +"&payType=line");
//                startActivity(new Intent(MeetingEnrolActivity.this, PayDemoActivity.class));
                academicPopupWindow.dismiss();

                Map<String, Object> options = new HashMap<>();
                options.put("paymentId", paymentId);
                options.put("payType", "line");
                HttpData.getInstance().HttpDataGetPayInfo(new Observer<HttpResult4>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult4 httpResult4) {
                        if (httpResult4.getStatus().equals("200")) {
                            WebView.reload();
                        } else {
                            ToastUtils.show(MeetingEnrolActivity.this, httpResult4.getReturnMsg());
                        }

                    }
                }, options);
            }
        });

        if (AlipayDisplay) {
            a.setVisibility(View.VISIBLE);
        } else {
            a.setVisibility(View.GONE);
        }

        if (OffLineDisplay) b.setVisibility(View.VISIBLE);
        else b.setVisibility(View.GONE);

        LinearLayout academicPopupParentLayout = (LinearLayout) academicPopupwindowView.findViewById(R.id.popup_parent);
        academicPopupParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (academicPopupWindow != null && academicPopupWindow.isShowing()) {
                    academicPopupWindow.dismiss();
                }
            }
        });

        academicPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        academicPopupWindow.setBackgroundDrawable(dw);
        academicPopupWindow.showAtLocation(academicPopupwindowView, Gravity.BOTTOM, 0, 0);
    }

    //支付金额
    private float amount;

    //获取支付订单信息
    private void getPayInfo(final View v, String paymentId, String payType){
        Map<String, Object> options = new HashMap<>();
        options.put("paymentId", paymentId);
        options.put("payType", payType);
        HttpData.getInstance().HttpDataGetPayInfo(new Observer<HttpResult4>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult4 httpResult4) {
                amount = httpResult4.getAmtOrder();
                if (httpResult4.getReturnMsg().equals("选择支付方式成功！")) {
                    pay(v, amount);
                }
            }
        }, options);
    }

    // 商户PID
    public static final String PARTNER = "2088311846356487";
    // 商户收款账号
    public static final String SELLER = "zhifubao@healife.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIoUMNT6sAOC05BU72JoQWbhmJhe9n917DtUrT8kU0nFGzeV+tmSSy+3bsbSbKUg9ngVpyXsfn4ouH33Ktx42H8TAHs3n39qaSAePKgB3o6pOV0vYnpVqnYB1UlecW/vbxv8XcvmcEOS1gE3OwcFh6NTzdgbr+rb+mLbAGlINfWNAgMBAAECgYBDY4FFoKegvwvkCB/g5kLtJDMmQkqJgJLvje8TvvXLLiCPa2pHH2gEfMDa1j3iBYlkqCSwlJBToCoSiDvp6Cy4cRtMKbTSNx00bhLWzpuropvSAH9EIsOJe+rCpOZox+DcIUOFS3TkXkXOKaAW9F2Onqr1nfK+1C9nXMPePOyLnQJBAPnuKnBgAYSxD8OeO2AIjF5iO93Ap1f9q8/L4bxcsw1WfKJlolFKxWC3nnuOrL9qe1DcKOPnKaJHA9XdFH7VNG8CQQCNbqL8HOh/9LhPe8TgwfUl4Bjz0WjQGq3qIQscy0o6JfkAHz+Po2zl+kTugtUguWnlhpFli/fT2d9yaVDj9cvDAkB5DuN/iwExRJJeLkaUPY/AJ9TXlHl6JWUTQa4VjtErpLi58ICu34i7UDVzo6gJD4qrn/gua8m+0KcK8Ar9ZEgBAkBFgan33P0mZU5vQZRwIOIpywh4SuIH5BS0i6i6be38xcypkrHaFabfHy/hR8sWWgkBFDFAhpk1NE3sHHX0kkehAkAud+wmYHchvQ2ME9yxNl5+LHsVRbEOscJIdbPTO95MrBufBfyFyIJ79SQvj/lb+ueEfyr+QUkJU5UxEH8rhhFM";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKFDDU+rADgtOQVO9iaEFm4ZiYXvZ/dew7VK0/JFNJxRs3lfrZkksvt27G0mylIPZ4Facl7H5+KLh99yrceNh/EwB7N59/amkgHjyoAd6OqTldL2J6Vap2AdVJXnFv728b/F3L5nBDktYBNzsHBYejU83YG6/q2/pi2wBpSDX1jQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    com.medmeeting.m.zhiyi.paydemo.PayResult payResult = new com.medmeeting.m.zhiyi.paydemo.PayResult((String) msg.obj);
                    /*
                      同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                      detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                      docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MeetingEnrolActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        WebView.reload();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MeetingEnrolActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MeetingEnrolActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(View v, float amount) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(eventTitle, "该测试商品的详细描述", Float.toString(amount), paymentId);

        /*
          特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /*
              仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /*
          完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MeetingEnrolActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price, String paymentId) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        orderInfo += "&out_trade_no=" + "\"" + paymentId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.medmeeting.com/toAliPay/alipayAppNotify" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return com.medmeeting.m.zhiyi.paydemo.SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


}



