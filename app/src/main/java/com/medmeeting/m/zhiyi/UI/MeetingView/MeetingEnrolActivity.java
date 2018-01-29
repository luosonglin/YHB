package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class MeetingEnrolActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static BridgeWebView WebView;
    @BindView(R.id.content_meeting_enrol)
    RelativeLayout contentMeetingEnrol;

    private static final String TAG = MeetingEnrolActivity.class.getSimpleName();

    private static String userAgent;
    private int eventId;
    private String version;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_enrol);
        ButterKnife.bind(this);

        WebView = (BridgeWebView) findViewById(R.id.WebView);

        eventId = getIntent().getIntExtra("eventId", 0);
        Log.e(getLocalClassName(), eventId + "");

        initMeetingData(eventId,
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("eventTitle"));
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

    private void initMeetingData(int eventId, String title, String eventTitle) {

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> {
            //退出web还是退出activity
            if (WebView.canGoBack()) {
                WebView.goBack(); //goBack()表示返回WebView的上一页面
            } else {
//                    onBackPressed();
                finish();
            }
        });
    }


    /**
     * 初始化WebView配置
     */
    private void initWebView() {
        WebSettings settings = WebView.getSettings();

        // BindViewUserInfo settings
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

        WebView.loadUrl(Constant.URL_Meeting_Enrol + eventId);
        Log.e(TAG, Constant.URL_Meeting_Enrol + eventId);

        WebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
//        WebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(android.webkit.WebView view, String title) {
//                Log.d(TAG, "－－－－－－setWebChromeClient ");
////                CreditActivity.this.onReceivedTitle(view, title);
//            }
//
//        });
        WebView.setWebChromeClient(mWebChromeClient);
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

        @JavascriptInterface
        public void pay(String info) {  //点击"支付订单"按钮时候调用
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(info);
                info = mJson.optString("PAY");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(" PAY", info); //E/ PAY: alipay|378|378_20171221131135754_7


            type = info.split("\\|")[0];
            mWebEventId = info.split("\\|")[1];
            orderId = info.split("\\|")[2];
            Log.e(TAG, "type: " + type + "  event: " + eventId + "  orderId: " + orderId);

            if (type.equals("alipay") || type.equals("wechat"))
                getPayType(type, Integer.parseInt(mWebEventId), orderId);
            else
                Log.e(getLocalClassName(), type + " 线下支付");
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
                ToastUtils.show(MeetingEnrolActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, UnifiedOrderResult> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MeetingEnrolActivity.this, data.getMsg());
                    return;
                }

                //alipay
                if (type.equals("alipay")) {
                    if (checkAliPayInstalled(MeetingEnrolActivity.this)) {
                        pay(data.getEntity().getAmount(), data.getEntity().getTradeTitle(), data.getEntity().getTradeTitle(), data.getEntity().getTradeId(), data.getEntity().getAlipayOrderString());
                    } else {
                        ToastUtils.show(MeetingEnrolActivity.this, "支付宝APP尚未安装，\n请重新选择其他支付方式");
                    }
                } else if (type.equals("wechat")) {
                    if (isWXAppInstalledAndSupported(MeetingEnrolActivity.this, api)) {
                        Data.setPayType(2);
                        Data.setTradeId(data.getEntity().getTradeId());
                        payByWechat(data.getEntity().getRequestPay().getPartnerid(),
                                data.getEntity().getRequestPay().getPrepayid(),
                                data.getEntity().getRequestPay().getNoncestr(),
                                data.getEntity().getRequestPay().getTimeStamp(),
                                data.getEntity().getRequestPay().getPackageX(),
                                data.getEntity().getRequestPay().getSign());
                    } else {
                        ToastUtils.show(MeetingEnrolActivity.this, "微信APP尚未安装，\n请重新选择其他支付方式");
                    }
                }

            }
        }, eventPrepayOrderRequestVO, eventId);
    }

    /**
     * 支付宝支付配置
     * ======================================================================================================================================================
     */
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
        }

    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(float amount, String title, String description, String paymentId, final String payInfo) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialoginterface, i) -> finish()).show();
            return;
        }

        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(MeetingEnrolActivity.this);
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
    private IWXAPI api = WXAPIFactory.createWXAPI(this, null);

    private void payByWechat(final String partnerId, final String prepayId, final String nonceStr, final String timeStamp, final String packageValue, final String sign) {
        Log.e(TAG, "payByWechat");

        api.registerApp(Constant.WeChat_AppID);
        this.runOnUiThread(() -> {
            try {
                PayReq req = new PayReq();
                req.appId = "wx7e6722fad8a0975c";
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
                Toast.makeText(MeetingEnrolActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public static void reloadWebView() {
        WebView.reload();
    }


    /**
     * 以下为android WebView 无法支持input type=file的解决方法
     */
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private ValueCallback<Uri[]> mFilePathCallback;

    private String mCameraPhotoPath;
    //在sdcard卡创建缩略图
    //createImageFileInSdcard
    @SuppressLint("SdCardPath")
    private File createImageFile() {
        //mCameraPhotoPath="/mnt/sdcard/tmp.png";
        File file=new File(Environment.getExternalStorageDirectory()+"/","tmp.png");
        mCameraPhotoPath=file.getAbsolutePath();
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        // android 5.0
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;
        }

        //The undocumented magic method override
        //Eclipse will swear at you if you try to put @Override here
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MeetingEnrolActivity.this.startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MeetingEnrolActivity.this.startActivityForResult(
                    Intent.createChooser(i, "Image Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MeetingEnrolActivity.this.startActivityForResult(Intent.createChooser(i, "Image Chooser"), MeetingEnrolActivity.FILECHOOSER_RESULTCODE);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null
                    : data.getData();
            if (result != null) {
                String imagePath = ImageFilePath.getPath(this, result);
                if (!TextUtils.isEmpty(imagePath)) {
                    result = Uri.parse("file:///" + imagePath);
                }
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
            // 5.0的回调
            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {    //&& !TextUtils.isEmpty(data.getDataString())
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        Log.d("camera_photo_path", mCameraPhotoPath);
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    Log.d("camera_dataString", dataString);
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

}



