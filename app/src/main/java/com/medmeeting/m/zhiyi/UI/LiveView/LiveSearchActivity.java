package com.medmeeting.m.zhiyi.UI.LiveView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MVP.Presenter.ListSearchListPresent;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveSearchActivity extends AppCompatActivity implements SpringView.OnFreshListener, LiveListView {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.search_edit)
    EditText searchEt;

    @Bind(R.id.search_tag_llyt)
    LinearLayout searchTagLlyt;
    @Bind(R.id.search_tag_text)
    TextView searchTagTv;
    @Bind(R.id.search_tag_delete)
    ImageView searchTagIv;

    private static final String TAG = LiveSearchActivity.class.getSimpleName();
    @Bind(R.id.type)
    TextView typeTv;
    @Bind(R.id.search_tv)
    TextView searchTv;

    private String classify = "";
    private List<String> classifys = new ArrayList<>();
    private String classifyId = "";
    private List<String> classifyIds = new ArrayList<>();
    private String searchKey;
    private String searchType;
    private static final String LABELID = "labelId";
    private static final String KEYWORD = "keyword";

    ProgressActivity progress;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private ListSearchListPresent present;
    private LiveSearchDto liveSearchDto = new LiveSearchDto();

    private String type = "公开";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        ButterKnife.bind(this);

        initToolbar("搜索直播");

        initTagsView();
    }

    private void initToolbar(String title) {
        toolbarTitleTv.setText(title);
        toolbarTitleTv.setTextColor(Color.BLACK);
        toolbarTitleTv.setFocusable(true);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initTagsView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        springView = (SpringView) findViewById(R.id.springview);

        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(this));

        progress = (ProgressActivity) findViewById(R.id.progress);
        //设置RecyclerView的显示模式  当前List模式
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(LiveSearchActivity.this, 2));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
//        mQuickAdapter = new TagAdapter(R.layout.item_tag, null);
        mQuickAdapter = new LiveAdapter(R.layout.item_live, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
        present = new ListSearchListPresent(this);
        //请求网络数据
//        liveSearchDto.setKeyword("");
//        present.LoadData(false, liveSearchDto);
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE2, Constant.EMPTY_CONTEXT2);
    }

    @Override
    public void showProgress() {
        progress.showLoading();
    }

    @Override
    public void hideProgress() {
        progress.showContent();
    }

    @Override
    public void newDatas(List<LiveDto> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10, true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<LiveDto> addList) {
        //新增自动加载的的数据
        mQuickAdapter.notifyDataChangedAfterLoadMore(addList, true);
    }

    @Override
    public void showLoadFailMsg() {
        //设置加载错误页显示
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PageIndex = 1;
//                present.LoadData("1",PageIndex,false);
                present.LoadData(false, liveSearchDto);
            }
        });
    }

    @Override
    public void showLoadCompleteAllData() {
        //所有数据加载完成后显示
        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
        View view = this.getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.addFooterView(view);
    }

    @Override
    public void showNoData() {
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadmore() {

    }

    @OnClick({R.id.type, R.id.search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.type:
                final View popupView = LiveSearchActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_broker, null);

                // 创建PopupWindow对象，指定宽度和高度
                final PopupWindow window = new PopupWindow(popupView, 300, 300);
                // 设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));

                // 设置可以获取焦点
                window.setFocusable(true);
                // 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);

                // 更新popupwindow的状态
                window.update();
                // 以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(typeTv, 0, 20);

                RelativeLayout relativeLayout = (RelativeLayout) popupView.findViewById(R.id.public_rlyt);
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        typeTv.setText("公开 ▼");
                        window.dismiss();
                        type = "公开";
                    }
                });
                RelativeLayout relativeLayout2 = (RelativeLayout) popupView.findViewById(R.id.private_rlyt);
                relativeLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        typeTv.setText("私密 ▼");
                        window.dismiss();
                        type = "私密";
                    }
                });

                break;
            case R.id.search_tv:
                if ("公开".equals(type)) {
                    liveSearchDto.setKeyword(searchEt.getText().toString());
                    liveSearchDto.setRoomNumber("");

                } else if ("私密".equals(type)) {
                    liveSearchDto.setKeyword("");
                    liveSearchDto.setRoomNumber(searchEt.getText().toString());// test data 411826
                }
                present.LoadData(false, liveSearchDto);

                break;
        }
    }
}
