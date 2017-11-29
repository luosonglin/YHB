package com.medmeeting.m.zhiyi.UI.IndexView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 2:02 PM
 * @describe 搜索页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchActicity extends AppCompatActivity {
private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBackView = (ImageView) findViewById(R.id.back);
        initToolbar();

    }

    private void initToolbar() {
        mBackView.setOnClickListener(view -> finish());
    }

}

