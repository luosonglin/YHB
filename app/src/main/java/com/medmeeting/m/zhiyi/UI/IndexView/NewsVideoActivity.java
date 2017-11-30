package com.medmeeting.m.zhiyi.UI.IndexView;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.MVP.Listener.SampleListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.BlogVideoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.DownloadImageTaskUtil;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.videoplayer.LandLayoutVideoPlayer;
import com.medmeeting.m.zhiyi.Widget.weibogridview.weiboGridView;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.blog_image)
    weiboGridView blogImage;
    @Bind(R.id.rv_list)
    RecyclerView rvList;

    @Bind(R.id.video_image)
    ImageView videoImage;
    @Bind(R.id.video_name)
    TextView videoName;
    @Bind(R.id.video_sum)
    TextView videoSum;
    @Bind(R.id.video_time)
    TextView videoTime;
    @Bind(R.id.video_source_llyt)
    LinearLayout videoSourceLlyt;


    NestedScrollView postDetailNestedScroll;
    LandLayoutVideoPlayer detailPlayer;
    RelativeLayout activityDetailPlayer;
    private boolean isPlay;
    private boolean isPause;
    private OrientationUtils orientationUtils;


    private Integer blogId;

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

                    UMWeb web = new UMWeb("http://wap.medmeeting.com/#!/video/" + programId);
                    web.setTitle(title);//标题
                    web.setThumb(new UMImage(NewsVideoActivity.this, photo));  //缩略图
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

                initShare(data.getEntity().getBlog().getId(), data.getEntity().getBlog().getTitle(), data.getEntity().getBlog().getImages(), data.getEntity().getBlog().getAuthorName());
            }
        }, map);
    }

    private void initBlogView(Blog blogDetail) {
        //刚打开页面的瞬间显示
        title.setText(blogDetail.getTitle());
        //微博内容
        name.setText(blogDetail.getAuthorName());
        time.setText(DateUtils.formatDate(blogDetail.getPushDate(), DateUtils.TYPE_10));

        content.setText(blogDetail.getContent());

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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(videoImage);
        videoName.setText(videoDetailsEntity.getTitle());
        videoSum.setText("收藏" + videoDetailsEntity.getCollectCount());
        videoTime.setText(DateUtils.formatDate(videoDetailsEntity.getCreateTime(), DateUtils.TYPE_06));
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
                    .setLockClickListener(new LockClickListener() {
                        @Override
                        public void onClick(View view, boolean lock) {
                            if (orientationUtils != null) {
                                //配合下方的onConfigurationChanged
                                orientationUtils.setEnable(!lock);
                            }
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
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
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


}