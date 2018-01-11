package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.WaitMeetingAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import info.hoang8f.android.segmented.SegmentedGroup;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 11/01/2018 11:29 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class MyMeetingActivity extends AppCompatActivity implements  SegmentedGroup.OnCheckedChangeListener{
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meeting_2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyMeetingActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyMeetingActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new WaitMeetingAdapter(R.layout.item_meeting, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyPayLiveService(1);
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> {
                finish();

        });
    }

    private void getMyPayLiveService(int type) {
        HttpData.getInstance().HttpDataGetMyEvents(new Observer<HttpResult3<VAppMyEvents, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MyMeetingActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VAppMyEvents, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyMeetingActivity.this, data.getMsg());
                    return;
                }

                mAdapter.setNewData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyMeetingActivity.this, MeetingDetailActivity.class);
                    i.putExtra("eventId", data.getData().get(position).getEventId());
                    startActivity(i);
                });

            }
        }, type);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.button21:
                getMyPayLiveService(0);
                return;
            case R.id.button22:
                getMyPayLiveService(1);
                return;
        }
    }
}


