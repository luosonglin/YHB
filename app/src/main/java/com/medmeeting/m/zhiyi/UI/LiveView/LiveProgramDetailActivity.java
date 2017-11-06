package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAndVideoPayDto;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailCommandFragment;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import rx.Observer;

/**
 * 直播节目详情页（普通用户）
 */
public class LiveProgramDetailActivity extends AppCompatActivity {
    private static final String TAG = LiveProgramDetailActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //背景图
    private ImageView imageView;
    private ScrollView scrollView;
    private TextView titleTv, userNameTv;
    //观看
    private TextView watchTv;

    private DisplayMetrics metric;

    private Integer programId = 0;

    //拉流地址
    private String url = "http://onmtzwa3g.bkt.clouddn.com/recordings/z1.yihuibao-test.yihuibao-test_20170926175549970_100169/1506500861_1506501520.m3u8";

    //payFalg (integer, optional): 是否购票 0:未购票，大于0:已购票 ,
    private Integer payFlag = 0;

    private String audienceUserName;
    private String audienceUserNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail);
        toolBar();
        initView(getIntent().getExtras().getString("coverPhoto"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("authorName"));
        initShare(savedInstanceState, getIntent().getExtras().getInt("programId"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("coverPhote"),
                "欢迎观看" + getIntent().getExtras().getString("title"));//getIntent().getExtras().getString("description")
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitle("");//标题
//        toolbar.setSubtitle(R.string.app_name);//二级标题
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置logo
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_grey));
        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ShareBoardConfig config = new ShareBoardConfig();
                    config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                    mShareAction.open(config);
                    break;
            }
            return true;
        });
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
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_live_program_toolbar, menu);
        return true;
    }

    public void initShare(Bundle savedInstanceState, final int roomId, final String title, final String phone, final String description) {
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

        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(LiveProgramDetailActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {

                    UMWeb web = new UMWeb("http://wap.medmeeting.com/#!/live/room/show/" + roomId);
                    web.setTitle(title);//标题
//                        web.setThumb(new UMImage(LiveProgramDetailActivity.this, phone));  //缩略图
                    web.setDescription(description);//描述
                    new ShareAction(LiveProgramDetailActivity.this)
                            .withMedia(web)
                            .setPlatform(share_media)
                            .setCallback(mShareListener)
                            .share();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //qq微信新浪授权防杀死
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }


    private void initView(String coverPhoto, String title, final String userName) {
        //直播间封面
        titleTv = (TextView) findViewById(R.id.title);
        userNameTv = (TextView) findViewById(R.id.userName);
        titleTv.setText(title);
        userNameTv.setText("主讲人：" + userName);

        //点击"观看"
        watchTv = (TextView) findViewById(R.id.watch);
        watchTv.setVisibility(View.INVISIBLE);


        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        // 获取控件
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imageView = (ImageView) findViewById(R.id.img);

        HttpData.getInstance().HttpDataGetLiveProgramAudienceDetail(new Observer<HttpResult3<Object, LiveAudienceDetailDto>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(final HttpResult3<Object, LiveAudienceDetailDto> liveDtoHttpResult3) {
                Log.e(TAG, "onNext");

                programId = liveDtoHttpResult3.getEntity().getId();
                url = liveDtoHttpResult3.getEntity().getRtmpPlayUrl();
                amount = liveDtoHttpResult3.getEntity().getPrice();
                payFlag = liveDtoHttpResult3.getEntity().getPayFalg();

                initTagsView(getIntent().getExtras().getInt("roomId"),
                        liveDtoHttpResult3.getEntity().getAuthorName(),
                        liveDtoHttpResult3.getEntity().getAuthorTitle(),
                        getIntent().getExtras().getString("userPic"),
                        liveDtoHttpResult3.getEntity().getDes());
                watchTv.setVisibility(View.VISIBLE);

                if ("yes".equals(liveDtoHttpResult3.getEntity().getChargeType())) {
                    if (payFlag == 0) {//0:未购票
                        watchTv.setText("支付" + amount + "元观看");
                        watchTv.setOnClickListener(view -> initPopupwindow());
                    } else if (payFlag > 0) { //大于0:已购票
                        switch (liveDtoHttpResult3.getEntity().getLiveStatus()) {
                            case "play":
                                watchTv.setText("观看");
                                watchTv.setOnClickListener(view -> {
                                    try {
                                        audienceUserName = DBUtils.get(LiveProgramDetailActivity.this, "userName");
                                        audienceUserNickName = DBUtils.get(LiveProgramDetailActivity.this, "userNickName");

                                        if (audienceUserName == null || audienceUserName.equals("") || audienceUserName.equals("null")) {
                                            loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity.this, "userId"), audienceUserNickName, url);
                                        } else {
                                            loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity.this, "userId"), audienceUserName, url);
                                        }
                                    } catch (SnappydbException e) {
                                        e.printStackTrace();
                                    } finally {
                                        Intent intent = new Intent(LiveProgramDetailActivity.this, LivePlayerActivity.class);
                                        intent.putExtra("rtmpPlayUrl", url);
                                        intent.putExtra("programId", programId);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case "ready":
                                watchTv.setText("准备中");
                                break;
                            case "wait":
                                watchTv.setText("主播已离开");
                                break;
                            case "end":
                                watchTv.setText("已结束");
                                break;
                        }
                    }

                } else if ("no".equals(liveDtoHttpResult3.getEntity().getChargeType())) {
                    switch (liveDtoHttpResult3.getEntity().getLiveStatus()) {
                        case "play":
                            watchTv.setText("观看");
                            watchTv.setOnClickListener(view -> {
                                try {
                                    audienceUserName = DBUtils.get(LiveProgramDetailActivity.this, "userName");
                                    audienceUserNickName = DBUtils.get(LiveProgramDetailActivity.this, "userNickName");
                                    Log.e(TAG, "haha" + audienceUserName + " " + audienceUserNickName);

                                    if (audienceUserName == null || audienceUserName.equals("") || audienceUserName.equals("null")) {
                                        loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity.this, "userId"), audienceUserNickName, url);
                                    } else {
                                        loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity.this, "userId"), audienceUserName, url);
                                    }
                                } catch (SnappydbException e) {
                                    e.printStackTrace();
                                } finally {
                                    Intent intent = new Intent(LiveProgramDetailActivity.this, LivePlayerActivity.class);
                                    intent.putExtra("rtmpPlayUrl", liveDtoHttpResult3.getEntity().getRtmpPlayUrl());
                                    intent.putExtra("programId", programId);
                                    startActivity(intent);
                                }
                            });
                            break;
                        case "ready":
                            watchTv.setText("准备中");
                            break;
                        case "wait":
                            watchTv.setText("主播已离开");
                            break;
                        case "end":
                            watchTv.setText("已结束");
                            break;
                    }
                }

                Glide.with(LiveProgramDetailActivity.this)
                        .load(liveDtoHttpResult3.getEntity().getCoverPhoto())
                        .crossFade()
                        .into(imageView);

            }
        }, getIntent().getExtras().getInt("programId"));
    }

    private void initTagsView(final Integer roomId, String name, String hospital, String userPic, String detail) {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = this.getWindowManager().getDefaultDisplay().getHeight() - 140 * 6;//800
        Log.e(TAG, this.getWindowManager().getDefaultDisplay().getHeight() + " " + params.height + " ");

        viewPager.setLayoutParams(params);
        Log.e(TAG, viewPager.getHeight() + " " + viewPager.getLayoutParams().height);

        setUpViewPager(viewPager, roomId, name, hospital, userPic, detail);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager, Integer roomId, String name, String hospital, String userPic, String detail) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(LiveProgramDetailActivity.this.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(VideoDetailCommandFragment.newInstance(roomId), "评论");
        mIndexChildAdapter.addFragment(LiveProgramDetailInfoFragment.newInstance(name, hospital, userPic, detail), "详情");
        mIndexChildAdapter.addFragment(LiveDetailVideoFragment.newInstance(roomId), "相关视频");

        viewPager.setOffscreenPageLimit(3);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }

    /**
     * 以下为支付弹窗
     */
    private PopupWindow academicPopupWindow;

    private void initPopupwindow() {
        final View academicPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_pay_type, null);
        academicPopupWindow = new PopupWindow(academicPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView a = (TextView) academicPopupwindowView.findViewById(R.id.alipay);
        final TextView b = (TextView) academicPopupwindowView.findViewById(R.id.wechat);

        /**
         * 支付宝
         */
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAliPayInstalled(LiveProgramDetailActivity.this)) {
                    getPayInfo(v, "ALIPAY", "APP", programId);
                    academicPopupWindow.dismiss();
                } else {
                    ToastUtils.show(LiveProgramDetailActivity.this, "支付宝APP尚未安装，\n请重新选择其他支付方式");
                }
            }
        });

        /**
         * 微信
         */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 检测是否有微信软件 */
                if (isWXAppInstalledAndSupported(LiveProgramDetailActivity.this, api)) {
                    getPayInfo(v, "WXPAY", "APP", programId);
                    academicPopupWindow.dismiss();
                } else {
                    ToastUtils.show(LiveProgramDetailActivity.this, "微信APP尚未安装，\n请重新选择其他支付方式");
                }
            }
        });

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

    /**
     * 支付宝
     */
    //支付金额
    private float amount;

    //流水号
    private String tradeId = "";

    //获取支付订单信息
    private void getPayInfo(final View v, String paymentChannel, String platformType, int programId) {
        if ("ALIPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: " + e.getMessage()
                            + "\n" + e.getCause()
                            + "\n" + e.getLocalizedMessage()
                            + "\n" + e.getStackTrace());
                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> payinfo) {
                    tradeId = payinfo.getEntity().getPrepayId();
                    pay(v, payinfo.getEntity().getAmount() + "", payinfo.getEntity().getTradeTitle(), "直播", payinfo.getEntity().getPrepayId(), payinfo.getEntity().getAlipayOrderString());
                }
            }, new LiveOrderDto("", paymentChannel, platformType, programId));
        } else if ("WXPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> livePayDtoHttpResult3) {
                    payByWechat(livePayDtoHttpResult3.getEntity().getRequestPay().getPartnerid(),
                            livePayDtoHttpResult3.getEntity().getRequestPay().getPrepayid(),
                            livePayDtoHttpResult3.getEntity().getRequestPay().getNoncestr(),
                            livePayDtoHttpResult3.getEntity().getRequestPay().getTimeStamp(),
                            livePayDtoHttpResult3.getEntity().getRequestPay().getPackageX(),
                            livePayDtoHttpResult3.getEntity().getRequestPay().getSign());
                }
            }, new LiveOrderDto("", paymentChannel, platformType, programId));
        }
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
                        //通知后端，防止后端接受不到支付成功
                        HttpData.getInstance().HttpDataUpdateLiveOrderStatus(new Observer<HttpResult3<Object, Object>>() {
                            @Override
                            public void onCompleted() {
                                Log.e(TAG, "HttpDataUpdateLiveOrderStatus onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "HttpDataUpdateLiveOrderStatus onError: " + e.getMessage()
                                        + "\n" + e.getCause()
                                        + "\n" + e.getLocalizedMessage()
                                        + "\n" + e.getStackTrace());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, Object> objectObjectHttpResult3) {
                                Log.e(TAG, "onNext");
                            }
                        }, tradeId);


                        Toast.makeText(LiveProgramDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LiveProgramDetailActivity.this, MyPayLiveRoomActivity.class));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(LiveProgramDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(LiveProgramDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v, String amount, String title, String description, String paymentId, final String payInfo) {
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
//        String orderInfo = getOrderInfo(title, description, amount, paymentId);

        /*
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
//        String sign = sign(orderInfo);
//        try {
//            /*
//              仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        /*
         * 完整的符合支付宝参数规范的订单信息
         */
//        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        //此处payInfo由后端返回

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(LiveProgramDetailActivity.this);
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
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return com.medmeeting.m.zhiyi.paydemo.SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
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
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                    Toast.makeText(LiveProgramDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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

    /**
     * 加入融云直播间
     */
    private void loginRongCloudChatRoom(String userId, final String name, final String url) {
        UserInfo user = new UserInfo(userId, name, Uri.parse(url));
        LiveKit.setCurrentUser(user);
        Log.e(TAG + " loginRongCloudChatRoom", user.getName() + " " + LiveKit.getCurrentUser().getName());
        LiveKit.initMessageType();

        HttpData.getInstance().HttpDataGetUserIm(new Observer<HttpResult3<Object, RCUserDto>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<Object, RCUserDto> data) {
                Log.e(TAG, "onNext");
                LiveKit.connect(data.getEntity().getToken(),
                        new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                Log.e(TAG, "connect onTokenIncorrect");
                                // 检查appKey 与token是否匹配.
                            }

                            @Override
                            public void onSuccess(String s) {
                                Log.e(TAG, "connect onSuccess");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.e(TAG, "connect onError = " + errorCode);
                                // 根据errorCode 检查原因.
                            }
                        });
            }
        });

    }
}
