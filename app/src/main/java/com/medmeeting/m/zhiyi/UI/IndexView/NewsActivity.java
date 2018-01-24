package com.medmeeting.m.zhiyi.UI.IndexView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.medmeeting.m.zhiyi.BuildConfig;
import com.medmeeting.m.zhiyi.Constant.Constant;
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
import com.medmeeting.m.zhiyi.Widget.LoadingFlashView;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.LinkMovementMethodExt;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.MessageSpan;
import com.medmeeting.m.zhiyi.Widget.TextVIewHtmlImage.TextViewHtmlImageGetter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.BrowseEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
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
    @Bind(R.id.loadingView)
    LoadingFlashView loadingView;
    @Bind(R.id.WebView)
    BridgeWebView mWebView;

    @Bind(R.id.input_editor)
    EditText inputEditor;
    @Bind(R.id.input_send)
    Button inputSend;
    private RecyclerView mCommandRecyclerView;
    private BaseQuickAdapter mCommandAdapter;
    private View mCommandFooterView;

    private int blogId;
    private String blogTitle;
    private boolean collectionType;

    @Bind(R.id.label_rv_list)
    RecyclerView mLabelRecyclerView;
    private BaseQuickAdapter mLabelAdapter;

    //统计浏览该页面时长
    private long startTime;
    private long endTime;

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


        //设置RecyclerView的显示模式  当前List模式
//        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
//        mLabelRecyclerView.setLayoutManager(flowLayoutManager);
        mLabelRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mLabelRecyclerView.setHasFixedSize(true);
        mLabelAdapter = new NewsLabelAdapter(R.layout.item_news_label_tag, null);
        mLabelRecyclerView.setAdapter(mLabelAdapter);
        getBlogDetailService(blogId);


        mCommandRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mCommandAdapter = new BlogCommentAdapter(R.layout.item_video_command, null);
        mCommandFooterView = LayoutInflater.from(NewsActivity.this).inflate(R.layout.item_blog_footer, null);
        getCommentService(blogId);

        startTime = System.nanoTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        endTime = System.nanoTime();
        Log.e(getLocalClassName(), endTime + " " + startTime);
        Log.e(getLocalClassName(), (endTime - startTime) + "毫微秒");

        //极光统计  浏览事件
        blogId = getIntent().getIntExtra("blogId", 0);
        BrowseEvent bEvent = new BrowseEvent(blogId + "", blogTitle, "新闻", (endTime - startTime)/1000000000);
        JAnalyticsInterface.onEvent(this, bEvent);

        super.onStop();
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
                initView(data.getEntity());
                collectionType = data.getEntity().isCollectionType();
                invalidateOptionsMenu(); //重新绘制menu
            }
        }, map);
    }


    /**
     * @param blogDetail
     */
    private void initView(Blog blogDetail) {
        //刚打开页面的瞬间显示
        blogTitle = blogDetail.getTitle();
        title.setText(blogTitle);
        //微博内容
        time.setText(DateUtils.formatDate(blogDetail.getPushDate(), DateUtils.TYPE_06));

        String author = blogDetail.getAuthorOrg();
        if (!blogDetail.getAuthorName().equals("")) {
            author += " 文/" + blogDetail.getAuthorName();
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

        loadingView.showLoading();
        mWebView.setVisibility(View.GONE);
        initWebView();

        //标签
        if (blogDetail.getLabelName() != null) {
            mLabelRecyclerView.setVisibility(View.VISIBLE);
            //标签提前转成list
            Log.e(getLocalClassName(), Arrays.asList(blogDetail.getLabelName().split(",")).get(0));
            mLabelAdapter.setNewData(Arrays.asList(blogDetail.getLabelName().split(",")));
        } else {
            mLabelRecyclerView.setVisibility(View.GONE);
        }


        //正则表达式,过滤新闻内容
        String regEx = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签    JS用"/<[^>]*>/g";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(blogDetail.getContent());

        Pattern p2 = Pattern.compile("&([a-zA-Z])+;");  //去掉中间有&nbsp;
        Matcher m2 = p2.matcher(m.replaceAll("").trim());

        String shareContent = m2.replaceAll("").trim();

        Log.e(getLocalClassName(), shareContent.replaceAll("\\s*", "") + "");//去掉中间大段空格
        //分享
        initShare(blogDetail.getId(), blogDetail.getTitle(), blogDetail.getImages(), shareContent.replaceAll("\\s*", ""));
    }

    private void getCommentService(int blogId) {
        //recyclerview禁止滑动
//        mCommandRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        });
        //分割线
//        mCommandRecyclerView.addItemDecoration(new DividerItemDecoration(NewsActivity.this, DividerItemDecoration.VERTICAL));
        //设置RecyclerView的布局管理器
        mCommandRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        //如果Item高度固定  增加该属性能够提高效率
        mCommandRecyclerView.setHasFixedSize(true);
        mCommandRecyclerView.setAdapter(mCommandAdapter);

        Map<String, Object> map = new HashMap<>();
        map.put("blogId", blogId);
        map.put("pageNum", 1);
        map.put("pageSize", 1000);
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
                mCommandAdapter.setNewData(data.getData());

                mCommandAdapter.addFooterView(mCommandFooterView);
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

        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
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

                    UMWeb web = new UMWeb(Constant.Share_News_Article + blogId);
                    web.setTitle(title);//标题

                    if (photo != null) {
                        if (photo.contains(",")) {
                            web.setThumb(new UMImage(NewsActivity.this, photo.split(",")[0]));  //缩略图
                        } else {
                            web.setThumb(new UMImage(NewsActivity.this, photo));  //缩略图
                        }
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


    private String TAG = NewsActivity.class.getName();

    /**
     * 初始化WebView配置
     */
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        // BindUserInfo settings
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
        Log.e(NewsActivity.this.getLocalClassName(), URL);

        mWebView.addJavascriptInterface(new JSHook(), "SetAndroidJavaScriptBridge");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(android.webkit.WebView view, String title) {
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
            ToastUtils.show(NewsActivity.this, "JSHook.JavaMethod() called! + " + p);
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

            Log.e(NewsActivity.this.getLocalClassName(), height);

            NewsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ViewGroup.LayoutParams params1 = mWebView.getLayoutParams();
//                    params1.height = Integer.parseInt(height) * 3;
//                    frameLayout.setLayoutParams(params1);


                    //为ViewPager设置高度
                    ViewGroup.LayoutParams params = mWebView.getLayoutParams();

                    params.height = Integer.parseInt(height) * 3;//this.getResources().getDisplayMetrics().heightPixels

//                    if (params.height == 4839) params.height = params.height + 4839;
                    mWebView.setLayoutParams(params);


                    Log.e(NewsActivity.this.getLocalClassName(), "webview " + mWebView.getLayoutParams().height + " ");
                }
            });
        }

        @JavascriptInterface
        public void isFinished(final String string) {
            try {
                // 解析js传递过来的json串
                JSONObject mJson = new JSONObject(string);
                if (mJson.optString("isFinished").equals("true")) {
                    NewsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.hideLoading();
                            loadingView.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                            Log.e(NewsActivity.this.getLocalClassName(), "Finished");
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(NewsActivity.this.getLocalClassName(), height);

        }


        @JavascriptInterface
        public void printWebLog(String str) {
            Log.e(TAG + " WebView: ", str);
        }

        public String getInfo() {
            return "获取手机内的信息！！";
        }
    }

}
