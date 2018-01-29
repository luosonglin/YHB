package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MVP.Presenter.MyLiveProgramListPresent;
import com.medmeeting.m.zhiyi.MVP.View.LiveListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyLiveProgramAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 直播间详情页
 */
public class MyLiveRoomDetailActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnFreshListener, LiveListView {

    private static final String TAG = MyLiveRoomDetailActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    ProgressActivity progress;
    private Toolbar toolbar;
    private TextView roomIdTv, pageName, titleTv, addTv;
    private ImageView backgroundIv;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private SpringView springView;
    private MyLiveProgramListPresent present;
    private Integer roomId = 0;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_my_program_detail);

        //qq微信新浪授权防杀死, 在onCreate中再设置一次回调
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        });
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
        pageName = (TextView) findViewById(R.id.page_name);
        pageName.setText(getIntent().getExtras().getString("title"));
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getIntent().getExtras().getString("title"));

        roomId = getIntent().getExtras().getInt("roomId");
        roomIdTv = (TextView) findViewById(R.id.room_id);
        roomIdTv.setText("房间No." + getIntent().getExtras().getString("number"));

        addTv = (TextView) findViewById(R.id.add);
        addTv.setOnClickListener(view -> {
            Intent intent = new Intent(MyLiveRoomDetailActivity.this, LiveBuildProgramActivity.class);
            intent.putExtra("roomId", roomId);
            startActivity(intent);
        });
        backgroundIv = (ImageView) findViewById(R.id.img);
        Glide.with(MyLiveRoomDetailActivity.this)
                .load(getIntent().getExtras().getString("coverPhoto"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
//                .placeholder(R.mipmap.ic_launcher)
                .into(backgroundIv);

        //分享
        findViewById(R.id.invitation_letter).setOnClickListener(view -> {
            initShare(getIntent().getExtras().getInt("roomId"),
                    getIntent().getExtras().getString("title"),
                    getIntent().getExtras().getString("coverPhoto"),
                    "欢迎观看" + getIntent().getExtras().getString("title"));

            ShareBoardConfig config = new ShareBoardConfig();
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
            mShareAction.open(config);
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
        mQuickAdapter = new MyLiveProgramAdapter(R.layout.item_live_program, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
//        present = new BookListPresent(this);
        present = new MyLiveProgramListPresent(this);
        //请求网络数据
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, roomId);
    }

    private void initListener() {
        //设置自动加载监听
        mQuickAdapter.setOnLoadMoreListener(this);
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        present.LoadData(false, roomId);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex = 1;
//        present.LoadData(false, roomId);
        present.LoadData(false, roomId);
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
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, v -> {
            PageIndex = 1;
//                present.LoadData("1",PageIndex,false);
            present.LoadData(false, roomId);
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
        Log.e(TAG, "no data");
        springView.onFinishFreshAndLoad();
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE, Constant.EMPTY_CONTEXT);
    }

    public void initShare(final int roomId, final String title, final String photo, final String description) {
        mShareListener = new MyLiveRoomDetailActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(MyLiveRoomDetailActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {
                    UMWeb web = new UMWeb(Constant.Share_Live_Room + roomId);
                    web.setTitle(title);//标题
                    web.setThumb(new UMImage(MyLiveRoomDetailActivity.this, photo));  //缩略图
                    web.setDescription(description);//描述
                    new ShareAction(MyLiveRoomDetailActivity.this)
                            .withMedia(web)
                            .setPlatform(share_media)
                            .setCallback(mShareListener)
                            .share();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<MyLiveRoomDetailActivity> mActivity;

        private CustomShareListener(MyLiveRoomDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(mActivity.get(), platform + "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.e(TAG, "umeng throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //qq微信新浪授权防杀死
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
