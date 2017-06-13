package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.ListvView.ListvViewActivity;

public class WelcomeActivity extends ListvViewActivity {

    private TextView timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setBackgroundDrawableResource(R.mipmap.hahaha);

        timeTv = (TextView) findViewById(R.id.time);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                timeTv.setText("" + l / 1000);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }
}
