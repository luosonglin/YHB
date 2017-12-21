package com.medmeeting.m.zhiyi.UI.IndexView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.library.flowlayout.FlowLayoutManager;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.CustomShareListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogCommentAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.NewsLabelAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.BlogComment;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.LinkMovementMethodExt;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.MessageSpan;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.TextViewHtmlImageGetter;
import com.medmeeting.m.zhiyi.Widget.weiboGridView.weiboGridView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class NewsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.blog_image)
    weiboGridView blogImage;

    @Bind(R.id.input_editor)
    EditText inputEditor;
    @Bind(R.id.input_send)
    Button inputSend;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private View mFooterView;

    private int blogId;
    private boolean collectionType;

    @Bind(R.id.label_rv_list)
    RecyclerView mLabelRecyclerView;
    private BaseQuickAdapter mLabelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

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

        initToolbar();

        blogId = getIntent().getIntExtra("blogId", 0);
//        mLabelRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) {
//                                                @Override
//                                                public boolean canScrollVertically() {
//                                                    return false;
//                                                }
//                                            }
//        );

        //设置RecyclerView的显示模式  当前List模式
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        mLabelRecyclerView.setLayoutManager(flowLayoutManager);
//        mLabelRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        mLabelRecyclerView.setHasFixedSize(true);
        mLabelAdapter = new NewsLabelAdapter(R.layout.item_news_label_tag, null);
        mLabelRecyclerView.setAdapter(mLabelAdapter);
        getBlogDetailService(blogId);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //recyclerview禁止滑动
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getBlogDetailService(Integer blogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("blogId", blogId);
        HttpData.getInstance().HttpDataGetPicNews(new Observer<HttpResult3<Object, Blog>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(NewsActivity.this, e.getMessage());
                finish();
            }

            @Override
            public void onNext(HttpResult3<Object, Blog> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(NewsActivity.this, data.getMsg());
                    return;
                }
                initView(data.getEntity(), Arrays.asList(data.getEntity().getLabelName().split(",")));
                collectionType = data.getEntity().isCollectionType();
                invalidateOptionsMenu(); //重新绘制menu
            }
        }, map);
    }


    /**
     * @param blogDetail
     * @param mLabels    标签提前转成list
     */
    private void initView(Blog blogDetail, List<String> mLabels) {
        //刚打开页面的瞬间显示
        title.setText(blogDetail.getTitle());
        //微博内容
        time.setText(DateUtils.formatDate(blogDetail.getPushDate(), DateUtils.TYPE_06));

        String author = blogDetail.getAuthorOrg();
        if (blogDetail.getAuthorName() != null) {
            author +=  " 文/" + blogDetail.getAuthorName();
        }
        name.setText(author);

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

        //标签
        mLabelAdapter.setNewData(mLabels);


        //分享
        initShare(blogDetail.getId(), blogDetail.getTitle(), blogDetail.getImages(), blogDetail.getContent());


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
                ToastUtils.show(NewsActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<BlogComment, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(NewsActivity.this, data.getMsg());
                    return;
                }
                if (mAdapter.getData() != null) {
                    mAdapter.setNewData(data.getData());
                } else {
                    mAdapter.addData(data.getData());
                }

                mFooterView = LayoutInflater.from(NewsActivity.this).inflate(R.layout.item_blog_footer, null);
                mAdapter.addFooterView(mFooterView);
            }
        }, map);
    }

    @OnClick(R.id.input_send)
    public void onClick() {
        if (inputEditor.getText().toString().trim().equals("")) {
            ToastUtils.show(NewsActivity.this, "不能发空评论");
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
                ToastUtils.show(NewsActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(NewsActivity.this, data.getMsg());
                    return;
                }
                getCommentService(blogId);
                inputEditor.setText("");
            }
        }, blogComment);
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ShareBoardConfig config = new ShareBoardConfig();
                    config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                    mShareAction.open(config);
                    break;
                case R.id.action_collect:
                    collectService(true);
                    break;
                case R.id.action_collect_no:
                    collectService(false);
                    break;
            }
            return true;
        });
    }

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blog_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (collectionType) {
            menu.findItem(R.id.action_collect).setVisible(true);
            menu.findItem(R.id.action_collect_no).setVisible(false);
        } else {
            menu.findItem(R.id.action_collect).setVisible(false);
            menu.findItem(R.id.action_collect_no).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 收藏API
     *
     * @param oldCollected
     */
    private void collectService(boolean oldCollected) {
        UserCollect userCollect = new UserCollect();
        userCollect.setServiceId(blogId);
        userCollect.setServiceType("BLOG");
        HttpData.getInstance().HttpDataCollect(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(NewsActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(NewsActivity.this, httpResult3.getMsg());
                    return;
                }
                if (oldCollected) {     //老状态是 已收藏
                    ToastUtils.show(NewsActivity.this, "取消收藏");
                    collectionType = false;
                    invalidateOptionsMenu(); //重新绘制menu
                } else {
                    ToastUtils.show(NewsActivity.this, "收藏成功");
                    collectionType = true;
                    invalidateOptionsMenu(); //重新绘制menu
                }
            }
        }, userCollect);
    }

    /**
     * 分享
     *
     * @param blogId
     * @param title
     * @param photo
     * @param description
     */
    public void initShare(final int blogId, final String title, final String photo, final String description) {

        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(NewsActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback((snsPlatform, share_media) -> {

                    UMWeb web = new UMWeb("http://mobile.medmeeting.com/#/new/article/share/" + blogId);
                    web.setTitle(title);//标题
                    if (photo != null) {
                        web.setThumb(new UMImage(NewsActivity.this, photo));  //缩略图
                    } else {
                        web.setThumb(new UMImage(NewsActivity.this, R.mipmap.news_bg));
                    }
                    web.setDescription(description);//描述
                    new ShareAction(NewsActivity.this)
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
}

