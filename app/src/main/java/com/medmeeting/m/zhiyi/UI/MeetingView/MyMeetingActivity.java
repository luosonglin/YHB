package com.medmeeting.m.zhiyi.UI.MeetingView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MVP.Presenter.FinishedEventListPresent;
import com.medmeeting.m.zhiyi.MVP.Presenter.MyMeetingListPresent;
import com.medmeeting.m.zhiyi.MVP.View.FinishedEventListView;
import com.medmeeting.m.zhiyi.MVP.View.MeetingListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.FinishedEventAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MeetingAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MyMeetingActivity extends AppCompatActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener,
            MeetingListView, FinishedEventListView, SegmentedGroup.OnCheckedChangeListener{

    private ImageView backIv;
    private RadioButton liveButton21, liveButton22;
    private View searchLiveBtn;
    private int PageIndex = 1;

    RecyclerView mRecyclerView;
    ProgressActivity progress;
    private BaseQuickAdapter mQuickAdapter;
    private SpringView springView;
    private MyMeetingListPresent present;

    RecyclerView mRecyclerView2;
    private BaseQuickAdapter mQuickAdapter2;
    private FinishedEventListPresent present2;

    private static final String TAG = MyMeetingActivity.class.getSimpleName();
    private Map<String, Object> options = new HashMap<>();
    private Map<String, Object> options2 = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meeting);

        initView(getIntent().getExtras().getString("userId"));
    }

    private void initView(String userId) {
        backIv = (ImageView)findViewById(R.id.back);
        backIv.setOnClickListener(view -> finish());

        SegmentedGroup segmented4 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented4.setTintColor(0xFF1b7ce8);
        segmented4.setOnCheckedChangeListener(this);

        liveButton21 = (RadioButton) findViewById(R.id.button21);
        liveButton22 = (RadioButton) findViewById(R.id.button22);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        springView = (SpringView) findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(MyMeetingActivity.this));
        progress = (ProgressActivity) findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyMeetingActivity.this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
        mQuickAdapter = new MeetingAdapter(R.layout.item_meeting, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
        present = new MyMeetingListPresent(this);
        //请求网络数据
        options.put("userId", userId);
        options.put("pageNum", PageIndex);
        options.put("pageSize", "100");
        present.LoadData(false, options);

        mRecyclerView2 = (RecyclerView) findViewById(R.id.rv_list2);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(MyMeetingActivity.this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView2.setHasFixedSize(true);
        //设置适配器
        mQuickAdapter2 = new FinishedEventAdapter(R.layout.item_finished_event, null);
        //设置加载动画
        mQuickAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter2.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView2.setAdapter(mQuickAdapter2);
        present2 = new FinishedEventListPresent(this);
        //请求网络数据
        options2.put("userId", userId);
        options2.put("pageNum", PageIndex);
        options2.put("pageSize", "100");
        present2.LoadData(false, options2);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button21:
                present.LoadData(false, options);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView2.setVisibility(View.GONE);
                return;

            case R.id.button22:
                present2.LoadData(false, options2);
                mRecyclerView.setVisibility(View.GONE);
                mRecyclerView2.setVisibility(View.VISIBLE);
                return;
        }
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        options.put("pageNum", PageIndex);
        present.LoadData(true, options);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex = 1;
//        present.LoadData("1",PageIndex,false);
        options.put("pageNum", PageIndex);
        present.LoadData(false, options);
    }

    //上啦加载  mRecyclerView内部集成的自动加载  上啦加载用不上   在其他View使用
    @Override
    public void onLoadmore() {

    }

    /*
    * MVP模式的相关状态
    *
    */
    @Override
    public void showProgress() {
        progress.showLoading();
    }

    @Override
    public void hideProgress() {
        progress.showContent();
    }

    @Override
    public void newDatas(List<MeetingDto> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10, true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<MeetingDto> addList) {
        //新增自动加载的的数据
        mQuickAdapter.notifyDataChangedAfterLoadMore(addList, true);
    }

    @Override
    public void showLoadFailMsg() {
        //设置加载错误页显示
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIndex = 1;
                options.put("pageNum", PageIndex);
                present.LoadData(false, options);
            }
        });
    }

    @Override
    public void showLoadCompleteAllData() {
        //所有数据加载完成后显示
        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
        View view = this.getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.addFooterView(view);
    }

    @Override
    public void showNoData() {
        springView.onFinishFreshAndLoad();
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }

    /**
     *
     */
    @Override
    public void showProgress2() {
        progress.showLoading();
    }

    @Override
    public void hideProgress2() {
        progress.showContent();
    }

    @Override
    public void newDatas2(List<FollowFinishedEvent> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter2.setNewData(newsList);//新增数据
        mQuickAdapter2.openLoadMore(10, true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas2(List<FollowFinishedEvent> addList) {
        //新增自动加载的的数据
        mQuickAdapter2.notifyDataChangedAfterLoadMore(addList, true);
    }

    @Override
    public void showLoadFailMsg2() {
        //设置加载错误页显示
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIndex = 1;
                options2.put("pageNum", PageIndex);
                present2.LoadData(false, options2);
            }
        });
    }

    @Override
    public void showLoadCompleteAllData2() {
        //所有数据加载完成后显示
        mQuickAdapter2.notifyDataChangedAfterLoadMore(false);
        View view = this.getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView2.getParent(), false);
        mQuickAdapter2.addFooterView(view);
    }

    @Override
    public void showNoData2() {
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }


}
