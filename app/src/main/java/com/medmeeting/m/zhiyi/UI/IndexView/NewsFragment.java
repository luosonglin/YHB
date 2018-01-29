package com.medmeeting.m.zhiyi.UI.IndexView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.HeaderMeetingAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.AdminEventActive;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveProListEntity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/11/2017 2:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsFragment extends BaseFragment {

    RecyclerView recyclerView;
    private SwipeRefreshLayout srl;

    private Integer mLabelId;
    private String mLabelName;
    protected List<Blog> mDatas = new ArrayList<>();
    protected BaseQuickAdapter mAdapter;

    //Header View
    private View mHeaderView;
    private Banner mBanner;
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    private RelativeLayout mHeaderLive;
    private TextView mHeaderMoreView;
    private RelativeLayout mHeaderLive1;
    private RelativeLayout mHeaderLive2;
    private TextView mHeaderLiveView;
    private ImageView mHeaderLiveImage1;
    private ImageView mHeaderLiveImage2;
    private TextView mHeaderLiveName1;
    private TextView mHeaderLiveName11;
    private TextView mHeaderLiveName2;
    private TextView mHeaderLiveName22;
    private TextView mHeaderLiveStatus1;
    private TextView mHeaderLiveStatus2;
    private TextView mHeaderLiveStartTime1;
    private TextView mHeaderLiveStartTime2;

    private RecyclerView mHeaderRecyclerView;
    private TextView mHeaderMeetingView;
    private HeaderMeetingAdapter mHeaderMeetingAdapter;

Unbinder unbinder;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, rootView);
        unbinder = ButterKnife.bind(this, rootView);
        return v;
    }

    public static NewsFragment newInstance(Integer labelId, String labelName) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstanceValue.DATA, labelId);
        bundle.putString("LabelName", labelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);

        //绑定HeaderView
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_header, null);
        mBanner = (Banner) mHeaderView.findViewById(R.id.banner_news);

        mHeaderLive = (RelativeLayout) mHeaderView.findViewById(R.id.live_title_rlyt);
        mHeaderMoreView = (TextView) mHeaderView.findViewById(R.id.live_more);
        mHeaderLive1 = (RelativeLayout) mHeaderView.findViewById(R.id.live_title_rlyt1);
        mHeaderLive2 = (RelativeLayout) mHeaderView.findViewById(R.id.live_title_rlyt2);
        mHeaderLiveView = (TextView) mHeaderView.findViewById(R.id.live_count);
        mHeaderLiveImage1 = (ImageView) mHeaderView.findViewById(R.id.live_image1);
        mHeaderLiveImage2 = (ImageView) mHeaderView.findViewById(R.id.live_image2);
        mHeaderLiveName1 = (TextView) mHeaderView.findViewById(R.id.live_name1);
        mHeaderLiveName11 = (TextView) mHeaderView.findViewById(R.id.live_name11);
        mHeaderLiveName2 = (TextView) mHeaderView.findViewById(R.id.live_name2);
        mHeaderLiveName22 = (TextView) mHeaderView.findViewById(R.id.live_name22);
        mHeaderLiveStatus1 = (TextView) mHeaderView.findViewById(R.id.status1);
        mHeaderLiveStatus2 = (TextView) mHeaderView.findViewById(R.id.status2);
        mHeaderLiveStartTime1 = (TextView) mHeaderView.findViewById(R.id.live_start_time1);
        mHeaderLiveStartTime2 = (TextView) mHeaderView.findViewById(R.id.live_start_time2);

        mHeaderMeetingView = (TextView) mHeaderView.findViewById(R.id.meeting_count);
        mHeaderRecyclerView = (RecyclerView) mHeaderView.findViewById(R.id.rv_list);
    }


    @Override
    protected void processLogic() {
        //下啦刷新
        srl.setOnRefreshListener(() -> {
            srl.setRefreshing(false);
            //如果是推荐页，自动加载header view
            if (mLabelId == 0)
                getHeaderView();
            else
                getData();
        });

        initCommonRecyclerView(createAdapter(), new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mLabelId = getArguments().getInt(ConstanceValue.DATA);
    }

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    protected BaseQuickAdapter createAdapter() {
        return mAdapter = new BlogAdapter(mDatas);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (TextUtils.isEmpty(mLabelId + ""))
            mLabelId = getArguments().getInt(ConstanceValue.DATA);

        if (TextUtils.isEmpty(mLabelName+""))
            mLabelName = getArguments().getString("LabelName");

        //如果是推荐页，自动加载header view
        if (mLabelId == 0) {
            getHeaderView();
            return;
        }
        getData();
    }

    private void getData() {
//        mvpPresenter.getNewsList(mTitleCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        map.put("labelId", mLabelId);
        HttpData.getInstance().HttpDataFindLabelBlogs(new Observer<HttpResult3<Blog, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(HttpResult3<Blog, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    srl.setRefreshing(false);
                    return;
                }
                mAdapter.setNewData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
                    Intent intent = null;
                    switch (data.getData().get(i).getBlogType()) {
                        case "1":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                        case "2":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                        case "3":
                            intent = new Intent(getActivity(), NewsVideoActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                    }
                    startActivity(intent);
                });
                srl.setRefreshing(false);
            }
        }, map);
    }


    private void getHeaderView() {
        HttpData.getInstance().HttpDataGetBanners(new Observer<HttpResult3<AdminEventActive, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity().getApplicationContext(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(HttpResult3<AdminEventActive, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity().getApplicationContext(), data.getMsg());
                    srl.setRefreshing(false);
                    return;
                }
                bannerImages.clear();
                bannerTitles.clear();
                for (AdminEventActive i : data.getData()) {
                    bannerImages.add(i.getBanner());
                    bannerTitles.add(i.getTitle());
                }
                mBanner.setImages(bannerImages)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setBannerTitles(bannerTitles)
                        .setBannerAnimation(Transformer.Default)
                        .setImageLoader(new GlideImageLoader())
                        .start();
                mBanner.setOnBannerClickListener(position -> {
                    Log.e(getActivity().getLocalClassName(), position + "");
                    Intent intent = null;
                    switch (data.getData().get(position - 1).getType()) {
                        case "active":
                            BrowserActivity.launch(getActivity(), data.getData().get(position - 1).getUrl(), data.getData().get(position - 1).getTitle());
                            break;
                        case "live":
                            intent = new Intent(getActivity(), LiveProgramDetailActivity2.class);
                            intent.putExtra("programId", data.getData().get(position - 1).getTypeId());
                            startActivity(intent);
                            break;
                        case "video":
                            intent = new Intent(getActivity(), VideoDetailActivity.class);
                            intent.putExtra("videoId", data.getData().get(position - 1).getTypeId());
                            startActivity(intent);
                            break;
                        case "event":
                            intent = new Intent(getActivity(), MeetingDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("eventId", data.getData().get(position - 1).getTypeId());
                            bundle.putString("eventTitle", data.getData().get(position - 1).getTitle());
                            bundle.putString("sourceType", data.getData().get(position - 1).getSourceType());
                            bundle.putString("photo", data.getData().get(position - 1).getBanner());
                            bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position - 1).getStartDate(), DateUtils.TYPE_02)
                                    + " 至 " + DateUtils.formatDate(data.getData().get(position - 1).getEndDate(), DateUtils.TYPE_02)
                                    + " 欢迎参加： " + data.getData().get(position - 1).getTitle());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                    }

                });
            }
        }, "HOME");

        HttpData.getInstance().HttpDataSelectVideoLive(new Observer<HttpResult3<LiveProListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity().getApplicationContext(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(HttpResult3<LiveProListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity().getApplicationContext(), data.getMsg());
                    srl.setRefreshing(false);
                    return;
                }
                mHeaderMoreView.setOnClickListener(view -> MainActivity.trunLiveView());
                switch (data.getData().size()) {
                    case 0:
                        mHeaderLive.setVisibility(View.GONE);
                        mHeaderLive1.setVisibility(View.GONE);
                        mHeaderLive2.setVisibility(View.GONE);
                        break;
                    case 1:
                        mHeaderLive.setVisibility(View.VISIBLE);
                        mHeaderLive1.setVisibility(View.VISIBLE);
                        mHeaderLive2.setVisibility(View.GONE);
                        mHeaderLiveView.setText("您好，今天有一场直播");
                        mHeaderLiveName1.setText(data.getData().get(0).getTitle());
                        mHeaderLiveName11.setText(data.getData().get(0).getAuthorName() + " " + data.getData().get(0).getAuthorTitle());
                        mHeaderLiveStartTime1.setText(DateUtils.formatDate(data.getData().get(0).getStartTime(), DateUtils.TYPE_11));

                        //（ready：准备中，play：直播中，wait：断开中，end：已结束）
                        if (data.getData().get(0).getLiveStatus().equals("play")) {
                            mHeaderLiveStatus1.setText("直播中");
                            mHeaderLiveStatus1.setBackgroundResource(R.mipmap.icon_live_adapter_status_red);
                            mHeaderLiveImage1.setImageResource(R.mipmap.index_alert1);
                        } else {
                            mHeaderLiveStatus1.setText("预告");
                            mHeaderLiveStatus1.setBackgroundResource(R.mipmap.icon_live_adapter_status_blue);
                            mHeaderLiveImage1.setImageResource(R.mipmap.index_alert2);
                        }
                        mHeaderLive1.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), LiveProgramDetailActivity2.class);
                            intent.putExtra("programId", data.getData().get(0).getId());
                            startActivity(intent);
                        });
                        break;
                    case 2:
                        mHeaderLive.setVisibility(View.VISIBLE);
                        mHeaderLive1.setVisibility(View.VISIBLE);
                        mHeaderLive2.setVisibility(View.VISIBLE);
                        mHeaderLiveView.setText("您好，今天有两场直播");
                        mHeaderLiveName1.setText(data.getData().get(0).getTitle());
                        mHeaderLiveName11.setText(data.getData().get(0).getAuthorName() + " " + data.getData().get(0).getAuthorTitle());

                        mHeaderLiveName2.setText(data.getData().get(1).getTitle());
                        mHeaderLiveName22.setText(data.getData().get(1).getAuthorName() + " " + data.getData().get(1).getAuthorTitle());
                        mHeaderLiveStartTime1.setText(DateUtils.formatDate(data.getData().get(0).getStartTime(), DateUtils.TYPE_11));
                        mHeaderLiveStartTime2.setText(DateUtils.formatDate(data.getData().get(1).getStartTime(), DateUtils.TYPE_11));

                        if (data.getData().get(0).getLiveStatus().equals("play")) {
                            mHeaderLiveStatus1.setText("直播中");
                            mHeaderLiveStatus1.setBackgroundResource(R.mipmap.icon_live_adapter_status_red);
                            mHeaderLiveImage1.setImageResource(R.mipmap.index_alert1);
                            Log.e(getActivity().getLocalClassName(),"mHeaderLiveStatus1直播中");
                        } else if (data.getData().get(0).getLiveStatus().equals("ready")){
                            mHeaderLiveStatus1.setText("预告");
                            mHeaderLiveStatus1.setBackgroundResource(R.mipmap.icon_live_adapter_status_blue);
                            mHeaderLiveImage1.setImageResource(R.mipmap.index_alert2);
                            Log.e(getActivity().getLocalClassName(),"mHeaderLiveStatus1预告");
                        }

                        if (data.getData().get(1).getLiveStatus().equals("play")) {
                            mHeaderLiveStatus2.setText("直播中");
                            mHeaderLiveStatus2.setBackgroundResource(R.mipmap.icon_live_adapter_status_red);
                            mHeaderLiveImage2.setImageResource(R.mipmap.index_alert1);
                            Log.e(getActivity().getLocalClassName(),"mHeaderLiveStatus2直播中");
                        } else if (data.getData().get(1).getLiveStatus().equals("ready")){
                            mHeaderLiveStatus2.setText("预告");
                            mHeaderLiveStatus2.setBackgroundResource(R.mipmap.icon_live_adapter_status_blue);
                            mHeaderLiveImage2.setImageResource(R.mipmap.index_alert2);
                            Log.e(getActivity().getLocalClassName(),"mHeaderLiveStatus2预告");
                        }


                        mHeaderLive1.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), LiveProgramDetailActivity2.class);
                            intent.putExtra("programId", data.getData().get(0).getId());
                            startActivity(intent);
                        });


                        mHeaderLive2.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), LiveProgramDetailActivity2.class);
                            intent.putExtra("programId", data.getData().get(1).getId());
                            startActivity(intent);
                        });
                        break;
                }
            }
        });


        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeaderRecyclerView.setLayoutManager(linearLayoutManager);
        //如果Item高度固定  增加该属性能够提高效率
        mHeaderRecyclerView.setHasFixedSize(true);
        mHeaderMeetingAdapter = new HeaderMeetingAdapter(R.layout.item_header_meeting, null);
        //设置是否自动加载以及加载个数
        mHeaderMeetingAdapter.openLoadMore(3, true);
        //将适配器添加到RecyclerView
        mHeaderRecyclerView.setAdapter(mHeaderMeetingAdapter);
        HttpData.getInstance().HttpDataGetgreatEventList(new Observer<HttpResult3<Event, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity().getApplicationContext(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(HttpResult3<Event, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity().getApplicationContext(), data.getMsg());
                    srl.setRefreshing(false);
                    return;
                }
                mHeaderMeetingView.setText("全部 (" + data.getData().size() + ") >");
                mHeaderMeetingView.setOnClickListener(view -> MainActivity.trunMeetingView());

                mHeaderMeetingAdapter.setNewData(data.getData());
                mHeaderMeetingAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", data.getData().get(position).getId());
                    bundle.putString("eventTitle", data.getData().get(position).getTitle());
                    bundle.putString("sourceType", data.getData().get(position).getSourceType());
                    bundle.putString("photo", data.getData().get(position).getBanner());
                    bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position).getStartDate(), DateUtils.TYPE_02)
                            + " 至 " + DateUtils.formatDate(data.getData().get(position).getEndDate(), DateUtils.TYPE_02)
                            + " 欢迎参加： " + data.getData().get(position).getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }
        });

        mAdapter.addHeaderView(mHeaderView);

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        HttpData.getInstance().HttpDataFindGenBlogList(new Observer<HttpResult3<Blog, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity().getApplicationContext(), e.getMessage());
                srl.setRefreshing(false);
            }

            @Override
            public void onNext(HttpResult3<Blog, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity().getApplicationContext(), data.getMsg());
                    srl.setRefreshing(false);
                    return;
                }


                mAdapter.setNewData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
                    Intent intent = null;
                    switch (data.getData().get(i).getBlogType()) {
                        case "1":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                        case "2":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                        case "3":
                            intent = new Intent(getActivity(), NewsVideoActivity.class);
                            intent.putExtra("blogId", data.getData().get(i).getId());
                            break;
                    }
                    startActivity(intent);
                });

                srl.setRefreshing(false);
            }
        }, map);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onStart() {
        JAnalyticsInterface.onPageStart(getActivity(), "新闻列表("+mLabelName+")页");
        super.onStart();
    }

    @Override
    public void onStop() {
        JAnalyticsInterface.onPageEnd(getActivity(), "新闻列表("+mLabelName+")页");
        super.onStop();
    }
}
