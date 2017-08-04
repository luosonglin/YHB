package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.ListvView.ListvViewActivity;
import com.medmeeting.m.zhiyi.Util.SpUtils;

/**
 * 启动页
 */
public class WelcomeActivity extends ListvViewActivity {

    private ImageView imageView;
//    private TextView timeTv;

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
        imageView = (ImageView) findViewById(R.id.background);
        Glide.with(WelcomeActivity.this)
                .load(R.mipmap.appbg)
//                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

//        timeTv = (TextView) findViewById(R.id.time);
//        timeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                countDownTimer.cancel();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
//            }
//        });

//        countDownTimer.start();
    }

//    private CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
//        @Override
//        public void onTick(long l) {
//            timeTv.setText("跳过   " + l / 1000 + "秒");
//        }
//
//        @Override
//        public void onFinish() {
//            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//            finish();
//        }
//    };
}
