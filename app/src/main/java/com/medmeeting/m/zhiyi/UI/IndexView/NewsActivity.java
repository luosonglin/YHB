package com.medmeeting.m.zhiyi.UI.IndexView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogCommentAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.BlogComment;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.TextViewHtmlImageGetter;
import com.medmeeting.m.zhiyi.Widget.weiboGridView.weiboGridView;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.net.URL;
import java.util.ArrayList;
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

    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            Log.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Log.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            Log.i("RG", "url---?>>>" + url);
            return drawable;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        initToolbar();

        blogId = getIntent().getIntExtra("blogId", 0);
        getBlogDetailService(blogId);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
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
                initView(data.getEntity());
                collectionType = data.getEntity().isCollectionType();
                invalidateOptionsMenu(); //重新绘制menu
            }
        }, map);
    }

    private void initView(Blog blogDetail) {
        //刚打开页面的瞬间显示
        title.setText(blogDetail.getTitle());
        //微博内容
        name.setText(blogDetail.getAuthorName());
        time.setText(DateUtils.formatDate(blogDetail.getPushDate(), DateUtils.TYPE_10));

        //文章View需要写带html标签的文本
//        content.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
//        content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
//        content.setText(Html.fromHtml(blogDetail.getContent(), imgGetter, null));
        content.setText(Html.fromHtml(blogDetail.getContent(), new TextViewHtmlImageGetter(getApplicationContext(), content), null));



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
                    ToastUtils.show(NewsActivity.this, "share");
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

}

