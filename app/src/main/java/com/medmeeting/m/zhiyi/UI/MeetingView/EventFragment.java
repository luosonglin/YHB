package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.EventAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.AdminEventActive;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.GlideImageLoader;
import com.medmeeting.m.zhiyi.Widget.LoadingFlashView;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

import static java.lang.Thread.sleep;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/11/2017 7:01 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventFragment extends Fragment implements SpringView.OnFreshListener{

    private Integer eventType;

    private SpringView sv;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    LoadingFlashView loadingView;

    private View mHeaderView;
    private com.youth.banner.Banner mBanner;
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    Unbinder unbinder;

    public static EventFragment newInstance(Integer eventType) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt("eventType", eventType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventType = getArguments().getInt("eventType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sv = (SpringView) view.findViewById(R.id.springview);
        //设置下拉刷新监听
        sv.setListener(this);
        //设置下拉刷新样式
        sv.setType(SpringView.Type.FOLLOW);
        sv.setHeader(new DefaultHeader(getActivity()));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new EventAdapter(R.layout.item_meeting, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);

        loadingView = (LoadingFlashView) view.findViewById(R.id.loadingView);
        loadingView.showLoading();
        mRecyclerView.setVisibility(View.INVISIBLE);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_event_header, null);
        mBanner = (Banner) mHeaderView.findViewById(R.id.banner_news);
        getMeetingService(eventType);
    }

    private void getMeetingService(Integer eventType) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        if (eventType != 0) {
            map.put("eventType", eventType);
        } else {
            HttpData.getInstance().HttpDataGetBanners(new Observer<HttpResult3<AdminEventActive, Object>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(getActivity(), e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<AdminEventActive, Object> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(getActivity(), data.getMsg());
                        sv.onFinishFreshAndLoad();
                        return;
                    }
                    bannerImages.clear();
                    bannerTitles.clear();
                    for (AdminEventActive i : data.getData()) {
                        bannerImages.add(i.getBanner()); //+"?imageMogr2/thumbnail/x160"
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
                                bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position-1).getStartDate(), DateUtils.TYPE_02)
                                        + " 至 " + DateUtils.formatDate(data.getData().get(position-1).getEndDate(), DateUtils.TYPE_02)
                                        + " 欢迎参加： " + data.getData().get(position-1).getTitle());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                        }
                    });

                    mAdapter.addHeaderView(mHeaderView);
                }
            }, "EVENT");
        }

        HttpData.getInstance().HttpDataGetAllEventList(new Observer<HttpResult3<Event, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Event, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                mAdapter.setNewData(data.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {

                    Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", data.getData().get(position).getId());
                    bundle.putString("sourceType", data.getData().get(position).getSourceType());
                    bundle.putString("eventTitle", data.getData().get(position).getTitle());
                    bundle.putString("photo", data.getData().get(position).getBanner());
                    bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position).getStartDate(), DateUtils.TYPE_02)
                            + " 至 " + DateUtils.formatDate(data.getData().get(position).getEndDate(), DateUtils.TYPE_02)
                            + " 欢迎参加： " + data.getData().get(position).getTitle());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);

                });
                sv.onFinishFreshAndLoad();


                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        try {
                            sleep(320);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            getActivity().runOnUiThread(() -> {
                                loadingView.hideLoading();
                                mRecyclerView.setVisibility(View.VISIBLE);
                            });
                        }
                    }
                },1102);
            }
        }, map);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        sv.onFinishFreshAndLoad();
        getMeetingService(eventType);
    }

    @Override
    public void onLoadmore() {

    }
}
