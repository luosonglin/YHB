package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.Util.DateUtil;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.pic)
    CircleImageView avatar;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        String blogId = getIntent().getExtras().getString("blogId");
        Serializable blog = getIntent().getExtras().getSerializable("blog");

        BlogDto.BlogBean.ListBean blogDetail = (BlogDto.BlogBean.ListBean) blog;

        //刚打开页面的瞬间显示
        //微博内容
        name.setText(blogDetail.getName());
        Picasso.with(this).load(blogDetail.getUserPic())
                .error(R.mipmap.avator_default)
                .into(avatar);
        time.setText(DateUtil.formatDate(blogDetail.getCreatedAt(), DateUtil.TYPE_05));

        title.setText(blogDetail.getTitle());
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
            for (String i : blogImages.split(";")) {
                images.add(i);
            }
            blogImage.render(images);
        } else {
            blogImage.setVisibility(View.GONE);
        }

    }

}

