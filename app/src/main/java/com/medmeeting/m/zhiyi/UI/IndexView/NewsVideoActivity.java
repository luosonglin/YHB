package com.medmeeting.m.zhiyi.UI.IndexView;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.BuildConfig;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.MVP.Listener.SampleListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogCommentAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.BlogComment;
import com.medmeeting.m.zhiyi.UI.Entity.BlogVideoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.UI.IdentityView.ActivateActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.DownloadImageTaskUtil;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.LinkMovementMethodExt;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.MessageSpan;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.TextViewHtmlImageGetter;
import com.medmeeting.m.zhiyi.Widget.videoplayer.LandLayoutVideoPlayer;
import com.medmeeting.m.zhiyi.Widget.weiboGridView.weiboGridView;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.snappydb.SnappydbException;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.BrowseEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import rx.Observer;

import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_NORMAL;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING_BUFFERING_START;
import static com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PREPAREING;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 6:12 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsVideoActivity extends AppCompatActivity {

    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.WebView)
    BridgeWebView mWebView;
    @BindView(R.id.blog_image)
    weiboGridView blogImage;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @BindView(R.id.video_image)
    ImageView videoImage;
    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_sum)
    TextView videoSum;
    @BindView(R.id.video_time)
    TextView videoTime;
    @BindView(R.id.video_source_llyt)
    LinearLayout videoSourceLlyt;
    @BindView(R.id.video_source_rlyt)
    RelativeLayout videoSourceRlyt;

    @BindView(R.id.collect)
    ImageView collect;


    NestedScrollView postDetailNestedScroll;
    LandLayoutVideoPlayer detailPlayer;
    RelativeLayout activityDetailPlayer;
    private boolean isPlay;
    private boolean isPause;
    private OrientationUtils orientationUtils;

    @BindView(R.id.input_editor)
    EditText inputEditor;
    @BindView(R.id.input_send)
    Button inputSend;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private View mFooterView;

    private Integer blogId;
    private String blogTitle;

    private boolean isCollect;

    //统计浏览该页面时长
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_video);
        ButterKnife.bind(this);

        postDetailNestedScroll = (NestedScrollView) findViewById(R.id.post_detail_nested_scroll);
        detailPlayer = (LandLayoutVideoPlayer) findViewById(R.id.detail_player);
        activityDetailPlayer = (RelativeLayout) findViewById(R.id.activity_detail_player);

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

        blogId = getIntent().getIntExtra("blogId", 0);
        initView(blogId);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //recyclerview禁止滑动
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsVideoActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BlogCommentAdapter(R.layout.item_video_command, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getCommentService(blogId);

        startTime = System.currentTimeMillis();
    }

    /**
     * 分享
     *
     * @param programId
     * @param title
     * @param photo
     * @param description
     */
    public void initShare(final int programId, final String title, final String photo, final String description) {

        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(NewsVideoActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {

                    UMWeb web = new UMWeb(Constant.Share_News_Video + programId);
                    web.setTitle(title);//标题
                    if (photo != null) {
                        web.setThumb(new UMImage(NewsVideoActivity.this, photo));  //缩略图
                    } else {
                        web.setThumb(new UMImage(NewsVideoActivity.this, R.mipmap.video_bg));
                    }
                    web.setDescription(description);//描述
                    new ShareAction(NewsVideoActivity.this)
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

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //qq微信新浪授权防杀死
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    private void initView(Integer blogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("blogId", blogId);
        HttpData.getInstance().HttpDataGetVideoNews(new Observer<HttpResult3<Object, BlogVideoEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(NewsVideoActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, BlogVideoEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(NewsVideoActivity.this, data.getMsg());
                    return;
                }
                initBlogView(data.getEntity().getBlog());

                initSourceVideoView(data.getEntity().getVideoDetailsEntity());

                initPlayer(data.getEntity().getBlog().getVideoUrl(), data.getEntity().getBlog().getImages(), data.getEntity().getBlog().getTitle());

                //正则表达式,过滤新闻内容
                String regEx = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签    JS用"/<[^>]*>/g";  iOS用"<[^>]*>|\n"
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(data.getEntity().getBlog().getContent());

                Pattern p2 = Pattern.compile("&([a-zA-Z])+;");
                Matcher m2 = p2.matcher(m.replaceAll("").trim());

                //分享
                initShare(data.getEntity().getBlog().getId(), data.getEntity().getBlog().getTitle(), data.getEntity().getBlog().getImages(), m2.replaceAll("").trim());
            }
        }, map);
    }

    private void initBlogView(Blog blogDetail) {
        //刚打开页面的瞬间显示
        blogTitle = blogDetail.getTitle();
        titleTv.setText(blogTitle);
        //微博内容
//        String author = ;
//        if (!blogDetail.getAuthorName().equals("")) {
//            author +=  " 文/" + blogDetail.getAuthorName();
//        }
        name.setText(DateUtils.formatDate(blogDetail.getPushDate(), DateUtils.TYPE_10));
        time.setText(blogDetail.getReadNum()+"次播放");

        isCollect = blogDetail.isCollectionType();
        if (isCollect) {
            Glide.with(NewsVideoActivity.this)
                    .load(R.mipmap.meeting_collect)
                    .dontAnimate()
                    .into(collect);
        } else {
            Glide.with(NewsVideoActivity.this)
                    .load(R.mipmap.meeting_collect_no)
                    .dontAnimate()
                    .into(collect);
        }


        //文章View需要写带html标签的文本
        content.setText(Html.fromHtml(blogDetail.getContent(), new TextViewHtmlImageGetter(getApplicationContext(), content), null));
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                int what = msg.what;
                if (what == 200) {
                    MessageSpan ms = (MessageSpan) msg.obj;
                    Object[] spans = (Object[]) ms.getObj();
                    final ArrayList<String> list = new ArrayList<>();
                    for (Object span : spans) {
                        if (span instanceof ImageSpan) {
                            Log.i("picUrl==", ((ImageSpan) span).getSource());
                            list.add(((ImageSpan) span).getSource());
                            Intent intent = new Intent(getApplicationContext(), ImageGalleryActivity.class);
                            intent.putStringArrayListExtra("images", list);
                            startActivity(intent);
                        }
                    }
                }
            }
        };
        content.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));

//        initWebView();

        //九图
        if (blogDetail.getImages() == null) {
            blogImage.setVisibility(View.GONE);
            return;
        }
        blogImage.setVisibility(View.VISIBLE);

        String blogImages = blogDetail.getImages();
        if (blogImages != null) {
            List<String> images = new ArrayList<>();
            for (String i : blogImages.split(",")) {
                images.add(i);
            }
            blogImage.render(images);
        } else {
            blogImage.setVisibility(View.GONE);
        }
    }

    private void initSourceVideoView(VideoDetailsEntity videoDetailsEntity) {
        Glide.with(NewsVideoActivity.this)
                .load(videoDetailsEntity.getCoverPhoto())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .crossFade()
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .into(videoImage);
        videoName.setText(videoDetailsEntity.getTitle());
        videoSum.setText("收藏" + videoDetailsEntity.getCollectCount());
        videoTime.setText(DateUtils.formatDate(videoDetailsEntity.getCreateTime(), DateUtils.TYPE_06));

        videoSourceRlyt.setOnClickListener(view -> {
            Intent intent = new Intent(NewsVideoActivity.this, VideoDetailActivity.class);
            intent.putExtra("videoId", videoDetailsEntity.getVideoId());
            startActivity(intent);
        });
    }

    private void getCommentService(int blogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("blogId", blogId);
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        HttpData.getInstance().HttpDataGetNewsCommentList(new Observer<HttpResult3<BlogComment, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(NewsVideoActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<BlogComment, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(NewsVideoActivity.this, data.getMsg());
                    return;
                }
                if (mAdapter.getData() != null) {
                    mAdapter.setNewData(data.getData());
                } else {
                    mAdapter.addData(data.getData());
                }

                mFooterView = LayoutInflater.from(NewsVideoActivity.this).inflate(R.layout.item_blog_footer, null);
                mAdapter.addFooterView(mFooterView);
            }
        }, map);
    }

    private String tocPortStatus;

    @OnClick({R.id.input_send, R.id.collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_send:
                try {
                    tocPortStatus = DBUtils.get(NewsVideoActivity.this, "tocPortStatus");
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
                if (tocPortStatus == null || tocPortStatus.equals("wait_activation")) {
                    startActivity(new Intent(NewsVideoActivity.this, ActivateActivity.class));
                    return;
                }


                if (inputEditor.getText().toString().trim().equals("")) {
                    ToastUtils.show(NewsVideoActivity.this, "不能发空评论");
                    return;
                }
                BlogComment blogComment = new BlogComment();
                blogComment.setBlogId(blogId);
                blogComment.setContent(inputEditor.getText().toString().trim());
                HttpData.getInstance().HttpDataInsertComment(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(NewsVideoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(NewsVideoActivity.this, data.getMsg());
                            return;
                        }
                        getCommentService(blogId);
                        inputEditor.setText("");
                    }
                }, blogComment);
                break;
            case R.id.collect:
                if (isCollect)
                    collectService(true);
                else
                    collectService(false);
                break;
        }
    }

    private void initPlayer(String url, String photo, String title) {
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        if (url != null)
            gsyVideoOption//.setThumbImageView(imageView)
                    .setThumbPlay(false)    //是否点击封面可以播放
                    .setIsTouchWiget(true)  //是否可以滑动界面改变进度，声音等
                    .setRotateViewAuto(false)   //是否开启自动旋转
                    .setLockLand(false) //一全屏就锁屏横屏，默认false竖屏，可配合setRotateViewAuto使用
                    .setShowFullAnimation(false)    //是否使用全屏动画效果
                    .setNeedLockFull(true)  //是否需要全屏锁定屏幕功能
                    .setSeekRatio(1)    //调整触摸滑动快进的比例
                    .setUrl(url)//"http://baobab.wdjcdn.com/1451897812703c.mp4"
                    .setCacheWithPlay(false)
                    .setVideoTitle(title)
                    .setStandardVideoAllCallBack(new SampleListener() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            Debuger.printfError("***** onPrepared **** " + objects[0]);
                            Debuger.printfError("***** onPrepared **** " + objects[1]);
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            super.onAutoComplete(url, objects);
                        }

                        @Override
                        public void onClickStartError(String url, Object... objects) {
                            super.onClickStartError(url, objects);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                        }


                        @Override
                        public void onClickStartIcon(String url, Object... objects) {
                            super.onClickStartIcon(url, objects);
                        }
                    })
                    .setLockClickListener((view, lock) -> {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    })
                    .build(detailPlayer);


        //增加封面
        if (photo != null)
            new DownloadImageTaskUtil(detailPlayer.getCoverPhoto()).execute(photo);

        if (detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING) {
            detailPlayer.getCoverPhoto().setVisibility(View.GONE);
        } else {
            detailPlayer.getCoverPhoto().setVisibility(View.VISIBLE);
        }


        if (detailPlayer.getFullscreenButton() != null) {
            detailPlayer.getFullscreenButton().setOnClickListener(v -> {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(NewsVideoActivity.this, true, true);
            });
        }

        //隐藏购买按钮
        detailPlayer.getStartButton().setVisibility(View.VISIBLE);
        detailPlayer.getBuyButton().setVisibility(View.GONE);

        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getTitleTextView().setText("           ");
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(view -> finish());
        detailPlayer.getShareButton().setOnClickListener(view -> {
            ShareBoardConfig config = new ShareBoardConfig();
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
            mShareAction.open(config);
        });

        if (detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING
                || detailPlayer.getCurrentState() == CURRENT_STATE_PLAYING_BUFFERING_START
                || detailPlayer.getCurrentState() == CURRENT_STATE_NORMAL
                || detailPlayer.getCurrentState() == CURRENT_STATE_PREPAREING) {
            HttpData.getInstance().HttpDataEditPlayCount(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3 httpResult3) {

                }
            }, blogId);
        }

    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onStop() {
        endTime = System.currentTimeMillis();
        Log.e(getLocalClassName(), endTime + " " + startTime);
        Log.e(getLocalClassName(), (endTime - startTime) + "毫秒");

        //极光统计  浏览事件
        BrowseEvent bEvent = new BrowseEvent(blogId + "", blogTitle, "视频新闻", (endTime - startTime)/1000000000);
        JAnalyticsInterface.onEvent(this, bEvent);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    private String TAG = NewsActivity.class.getName();

    /**
     * 初始化WebView配置
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        // BindViewUserInfo settings
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);    //设置webview支持javascript
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true); //和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setSavePassword(true);    //设置webview保存密码
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //支持缩放

        // Technical settings
        settings.setAppCacheEnabled(false);  //是否使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);    //DOM Storage

        settings.setSupportMultipleWindows(true);
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);

        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }

        String userAgent = mWebView.getSettings().getUserAgentString() + "; yihuibao_a Version/" + BuildConfig.VERSION_NAME;
        settings.setUserAgentString(userAgent);//设置用户代理

        //哈哈哈哈哈哈哈
//        if (getIntent().getStringExtra("sourceType") == null) {
//            ToastUtils.show(MeetingDetailActivity.this, "该会议没有sourceType字段，找文戈！");
//            finish();
//            return;
//        }
        String URL = Constant.URL_BLOG_CONTENT + blogId;
        mWebView.loadUrl(URL);
        Log.e(NewsVideoActivity.this.getLocalClassName(), URL);

        mWebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d(TAG, "－－－－－－setWebChromeClient ");
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候WebView打开，为false则系统浏览器或第三方浏览器打开。
                //如果要下载页面中的游戏或者继续点击网页中的链接进入下一个网页的话，重写此方法下，不然就会跳到手机自带的浏览器了，而不继续在你这个webview里面展现了
//                return super.shouldOverrideUrlLoading(view, url);
                Log.d(TAG, " url:" + url);
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //想在页面开始加载时有操作，在这添加
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //想在页面加载结束时有操作，在这添加
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //想在收到错误信息的时候，执行一些操作，走此方法
                super.onReceivedError(view, request, error);
            }

        });
    }

    public Integer mHeight = 0;

    public class JSHook {
        @JavascriptInterface
        public void javaMethod(String p) {
            Log.e(TAG, "JSHook.JavaMethod() called! + " + p);
            ToastUtils.show(NewsVideoActivity.this, "JSHook.JavaMethod() called! + " + p);
        }

        public String height;

        @JavascriptInterface
        public void getHeight(final String string) {
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(string);
                height = mJson.optString("height");
                mHeight = Integer.parseInt(height);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(NewsVideoActivity.this.getLocalClassName(), height);

            NewsVideoActivity.this.runOnUiThread(() -> {
                //为ViewPager设置高度
                ViewGroup.LayoutParams params = mWebView.getLayoutParams();

                params.height = Integer.parseInt(height) * 3;//this.getResources().getDisplayMetrics().heightPixels

//                    if (params.height == 4839) params.height = params.height + 4839;
                mWebView.setLayoutParams(params);

                Log.e(NewsVideoActivity.this.getLocalClassName(), "webview " + mWebView.getLayoutParams().height + " ");
            });
        }

        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(TAG + " WebView: ", str);
        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }


    /**
     * 收藏API
     *
     * @param oldCollected
     */
    private void collectService(boolean oldCollected) {
        try {
            tocPortStatus = DBUtils.get(NewsVideoActivity.this, "tocPortStatus");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        if (tocPortStatus == null || tocPortStatus.equals("wait_activation")) {
            startActivity(new Intent(NewsVideoActivity.this, ActivateActivity.class));
            return;
        }

        UserCollect userCollect = new UserCollect();
        userCollect.setServiceId(blogId);
        userCollect.setServiceType("BLOG");
        HttpData.getInstance().HttpDataCollect(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(NewsVideoActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(NewsVideoActivity.this, httpResult3.getMsg());
                    return;
                }
                if (oldCollected) {     //老状态是 已收藏
                    ToastUtils.show(NewsVideoActivity.this, "取消收藏");
                    isCollect = false;
                    Glide.with(NewsVideoActivity.this)
                            .load(R.mipmap.meeting_collect_no)
                            .crossFade()
                            .dontAnimate()
                            .into(collect);
                } else {
                    ToastUtils.show(NewsVideoActivity.this, "收藏成功");
                    isCollect = true;
                    Glide.with(NewsVideoActivity.this)
                            .load(R.mipmap.meeting_collect)
                            .crossFade()
                            .dontAnimate()
                            .into(collect);
                }
            }
        }, userCollect);
    }

}
