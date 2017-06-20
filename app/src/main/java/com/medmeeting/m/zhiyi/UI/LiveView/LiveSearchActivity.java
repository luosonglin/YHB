package com.medmeeting.m.zhiyi.UI.LiveView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.MVP.Presenter.BannerListPresent;
import com.medmeeting.m.zhiyi.MVP.View.BannerListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BannersAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.SmartTab.SmartTabLayout;
import com.xiaochao.lcrapiddeveloplibrary.SmartTab.UtilsV4.v4.FragmentPagerItem;
import com.xiaochao.lcrapiddeveloplibrary.SmartTab.UtilsV4.v4.FragmentPagerItemAdapter;
import com.xiaochao.lcrapiddeveloplibrary.SmartTab.UtilsV4.v4.FragmentPagerItems;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveSearchActivity extends AppCompatActivity implements SpringView.OnFreshListener, BannerListView {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.search_edit)
    EditText searchEt;

    @Bind(R.id.search_tag_llyt)
    LinearLayout searchTagLlyt;
    @Bind(R.id.search_tag_text)
    TextView searchTagTv;
    @Bind(R.id.search_tag_delete)
    ImageView searchTagIv;


    private static final String TAG = LiveSearchActivity.class.getSimpleName();

    private String classify = "";
    private List<String> classifys = new ArrayList<>();
    private String classifyId = "";
    private List<String> classifyIds = new ArrayList<>();
    private String searchKey;
    private String searchType;
    private static final String LABELID = "labelId";
    private static final String KEYWORD = "keyword";

    private ViewGroup tab;
    private ViewPager viewpager;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex=1;
    private SpringView springView;
    private BannerListPresent present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        ButterKnife.bind(this);

        initToolbar("搜索直播");

        initTagsView();

    }

    private void initToolbar(String title) {
        toolbarTitleTv.setText(title);
        toolbarTitleTv.setTextColor(Color.BLACK);
        toolbarTitleTv.setFocusable(true);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initTagsView() {
        tab = (ViewGroup) findViewById(R.id.tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        mRecyclerView = (RecyclerView) findViewById(R.id.tags_rv_list);
        springView = (SpringView) findViewById(R.id.tags_springview);

        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置适配器
        mQuickAdapter = new BannersAdapter(R.layout.list_view_item_layout, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6,true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
        present = new BannerListPresent(this);
        //请求网络数据
        present.LoadData(false);


        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_top_layout, tab, false));
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        FragmentPagerItems pages = new FragmentPagerItems(this);

        pages.add(FragmentPagerItem.of("公开直播", LiveSearchPublicFragment.class));
        pages.add(FragmentPagerItem.of("私密直播", LiveSearchPublicFragment.class));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewpager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewpager);

        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                switch (position) {
                    case 0:
                        springView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        springView.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void newDatas(List<BannerDto.BannersBean> newsList) {

    }

    @Override
    public void addDatas(List<BannerDto.BannersBean> addList) {

    }

    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void showLoadCompleteAllData() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadmore() {

    }
}
