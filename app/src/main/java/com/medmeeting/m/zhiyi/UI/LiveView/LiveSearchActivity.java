package com.medmeeting.m.zhiyi.UI.LiveView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Presenter.ListSearchListPresent;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.TagAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class LiveSearchActivity extends AppCompatActivity implements SpringView.OnFreshListener, LiveListView {

    //    @Bind(R.id.toolbar_title)
//    TextView toolbarTitleTv;
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

    @Bind(R.id.tags_recyclerView)
    RecyclerView tagsRecyclerView;
    @Bind(R.id.tags_swipe_refresh_lyt)
    SwipeRefreshLayout tagsSwipeRefreshLyt;

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
    private List<Integer> labelIds = new ArrayList<>();
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

    private TagAdapter mBaseQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        ButterKnife.bind(this);

        initToolbar();

        initTagsView();
        initLivesView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initLivesView() {
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
        springView.onFinishFreshAndLoad();
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }


    @Override
    public void onRefresh() {
        springView.onFinishFreshAndLoad();
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
                        searchEt.setHint("请输入直播间、直播、描述信息");
                        tagsSwipeRefreshLyt.setVisibility(View.VISIBLE);
                    }
                });
                RelativeLayout relativeLayout2 = (RelativeLayout) popupView.findViewById(R.id.private_rlyt);
                relativeLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        typeTv.setText("私密 ▼");
                        window.dismiss();
                        type = "私密";
                        searchEt.setHint("请输入房间号ID");
                        tagsSwipeRefreshLyt.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.search_tv:
                if ("公开".equals(type)) {
                    liveSearchDto.setKeyword(searchEt.getText().toString());
                    liveSearchDto.setRoomNumber("");
                    liveSearchDto.setLabelIds(labelIds);
                } else if ("私密".equals(type)) {
                    liveSearchDto.setKeyword("");
                    liveSearchDto.setRoomNumber(searchEt.getText().toString());// test data 411826
                    liveSearchDto.setLabelIds(labelIds);
                }
                present.LoadData(false, liveSearchDto);
                break;
        }
    }

    private void initTagsView() {
//        searchEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                searchType = LiveSearchActivity.KEYWORD;
//                searchKey = charSequence.toString();
//                initSearchView(searchType, searchKey);
//
//                if (charSequence.length() == 0) {
//                    tagsSwipeRefreshLyt.setVisibility(View.VISIBLE);
//                } else {
//                    tagsSwipeRefreshLyt.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        tagsSwipeRefreshLyt.setColorSchemeResources(R.color.colorAccent);
        tagsSwipeRefreshLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tagsSwipeRefreshLyt.setRefreshing(false);
                testSingleTagsAdapter();
            }
        });

        testSingleTagsAdapter();
    }

    private void testSingleTagsAdapter() {
        tagsRecyclerView.setLayoutManager(new GridLayoutManager(LiveSearchActivity.this, 4));
        tagsRecyclerView.setHasFixedSize(true);
        tagsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mBaseQuickAdapter = new TagAdapter(R.layout.item_tag, null);
        tagsRecyclerView.setAdapter(mBaseQuickAdapter);

        HttpData.getInstance().HttpDataGetLiveTags(new Observer<HttpResult3<TagDto, Object>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<TagDto, Object> tagDtoObjectHttpResult3) {
                mBaseQuickAdapter.addData(tagDtoObjectHttpResult3.getData());
                Log.e(TAG, "onNext");
            }
        });
        mBaseQuickAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TagDto tagDto = (TagDto) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.name:
//                        if (!tags_confirm.contains(tagDto)) {
//                            tags_confirm.add(tagDto);
//                            view.setBackgroundResource(R.drawable.textview_all_blue);
//                        } else {
//                            tags_confirm.remove(tagDto);
//                            view.setBackgroundResource(R.drawable.textview_radius_grey);
//                        }

                        searchEt.setHint("");
                        searchEt.setEnabled(false);
                        searchTagLlyt.setVisibility(View.VISIBLE);
                        searchTagTv.setText(tagDto.getLabelName());
                        searchTagIv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchTagLlyt.setVisibility(View.GONE);
                                tagsSwipeRefreshLyt.setVisibility(View.VISIBLE);
                                searchEt.setHint("请输入主播名或选择某一直播分类");
                                searchEt.setEnabled(true);
                            }
                        });
                        tagsSwipeRefreshLyt.setVisibility(View.GONE);

                        liveSearchDto.setKeyword("");
                        liveSearchDto.setRoomNumber("");
                        labelIds.clear();
                        labelIds.add(tagDto.getId());
                        liveSearchDto.setLabelIds(labelIds);
                        present.LoadData(false, liveSearchDto);
                        break;
                }
            }
        });
//        final SLSingleAdapter<ClassifyTag.ListBean> adapter = new SLSingleAdapter<ClassifyTag.ListBean>(LiveSearchActivity.this, R.layout.item_live_classify_tag) {
//            @Override
//            protected void bindData(SLViewHolder holder, final ClassifyTag.ListBean item) {
//                final TextView tagNameTv = holder.getView(R.id.tag_name);
//                tagNameTv.setText(item.getLabelName());
//                tagNameTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        searchKey = Integer.toString(item.getId());
//
//                        searchEt.setHint("");
//                        searchEt.setEnabled(false);
//
//                        searchTagLlyt.setVisibility(View.VISIBLE);
//                        searchTagTv.setText(item.getLabelName());
//                        searchTagIv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                searchTagLlyt.setVisibility(View.GONE);
//                                tagsSwipeRefreshLyt.setVisibility(View.VISIBLE);
//                                searchEt.setHint("请输入主播名或选择某一直播分类");
//                                searchEt.setEnabled(true);
//                            }
//                        });
//
//                        tagsSwipeRefreshLyt.setVisibility(View.GONE);
//
//                        searchType = LiveSearchActivity.LABELID;
//                        initSearchView(searchType, searchKey);
//                    }
//                });
//            }
//        };
//        tagsRecyclerView.setAdapter(adapter);
//
//        Subscriber subscriber = new Subscriber() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ToastUtils.show(LiveSearchActivity.this, e.getMessage());
//                Log.e(TAG, e.getMessage());
//            }
//
//            @Override
//            public void onNext(Object o) {
//                if (o instanceof ClassifyTag) {
//                    tags = ((ClassifyTag) o).getList();
//                    adapter.setData(tags);
//                }
//            }
//        };
//
//        HttpManager.generate(LiveService.class, LiveSearchActivity.this)
//                .getTagList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }
}
