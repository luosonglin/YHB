package com.medmeeting.m.zhiyi.UI.VideoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 02/11/2017 4:12 PM
 * @describe 视频的种类详情页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoInTagActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleTv;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_in_tag);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getIntent().getStringExtra("title"));
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(VideoInTagActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(VideoInTagActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyOrderAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getVideoInTagService(getIntent().getIntExtra("labelId", 1));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getVideoInTagService(Integer labelId) {
        VideoListSearchEntity searchEntity = new VideoListSearchEntity();
        searchEntity.setPageNum(1);
        searchEntity.setPageSize(100);
        searchEntity.setKeyword(null);
        searchEntity.setLabelId(labelId);
        searchEntity.setRoomId(null);
        searchEntity.setProgramId(null);
        searchEntity.setRoomNumber(null);
        searchEntity.setVideoUserId(null);
        HttpData.getInstance().HttpDataGetVideos(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(VideoInTagActivity.this, data.getMsg());
                    return;
                }
                mAdapter.addData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(VideoInTagActivity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    i.putExtra("title", data.getData().get(position).getTitle());
                    i.putExtra("photo", data.getData().get(position).getCoverPhoto());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }
}
