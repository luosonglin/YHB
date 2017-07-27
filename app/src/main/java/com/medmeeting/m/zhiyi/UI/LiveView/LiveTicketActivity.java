package com.medmeeting.m.zhiyi.UI.LiveView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Presenter.LiveTicketListPresent;
import com.medmeeting.m.zhiyi.MVP.View.LiveTicketListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveTicketAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.List;

import rx.Observer;

public class LiveTicketActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, LiveTicketListView {

    RecyclerView mRecyclerView;
    ProgressActivity progress;
    private Toolbar toolbar;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private LiveTicketListPresent present;
    private Integer programId;

    private TextView moneyTv, statusTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ticket);
        toolBar();
        initView();
        initListener();
        initBottomView();
    }

    private void initBottomView() {
        moneyTv = (TextView) findViewById(R.id.money);
        statusTv = (TextView) findViewById(R.id.status);
        HttpData.getInstance().HttpDataGetPayList(new Observer<HttpResult3<Object, LiveTicketDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, LiveTicketDto> data) {
                moneyTv.setText("合计收入/人数：¥" + data.getEntity().getAmountSum()+"元/"+data.getEntity().getPayList().size()+"人");

                if (data.getEntity().getExtractStatus() != null) {
                    switch (data.getEntity().getExtractStatus()) {
                        case "PENDING": //待处理
                            statusTv.setText("提现中");
                            statusTv.setBackgroundResource(R.color.grey1);
                            break;
                        case "ADOPT":   //通过
                            statusTv.setText("已提现");
                            statusTv.setBackgroundResource(R.color.grey1);
                            break;
                        case "REJECT":  //驳回
                            statusTv.setText("已驳回");
                            statusTv.setBackgroundResource(R.color.grey1);
                            break;
                        default:
                            statusTv.setText("提现");
                            statusTv.setBackgroundResource(R.color.skyblue);
                            break;

                    }
                } else {    //null
                    statusTv.setText("提现");
                    statusTv.setBackgroundResource(R.color.skyblue);
                }

                if(statusTv.getText().toString().trim().equals("提现")) {
                    statusTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AlertDialog.Builder(LiveTicketActivity.this)
                                    .setMessage("确定提现吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                            HttpData.getInstance().HttpDataExtract(new Observer<HttpResult3>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onNext(HttpResult3 httpResult3) {
                                                    if (httpResult3.getStatus().equals("success")) {
                                                        ToastUtils.show(LiveTicketActivity.this, "提现成功");
                                                        statusTv.setText("已驳回");
                                                        statusTv.setBackgroundResource(R.color.grey1);
                                                    } else if (httpResult3.getStatus().equals("error")) {
                                                        ToastUtils.show(LiveTicketActivity.this, httpResult3.getMsg());
                                                    }
                                                    onResume();
                                                    dialogInterface.dismiss();
                                                }
                                            }, programId);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });
                }

            }
        }, programId);
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    private void initView() {
        programId = getIntent().getExtras().getInt("programId");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        springView = (SpringView) findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));

        progress = (ProgressActivity) findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
        mQuickAdapter = new LiveTicketAdapter(R.layout.item_live_ticket, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
        present = new LiveTicketListPresent(this);
        //请求网络数据
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, programId);
    }

    private void initListener() {
        //设置自动加载监听
        mQuickAdapter.setOnLoadMoreListener(this);

//        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(LiveTicketActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        mQuickAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(View view, int position) {
//                Toast.makeText(LiveTicketActivity.this, "长按了" + position, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        present.LoadData(true, programId);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex = 1;
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, programId);
    }

    //上啦加载  mRecyclerView内部集成的自动加载  上啦加载用不上   在其他View使用
    @Override
    public void onLoadmore() {

    }

    /*
    * MVP模式的相关状态
    *
    * */
    @Override
    public void showProgress() {
        progress.showLoading();
    }

    @Override
    public void hideProgress() {
        progress.showContent();
    }

    @Override
    public void newDatas(List<LiveTicketDto.PayListBean> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10, true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<LiveTicketDto.PayListBean> addList) {
        //新增自动加载的的数据
        mQuickAdapter.notifyDataChangedAfterLoadMore(addList, true);
    }

    @Override
    public void showLoadFailMsg() {
        //设置加载错误页显示
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIndex = 1;
//                present.LoadData("1",PageIndex,false);
                present.LoadData(false, programId);
            }
        });
    }

    @Override
    public void showLoadCompleteAllData() {
        //所有数据加载完成后显示
        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
        View view = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.addFooterView(view);
    }

    @Override
    public void showNoData() {
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }
}
