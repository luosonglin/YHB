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
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity;
import com.medmeeting.m.zhiyi.UI.MineView.MyCollectLiveActivity;
import com.medmeeting.m.zhiyi.UI.MineView.MyCollectVideoActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

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
        getMyCollectVideoService();
        //头view
        mHeaderView = LayoutInflater.from(MyCollectActivity.this).inflate(R.layout.item_collect_video_header, null);
        mTypeView = (TextView) mHeaderView.findViewById(R.id.type);
        mTypeView.setText("视频收藏");
        mMoreView = (TextView) mHeaderView.findViewById(R.id.more);


        mRecyclerView2 = (RecyclerView) findViewById(R.id.rv_list2);
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(MyCollectActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(MyCollectActivity.this));
        mRecyclerView2.setHasFixedSize(true);
        mAdapter2 = new MyPayLiveAdapter(R.layout.item_video_others, null);
        mAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter2.openLoadMore(8, true);
        mRecyclerView2.setAdapter(mAdapter2);
        getMyCollectLiveService();
        //头view
        mHeaderView2 = LayoutInflater.from(MyCollectActivity.this).inflate(R.layout.item_collect_video_header, null);
        mTypeView2 = (TextView) mHeaderView2.findViewById(R.id.type);
        mTypeView2.setText("直播收藏");
        mMoreView2 = (TextView) mHeaderView2.findViewById(R.id.more);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getMyCollectVideoService() {
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

                List<VideoListEntity> mLives = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mLives.add(0, data.getData().get(0));
                    mLives.add(1, data.getData().get(1));
                    mLives.add(2, data.getData().get(2));
                    mMoreView.setVisibility(View.VISIBLE);
                    mMoreView.setOnClickListener(view -> startActivity(new Intent(MyCollectActivity.this, MyCollectVideoActivity.class)));
                } else {
                    mLives.addAll(data.getData());
                    mMoreView.setVisibility(View.GONE);
                }
                mAdapter.addData(mLives);
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyCollectActivity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }

    private void getMyCollectLiveService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        HttpData.getInstance().HttpDataGetLiveCollect(new Observer<HttpResult3<LiveDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyCollectActivity.this, data.getMsg());
                    return;
                }
                mAdapter2.addHeaderView(mHeaderView2);

                List<LiveDto> mLives = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mLives.add(0, data.getData().get(0));
                    mLives.add(1, data.getData().get(1));
                    mLives.add(2, data.getData().get(2));
                    mMoreView2.setVisibility(View.VISIBLE);
                    mMoreView2.setOnClickListener(view -> startActivity(new Intent(MyCollectActivity.this, MyCollectLiveActivity.class)));
                } else {
                    mLives.addAll(data.getData());
                    mMoreView2.setVisibility(View.GONE);
                }
                mAdapter2.addData(mLives);
                mAdapter2.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(MyCollectActivity.this, LiveProgramDetailActivity.class);
                    i.putExtra("programId", data.getData().get(position).getId());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }
}
