package com.medmeeting.m.zhiyi.UI.MineView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 1:13 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        initTagsView();

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initTagsView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = this.getWindowManager().getDefaultDisplay().getHeight();
        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();
    }

    private void setUpViewPager(ViewPager viewPager) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(MyOrderActivity.this.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(MyOrderLiveFragment.newInstance(), "直播回播");
        mIndexChildAdapter.addFragment(MyOrderMeetingFragment.newInstance(), "会议订单");

        viewPager.setOffscreenPageLimit(2);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }


}

