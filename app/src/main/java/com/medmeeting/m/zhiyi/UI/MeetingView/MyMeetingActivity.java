package com.medmeeting.m.zhiyi.UI.MeetingView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.medmeeting.m.zhiyi.MVP.Presenter.MyMeetingListPresent;
import com.medmeeting.m.zhiyi.MVP.View.MeetingListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.WaitMeetingAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MyMeetingActivity extends AppCompatActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, MeetingListView, SegmentedGroup.OnCheckedChangeListener{

    private ImageView backIv;
    private RadioButton liveButton21, liveButton22;

    RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;
    private MyMeetingListPresent present;

    private Integer type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meeting);

        initView();
    }

    private void initView() {
        backIv = (ImageView)findViewById(R.id.back);
        backIv.setOnClickListener(view -> finish());

        SegmentedGroup segmented4 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented4.setTintColor(0xFF1b7ce8);
        segmented4.setOnCheckedChangeListener(this);

        liveButton21 = (RadioButton) findViewById(R.id.button21);
        liveButton22 = (RadioButton) findViewById(R.id.button22);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyMeetingActivity.this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置适配器
        mQuickAdapter = new WaitMeetingAdapter(R.layout.item_meeting, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
        present = new MyMeetingListPresent(this);
        //请求网络数据
        present.LoadData(false, type);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button21:
                type = 0;
                Log.e(getLocalClassName(), type+"");
                return;
            case R.id.button22:
                type = 1;
                Log.e(getLocalClassName(), type+"");
                return;
        }
        Log.e(getLocalClassName(), type+"");
        present.LoadData(false, type);
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        present.LoadData(true, type);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void newDatas(List<VAppMyEvents> newsList) {

    }

    @Override
    public void addDatas(List<VAppMyEvents> addList) {

    }

    @Override
    public void showLoadFailMsg() {

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

    }
}
