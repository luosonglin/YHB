package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.ListvView.ListvViewActivity;

public class WelcomeActivity extends ListvViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                startActivity(new Intent(WelcomeActivity.this, ListvViewActivity.class));
                finish();
            }
        }, 1000);
    }
}
