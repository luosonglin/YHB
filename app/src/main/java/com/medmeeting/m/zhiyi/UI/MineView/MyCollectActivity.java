package com.medmeeting.m.zhiyi.UI.MineView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.VideoView.LiveIndexTabFragment2;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/10/2017 3:33 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyCollectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//            params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight();//800

        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void setUpViewPager(ViewPager viewPager) {

        //如果是在fragment中使用viewpager, 记得要用getChildFragmentManager, 否则你会发现fragment异常的生命周期.
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(MyCollectActivity.this.getSupportFragmentManager());//.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(MyCollectLiveFragment.newInstance(), "直播");
        mIndexChildAdapter.addFragment(MyCollectVideoFragment.newInstance(), "视频");
        mIndexChildAdapter.addFragment(MyCollectMeetingFragment.newInstance(), "会议");
        mIndexChildAdapter.addFragment(LiveIndexTabFragment2.newInstance(), "新闻");

        viewPager.setOffscreenPageLimit(2);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }
}
