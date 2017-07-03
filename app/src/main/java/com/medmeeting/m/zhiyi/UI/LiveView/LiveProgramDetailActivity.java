package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveAudienceDetailDto;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private Integer roomId = 0;

    //拉流地址
    private String url;

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
        initShare(savedInstanceState, getIntent().getExtras().getInt("roomId"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("coverPhote"),
                "haha");//getIntent().getExtras().getString("description")
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

    public void initShare(Bundle savedInstanceState, final int roomId, final String title, final String phone, final String description){
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
                        web.setThumb(new UMImage(LiveProgramDetailActivity.this, phone));  //缩略图
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

        HttpData.getInstance().HttpDataGetLiveProgramAudienceDetail(new Observer<HttpResult3<Object, LiveAudienceDetailDto>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage()
                        +"\n"+e.getCause()
                        +"\n"+e.getLocalizedMessage()
                        +"\n"+e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<Object, LiveAudienceDetailDto> liveDtoHttpResult3) {
                Log.e(TAG, "onNext");

                url = liveDtoHttpResult3.getEntity().getRtmpPlayUrl();

                initTagsView(getIntent().getExtras().getInt("roomId"),
                        liveDtoHttpResult3.getEntity().getAuthorName(),
                        liveDtoHttpResult3.getEntity().getAuthorTitle(),
                        liveDtoHttpResult3.getEntity().getCoverPhoto(),
                        liveDtoHttpResult3.getEntity().getDes());

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

        //点击"观看"
        watchTv = (TextView) findViewById(R.id.watch);
        if ("yes".equals(chargeType)) {
            watchTv.setText("支付" + price + "元观看");
            watchTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initPopupwindow();
                }
            });
        } else if ("no".equals(chargeType)) {
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

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getPayInfo(v, paymentId, "alipay");
                academicPopupWindow.dismiss();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                academicPopupWindow.dismiss();
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
}


