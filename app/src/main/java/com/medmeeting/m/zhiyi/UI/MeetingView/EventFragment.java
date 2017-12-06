package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.EventBanner;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.GlideImageLoader;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/11/2017 7:01 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventFragment extends Fragment {

    private Integer eventType;

    private SwipeRefreshLayout srl;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    private View mHeaderView;
    private com.youth.banner.Banner mBanner;
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new EventAdapter(R.layout.item_meeting, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);

        getMeetingService(eventType);

        //下啦刷新
        srl.setOnRefreshListener(() -> {
            srl.setRefreshing(false);
            getMeetingService(eventType);
        });
    }

    private void getMeetingService(Integer eventType) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        if (eventType != 0) {
            map.put("eventType", eventType);
        } else {
            mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_event_header, null);
            mBanner = (Banner) mHeaderView.findViewById(R.id.banner_news);

            HttpData.getInstance().HttpDataGetMeetingBanner(new Observer<HttpResult<EventBanner>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(getActivity(), e.getMessage());
                    Log.e(getActivity().getLocalClassName(), e.getMessage());
                }

                @Override
                public void onNext(HttpResult<EventBanner> data) {
                    for (EventBanner.BannersBean i : data.getData().getBanners()) {
                        bannerImages.add(i.getBanner());
                        bannerTitles.add(i.getTitle());
                    }
                    mBanner.setImages(bannerImages)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                            .setBannerTitles(bannerTitles)
                            .setBannerAnimation(Transformer.BackgroundToForeground)
                            .setImageLoader(new GlideImageLoader())
                            .start();
                    mBanner.setOnBannerClickListener(position -> {
//                    Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("eventId", bannerDto.getBanners().get(position - 1).getId() + "");
//                    bundle.putString("eventTitle", bannerDto.getBanners().get(position - 1).getTitle());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    });

                    mAdapter.addHeaderView(mHeaderView);
                    srl.setRefreshing(false);
                }
            });
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
                mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                srl.setRefreshing(false);
            }
        }, map);

//        if (eventType == 0) {

//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
