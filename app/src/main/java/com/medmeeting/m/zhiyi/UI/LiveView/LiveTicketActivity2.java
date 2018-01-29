package com.medmeeting.m.zhiyi.UI.LiveView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoPayUserAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSettlementEntity;
import com.medmeeting.m.zhiyi.UI.WalletView.MyWalletActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 10/11/2017 5:13 PM
 * @describe 报名统计
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveTicketActivity2 extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.settlementBtn)
    TextView settlementBtn;
    @BindView(R.id.totalAmount)
    TextView totalAmount;
    @BindView(R.id.actualAmount)
    TextView actualAmount;
    @BindView(R.id.detail)
    TextView detailTv;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    private Integer programId;

    private BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticket2);
        ButterKnife.bind(this);

        programId = getIntent().getExtras().getInt("programId");

        toolBar();
        initView();
    }


    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        mRecyclerView.addItemDecoration(new DividerItemDecoration(LiveTicketActivity2.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(LiveTicketActivity2.this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoPayUserAdapter(R.layout.item_video_pay_user, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        HttpData.getInstance().HttpDataGetLiveSettlement(new Observer<HttpResult3<Object, LiveSettlementEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(LiveTicketActivity2.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, LiveSettlementEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(LiveTicketActivity2.this, data.getMsg());
                    return;
                }
                totalAmount.setText("¥ " + data.getEntity().getTotalAmount() + "元");
                actualAmount.setText("实际到账金额：    ¥ " + data.getEntity().getActualAmount() + "元");
                settlementBtn.setOnClickListener(view -> {
//                    HttpData.getInstance().HttpDataExtract(new Observer<HttpResult3>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            ToastUtils.show(LiveTicketActivity2.this, e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(HttpResult3 httpResult3) {
//                            if (!httpResult3.getStatus().equals("success")) {
//                                if (httpResult3.getMsg().equals("直播节目尚未结束，不可提现。")) {
//                                    new AlertDialog.Builder(LiveTicketActivity2.this)
//                                            .setIcon(R.mipmap.logo)
//                                            .setTitle("是否关闭该直播")
//                                            .setMessage("提示: 关闭该直播后才能提现")
//                                            .setNegativeButton("取消", (dialogInterface, i) -> finish())
//                                            .setPositiveButton("确认", (dialogInterface, i) -> HttpData.getInstance().HttpDataCloseProgram(new Observer<HttpResult3>() {
//                                                @Override
//                                                public void onCompleted() {
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable e) {
//                                                }
//
//                                                @Override
//                                                public void onNext(HttpResult3 httpResult3) {
//                                                    if (!httpResult3.getStatus().equals("success")) {
//                                                        ToastUtils.show(LiveTicketActivity2.this, httpResult3.getMsg());
//                                                        return;
//                                                    }
//                                                    ToastUtils.show(LiveTicketActivity2.this, "成功关闭该场直播，\n 再次点击可进行提现");
//                                                }
//                                            }, programId))
//                                            .show();
//                                } else {
//                                    ToastUtils.show(LiveTicketActivity2.this, httpResult3.getMsg());
//                                }
//
//                                return;
//                            }
//                            startActivity(new Intent(LiveTicketActivity2.this, MyWalletActivity.class));
//                        }
//                    }, programId);

                    HttpData.getInstance().HttpDataPostLiveSettlement(new Observer<HttpResult3>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.show(LiveTicketActivity2.this, e.getMessage());
                        }

                        @Override
                        public void onNext(HttpResult3 httpResult3) {
                            if (!httpResult3.getStatus().equals("success")) {
                                if (httpResult3.getErrorCode().equals("70001")) {   //httpResult3.getMsg().equals("直播节目尚未结束，不可结算。") ||
                                    new AlertDialog.Builder(LiveTicketActivity2.this)
                                            .setIcon(R.mipmap.logo)
                                            .setTitle(" ")
                                            .setMessage("温馨提示: \n关闭直播后将无法开启直播，确定要结束直播吗？")
                                            .setNegativeButton("取消", (dialogInterface, i) -> finish())
                                            .setPositiveButton("确认", (dialogInterface, i) -> HttpData.getInstance().HttpDataCloseProgram(new Observer<HttpResult3>() {
                                                @Override
                                                public void onCompleted() {
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                }

                                                @Override
                                                public void onNext(HttpResult3 httpResult3) {
                                                    if (!httpResult3.getStatus().equals("success")) {
                                                        ToastUtils.show(LiveTicketActivity2.this, httpResult3.getMsg());
                                                        return;
                                                    }
                                                    ToastUtils.show(LiveTicketActivity2.this, "成功关闭该场直播，\n 再次点击可进行提现");
                                                }
                                            }, programId))
                                            .show();
                                } else {
                                    ToastUtils.show(LiveTicketActivity2.this, httpResult3.getMsg());
                                }

                                return;
                            }
                            startActivity(new Intent(LiveTicketActivity2.this, MyWalletActivity.class));
                        }
                    }, programId);

                });
                detailTv.setOnClickListener(view -> new AlertDialog.Builder(LiveTicketActivity2.this)
                        .setMessage("本场直播收取" + data.getEntity().getFeeAmount() + "元为平台服务费")
                        .setTitle("温馨提示")
                        .setPositiveButton("知道啦", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show());
                mAdapter.setNewData(data.getEntity().getPayList());
            }
        }, programId);
    }


}

