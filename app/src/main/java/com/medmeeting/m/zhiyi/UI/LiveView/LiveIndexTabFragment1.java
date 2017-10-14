package com.medmeeting.m.zhiyi.UI.LiveView;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto2;
import com.medmeeting.m.zhiyi.Util.DateUtil;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.List;

import rx.Observer;

public class LiveIndexTabFragment1 extends Fragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, LiveListView {

    private RecyclerView mRecyclerView;
    //    private ProgressActivity progress;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private LiveSearchDto2 liveSearchDto2 = new LiveSearchDto2();
    private static final String TAG = LiveIndexTabFragment1.class.getSimpleName();


    private View mHeaderView;
    private ImageView mBackgroundTv;
    private TextView mStatusTv;
    private TextView mPriceTv;
    private ImageView mAvatarIv;
    private TextView mTitleTv;
    private TextView mUserTv;
    private TextView mTimeTv;

    public static LiveIndexTabFragment1 newInstance() {
        LiveIndexTabFragment1 fragment = new LiveIndexTabFragment1();
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
//        progress = (ProgressActivity) view.findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
//        progress.showLoading();
        //设置适配器
        mQuickAdapter = new LiveAdapter(R.layout.item_live, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);

        //头view
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_live_header, null);
        mBackgroundTv = (ImageView) mHeaderView.findViewById(R.id.background);
        mStatusTv = (TextView) mHeaderView.findViewById(R.id.status);
        mPriceTv = (TextView) mHeaderView.findViewById(R.id.price);
        mAvatarIv = (ImageView) mHeaderView.findViewById(R.id.avatar);
        mTitleTv = (TextView) mHeaderView.findViewById(R.id.title);
        mUserTv = (TextView) mHeaderView.findViewById(R.id.user);
        mTimeTv = (TextView) mHeaderView.findViewById(R.id.time);

        //请求网络数据
        HttpData.getInstance().HttpDataGetAllLives(new Observer<HttpResult3<LiveDto, Object>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
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
            public void onNext(HttpResult3<LiveDto, Object> data) {

                List<LiveDto> datas = data.getData();

                if (data.getData().size() > 1) {
                    LiveDto firstData = data.getData().get(0);

                    Glide.with(getActivity())
                            .load(firstData.getCoverPhoto())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .into(mBackgroundTv);
                    switch (firstData.getLiveStatus()) {
                        case "ready":
                            mStatusTv.setText("预告");
                            mStatusTv.setBackground(getResources().getDrawable(R.color.skyblue));
                            break;
                        case "play":
                            mStatusTv.setText("直播");
                            mStatusTv.setBackground(getResources().getDrawable(R.color.red));
                            break;
                        case "wait":
                            mStatusTv.setText("离开");
                            mStatusTv.setBackground(getResources().getDrawable(R.color.yellow));
                            break;
                        case "end":
                            mStatusTv.setText("结束");
                            mStatusTv.setBackground(getResources().getDrawable(R.color.grey1));
                            break;
                    }
                    if (firstData.getChargeType().equals("no")) {
                        mPriceTv.setVisibility(View.GONE);
                    } else if (firstData.getChargeType().equals("yes")) {
                        mPriceTv.setText("¥ " + firstData.getPrice());
                    }
                    Glide.with(getActivity())
                            .load(firstData.getUserPic())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .error(R.mipmap.avator_default)
                            .transform(new GlideCircleTransform(getActivity()))
                            .into(mAvatarIv);
                    mTitleTv.setText(firstData.getTitle());
                    mUserTv.setText(firstData.getAuthorName() + " | " + firstData.getAuthorTitle());
                    mTimeTv.setText(DateUtil.formatDate(firstData.getStartTime(), DateUtil.TYPE_06));

                    mHeaderView.setOnClickListener(view -> {
                        Intent intent = new Intent(getActivity(), LiveProgramDetailActivity.class);
                        intent.putExtra("authorName", firstData.getAuthorName());
                        intent.putExtra("userPic", firstData.getUserPic());
                        intent.putExtra("programId", firstData.getId());
                        intent.putExtra("roomId", firstData.getRoomId());
                        intent.putExtra("coverPhoto", firstData.getCoverPhoto());
                        intent.putExtra("title", firstData.getTitle());
                        getActivity().startActivity(intent);
                    });
                    mQuickAdapter.addHeaderView(mHeaderView);
                    datas.remove(0);
                }

                mQuickAdapter.addData(datas);
                Log.e(TAG, "onNext");
            }
        }, liveSearchDto2);

        return view;
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

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadmore() {

    }
}
