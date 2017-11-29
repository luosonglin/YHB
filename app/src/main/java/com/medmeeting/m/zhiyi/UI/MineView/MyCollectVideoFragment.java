package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.List;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 11:24 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyCollectVideoFragment extends Fragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, LiveListView {

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;
    private SpringView springView;

    public static MyCollectVideoFragment newInstance() {
        MyCollectVideoFragment fragment = new MyCollectVideoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        springView = (SpringView) view.findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置适配器
        mQuickAdapter = new MyOrderAdapter(R.layout.item_video_others, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);

        getMyCollectVideoService();

        return view;
    }

    private void getMyCollectVideoService() {
        BasePageSearchEntity searchEntity = new BasePageSearchEntity();
        HttpData.getInstance().HttpDataGetCollect(new Observer<HttpResult3<VideoListEntity, Object>>() {
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
                mQuickAdapter.setNewData(data.getData());
                mQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void newDatas(List<LiveDto> newsList) {

    }

    @Override
    public void addDatas(List<LiveDto> addList) {

    }

    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void showLoadCompleteAllData() {
        //所有数据加载完成后显示
        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.addFooterView(view);
    }

    @Override
    public void showNoData() {
        springView.onFinishFreshAndLoad();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh() {
        springView.onFinishFreshAndLoad();
        getMyCollectVideoService();
    }

    @Override
    public void onLoadmore() {

    }
}