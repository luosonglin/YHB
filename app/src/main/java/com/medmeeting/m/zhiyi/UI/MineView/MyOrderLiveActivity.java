package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 2:54 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderLiveActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_live);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyOrderLiveActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyOrderLiveActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyPayLiveAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyPayLiveService();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getMyPayLiveService() {
        HttpData.getInstance().HttpDataGetMyPayLives(new Observer<HttpResult3<LiveDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MyOrderLiveActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyOrderLiveActivity.this, data.getMsg());
                    return;
                }

                mAdapter.addData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    startActivity(new Intent(MyOrderLiveActivity.this, LiveProgramDetailActivity2.class)
                            .putExtra("programId", data.getData().get(position).getId()));
                });
            }
        });
    }
}

