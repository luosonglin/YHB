package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MVP.Presenter.BannerListPresent;
import com.medmeeting.m.zhiyi.MVP.View.BannerListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveProgramAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.List;

/**
 * 直播间详情页
 */
public class MyLiveRoomDetailActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, BannerListView {


    RecyclerView mRecyclerView;
    ProgressActivity progress;
    private Toolbar toolbar;
    private TextView pageName, titleTv, addTv;
    private ImageView backgroundIv;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private BannerListPresent present;
    private Integer roomId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_my_program_detail);
        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        pageName = (TextView) findViewById(R.id.page_name);
        pageName.setText(getIntent().getExtras().getString("title"));
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getIntent().getExtras().getString("title"));

        roomId = getIntent().getExtras().getInt("roomId");

        addTv = (TextView) findViewById(R.id.add);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLiveRoomDetailActivity.this, LiveBuildProgramActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });
        backgroundIv = (ImageView) findViewById(R.id.img);
        Glide.with(MyLiveRoomDetailActivity.this)
                .load(getIntent().getExtras().getString("coverPhoto"))
                .crossFade()
//                .placeholder(R.mipmap.ic_launcher)
                .into(backgroundIv);

        findViewById(R.id.invitation_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLiveRoomDetailActivity.this, LiveInvitationLetterActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        springView = (SpringView) findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));
//        springView.setFooter(new DefaultFooter(this));
//        springView.setHeader(new RotationHeader(this));
//        springView.setFooter(new RotationFooter(this)); //mRecyclerView内部集成的自动加载  上啦加载用不上   在其他View使用

        progress = (ProgressActivity) findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
//        mQuickAdapter = new ListViewAdapter(R.layout.list_view_item_layout,null);
        mQuickAdapter = new LiveProgramAdapter(R.layout.item_live_program, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
//        present = new BookListPresent(this);
        present = new BannerListPresent(this);
        //请求网络数据
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false);
    }

    private void initListener() {
        //设置自动加载监听
        mQuickAdapter.setOnLoadMoreListener(this);

        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MyLiveRoomDetailActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mQuickAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(MyLiveRoomDetailActivity.this, "长按了" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        present.LoadData(true);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex = 1;
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false);
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
    public void newDatas(List<BannerDto.BannersBean> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10, true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<BannerDto.BannersBean> addList) {
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
                present.LoadData(false);
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
