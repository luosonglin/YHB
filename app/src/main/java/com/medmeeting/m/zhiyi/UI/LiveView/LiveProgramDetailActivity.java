package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LivePayDto;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
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
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import rx.Observer;

/**
 * 直播节目详情页（普通用户）
 */
public class LiveProgramDetailActivity extends AppCompatActivity {
    private static final String TAG = LiveProgramDetailActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView shareIv;
    //    private ViewGroup tab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context = this;
    private ScrollView scrollView;
    //背景图
    private ImageView imageView;
    //直播间头像
    private ImageView coverPhotoTv;
    private TextView titleTv, userNameTv;
    //观看
    private TextView watchTv;

    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;

    private Integer programId = 0;

    //拉流地址
    private String url;

    //payFalg (integer, optional): 是否购票 0:未购票，大于0:已购票 ,
    private Integer payFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail);
        toolBar();
        initView(getIntent().getExtras().getString("coverPhoto"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("authorName"),
                getIntent().getExtras().getString("chargeType"),
                getIntent().getExtras().getFloat("price"));
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
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        shareIv = (ImageView) findViewById(R.id.share);
        shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                mShareAction.open(config);
            }
        });
        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new LiveProgramDetailActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(LiveProgramDetailActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        UMWeb web = new UMWeb("http://wap.medmeeting.com/#!/live/room/show/" + roomId);
                        web.setTitle(title);//标题
//                        web.setThumb(new UMImage(LiveProgramDetailActivity.this, phone));  //缩略图
                        web.setDescription(description);//描述
                        new ShareAction(LiveProgramDetailActivity.this)
                                .withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
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

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<LiveProgramDetailActivity> mActivity;

        private CustomShareListener(LiveProgramDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(mActivity.get(), platform + "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.e(TAG, "umeng throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
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

    private void initView(String coverPhone, String title, String userName, String chargeType, float price) {
        //直播间封面
        coverPhotoTv = (ImageView) findViewById(R.id.coverPhoto);
        titleTv = (TextView) findViewById(R.id.title);
        userNameTv = (TextView) findViewById(R.id.userName);
        Glide.with(LiveProgramDetailActivity.this)
                .load(coverPhone)
                .crossFade()
                .transform(new GlideCircleTransform(LiveProgramDetailActivity.this))
                .placeholder(R.mipmap.ic_launcher)
                .into(coverPhotoTv);
        titleTv.setText(title);
        userNameTv.setText("主讲人：" + userName);

        //点击"观看"
        watchTv = (TextView) findViewById(R.id.watch);

        HttpData.getInstance().HttpDataGetLiveProgramAudienceDetail(new Observer<HttpResult3<Object, LiveAudienceDetailDto>>() {
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
            public void onNext(final HttpResult3<Object, LiveAudienceDetailDto> liveDtoHttpResult3) {
                Log.e(TAG, "onNext");

                programId = liveDtoHttpResult3.getEntity().getId();
                url = liveDtoHttpResult3.getEntity().getRtmpPlayUrl();
                amount = liveDtoHttpResult3.getEntity().getPrice();
                payFlag = liveDtoHttpResult3.getEntity().getPayFalg();

                initTagsView(getIntent().getExtras().getInt("roomId"),
                        liveDtoHttpResult3.getEntity().getAuthorName(),
                        liveDtoHttpResult3.getEntity().getAuthorTitle(),
                        liveDtoHttpResult3.getEntity().getCoverPhoto(),
                        liveDtoHttpResult3.getEntity().getDes());


                if ("yes".equals(liveDtoHttpResult3.getEntity().getChargeType())) {
                    if (payFlag == 0) {//0:未购票
                        watchTv.setText("支付" + amount + "元观看");
                        watchTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initPopupwindow();
                            }
                        });
                    } else if (payFlag > 0) { //大于0:已购票
                        watchTv.setText("观看");
                        watchTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(LiveProgramDetailActivity.this, LivePlayerActivity.class);
                                intent.putExtra("rtmpPlayUrl", url);
                                intent.putExtra("onlineVidoId", getIntent().getExtras().getInt("programId"));
                                startActivity(intent);
                            }
                        });
                    }

                } else if ("no".equals(liveDtoHttpResult3.getEntity().getChargeType())) {
                    watchTv.setText("观看");
                    watchTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(LiveProgramDetailActivity.this, LivePlayerActivity.class);
                            intent.putExtra("rtmpPlayUrl", liveDtoHttpResult3.getEntity().getRtmpPlayUrl());
                            intent.putExtra("onlineVidoId", getIntent().getExtras().getInt("programId"));
                            startActivity(intent);
                        }
                    });
                }

            }
        }, getIntent().getExtras().getInt("programId"));

        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        // 获取控件
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imageView = (ImageView) findViewById(R.id.img);

        // 设置图片初始大小 这里我设为满屏的16:9
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        imageView.setLayoutParams(lp);

        // 监听滚动事件
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView
                        .getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 手指离开后恢复图片
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            if (scrollView.getScrollY() == 0) {
                                mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                        if (distance < 0) { // 当前位置比记录位置要小，正常返回
                            break;
                        }

                        // 处理放大
                        mScaling = true;
                        lp.width = metric.widthPixels + distance;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        imageView.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });


    }

    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView
                .getLayoutParams();
        final float w = imageView.getLayoutParams().width;// 图片当前宽度
        final float h = imageView.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F)
                .setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                imageView.setLayoutParams(lp);
            }
        });
        anim.start();
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
        LiveProgramDetailActivity.IndexChildAdapter mIndexChildAdapter = new LiveProgramDetailActivity.IndexChildAdapter(LiveProgramDetailActivity.this.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(LiveProgramDetailInfoFragment.newInstance(name, hospital, userPic, detail), "直播详情");
        mIndexChildAdapter.addFragment(LiveDetailVideoFragment.newInstance(roomId), "相关视频");
        mIndexChildAdapter.addFragment(LiveDetailSummaryFragment.newInstance("haa"), "TA的直播");

        viewPager.setOffscreenPageLimit(3);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }

    public class IndexChildAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public IndexChildAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
//            if("待确认".equals(title)) {
//                TrackerUtils.sendUvLog(getActivity(), "3");
//            } else if ("已确认".equals(title)) {
//                TrackerUtils.sendUvLog(getActivity(), "4");
//            } else if ("配送中".equals(title)) {
//                TrackerUtils.sendUvLog(getActivity(), "5");
//            } else if ("所有".equals(title)) {
//                TrackerUtils.sendUvLog(getActivity(), "6");
//            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
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
                    ToastUtils.show(LiveProgramDetailActivity.this, "支付宝APP尚未安装，请重新选择其他支付方式");
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
                    ToastUtils.show(LiveProgramDetailActivity.this, "亲，您还没有安装微信");
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

    //获取支付订单信息
    private void getPayInfo(final View v, String paymentChannel, String platformType, int programId) {
        if ("ALIPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LivePayDto>>() {
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
                public void onNext(HttpResult3<Object, LivePayDto> payinfo) {
                    pay(v, payinfo.getEntity().getAmount() + "", payinfo.getEntity().getTradeTitle(), "某商品", payinfo.getEntity().getPrepayId(), payinfo.getEntity().getAlipayOrderString());
                }
            }, new LiveOrderDto("", paymentChannel, platformType, programId));
        } else if ("WXPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LivePayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3<Object, LivePayDto> livePayDtoHttpResult3) {
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
                        Toast.makeText(LiveProgramDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
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
                    req.extData = "哈哈，松林测试";//"app data"; // optional
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
}


