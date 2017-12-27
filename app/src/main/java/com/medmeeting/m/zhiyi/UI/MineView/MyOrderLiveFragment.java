package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/12/2017 4:04 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderLiveFragment extends Fragment {


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

    public static MyOrderLiveFragment newInstance() {
        MyOrderLiveFragment fragment = new MyOrderLiveFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order_live, container, false);


        //直播订单
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyPayLiveAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getMyPayLiveService();
        //头view
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mTypeView = (TextView) mHeaderView.findViewById(R.id.type);
        mTypeView.setText("直播订单");
        mMoreView = (TextView) mHeaderView.findViewById(R.id.more);


        //视频订单
        mRecyclerView2 = (RecyclerView) view.findViewById(R.id.rv_list2);
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView2.setHasFixedSize(true);
        mAdapter2 = new MyOrderAdapter(R.layout.item_video_others, null);
        mAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter2.openLoadMore(8, true);
        mRecyclerView2.setAdapter(mAdapter2);
        getMyPayVideoService();
        //头view
        mHeaderView2 = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mTypeView2 = (TextView) mHeaderView2.findViewById(R.id.type);
        mTypeView2.setText("视频订单");
        mMoreView2 = (TextView) mHeaderView2.findViewById(R.id.more);

        return view;
    }


    private void getMyPayLiveService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        searchEntity.setPageNum(1);
        searchEntity.setPageSize(100);
        HttpData.getInstance().HttpDataGetMyPayLive(new Observer<HttpResult3<LiveDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                mAdapter.addHeaderView(mHeaderView);

                List<LiveDto> mLives = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mLives.add(0, data.getData().get(0));
                    mLives.add(1, data.getData().get(1));
                    mLives.add(2, data.getData().get(2));
                    mMoreView.setVisibility(View.VISIBLE);
                    mMoreView.setOnClickListener(view -> startActivity(new Intent(getActivity(), MyOrderLiveActivity.class)));
                } else {
                    mLives.addAll(data.getData());
                    mMoreView.setVisibility(View.GONE);
                }
                mAdapter.addData(mLives);
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    startActivity(new Intent(getActivity(), LiveProgramDetailActivity2.class)
                            .putExtra("programId", data.getData().get(position).getId()));
                });
            }
        }, searchEntity);

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
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                mAdapter2.addHeaderView(mHeaderView2);

                List<VideoListEntity> mVideos = new ArrayList<>();
                if (data.getData().size() > 3) {
                    mVideos.add(0, data.getData().get(0));
                    mVideos.add(1, data.getData().get(1));
                    mVideos.add(2, data.getData().get(2));
                    mMoreView2.setVisibility(View.VISIBLE);
                    mMoreView2.setOnClickListener(view -> startActivity(new Intent(getActivity(), MyOrderVideoActivity.class)));
                } else {
                    mVideos.addAll(data.getData());
                    mMoreView2.setVisibility(View.GONE);
                }
                mAdapter2.addData(mVideos);
                mAdapter2.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }

}
