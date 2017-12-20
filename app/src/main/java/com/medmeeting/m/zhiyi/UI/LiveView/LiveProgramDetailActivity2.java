package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.MVP.Listener.SampleListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAndVideoPayDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveOrderDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveProgramDateilsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.ChatListAdapter;
import com.medmeeting.m.zhiyi.UI.MineView.MyOrderActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.videoplayer.LandLayoutLivePlayer;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
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
import java.util.Objects;
import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import rx.Observer;

import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_NORMAL;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PREPAREING;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 07/11/2017 6:44 PM
 * @describe 直播节目详情页（普通用户）
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveProgramDetailActivity2 extends AppCompatActivity implements Handler.Callback {

    NestedScrollView postDetailNestedScroll;
    LandLayoutLivePlayer detailPlayer;
    RelativeLayout activityDetailPlayer;

    private boolean isPlay;
    private boolean isPause;

    private OrientationUtils orientationUtils;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    static final String TAG = LiveProgramDetailActivity2.class.getSimpleName();
    private Integer programId;
    private String url;


    //以下为直播室互动参数
    private Handler handler = new Handler(this);
    private ChatListAdapter chatListAdapter;
    private Random random = new Random();
    private String audienceUserName;
    private String audienceUserNickName;

    private ImageView cover;
    private TextView buyBtn;
    private ImageView back;
    private ImageView share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail2);

        postDetailNestedScroll = (NestedScrollView) findViewById(R.id.post_detail_nested_scroll);
        detailPlayer = (LandLayoutLivePlayer) findViewById(R.id.detail_player);
        activityDetailPlayer = (RelativeLayout) findViewById(R.id.activity_detail_player);

        cover = (ImageView) findViewById(R.id.cover);
        buyBtn = (TextView) findViewById(R.id.buy);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(view -> {
            ShareBoardConfig config = new ShareBoardConfig();
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
            mShareAction.open(config);
        });

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

        programId = getIntent().getIntExtra("programId", 0);
        initView(programId);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();

        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    public void initShare(final int programId, final String title, final String photo, final String description) {

        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(LiveProgramDetailActivity2.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {

                    UMWeb web = new UMWeb("http://mobile.medmeeting.com/#/live/show/share/" + programId); //http://wap.medmeeting.com/#!/live/room/show/
                    web.setTitle(title);//标题
                    web.setThumb(new UMImage(LiveProgramDetailActivity2.this, photo));  //缩略图
                    web.setDescription(description);//描述
                    new ShareAction(LiveProgramDetailActivity2.this)
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


//    @Override
//    protected void onResumeFragments() {
//        initView(programId);
//        super.onResumeFragments();
//    }


//    @Override
//    protected void onRestart() {
//        initView(programId);
//        super.onRestart();
//    }

    private void initView(Integer programId) {
        HttpData.getInstance().HttpDataGetOpenProgramDetail(new Observer<HttpResult3<Object, LiveProgramDateilsEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(LiveProgramDetailActivity2.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, LiveProgramDateilsEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(LiveProgramDetailActivity2.this, data.getMsg());
                    return;
                }
                url = data.getEntity().getRtmpPlayUrl() + "";

                initFakerPlayer(url, data.getEntity().getCoverPhoto(), data.getEntity().getTitle(),
                        data.getEntity().getChargeType(), data.getEntity().getPrice(),
                        data.getEntity().getPayFalg(), data.getEntity().getRoomUserId(),
                        data.getEntity().getLiveStatus());

//                try {
//                    audienceUserName = DBUtils.get(LiveProgramDetailActivity2.this, "userName");
//                    audienceUserNickName = DBUtils.get(LiveProgramDetailActivity2.this, "userNickName");
//                    Log.e(TAG, "haha " + audienceUserName + " " + audienceUserNickName);
//
//                    if (audienceUserName == null || audienceUserName.equals("") || audienceUserName.equals("null")) {
//                        loginRongCloudChatRoom(Data.getUserId() + "", audienceUserNickName, url);
//                    } else {
//                        loginRongCloudChatRoom(Data.getUserId() + "", audienceUserName, url);
//                    }
//                } catch (SnappydbException e) {
//                    e.printStackTrace();
//                }

                initTagsView(data.getEntity());

                initShare(programId,
                        data.getEntity().getTitle(),
                        data.getEntity().getCoverPhoto(),
                        data.getEntity().getDes());
            }
        }, programId);
    }

    //liveStatus (string, optional): 直播状态（ready：准备中，play：直播中，wait：断开中，end：已结束） ,
    private void initFakerPlayer(String url, String photo, String title, String chargeType, float price, Integer payFlag, Integer userId, String liveStatus) {
        Glide.with(LiveProgramDetailActivity2.this)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(cover);

        Log.e("initPlayer(1", url);
        if (url == null) {
            Log.e("initPlayer(1", "fuck");
        }

        if (chargeType.equals("yes") && payFlag == 0) {
            buyBtn.setVisibility(View.VISIBLE);
            buyBtn.setText("购买 " + price + " 元");
            buyBtn.setTextSize(12);
            buyBtn.setTextColor(Color.WHITE);
            buyBtn.setOnClickListener(view -> initPopupwindow(programId));
            Log.e("eeee", chargeType + " " + payFlag + " " + detailPlayer.getBuyButton().getText().toString());
        } else {
            switch (liveStatus) {
                case "ready":
                    buyBtn.setVisibility(View.VISIBLE);
                    buyBtn.setText("主播正在准备中");
                    buyBtn.setTextSize(12);
                    buyBtn.setTextColor(Color.WHITE);
                    buyBtn.setClickable(false);
                    break;
                case "play":
                    buyBtn.setVisibility(View.VISIBLE);
                    buyBtn.setText("点击开始");
                    buyBtn.setTextSize(12);
                    buyBtn.setTextColor(Color.WHITE);
                    buyBtn.setClickable(true);
                    buyBtn.setOnClickListener(view -> {

                        try {
                            audienceUserName = DBUtils.get(LiveProgramDetailActivity2.this, "userName");
                            audienceUserNickName = DBUtils.get(LiveProgramDetailActivity2.this, "userNickName");
                            Log.e(TAG, "haha " + audienceUserName + " " + audienceUserNickName);

                            if (audienceUserName == null || audienceUserName.equals("") || audienceUserName.equals("null")) {
                                loginRongCloudChatRoom(Data.getUserId() + "", audienceUserNickName, url);
                            } else {
                                loginRongCloudChatRoom(Data.getUserId() + "", audienceUserName, url);
                            }
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(LiveProgramDetailActivity2.this, LivePlayerActivity2.class)
                                .putExtra("programId", programId)
                                .putExtra("url", url));

                    });
                    break;
                case "wait":
                    buyBtn.setVisibility(View.VISIBLE);
                    buyBtn.setText("主播已离开");
                    buyBtn.setTextSize(12);
                    buyBtn.setTextColor(Color.WHITE);
                    buyBtn.setClickable(false);
                    break;
                case "end":
                    buyBtn.setVisibility(View.VISIBLE);
                    buyBtn.setText("直播已结束");
                    buyBtn.setTextSize(12);
                    buyBtn.setTextColor(Color.WHITE);
                    buyBtn.setClickable(false);
                    break;
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initPlayer(String url, String photo, String title, String chargeType, float price, Integer payFlag, Integer userId) {
        Log.e("initPlayer() ", url);
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
                    .setUrl(url)
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
//                            initChat(url);
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
                    .setLockClickListener(new LockClickListener() {
                        @Override
                        public void onClick(View view, boolean lock) {
                            if (orientationUtils != null) {
                                //配合下方的onConfigurationChanged
                                orientationUtils.setEnable(!lock);
                            }
                        }
                    })
                    .build(detailPlayer);

//        new DownloadImageTaskUtil(detailPlayer.getCoverPhoto()).execute(photo);
        detailPlayer.getCoverPhoto().setImageResource(R.mipmap.video_bg);

        if (detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING) {
            detailPlayer.getCoverPhoto().setVisibility(View.GONE);
        } else {
            detailPlayer.getCoverPhoto().setVisibility(View.VISIBLE);
        }


        if (detailPlayer.getFullscreenButton() != null) {
            detailPlayer.getFullscreenButton().setOnClickListener(v -> {
//                //直接横屏
//                orientationUtils.resolveByClick();
//
//                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
//                detailPlayer.startWindowFullscreen(LiveProgramDetailActivity2.this, true, true);

                startActivity(new Intent(LiveProgramDetailActivity2.this, LivePlayerActivity2.class)
                        .putExtra("programId", programId)
                        .putExtra("url", url));
            });
        }
        Log.e("initPlayer(2", url);
        /**
         * 对比userId chargeType url
         */
        Log.e("aaaaa", Data.getUserId() + " " + userId + " " + chargeType + " " + payFlag);
        if (Objects.equals(Data.getUserId(), userId)) {
            detailPlayer.getStartButton().setVisibility(View.VISIBLE);
            detailPlayer.getBuyButton().setVisibility(View.GONE);
        } else {
            if (chargeType.equals("yes") && payFlag == 0) {
                detailPlayer.getStartButton().setVisibility(View.GONE);
                detailPlayer.getBuyButton().setVisibility(View.VISIBLE);
                detailPlayer.getBuyButton().setText("购买 " + price + " 元");
                detailPlayer.getBuyButton().setTextSize(12);
                detailPlayer.getBuyButton().setOnClickListener(view -> initPopupwindow(programId));
                Log.e("eeee", chargeType + " " + payFlag + " " + detailPlayer.getBuyButton().getText().toString());
            } else {
                detailPlayer.getStartButton().setVisibility(View.VISIBLE);
                detailPlayer.getBuyButton().setVisibility(View.GONE);
            }
        }

        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(view -> finish());
        detailPlayer.getShareButton().setOnClickListener(view -> ToastUtils.show(LiveProgramDetailActivity2.this, "share"));

//        detailPlayer.getFullscreenButton().setOnClickListener(view -> {
//            try {
//                audienceUserName = DBUtils.get(LiveProgramDetailActivity2.this, "userName");
//                audienceUserNickName = DBUtils.get(LiveProgramDetailActivity2.this, "userNickName");
//                Log.e(TAG, "haha" + audienceUserName + " " + audienceUserNickName);
//
//                if (audienceUserName == null || audienceUserName.equals("、") || audienceUserName.equals("null")) {
//                    loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity2.this, "userId"), audienceUserNickName, url);
//                } else {
//                    loginRongCloudChatRoom(DBUtils.get(LiveProgramDetailActivity2.this, "userId"), audienceUserName, url);
//                }
//            } catch (SnappydbException e) {
//                e.printStackTrace();
//            } finally {
////                Intent intent = new Intent(LiveProgramDetailActivity2.this, LivePlayerActivity.class);
////                intent.putExtra("rtmpPlayUrl", url);
////                intent.putExtra("programId", programId);
////                startActivity(intent);
//            }
//        });

//        detailPlayer.getFullscreenButton().setVisibility(View.GONE);


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


        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);

                // 配合ios，tony说"去掉离开了房间"
//                TextMessage content = TextMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);
//                Log.e(TAG, content + " " + content.getUserInfo().getName());

                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }

    private void initTagsView(LiveProgramDateilsEntity liveProgramDateilsEntity) {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = this.getWindowManager().getDefaultDisplay().getHeight() - 140 * 6;//800

        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager, liveProgramDateilsEntity);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
    }

    private void setUpViewPager(ViewPager viewPager, LiveProgramDateilsEntity liveProgramDateilsEntity) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(LiveProgramDetailActivity2.this.getSupportFragmentManager());//.getChildFragmentManager()

//        mIndexChildAdapter.addFragment(VideoDetailCommandFragment.newInstance(liveProgramDateilsEntity.getRoomId()), "评论");
        mIndexChildAdapter.addFragment(LiveProgramDetailInfoFragment.newInstance(liveProgramDateilsEntity, liveProgramDateilsEntity.getId()), "详情");
        mIndexChildAdapter.addFragment(LiveDetailVideoFragment.newInstance(liveProgramDateilsEntity.getRoomId()), "相关预告");

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

        /**
         * 支付宝
         */
        a.setOnClickListener(v -> {
            if (checkAliPayInstalled(LiveProgramDetailActivity2.this)) {
                getPayInfo(v, "ALIPAY", "APP", videoId);
                academicPopupWindow.dismiss();
            } else {
                ToastUtils.show(LiveProgramDetailActivity2.this, "支付宝APP尚未安装，\n请重新选择其他支付方式");
            }
        });

        /**
         * 微信
         */
        b.setOnClickListener(v -> {
            /** 检测是否有微信软件 */
            if (isWXAppInstalledAndSupported(LiveProgramDetailActivity2.this, api)) {
                getPayInfo(v, "WXPAY", "APP", videoId);
                academicPopupWindow.dismiss();
            } else {
                ToastUtils.show(LiveProgramDetailActivity2.this, "微信APP尚未安装，\n请重新选择其他支付方式");
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
    private void getPayInfo(final View v, String paymentChannel, String platformType, int programId) {

        LiveOrderDto liveOrderDto = new LiveOrderDto();
        liveOrderDto.setOpenId("");
        liveOrderDto.setPaymentChannel(paymentChannel);
        liveOrderDto.setPlatformType(platformType);
        liveOrderDto.setProgramId(programId);

        if ("ALIPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(LiveProgramDetailActivity2.this, e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(LiveProgramDetailActivity2.this, data.getMsg());
                        return;
                    }
                    tradeId = data.getEntity().getPrepayId();
                    pay(v, data.getEntity().getAmount() + "", data.getEntity().getTradeTitle(), "直播", data.getEntity().getPrepayId(), data.getEntity().getAlipayOrderString());
                }
            }, liveOrderDto);
        } else if ("WXPAY".equals(paymentChannel)) {
            HttpData.getInstance().HttpDataGetLiveOrder(new Observer<HttpResult3<Object, LiveAndVideoPayDto>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(LiveProgramDetailActivity2.this, e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<Object, LiveAndVideoPayDto> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(LiveProgramDetailActivity2.this, data.getMsg());
                        return;
                    }
                    payByWechat(data.getEntity().getRequestPay().getPartnerid(),
                            data.getEntity().getRequestPay().getPrepayid(),
                            data.getEntity().getRequestPay().getNoncestr(),
                            data.getEntity().getRequestPay().getTimeStamp(),
                            data.getEntity().getRequestPay().getPackageX(),
                            data.getEntity().getRequestPay().getSign());
                }
            }, liveOrderDto);
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
                                ToastUtils.show(LiveProgramDetailActivity2.this, e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, Object> data) {
                                startActivity(new Intent(LiveProgramDetailActivity2.this, MyOrderActivity.class));
                                Log.e(TAG, "onNext");
                            }
                        }, tradeId);

                        Toast.makeText(LiveProgramDetailActivity2.this, "支付成功", Toast.LENGTH_SHORT).show();
                        initView(programId);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(LiveProgramDetailActivity2.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(LiveProgramDetailActivity2.this, "支付失败", Toast.LENGTH_SHORT).show();
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
    public void pay(View v, String amount, String title, String description, String paymentId, final String payInfo) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialoginterface, i) -> {
                        finish();
                    }).show();
            return;
        }

        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(LiveProgramDetailActivity2.this);
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
                Toast.makeText(LiveProgramDetailActivity2.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
     * 直播聊天室
     * ======================================================================================================================================================
     */
    private void initChat(String url) {

        Log.e("initchat", "initChat()");


        joinChatRoom(programId + "");
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
            }

            @Override
            public void onNext(HttpResult3<Object, RCUserDto> data) {
                LiveKit.connect(data.getEntity().getToken(),
                        new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                Log.e(TAG, "LiveKit.connect onTokenIncorrect");
                                // 检查appKey 与token是否匹配.
                            }

                            @Override
                            public void onSuccess(String s) {
                                Log.e(TAG, "LiveKit.connect onSuccess " + s);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.e(TAG, "LiveKit.connect onError = " + errorCode);
                                // 根据errorCode 检查原因.
                            }
                        });
            }
        });
    }


    /**
     * im
     */
    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom(roomId, 15, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了房间");
//                LiveKit.sendMessage(content);

                // 配合ios
                TextMessage content = TextMessage.obtain("进入了房间");
                LiveKit.sendMessage(content);

                Log.e(TAG + " LiveKit.joinChatRoom: ", content + "" + content.getUserInfo().getName());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Toast.makeText(LivePlayerActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "LiveKit.聊天室加入失败! errorCode = " + errorCode);
            }
        });
    }
}