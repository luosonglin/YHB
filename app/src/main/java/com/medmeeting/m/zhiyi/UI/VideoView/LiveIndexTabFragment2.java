package com.medmeeting.m.zhiyi.UI.VideoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoTagAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;

public class LiveIndexTabFragment2 extends Fragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, LiveListView {

    private RecyclerView mRecyclerView;
    //    private ProgressActivity progress;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private LiveSearchDto2 liveSearchDto2 = new LiveSearchDto2();
    private static final String TAG = LiveIndexTabFragment2.class.getSimpleName();

    private View mHeaderView;
    private RecyclerView mTagsRecyclerView;
    private BaseQuickAdapter mTagsAdapter;
    private TextView mTagsTurnUpTv;


    private int page = 1;

    public static LiveIndexTabFragment2 newInstance() {
        LiveIndexTabFragment2 fragment = new LiveIndexTabFragment2();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        springView = (SpringView) view.findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));
//        progress = (ProgressActivity) view.findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
//        progress.showLoading();
        //设置适配器
        mQuickAdapter = new VideoAdapter(R.layout.item_live, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
//        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);

        //头view
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_video_header, null);
        mTagsRecyclerView = (RecyclerView) mHeaderView.findViewById(R.id.rv_list);
        mTagsTurnUpTv = (TextView) mHeaderView.findViewById(R.id.turn_up);
        mTagsTurnUpTv.setVisibility(View.GONE);
        mTagsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mTagsRecyclerView.setHasFixedSize(true);
        mTagsAdapter = new VideoTagAdapter(R.layout.item_video_tag, null);
        mTagsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mTagsAdapter.openLoadMore(8, true);
        mTagsRecyclerView.setAdapter(mTagsAdapter);
        getVideoTag();

        getVideos(page);

        return view;
    }

    private void getVideoTag() {
        Map<String, Integer> options = new HashMap<>();
        options.put("limit", 100);
        HttpData.getInstance().HttpDataGetVideoTags(new Observer<HttpResult3<TagDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<TagDto, Object> tags) {
                if (!tags.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), tags.getMsg());
                    return;
                }
                List<TagDto> taglist = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    taglist.add(tags.getData().get(i));
                    Log.e("e", i + " " + tags.getData().get(i).getId() + " " + tags.getData().get(i).getLabelName());
                }
                taglist.add(new TagDto("其他", "http://ovjdaa6w0.bkt.clouddn.com/icon_video_tag.png"));
                mTagsAdapter.addData(taglist);
                mTagsAdapter.setOnRecyclerViewItemClickListener((view, position) -> {

                    if (position == 7 && mTagsAdapter.getData().size() == 8) {    //"其他"标签
                        mTagsAdapter.setNewData(tags.getData());
                        mTagsTurnUpTv.setVisibility(View.VISIBLE);
                        mTagsTurnUpTv.setOnClickListener(view1 -> {
                            mTagsAdapter.setNewData(taglist);
                            mTagsTurnUpTv.setVisibility(View.GONE);
                        });
                        return;
                    }
                    startActivity(new Intent(getActivity(), VideoInTagActivity.class)
                            .putExtra("title", tags.getData().get(position).getLabelName())
                            .putExtra("labelId", tags.getData().get(position).getId()));
                });
            }
        }, options);
    }


    private void getVideos(int page) {
        VideoListSearchEntity searchEntity = new VideoListSearchEntity();
        searchEntity.setPageNum(page);
        searchEntity.setPageSize(10000);
        searchEntity.setKeyword(null);
        searchEntity.setLabelId(null);
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
                //设置页面为加载错误
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                if (mQuickAdapter.getData().size() != 0) {
                    mQuickAdapter.addData(data.getData());
                } else {
                    mQuickAdapter.setNewData(data.getData());
                }
                mQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    startActivity(i);
                });
                mQuickAdapter.addHeaderView(mHeaderView);
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
        getVideos(page);
        springView.onFinishFreshAndLoad();
    }

    @Override
    public void onLoadmore() {
        page++;
        getVideos(page);
        springView.onFinishFreshAndLoad();
    }
}

