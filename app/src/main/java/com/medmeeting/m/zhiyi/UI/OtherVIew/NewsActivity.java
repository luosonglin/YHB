package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    @Bind(R.id.content_wei_bo)
    RelativeLayout contentWeiBo;
    @Bind(R.id.blog_image)
    com.medmeeting.m.zhiyi.Widget.weibogridview.weiboGridView blogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(view -> finish());

        getBlogDetailService(getIntent().getIntExtra("blogId", 0));
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
            }
        }, map);
    }

    private void initView(Blog blogDetail) {
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


}

