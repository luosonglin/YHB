package com.medmeeting.m.zhiyi.UI.MineView;

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
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 1:13 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private View mHeaderView;
    private TextView mTypeView;
    private TextView mMoreView;

    private RecyclerView mRecyclerView2;
    private BaseQuickAdapter mAdapter2;
    private View mHeaderView2;
    private TextView mTypeView2;
    private TextView mMoreView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyOrderActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyPayLiveAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyPayLiveService();
        //头view
        mHeaderView = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.item_collect_video_header, null);
        mTypeView = (TextView) mHeaderView.findViewById(R.id.type);
        mTypeView.setText("已购直播");
        mMoreView = (TextView) mHeaderView.findViewById(R.id.more);
        mMoreView.setOnClickListener(view -> startActivity(new Intent(MyOrderActivity.this, MyOrderLiveActivity.class)));


        mRecyclerView2 = (RecyclerView) findViewById(R.id.rv_list2);
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(MyOrderActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
        mRecyclerView2.setHasFixedSize(true);
        mAdapter2 = new VideoAdapter(R.layout.item_video_others, null);
        mAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter2.openLoadMore(8, true);
        mRecyclerView2.setAdapter(mAdapter2);
        getMyPayVideoService();
        //头view
        mHeaderView2 = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.item_collect_video_header, null);
        mTypeView2 = (TextView) mHeaderView2.findViewById(R.id.type);
        mTypeView2.setText("已购录像");
        mMoreView2 = (TextView) mHeaderView2.findViewById(R.id.more);
        mMoreView2.setOnClickListener(view -> startActivity(new Intent(MyOrderActivity.this, MyOrderVideoActivity.class)));
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
                ToastUtils.show(MyOrderActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyOrderActivity.this, data.getMsg());
                    return;
                }
                mAdapter.addHeaderView(mHeaderView);

                List<LiveDto> mLives = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mLives.add(0, data.getData().get(0));
                    mLives.add(1, data.getData().get(1));
                    mLives.add(2, data.getData().get(2));
                } else {
                    mLives.addAll(data.getData());
                }
                mAdapter.addData(mLives);
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = new Intent(MyOrderActivity.this, LiveProgramDetailActivity.class);
                    intent.putExtra("authorName", data.getData().get(position).getAuthorName());
                    intent.putExtra("userPic", data.getData().get(position).getUserPic());
                    intent.putExtra("programId", data.getData().get(position).getId());
                    intent.putExtra("roomId", data.getData().get(position).getRoomId());
                    intent.putExtra("coverPhoto", data.getData().get(position).getCoverPhoto());
                    intent.putExtra("title", data.getData().get(position).getTitle());
                    startActivity(intent);
                });
            }
        });
    }

    private void getMyPayVideoService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        searchEntity.setPageNum(1);
        searchEntity.setPageSize(3);
        HttpData.getInstance().HttpDataGetMyPayVideo(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MyOrderActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyOrderActivity.this, data.getMsg());
                    return;
                }
                mAdapter2.addHeaderView(mHeaderView2);

                List<VideoListEntity> mVideos = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mVideos.add(0, data.getData().get(0));
                    mVideos.add(1, data.getData().get(1));
                    mVideos.add(2, data.getData().get(2));
                } else {
                    mVideos.addAll(data.getData());
                }
                mAdapter2.addData(mVideos);
                mAdapter2.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyOrderActivity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    i.putExtra("title", data.getData().get(position).getTitle());
                    i.putExtra("photo", data.getData().get(position).getCoverPhoto());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }
}

