package com.medmeeting.m.zhiyi.UI.IndexView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Widget.photoview.PhotoViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends AppCompatActivity {

    private int position;
    private List<String> imgUrls; //图片列表
    private ViewPager mViewPager;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_gallery);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        imgUrls = intent.getStringArrayListExtra("images");
        if(imgUrls == null) {
            imgUrls = new ArrayList<>();
        }
        for (String i:imgUrls) {
            Log.e(getLocalClassName(), i);
        }
        initGalleryViewPager();

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initGalleryViewPager() {
        PhotoViewAdapter pagerAdapter = new PhotoViewAdapter(this, imgUrls);
        pagerAdapter.setOnItemChangeListener(currentPosition -> Log.e("eee", currentPosition+""));
        mViewPager = (ViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
    }

}
