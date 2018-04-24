package com.medmeeting.m.zhiyi.UI.MineView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 11/01/2018 11:29 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class MyMeetingActivity extends AppCompatActivity implements SegmentedGroup.OnCheckedChangeListener {
    private ImageView back;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meeting_2);

        back = (ImageView) findViewById(R.id.back);
        initToolbar();

        SegmentedGroup segmented4 = (SegmentedGroup)findViewById(R.id.segmented2);
        segmented4.setTintColor(0xFF1b7ce8);
        segmented4.setOnCheckedChangeListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setUpViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        back.setOnClickListener(view -> finish());
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.button21:
                tabLayout.getTabAt(0).select();
                return;
            case R.id.button22:
                tabLayout.getTabAt(1).select();
                return;
        }
    }

    private void setUpViewPager(ViewPager viewPager) {
        //如果是在fragment中使用viewpager, 记得要用getChildFragmentManager, 否则你会发现fragment异常的生命周期.
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(MyMeetingActivity.this.getSupportFragmentManager());//.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(MyMeetingWaitFragment.newInstance(), "待参会");
        mIndexChildAdapter.addFragment(MyMeetingFinishedFragment.newInstance(), "已结束");

        viewPager.setOffscreenPageLimit(2);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }
}
