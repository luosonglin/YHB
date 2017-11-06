package com.medmeeting.m.zhiyi.UI.VideoView;

import android.os.Bundle;
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
import com.medmeeting.m.zhiyi.UI.Adapter.VideoPayUserAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoSettlementEntity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 02/11/2017 4:57 PM
 * @describe 主播自己的的收费统计页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailFareFragment extends Fragment {

    private static Integer videoId;
    @Bind(R.id.settlementAmount)
    TextView settlementAmount;
    @Bind(R.id.totalAmount)
    TextView totalAmount;
    @Bind(R.id.actualAmount)
    TextView actualAmount;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    public static VideoDetailFareFragment newInstance(Integer classifys1) {
        VideoDetailFareFragment fragment = new VideoDetailFareFragment();
        Bundle args = new Bundle();
        args.putInt("videoId", classifys1);
        fragment.setArguments(args);

        videoId = classifys1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            videoId = getArguments().getInt("videoId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_detail_fare, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoPayUserAdapter(R.layout.item_video_pay_user, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getSettlementDetail(videoId);
    }

    private void getSettlementDetail(Integer videoId) {
        HttpData.getInstance().HttpDataGetSettlementDetail(new Observer<HttpResult3<Object, VideoSettlementEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, VideoSettlementEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                settlementAmount.setText("¥ " + data.getEntity().getSettlementAmount() + "元");
                totalAmount.setText("总收入:  ¥ " + data.getEntity().getTotalAmount() + "元");
                actualAmount.setText("实际到账金额：    ¥ " + data.getEntity().getActualAmount() + "元");
                mAdapter.addData(data.getEntity().getPayList());
            }
        }, videoId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
