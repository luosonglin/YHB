package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Presenter.ListSearchListPresent;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.TagAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class LiveSearchActivity extends AppCompatActivity {

    //    @BindView(R.id.toolbar_title)
//    TextView toolbarTitleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_edit)
    EditText searchEt;

    @BindView(R.id.search_tag_llyt)
    LinearLayout searchTagLlyt;
    @BindView(R.id.search_tag_text)
    TextView searchTagTv;
    @BindView(R.id.search_tag_delete)
    ImageView searchTagIv;

    @BindView(R.id.tags_recyclerView)
    RecyclerView tagsRecyclerView;
    @BindView(R.id.tags_swipe_refresh_lyt)
    SwipeRefreshLayout tagsSwipeRefreshLyt;

    private static final String TAG = LiveSearchActivity.class.getSimpleName();
    @BindView(R.id.type)
    TextView typeTv;
    @BindView(R.id.search_tv)
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

    //以下为视频

    private RecyclerView mRecyclerView2;
    private BaseQuickAdapter mQuickAdapter2;
    private Integer labelVideo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        ButterKnife.bind(this);

        initToolbar();

        initTagsView();

        initLivesView();

        initVideoView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initLivesView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
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
//        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
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
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                if ("公开".equals(type)) {
                    liveSearchDto.setKeyword(searchEt.getText().toString());
                    liveSearchDto.setRoomNumber(null);
                    liveSearchDto.setLabelIds(labelIds);
                } else if ("私密".equals(type)) {
                    liveSearchDto.setKeyword("");
                    liveSearchDto.setRoomNumber(searchEt.getText().toString());// test data 411826
                    liveSearchDto.setLabelIds(labelIds);
                }
                //请求网络数据
                HttpData.getInstance().HttpDataGetPrograms(new Observer<HttpResult3<LiveDto, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3<LiveDto, Object> data) {
//                        if (data.getData().size() > 4) {
//                            List<LiveDto> lives = new ArrayList<>()
//
//                        } else {
//
//                        }
                        mQuickAdapter.setNewData(data.getData());
                        mQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                            Intent i = new Intent(LiveSearchActivity.this, LiveProgramDetailActivity2.class);
                            i.putExtra("programId", data.getData().get(position).getId());
                            startActivity(i);
                        });
                    }
                }, liveSearchDto);


                if (mQuickAdapter.getData() != null) {
                    //视频api
                    VideoListSearchEntity searchEntity = new VideoListSearchEntity();
                    searchEntity.setPageNum(1);
                    searchEntity.setPageSize(100);
                    if ("公开".equals(type)) {
                        searchEntity.setKeyword(searchEt.getText().toString());
                        searchEntity.setRoomNumber(null);
                    } else if ("私密".equals(type)) {
                        searchEntity.setKeyword(null);
                        searchEntity.setRoomNumber(searchEt.getText().toString());
                    }
                    searchEntity.setLabelId(labelVideo);
                    searchEntity.setRoomId(null);
                    searchEntity.setProgramId(null);
                    searchEntity.setVideoUserId(null);
                    HttpData.getInstance().HttpDataGetVideos(new Observer<HttpResult3<VideoListEntity, Object>>() {
                        @Override
                        public void onCompleted() {

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
                        public void onNext(HttpResult3<VideoListEntity, Object> videoListEntityObjectHttpResult3) {
                            if (!videoListEntityObjectHttpResult3.getStatus().equals("success")) {
                                ToastUtils.show(LiveSearchActivity.this, videoListEntityObjectHttpResult3.getMsg());
                                return;
                            }
                            mQuickAdapter2.setNewData(videoListEntityObjectHttpResult3.getData());
                            mQuickAdapter2.setOnRecyclerViewItemClickListener((view, position) -> {
                                Intent i = new Intent(LiveSearchActivity.this, VideoDetailActivity.class);
                                i.putExtra("videoId", videoListEntityObjectHttpResult3.getData().get(position).getVideoId());
                                startActivity(i);
                            });
                        }
                    }, searchEntity);
                }

                break;
        }
    }

    private void initTagsView() {
        tagsSwipeRefreshLyt.setColorSchemeResources(R.color.colorAccent);
        tagsSwipeRefreshLyt.setOnRefreshListener(() -> {
            tagsSwipeRefreshLyt.setRefreshing(false);
            testSingleTagsAdapter();
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
        mBaseQuickAdapter.setOnRecyclerViewItemChildClickListener((adapter, view, position) -> {
            TagDto tagDto = (TagDto) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.name:
                    searchEt.setHint("");
                    searchEt.setEnabled(false);
                    searchTagLlyt.setVisibility(View.VISIBLE);
                    searchTagTv.setText(tagDto.getLabelName());
                    searchTagIv.setOnClickListener(view1 -> {
                        searchTagLlyt.setVisibility(View.GONE);
                        tagsSwipeRefreshLyt.setVisibility(View.VISIBLE);
                        searchEt.setHint("请输入主播名或选择某一直播分类");
                        searchEt.setEnabled(true);
                    });
                    tagsSwipeRefreshLyt.setVisibility(View.GONE);

                    liveSearchDto.setKeyword("");
                    liveSearchDto.setRoomNumber("");
                    labelIds.clear();
                    labelIds.add(tagDto.getId());
                    liveSearchDto.setLabelIds(labelIds);
//                    present.LoadData(false, liveSearchDto);

                    labelVideo = tagDto.getId();

                    searchEt.setText("");
                    break;
            }
        });
    }

    private void initVideoView() {
        mRecyclerView2 = (RecyclerView) findViewById(R.id.rv_list2);
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView2.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView2.setHasFixedSize(true);
        //设置页面为加载中..
//        progress.showLoading();
        //设置适配器
        mQuickAdapter2 = new VideoAdapter(R.layout.item_live, null);
        //设置加载动画
        mQuickAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
//        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView2.setAdapter(mQuickAdapter2);
    }
}
