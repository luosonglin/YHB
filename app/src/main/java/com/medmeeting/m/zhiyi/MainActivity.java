package com.medmeeting.m.zhiyi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.MVP.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.UI.Entity.BookListDto;
import com.medmeeting.m.zhiyi.UI.ListvView.ListvViewActivity;
import com.medmeeting.m.zhiyi.UI.OtherVIew.IndexFragment;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Widget.popmenu.PopMenu;
import com.medmeeting.m.zhiyi.Widget.popmenu.PopMenuItem;
import com.medmeeting.m.zhiyi.Widget.popmenu.PopMenuItemListener;
import com.snappydb.SnappydbException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements IndexFragment.OnFragmentInteractionListener{

    private static final String TABORDERSTAG = "TABORDERSTAG";
    private static final String TABMERCHANDISE = "TABMERCHANDISE";
    private static final String TABPURCHASE = "TABPURCHASE";
    private static final String TABSELFTAG = "TABSELFTAG";

    private PopMenu mPopMenu;

    //首页
    @Bind(R.id.tab_index)
    View tabOrders;
    @Bind(R.id.tab_index_img)
    ImageView tabOrdersImg;
    @Bind(R.id.tab_index_title)
    TextView tabOrdersTitle;

    //知医
    @Bind(R.id.tab_doctor)
    View tabMerchandise;
    @Bind(R.id.tab_doctor_img)
    ImageView tabMerchandiseImg;
    @Bind(R.id.tab_doctor_title)
    TextView tabMerchandiseTitle;

    //加号
    @Bind(R.id.tab_plus)
    View tabPlus;
    @Bind(R.id.tab_plus_img)
    ImageView tabPlusIv;

    //圈子
    @Bind(R.id.tab_community)
    View tabPurchase;
    @Bind(R.id.tab_community_img)
    ImageView tabPurchaseImg;
    @Bind(R.id.tab_community_title)
    TextView tabPurchaseTitle;

    //个人
    @Bind(R.id.tab_mine)
    View tabSelf;
    @Bind(R.id.tab_mine_img)
    ImageView tabSelfImg;
    @Bind(R.id.tab_mine_title)
    TextView tabSelfTitle;

    private FragmentManager fragmentManager;
    private IndexFragment mIndexFragment;
    private IndexFragment mMeetingFragment;
    //    private CommunityFragment mCommunityFragment;
    private IndexFragment mCommunityFragment;
    private IndexFragment mMineFragment;

    @OnClick(R.id.tab_index) void orderTab() {
        setTabSelection(tabOrders);
    }

    @OnClick(R.id.tab_doctor) void merchandiseTab() {
        setTabSelection(tabMerchandise);
    }

    @OnClick(R.id.tab_plus) void showWeiboTab() {
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

        ButterKnife.bind(this);

//        initUmengConfig();

        //默认第一项选中
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mIndexFragment = (IndexFragment) fragmentManager.findFragmentByTag(TABORDERSTAG);
            mMeetingFragment = (IndexFragment) fragmentManager.findFragmentByTag(TABMERCHANDISE);
            mCommunityFragment = (IndexFragment) fragmentManager.findFragmentByTag(TABPURCHASE);
            mMineFragment = (IndexFragment) fragmentManager.findFragmentByTag(TABSELFTAG);
        }
        setTabSelection(tabOrders);
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
                    mMeetingFragment = new IndexFragment();
                    fragmentTransaction.add(R.id.container, mMeetingFragment, TABMERCHANDISE);
                } else {
                    fragmentTransaction.show(mMeetingFragment);
                }
                break;

            case R.id.tab_plus:
                tabPlusIv.setImageResource(R.mipmap.tab_plus_b);
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
                if (mCommunityFragment == null) {
                    mCommunityFragment = new IndexFragment();
                    fragmentTransaction.add(R.id.container, mCommunityFragment, TABPURCHASE);
                } else {
                    fragmentTransaction.show(mCommunityFragment);
                }
                break;

            case R.id.tab_mine:
                hideFragments(fragmentTransaction);
                tabSelfImg.setImageResource(R.mipmap.tab4_b);
                tabSelfTitle.setTextColor(activeColorRecourse);
                if (mMineFragment == null) {
                    mMineFragment = new IndexFragment();
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
     * @param isProfessor   是否是大咖
     */
    private void initPopMenu(boolean isProfessor){
        if (isProfessor) {
            mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                    .addMenuItem(new PopMenuItem("大会签到", getResources().getDrawable(R.mipmap.tabbar_compose_idea)))
                    .addMenuItem(new PopMenuItem("交换名片", getResources().getDrawable(R.mipmap.tabbar_compose_photo)))
                    .addMenuItem(new PopMenuItem("发帖子", getResources().getDrawable(R.mipmap.tabbar_compose_headlines)))
                    .addMenuItem(new PopMenuItem("发病例", getResources().getDrawable(R.mipmap.tabbar_compose_lbs)))
                    .addMenuItem(new PopMenuItem("我的钱包", getResources().getDrawable(R.mipmap.tabbar_compose_review)))
                    .addMenuItem(new PopMenuItem("发起直播", getResources().getDrawable(R.mipmap.tabbar_compose_more)))
                    .setOnItemClickListener(new PopMenuItemListener() {
                        @Override
                        public void onItemClick(PopMenu popMenu, int position) {
                            PopMenuItemClick(position);
                        }
                    })
                    .build();
        } else {
            mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                    .addMenuItem(new PopMenuItem("大会签到", getResources().getDrawable(R.mipmap.tabbar_compose_idea)))
                    .addMenuItem(new PopMenuItem("交换名片", getResources().getDrawable(R.mipmap.tabbar_compose_photo)))
                    .addMenuItem(new PopMenuItem("发帖子", getResources().getDrawable(R.mipmap.tabbar_compose_headlines)))
                    .addMenuItem(new PopMenuItem("发病例", getResources().getDrawable(R.mipmap.tabbar_compose_lbs)))
                    .addMenuItem(new PopMenuItem("我的钱包", getResources().getDrawable(R.mipmap.tabbar_compose_review)))
                    .setOnItemClickListener(new PopMenuItemListener() {
                        @Override
                        public void onItemClick(PopMenu popMenu, int position) {
                            PopMenuItemClick(position);
                        }
                    })
                    .build();
        }
    }

    /**
     *
     * @param position
     */
    private void PopMenuItemClick(int position) {
        Intent intent;
        switch (position) {
            case 0:
//                startActivity(new Intent(MainActivity.this, PlusSignedDetailsActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                String confirmNumber = "";
//                final String TAG_CARD = "002";
//                try {
//                    if (!DBUtils.isSet(MainActivity.this, "tokenId") && !DBUtils.isSet(MainActivity.this, "openId")) {
//                        Intent loginIntent = new Intent(MainActivity.this, SignUpByPhoneCodeActivity.class);
//                        int requestCode = 2;
//                        startActivityForResult(loginIntent, requestCode);
//                        return;
//                    }
//                    confirmNumber = DBUtils.get(MainActivity.this, "confirmNumber");
//                } catch (SnappydbException e) {
//                    e.printStackTrace();
//                }
//                Intent intent2 = new Intent(MainActivity.this, MyBusinessCardActivity.class);
//                Bundle bundle2 = new Bundle();
//                bundle2.putString("confirmNumber", confirmNumber);
//                bundle2.putString("card_type", TAG_CARD);
//                intent2.putExtras(bundle2);
//                startActivity(intent2);
                break;
            case 2:
                intent = new Intent(MainActivity.this, ListvViewActivity.class);
                startActivity(intent);
                break;
            case 3:
                // 唤出RecoveryActivity
                BookListDto bookListDto=null;
                Toast.makeText(MainActivity.this, bookListDto.getBookName(), Toast.LENGTH_SHORT).show();
                break;
            case 4:
//                startActivity(new Intent(MainActivity.this, WalletActivity.class));
                break;
            case 5:
//                startActivity(new Intent(MainActivity.this, LiveBuildActivity.class));
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
        if (mCommunityFragment != null) {
            transaction.hide(mCommunityFragment);
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
}

