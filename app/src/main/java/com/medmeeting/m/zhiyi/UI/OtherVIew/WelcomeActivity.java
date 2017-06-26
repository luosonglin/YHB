package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.ListvView.ListvViewActivity;
import com.medmeeting.m.zhiyi.Util.SpUtils;

/**
 *  启动页
 */
public class WelcomeActivity extends ListvViewActivity {

    private TextView timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, Constant.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);
        getWindow().setBackgroundDrawableResource(R.mipmap.haha);

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
