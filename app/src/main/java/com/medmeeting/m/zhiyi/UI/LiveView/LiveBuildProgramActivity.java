package com.medmeeting.m.zhiyi.UI.LiveView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.R;

public class LiveBuildProgramActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //新增直播间
    private int userId;  //用户ID
    private String vidoTitle = "";  //直播间标题
    private long expectBeginTime;  //预计开始时间（预约模式为马上直播时，可传null）
    private long expectEndTime;  //预计结束时间
    private String vidoLabel = "";  //直播间标题
    private String payType = "fee";  //付费方式（免费：fee，收费：toll）
    private String price = "0";  //价格
    private String pushSwitch = "pub";  //是否公开(公开:pub,隐私:pri)
    private String vidoDesc = "";  //直播间描述
    private String expectType = "";  //预约模式(liveNow:马上直播,expect:预约直播)
    private String titlePhoto = "";  //直播间封面图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_build_program);
        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("新建直播节目");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
    }
}
