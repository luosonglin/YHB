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
import com.medmeeting.m.zhiyi.UI.Adapter.VideoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 3:04 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderVideoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_video);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyOrderVideoActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyOrderVideoActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_CUSTOM);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyPayVideoService();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getMyPayVideoService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        searchEntity.setPageNum(1);
        searchEntity.setPageSize(100);
        HttpData.getInstance().HttpDataGetMyPayVideo(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MyOrderVideoActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyOrderVideoActivity.this, data.getMsg());
                    return;
                }
                mAdapter.addData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyOrderVideoActivity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    i.putExtra("title", data.getData().get(position).getTitle());
                    i.putExtra("photo", data.getData().get(position).getCoverPhoto());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }
}