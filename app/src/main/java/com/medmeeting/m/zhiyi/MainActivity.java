package com.medmeeting.m.zhiyi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.RCUserDto;
import com.medmeeting.m.zhiyi.UI.Entity.Version;
import com.medmeeting.m.zhiyi.UI.IndexView.IndexFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveBuildRoomActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveIndexFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.MeetingView.ExchangeBusinessCardActivity;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingFragment2;
import com.medmeeting.m.zhiyi.UI.MeetingView.PlusSignedDetailsActivity;
import com.medmeeting.m.zhiyi.UI.MineView.MineFragment;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.Login_v2Activity;
import com.medmeeting.m.zhiyi.Util.CustomUtils;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.ImageGalleryPopmenu.PopMenu;
import com.medmeeting.m.zhiyi.Widget.ImageGalleryPopmenu.PopMenuItem;
import com.medmeeting.m.zhiyi.Widget.UpdataDialog;
import com.snappydb.SnappydbException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import io.rong.imlib.RongIMClient;
import rx.Observer;

public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener,
        MeetingFragment2.OnFragmentInteractionListener,
        LiveIndexFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener {

    private static final String TABORDERSTAG = "TABORDERSTAG";
    private static final String TABMERCHANDISE = "TABMERCHANDISE";
    private static final String TABPURCHASE = "TABPURCHASE";
    private static final String TABSELFTAG = "TABSELFTAG";

    private static final String TAG = MainActivity.class.getSimpleName();

    private PopMenu mPopMenu;

    private UpdataDialog updataDialog;
    private String versionName = "";
    private int versioncode;
    private String oldVersion, newVersion, versionmsg, url, channelid;
    private TextView tvmsg, tvcode;
    private ImageView updateDeletIv;

    //首页
    @BindView(R.id.tab_index)
    View tabOrders;
    @BindView(R.id.tab_index_img)
    ImageView tabOrdersImg;
    @BindView(R.id.tab_index_title)
    TextView tabOrdersTitle;

    //知医
    private static View tabMerchandise;
    @BindView(R.id.tab_doctor_img)
    ImageView tabMerchandiseImg;
    @BindView(R.id.tab_doctor_title)
    TextView tabMerchandiseTitle;

    //加号
    @BindView(R.id.tab_plus)
    View tabPlus;
    @BindView(R.id.tab_plus_img)
    ImageView tabPlusIv;

    //圈子
    private static View tabPurchase;
    @BindView(R.id.tab_community_img)
    ImageView tabPurchaseImg;
    @BindView(R.id.tab_community_title)
    TextView tabPurchaseTitle;

    //个人
    @BindView(R.id.tab_mine)
    View tabSelf;
    @BindView(R.id.tab_mine_img)
    ImageView tabSelfImg;
    @BindView(R.id.tab_mine_title)
    TextView tabSelfTitle;

    private FragmentManager fragmentManager;
    private IndexFragment mIndexFragment;
    private MeetingFragment2 mMeetingFragment;
    private LiveIndexFragment mLiveIndexFragment;
    private MineFragment mMineFragment;

    @OnClick(R.id.tab_index)
    void orderTab() {
        setTabSelection(tabOrders);
    }

    @OnClick(R.id.tab_doctor)
    void merchandiseTab() {
        setTabSelection(tabMerchandise);
    }

    @OnClick(R.id.tab_plus)
    void showWeiboTab() {
        setTabSelection(tabPlus);
    }

    @OnClick(R.id.tab_community)
    void purchaseTab() {
        setTabSelection(tabPurchase);
    }

    @OnClick(R.id.tab_mine)
    void personTab() {
        setTabSelection(tabSelf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.setStatusBarColor(this, Color.LTGRAY, true);

        tabMerchandise = findViewById(R.id.tab_doctor);
        tabPurchase = findViewById(R.id.tab_community);

        ButterKnife.bind(this);

//        initUmengConfig();

        //默认第一项选中
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mIndexFragment = (IndexFragment) fragmentManager.findFragmentByTag(TABORDERSTAG);
            mMeetingFragment = (MeetingFragment2) fragmentManager.findFragmentByTag(TABMERCHANDISE);
            mLiveIndexFragment = (LiveIndexFragment) fragmentManager.findFragmentByTag(TABPURCHASE);
            mMineFragment = (MineFragment) fragmentManager.findFragmentByTag(TABSELFTAG);
        }
        setTabSelection(tabOrders);

        initUserToken();

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
            public void onNext(final HttpResult3<Object, RCUserDto> data) {
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
                                Log.e(TAG, "connect onSuccess " + s + " " + data.getEntity().getToken());
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.e(TAG, "connect onError = " + errorCode);
                                // 根据errorCode 检查原因.
                            }
                        });
            }
        });

//        //检查android最新版本
        getLatestAndroidVersion();

//        fakerAction();
    }

    private void initUserToken() {
        if ("".equals(Data.getUserToken())) {
            try {
                Data.setUserToken(DBUtils.get(MainActivity.this, "userToken"));
                Data.setUserId(Integer.parseInt(DBUtils.get(MainActivity.this, "userId")));
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
        }
    }

    private void setTabSelection(View view) {
        int id = view.getId();

        int activeColorRecourse = getResources().getColor(R.color.dodgerblue);
        int inactiveColorRecourse = getResources().getColor(R.color.grey);

        initSelection(inactiveColorRecourse);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 开启一个Fragment事务

        switch (id) {
            case R.id.tab_index:
                // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                hideFragments(fragmentTransaction);

//                tabOrdersImg.setText("\uE903");
//                tabOrdersImg.setTextColor(activeColorRecourse);
                tabOrdersImg.setImageResource(R.mipmap.tab1_b);
                tabOrdersTitle.setTextColor(activeColorRecourse);
                if (mIndexFragment == null) {
                    // 如果Fragment为空，则创建一个并添加到界面上
                    mIndexFragment = new IndexFragment();
                    fragmentTransaction.add(R.id.container, mIndexFragment, TABORDERSTAG);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    fragmentTransaction.show(mIndexFragment);
                }
                break;

            case R.id.tab_doctor:
                hideFragments(fragmentTransaction);
                tabMerchandiseImg.setImageResource(R.mipmap.tab2_b);
                tabMerchandiseTitle.setTextColor(activeColorRecourse);
                if (mMeetingFragment == null) {
                    mMeetingFragment = new MeetingFragment2();
                    fragmentTransaction.add(R.id.container, mMeetingFragment, TABMERCHANDISE);
                } else {
                    fragmentTransaction.show(mMeetingFragment);
                }
                break;

            case R.id.tab_plus:
                tabPlusIv.setImageResource(R.mipmap.tab_plus_b2);
                //只有大咖认证用户才可以发起
                try {
                    if (!DBUtils.isSet(MainActivity.this, "authentication")) {
                        initPopMenu(false);
                    } else if (!DBUtils.get(MainActivity.this, "authentication").equals("C")) {
                        initPopMenu(false);
                    } else {
                        initPopMenu(true);
                    }
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }

                if (mPopMenu.isShowing()) return;
                mPopMenu.show();

                break;

            case R.id.tab_community:
                //直播
                hideFragments(fragmentTransaction);
                tabPurchaseImg.setImageResource(R.mipmap.tab3_b);
                tabPurchaseTitle.setTextColor(activeColorRecourse);
                if (mLiveIndexFragment == null) {
                    mLiveIndexFragment = new LiveIndexFragment();
                    fragmentTransaction.add(R.id.container, mLiveIndexFragment, TABPURCHASE);
                } else {
                    fragmentTransaction.show(mLiveIndexFragment);
                }
                break;

            case R.id.tab_mine:
                hideFragments(fragmentTransaction);
                tabSelfImg.setImageResource(R.mipmap.tab4_b);
                tabSelfTitle.setTextColor(activeColorRecourse);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.container, mMineFragment, TABSELFTAG);
                } else {
                    fragmentTransaction.show(mMineFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 仿微博弹出菜单
     *
     * @param isProfessor 是否是大咖
     */
    private void initPopMenu(boolean isProfessor) {
        if (isProfessor) {
            mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                    .addMenuItem(new PopMenuItem("大会签到", getResources().getDrawable(R.mipmap.tabbar_compose_idea)))
                    .addMenuItem(new PopMenuItem("交换名片", getResources().getDrawable(R.mipmap.tabbar_compose_photo)))
                    .addMenuItem(new PopMenuItem("创建直播", getResources().getDrawable(R.mipmap.tabbar_compose_headlines)))
//                    .addMenuItem(new PopMenuItem("发病例", getResources().getDrawable(R.mipmap.tabbar_compose_lbs)))
//                    .addMenuItem(new PopMenuItem("我的钱包", getResources().getDrawable(R.mipmap.tabbar_compose_review)))
//                    .addMenuItem(new PopMenuItem("发起直播", getResources().getDrawable(R.mipmap.tabbar_compose_more)))
                    .setOnItemClickListener((popMenu, position) -> PopMenuItemClick(position))
                    .build();
        } else {
            mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                    .addMenuItem(new PopMenuItem("大会签到", getResources().getDrawable(R.mipmap.tabbar_compose_idea)))
                    .addMenuItem(new PopMenuItem("交换名片", getResources().getDrawable(R.mipmap.tabbar_compose_photo)))
//                    .addMenuItem(new PopMenuItem("发帖子", getResources().getDrawable(R.mipmap.tabbar_compose_headlines)))
//                    .addMenuItem(new PopMenuItem("发病例", getResources().getDrawable(R.mipmap.tabbar_compose_lbs)))
//                    .addMenuItem(new PopMenuItem("我的钱包", getResources().getDrawable(R.mipmap.tabbar_compose_review)))
                    .setOnItemClickListener((popMenu, position) -> PopMenuItemClick(position))
                    .build();
        }
    }

    /**
     * @param position
     */
    private void PopMenuItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(MainActivity.this, PlusSignedDetailsActivity.class));
                break;
            case 1:
                String confirmNumber = "";
                final String TAG_CARD = "002";
                try {
                    if (!DBUtils.isSet(MainActivity.this, "tokenId") && !DBUtils.isSet(MainActivity.this, "openId")) {
                        Intent loginIntent = new Intent(MainActivity.this, Login_v2Activity.class);
                        int requestCode = 2;
                        startActivityForResult(loginIntent, requestCode);
                        return;
                    }
                    confirmNumber = DBUtils.get(MainActivity.this, "confirmNumber");
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(MainActivity.this, ExchangeBusinessCardActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("confirmNumber", confirmNumber);
                bundle2.putString("card_type", TAG_CARD);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case 2:
                HttpData.getInstance().HttpDataGetLiveRoom(new Observer<HttpResult3<LiveRoomDto, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3<LiveRoomDto, Object> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(MainActivity.this, data.getMsg());
                            return;
                        }
                        Intent intent = new Intent(MainActivity.this, LiveBuildRoomActivity.class);
                        intent.putExtra("times", data.getData().size() + "");    //看创建过几个直播间，如果没创建过，则第一次创建要弹出直播间协议弹窗
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    /**
     * 每次选中之前先清除上次的选中状态
     */
    public void initSelection(int inactiveResources) {
        tabOrdersImg.setImageResource(R.mipmap.tab1_g);
        tabOrdersTitle.setTextColor(inactiveResources);

        tabMerchandiseImg.setImageResource(R.mipmap.tab2_g);
        tabMerchandiseTitle.setTextColor(inactiveResources);

        tabPurchaseImg.setImageResource(R.mipmap.tab3_g);
        tabPurchaseTitle.setTextColor(inactiveResources);

        tabSelfImg.setImageResource(R.mipmap.tab4_g);
        tabSelfTitle.setTextColor(inactiveResources);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mIndexFragment != null) {
            transaction.hide(mIndexFragment);
        }
        if (mMeetingFragment != null) {
            transaction.hide(mMeetingFragment);
        }
        if (mLiveIndexFragment != null) {
            transaction.hide(mLiveIndexFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("ReturnToMiniSupplierActivity", 0);
        if (id == 1) {
            setTabSelection(tabPurchase);
        } else if (id == 2) {
            setTabSelection(tabOrders);
        }
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void getLatestAndroidVersion() {
        //初始化弹窗 布局 点击事件的id
        updataDialog = new UpdataDialog(this, R.layout.dialog_updataversion, new int[]{R.id.dialog_sure});

        oldVersion = CustomUtils.getVersion(MainActivity.this) + "";


        HttpData.getInstance().HttpDataGetAndroidVersion(new Observer<HttpResult3<Object, Version>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, Version> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MainActivity.this, "网络错误");
                    return;
                }
                newVersion = data.getEntity().getVersion();
                Log.e(getLocalClassName(), oldVersion + " " + newVersion);

                String[] yours = oldVersion.split("\\.");   //已经安装的版本
                String[] news = newVersion.split("\\.");
                Log.e(getLocalClassName(), yours[0] + " " + yours[1] + " " + yours[2]);
                Log.e(getLocalClassName(), news[0] + " " + news[1] + " " + news[2]);

                if (Integer.parseInt(yours[0]) >= Integer.parseInt(news[0])) { //&&  &&
                    Log.e(MainActivity.this.getLocalClassName(), "已经是最新版本");

                } else if (Integer.parseInt(yours[0]) < Integer.parseInt(news[0])) {
                    if (Integer.parseInt(yours[1]) >= Integer.parseInt(news[1])) {
                        Log.e(MainActivity.this.getLocalClassName(), "已经是最新版本");

                    } else if (Integer.parseInt(yours[1]) < Integer.parseInt(news[1])) {
                        if (Integer.parseInt(yours[2]) >= Integer.parseInt(news[2])) {
                            Log.e(MainActivity.this.getLocalClassName(), "已经是最新版本");
                        } else {
                            updataDialog.show();

                            tvmsg = (TextView) updataDialog.findViewById(R.id.updataversion_msg);
                            tvcode = (TextView) updataDialog.findViewById(R.id.updataversioncode);
                            updateDeletIv = (ImageView) updataDialog.findViewById(R.id.delete);
                            tvcode.setText(newVersion);
                            tvmsg.setText(data.getEntity().getLog());

                            updateDeletIv.setOnClickListener(view1 -> updataDialog.dismiss());
                            updataDialog.setOnCenterItemClickListener((dialog, view12) -> {
                                switch (view12.getId()) {
                                    case R.id.dialog_sure:
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(data.getEntity().getUrl());
                                        intent.setData(content_url);
                                        startActivity(intent);
                                        break;
                                }
                                updataDialog.dismiss();
                            });
                        }
                    }
                }
            }
        });
    }


    public static void trunMeetingView() {
        tabMerchandise.performClick();
    }

    public static void trunLiveView() {
        tabPurchase.performClick();
    }

    @Override
    protected void onStart() {
        JAnalyticsInterface.onPageStart(this, "主页");//this.getClass().getCanonicalName()
        super.onStart();
    }

    @Override
    protected void onStop() {
        JAnalyticsInterface.onPageEnd(this, "主页");
        super.onStop();
    }
}
