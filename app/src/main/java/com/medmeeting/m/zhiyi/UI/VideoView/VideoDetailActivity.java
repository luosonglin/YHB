package com.medmeeting.m.zhiyi.UI.VideoView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.MVP.Listener.SampleListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAndVideoPayDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoInfo;
import com.medmeeting.m.zhiyi.UI.Entity.VideoOrderDto;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.Login_v2Activity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.DownloadImageTaskUtil;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.videoplayer.LandLayoutVideoPlayer;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.snappydb.SnappydbException;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.Map;

import cn.jiguang.analytics.android.api.BrowseEvent;
import cn.jiguang.analytics.android.api.Currency;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.PurchaseEvent;
import rx.Observer;

import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_NORMAL;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING_BUFFERING_START;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PREPAREING;

public class VideoDetailActivity extends AppCompatActivity {

    //    NestedScrollView postDetailNestedScroll;
    LandLayoutVideoPlayer detailPlayer;
    RelativeLayout activityDetailPlayer;

    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String TAG = VideoDetailActivity.class.getSimpleName();
    private Integer videoId;
    private String videoTitle;

    private TextView start_status;

    /**
     * 极光统计、购买对象
     */
    PurchaseEvent pEvent;

    //统计浏览该页面时长
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

//        postDetailNestedScroll = (NestedScrollView) findViewById(R.id.post_detail_nested_scroll);
        detailPlayer = (LandLayoutVideoPlayer) findViewById(R.id.detail_player);
        activityDetailPlayer = (RelativeLayout) findViewById(R.id.activity_detail_player);

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

        videoId = getIntent().getIntExtra("videoId", 0);
        initView(videoId);

        startTime = System.nanoTime();
    }

    /**
     * 分享
     *
     * @param programId
     * @param title
     * @param photo
     * @param description
     */
    public void initShare(final int programId, final String title, final String photo, final String description) {

        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(VideoDetailActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {

                    UMWeb web = new UMWeb(Constant.Share_Video + programId); //http://wap.medmeeting.com/#!/video/
                    web.setTitle(title);//标题
                    web.setThumb(new UMImage(VideoDetailActivity.this, photo));  //缩略图
                    web.setDescription(description);//描述
                    new ShareAction(VideoDetailActivity.this)
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

    private void initView(Integer videoId) {
        HttpData.getInstance().HttpDataGetVideoDetail(new Observer<HttpResult3<Object, VideoDetailsEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(VideoDetailActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, VideoDetailsEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(VideoDetailActivity.this, data.getMsg());
                    return;
                }

                //视频标题
                videoTitle = data.getEntity().getTitle();

                if (data.getEntity().getUserId() == Data.getUserId()) {
                    HttpData.getInstance().HttpDataGetVideo2(new Observer<HttpResult3<Object, VideoInfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpResult3<Object, VideoInfo> data) {
                            initPlayer(data.getEntity().getUrl(), data.getEntity().getCoverPhoto(), data.getEntity().getTitle(), data.getEntity().getChargeType(), 0,
                                    data.getEntity().getUserId(), true);
                        }
                    }, videoId);
                } else {
                    initPlayer(data.getEntity().getUrl(), data.getEntity().getCoverPhoto(), data.getEntity().getTitle(), data.getEntity().getChargeType(), data.getEntity().getPrice(),
                            data.getEntity().getUserId(), data.getEntity().isPayFlag());
                }


                initTagsView(data.getEntity().getVideoId(), data.getEntity().getRoomId(), data.getEntity().getUserId());

                initShare(data.getEntity().getVideoId(), data.getEntity().getTitle(), data.getEntity().getCoverPhoto(), data.getEntity().getDes());
            }
        }, videoId);
    }

    private void initPlayer(String url, String photo, String title, String chargeType, float price, Integer userId, boolean payFlag) {
        //断网自动重新链接，url前接上ijkhttphook:
//        String url1= "http://baobab.wdjcdn.com/1451897812703c.mp4";
//        String url1 = "ijkhttphook:http://baobab.wdjcdn.com/14564977406580.mp4";
//        String url1 = "http://baobab.wdjcdn.com/14564977406580.mp4";
//        String url = "http://hcjs2ra2rytd8v8np1q.exp.bcevod.com/mda-hegtjx8n5e8jt9zv/mda-hegtjx8n5e8jt9zv.m3u8";
        //String url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
        //String url = "http://ocgk7i2aj.bkt.clouddn.com/17651ac2-693c-47e9-b2d2-b731571bad37";
//        String url1 = "http://111.198.24.133:83/yyy_login_server/pic/YB059284/97778276040859/1.mp4";
//        url = url1;
        //String url = "http://vr.tudou.com/v2proxy/v?sid=95001&id=496378919&st=3&pw=";
        //String url = "http://pl-ali.youku.com/playlist/m3u8?type=mp4&ts=1490185963&keyframe=0&vid=XMjYxOTQ1Mzg2MA==&ep=ciadGkiFU8cF4SvajD8bYyuwJiYHXJZ3rHbN%2FrYDAcZuH%2BrC6DPcqJ21TPs%3D&sid=04901859548541247bba8&token=0524&ctype=12&ev=1&oip=976319194";
//        String url = "http://hls.ciguang.tv/hdtv/video.m3u8";
        //String url = "https://res.exexm.com/cw_145225549855002";
        //String url = "http://storage.gzstv.net/uploads/media/huangmeiyan/jr05-09.mp4";//mepg

        //detailPlayer.setUp(url, false, null, "测试视频");
        //detailPlayer.setLooping(true);
        //detailPlayer.setShowPauseCover(false);

        //如果视频帧数太高导致卡画面不同步
        //VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);
        //如果视频seek之后从头播放
        //VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        //List<VideoOptionModel> list = new ArrayList<>();
        //list.add(videoOptionModel);
        //GSYVideoManager.instance().setOptionModelList(list);

        //GSYVideoManager.instance().setTimeOut(4000, true);


        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        if (url != null)
            gsyVideoOption//.setThumbImageView(imageView)
                    .setThumbPlay(false)    //是否点击封面可以播放
                    .setIsTouchWiget(true)  //是否可以滑动界面改变进度，声音等
                    .setRotateViewAuto(false)   //是否开启自动旋转
                    .setLockLand(false) //一全屏就锁屏横屏，默认false竖屏，可配合setRotateViewAuto使用
                    .setShowFullAnimation(false)    //是否使用全屏动画效果
                    .setNeedLockFull(true)  //是否需要全屏锁定屏幕功能
                    .setSeekRatio(1)    //调整触摸滑动快进的比例
                    .setUrl(url)//"http://baobab.wdjcdn.com/1451897812703c.mp4"
                    .setCacheWithPlay(false)
                    .setVideoTitle(title)
                    .setStandardVideoAllCallBack(new SampleListener() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            Debuger.printfError("***** onPrepared **** " + objects[0]);
                            Debuger.printfError("***** onPrepared **** " + objects[1]);
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            super.onAutoComplete(url, objects);
                        }

                        @Override
                        public void onClickStartError(String url, Object... objects) {
                            super.onClickStartError(url, objects);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                        }


                        @Override
                        public void onClickStartIcon(String url, Object... objects) {
                            super.onClickStartIcon(url, objects);
                        }
                    })
                    .setLockClickListener((view, lock) -> {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    })
                    .build(detailPlayer);


        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////        imageView.setImageResource(R.mipmap.video_bg);
//        new DownloadImageTaskUtil(imageView).execute(photo);
////        imageView.setImageURI(Uri.parse(photo));
//        detailPlayer.setThumbImageView(imageView);
        new DownloadImageTaskUtil(detailPlayer.getCoverPhoto()).execute(photo);
        if (detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING) {
            detailPlayer.getCoverPhoto().setVisibility(View.GONE);
        } else {
            detailPlayer.getCoverPhoto().setVisibility(View.VISIBLE);
        }


        if (detailPlayer.getFullscreenButton() != null) {
            detailPlayer.getFullscreenButton().setOnClickListener(v -> {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(VideoDetailActivity.this, true, true);
            });
        }

        if (Data.getUserId() == userId) {
            detailPlayer.getStartButton().setVisibility(View.VISIBLE);
            detailPlayer.getBuyButton().setVisibility(View.GONE);
        } else {
            if (chargeType.equals("yes") && !payFlag) {
                detailPlayer.getStartButton().setVisibility(View.GONE);
                detailPlayer.getBuyButton().setVisibility(View.VISIBLE);
                detailPlayer.getBuyButton().setText("购买 " + price + " 元");
                detailPlayer.getBuyButton().setTextSize(12);
                detailPlayer.getBuyButton().setOnClickListener(view -> {
                    try {
                        if (!DBUtils.isSet(VideoDetailActivity.this, "userToken")) {
                            startActivity(new Intent(VideoDetailActivity.this, Login_v2Activity.class));
                            ToastUtils.show(VideoDetailActivity.this, "请先登录");
                            return;
                        }
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                    initPopupwindow(videoId);
                });
            } else {
                detailPlayer.getStartButton().setVisibility(View.VISIBLE);
                detailPlayer.getBuyButton().setVisibility(View.GONE);
            }
        }

        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getTitleTextView().setText("           ");
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(view -> finish());
        detailPlayer.getShareButton().setOnClickListener(view -> {
            ShareBoardConfig config = new ShareBoardConfig();
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
            mShareAction.open(config);
        });

        if (detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING_BUFFERING_START
                || detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING) {
            HttpData.getInstance().HttpDataEditPlayCount(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3 httpResult3) {

                }
            }, videoId);
        }

    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }

    private void initTagsView(Integer videoId, Integer roomId, Integer userId) {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
//        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.height = this.getResources().getDisplayMetrics().heightPixels - 230 - 50;
//        Log.e(getLocalClassName(), params.height+"  hhhh "+this.getResources().getDisplayMetrics().heightPixels);
//
//        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager, videoId, roomId, userId);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
    }

    private void setUpViewPager(ViewPager viewPager, Integer videoId, Integer roomId, Integer userId) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(VideoDetailActivity.this.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(VideoDetailCommandFragment.newInstance(videoId), "评论");
        mIndexChildAdapter.addFragment(VideoDetailInfomationFragment.newInstance(videoId), "详情");

        if (userId.equals(Data.getUserId())) {
            mIndexChildAdapter.addFragment(VideoDetailFareFragment.newInstance(videoId), "收费统计");
        } else {
            mIndexChildAdapter.addFragment(VideoDetailOtherFragment.newInstance(roomId), "相关视频");
        }

        viewPager.setOffscreenPageLimit(3);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }

    /**
     * 以下为支付弹窗
     */
    private PopupWindow academicPopupWindow;

    private void initPopupwindow(Integer videoId) {
        final View academicPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_pay_type, null);
        academicPopupWindow = new PopupWindow(academicPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView a = (TextView) academicPopupwindowView.findViewById(R.id.alipay);
        final TextView b = (TextView) academicPopupwindowView.findViewById(R.id.wechat);

        a.setOnClickListener(v -> {
            if (checkAliPayInstalled(VideoDetailActivity.this)) {
                getPayInfo(v, "ALIPAY", "APP", videoId);
                academicPopupWindow.dismiss();
            } else {
                ToastUtils.show(VideoDetailActivity.this, "支付宝APP尚未安装，\n请重新选择其他支付方式");
            }
        });

        b.setOnClickListener(v -> {
            if (isWXAppInstalledAndSupported(VideoDetailActivity.this, api)) {
                getPayInfo(v, "WXPAY", "APP", videoId);
                academicPopupWindow.dismiss();
            } else {
                ToastUtils.show(VideoDetailActivity.this, "微信APP尚未安装，\n请重新选择其他支付方式");
            }
        });

        LinearLayout academicPopupParentLayout = (LinearLayout) academicPopupwindowView.findViewById(R.id.popup_parent);
        academicPopupParentLayout.setOnClickListener(v -> {
            if (academicPopupWindow != null && academicPopupWindow.isShowing()) {
                academicPopupWindow.dismiss();
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
    private void getPayInfo(final View v, String paymentChannel, String platformType, int videoId) {
        Data.setVideoId(videoId);

        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setOpenId("");
        videoOrderDto.setPaymentChannel(paymentChannel);
        videoOrderDto.setPlatformType(platformType);
        videoOrderDto.setVideoId(videoId);

        if ("ALIPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataPayVideo(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(VideoDetailActivity.this, e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(VideoDetailActivity.this, data.getMsg());
                        return;
                    }
                    tradeId = data.getEntity().getPrepayId();
                    Data.setPayType(0);
                    Data.setTradeId(tradeId);
                    pay(v, data.getEntity().getAmount() + "", data.getEntity().getTradeTitle(), "视频", data.getEntity().getPrepayId(), data.getEntity().getAlipayOrderString());

                    //极光统计  购买对象
                    pEvent = new PurchaseEvent(videoId + "", data.getEntity().getTradeTitle(), data.getEntity().getAmount(), true, Currency.CNY, "视频", 1);
                }
            }, videoOrderDto);
        } else if ("WXPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataPayVideo(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(VideoDetailActivity.this, e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(VideoDetailActivity.this, data.getMsg());
                        return;
                    }
                    tradeId = data.getEntity().getPrepayId();
                    Data.setPayType(1);
                    Data.setTradeId(tradeId);
                    finish();//
                    payByWechat(data.getEntity().getRequestPay().getPartnerid(),
                            data.getEntity().getRequestPay().getPrepayid(),
                            data.getEntity().getRequestPay().getNoncestr(),
                            data.getEntity().getRequestPay().getTimeStamp(),
                            data.getEntity().getRequestPay().getPackageX(),
                            data.getEntity().getRequestPay().getSign());
                    //payByWechat(final String partnerId, final String prepayId, final String nonceStr, final String timeStamp, final String packageValue, final String sign) {

                    //极光统计  购买对象
                    pEvent = new PurchaseEvent(videoId + "", data.getEntity().getTradeTitle(), data.getEntity().getAmount(), true, Currency.CNY, "视频", 1);
                    Data.setPurchaseEvent(pEvent);
                }
            }, videoOrderDto);
        }
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
                        //通知后端，防止后端接受不到支付成功
                        HttpData.getInstance().HttpDataUpdateLiveOrderStatus(new Observer<HttpResult3<Object, Object>>() {
                            @Override
                            public void onCompleted() {
                                Log.e(TAG, "HttpDataUpdateLiveOrderStatus onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(VideoDetailActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, Object> objectObjectHttpResult3) {
                                Log.e(TAG, "onNext");
                            }
                        }, tradeId);

                        Toast.makeText(VideoDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        initView(videoId);

                        //购买事件
                        pEvent.setPurchaseSuccess(true);
                        JAnalyticsInterface.onEvent(VideoDetailActivity.this, pEvent);

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(VideoDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(VideoDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }

                        //购买事件
                        pEvent.setPurchaseSuccess(false);
                        JAnalyticsInterface.onEvent(VideoDetailActivity.this, pEvent);
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
    public void pay(View v, String amount, String title, String description, String paymentId, final String payInfo) {
        if (TextUtils.isEmpty(Constant.PARTNER) || TextUtils.isEmpty(Constant.RSA_PRIVATE) || TextUtils.isEmpty(Constant.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialoginterface, i) -> finish()).show();
            return;
        }

        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(VideoDetailActivity.this);
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
                Toast.makeText(VideoDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onStop() {
        endTime = System.nanoTime();

        //极光统计  浏览事件
        BrowseEvent bEvent = new BrowseEvent(videoId + "", videoTitle, "视频", (endTime - startTime)/1000000000);
        JAnalyticsInterface.onEvent(this, bEvent);

        super.onStop();
    }
}
