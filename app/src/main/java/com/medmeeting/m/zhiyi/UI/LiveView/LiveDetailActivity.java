package com.medmeeting.m.zhiyi.UI.LiveView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.medmeeting.m.zhiyi.R;

import java.util.ArrayList;
import java.util.List;

public class LiveDetailActivity extends AppCompatActivity {
    private static final String TAG = LiveDetailActivity.class.getSimpleName();
    private Toolbar toolbar;
//    private ViewGroup tab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context = this;
    private ScrollView scrollView;
    private ImageView imageView;

    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        toolBar();
        initView();
        initTagsView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitle("");//标题
//        toolbar.setSubtitle(R.string.app_name);//二级标题
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置logo
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
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


    private void initTagsView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = this.getWindowManager().getDefaultDisplay().getHeight() - 140 * 6;//800
        Log.e(TAG, this.getWindowManager().getDefaultDisplay().getHeight() + " " + params.height + " ");

        viewPager.setLayoutParams(params);
        Log.e(TAG, viewPager.getHeight() + " " + viewPager.getLayoutParams().height);

        setUpViewPager(viewPager, "0");
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager, String mParam1) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(LiveDetailActivity.this.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(LiveDetailLiveFragment.newInstance(mParam1), "直播");
        mIndexChildAdapter.addFragment(LiveDetailVideoFragment.newInstance(mParam1), "视频");
        mIndexChildAdapter.addFragment(LiveDetailSummaryFragment.newInstance(mParam1), "简介");

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
}

