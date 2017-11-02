package com.medmeeting.m.zhiyi.UI.VideoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/10/2017 3:33 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyCollectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private View mHeaderView;
    private TextView mMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyCollectActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyCollectActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyOrderAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyCollectService();

        //头view
        mHeaderView = LayoutInflater.from(MyCollectActivity.this).inflate(R.layout.item_collect_video_header, null);
        mMoreView = (TextView) mHeaderView.findViewById(R.id.more);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getMyCollectService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        HttpData.getInstance().HttpDataGetCollect(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyCollectActivity.this, data.getMsg());
                    return;
                }
                mAdapter.addHeaderView(mHeaderView);
                mAdapter.addData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyCollectActivity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    i.putExtra("title", data.getData().get(position).getTitle());
                    i.putExtra("photo", data.getData().get(position).getCoverPhoto());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }
}
