package com.medmeeting.m.zhiyi.UI.MineView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EventPrepayOrderRequestVO;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UnifiedOrderResult;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingPayOrderActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/12/2017 4:32 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderMeetingFragment extends Fragment {

    BridgeWebView WebView;
    private static String userAgent;
    private static final String TAG = MyOrderMeetingFragment.class.getSimpleName();

    public static MyOrderMeetingFragment newInstance() {
        MyOrderMeetingFragment fragment = new MyOrderMeetingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order_meeting, container, false);

        WebView = (BridgeWebView) view.findViewById(R.id.WebView);

        initWebView();
        return view;
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
            userAgent = WebView.getSettings().getUserAgentString() + "; yihuibao_a Version/2.1.2";
        }
        settings.setUserAgentString(userAgent);//设置用户代理
        Log.e(TAG, userAgent);

        WebView.loadUrl(Constant.URL_Meeting_Order);
        Log.e(TAG, Constant.URL_Meeting_Order);

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
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, WebResourceRequest request) {
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


    public String PAY;
    public String type;
    public String mWebEventId;
    public String orderId;


    public String paymentId;


    public class JSHook {
        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e("printWebLog", str);
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
            Log.e("getUserIdInWeb", Data.getUserToken().substring(7));

            return Data.getUserToken().substring(7);
        }

        String eventPay;
        String eventId;

        @JavascriptInterface
        public void pay(String info) {  //点击"支付订单"按钮时候调用
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(info);

                eventPay = mJson.optString("EVENT_PAY");//点"去支付"
                eventId = mJson.optString("EVENT");

                Log.e(" eventPay", eventPay);
                Log.e(" eventId", eventId + "");

                if (eventId.equals("null")) {
                    Log.e(" PAY1", eventPay);
                    Intent i = new Intent(getActivity(), MeetingPayOrderActivity.class);
                    i.putExtra("url", eventPay);
                    startActivity(i);
                } else if (eventPay.equals("null")) {
                    Log.e(" PAY2", eventId);
                    startActivity(new Intent(getActivity(), MeetingDetailActivity.class)
                            .putExtra("eventId", Integer.parseInt(eventId))
                    );
                } else {
                    Log.e(" PAY3", eventPay+ " " +eventId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void EVENT(String info) {  //点击"支付订单"按钮时候调用
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(info);
                info = mJson.optString("EVENT");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(" EVENT", info); //E/ PAY: alipay|378|378_20171221131135754_7


        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }

    public void getPayType(String type, Integer eventId, String orderId) {
        EventPrepayOrderRequestVO eventPrepayOrderRequestVO = new EventPrepayOrderRequestVO();
        eventPrepayOrderRequestVO.setOrderId(orderId);
        eventPrepayOrderRequestVO.setPaymentChannel(type);
        eventPrepayOrderRequestVO.setPlatformType("APP");

        HttpData.getInstance().HttpDataGetPayType(new Observer<HttpResult3<Object, UnifiedOrderResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, UnifiedOrderResult> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }

                //alipay
                if (type.equals("alipay")) {
                    if (checkAliPayInstalled(getActivity())) {
                        pay(data.getEntity().getAmount(), data.getEntity().getTradeTitle(), data.getEntity().getTradeTitle(), data.getEntity().getTradeId(), data.getEntity().getAlipayOrderString());
                    } else {
                        ToastUtils.show(getActivity(), "支付宝APP尚未安装，\n请重新选择其他支付方式");
                    }
                } else if (type.equals("wechat")) {
                    if (isWXAppInstalledAndSupported(getActivity(), api)) {
                        Data.setPayType(2);
                        Data.setTradeId(data.getEntity().getTradeId());
                        payByWechat(data.getEntity().getRequestPay().getPartnerid(),
                                data.getEntity().getRequestPay().getPrepayid(),
                                data.getEntity().getRequestPay().getNoncestr(),
                                data.getEntity().getRequestPay().getTimeStamp(),
                                data.getEntity().getRequestPay().getPackageX(),
                                data.getEntity().getRequestPay().getSign());
                    } else {
                        ToastUtils.show(getActivity(), "微信APP尚未安装，\n请重新选择其他支付方式");
                    }
                }

            }
        }, eventPrepayOrderRequestVO, eventId);
    }

    /**
     * 支付宝支付配置
     * ======================================================================================================================================================
     */
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
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                        WebView.reload();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(float amount, String title, String description, String paymentId, final String payInfo) {
        if (TextUtils.isEmpty(Constant.PARTNER) || TextUtils.isEmpty(Constant.RSA_PRIVATE) || TextUtils.isEmpty(Constant.SELLER)) {
            new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialoginterface, i) -> getActivity().finish()).show();
            return;
        }

        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(getActivity());
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 判断支付宝是否安装
     *
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 微信支付配置
     * ======================================================================================================================================================
     */
    private IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), null);

    private void payByWechat(final String partnerId, final String prepayId, final String nonceStr, final String timeStamp, final String packageValue, final String sign) {
        Log.e(TAG, "payByWechat");

        api.registerApp(Constant.WeChat_AppID);
        getActivity().runOnUiThread(() -> {
            try {
                PayReq req = new PayReq();
                req.appId = Constant.WeChat_AppID;
                req.partnerId = partnerId;
                req.prepayId = prepayId;
                req.nonceStr = nonceStr;
                req.timeStamp = timeStamp;
                req.packageValue = packageValue;
                req.sign = sign;
                req.extData = "";//"app data"; // optional
                api.sendReq(req);
            } catch (Exception e) {
                Log.e("PAY_GET", e.getMessage());
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static boolean isWXAppInstalledAndSupported(Context context, IWXAPI api) {
        // LogOutput.d(TAG, "isWXAppInstalledAndSupported");
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            Log.w(TAG, "~~~~~~~~~~~~~~微信客户端未安装，请确认");
            ToastUtils.show(context, "微信客户端未安装，请确认");
        }

        return sIsWXAppInstalledAndSupported;
    }
}
