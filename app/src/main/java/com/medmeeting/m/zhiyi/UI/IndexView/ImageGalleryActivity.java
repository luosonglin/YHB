package com.medmeeting.m.zhiyi.UI.IndexView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Widget.photoview.PhotoViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends AppCompatActivity {

    private int position;
    private List<String> imgUrls; //图片列表
    private ViewPager mViewPager;

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
        initGalleryViewPager();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void initGalleryViewPager() {
        PhotoViewAdapter pagerAdapter = new PhotoViewAdapter(this, imgUrls);
        pagerAdapter.setOnItemChangeListener(new PhotoViewAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
//
            }
        });
        mViewPager = (ViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
    }

}
