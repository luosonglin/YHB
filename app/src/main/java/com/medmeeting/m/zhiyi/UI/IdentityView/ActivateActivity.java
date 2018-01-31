package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivateActivity extends AppCompatActivity {

    @BindView(R.id.activate1)
    TextView activate1;
    @BindView(R.id.activate2)
    TextView activate2;
    @BindView(R.id.activate3)
    TextView activate3;
    @BindView(R.id.activate4)
    TextView activate4;
    @BindView(R.id.activate5)
    TextView activate5;
    @BindView(R.id.activate6)
    TextView activate6;
    @BindView(R.id.activate_1)
    LinearLayout activate_1;
    @BindView(R.id.activate_2)
    LinearLayout activate_2;
    @BindView(R.id.activate_3)
    LinearLayout activate_3;
    @BindView(R.id.activate_4)
    LinearLayout activate_4;
    @BindView(R.id.activate_5)
    LinearLayout activate_5;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        ButterKnife.bind(this);
        toolBar();

        activate_1.setVisibility(View.VISIBLE);
        activate_2.setVisibility(View.GONE);
        activate_3.setVisibility(View.GONE);
        activate_4.setVisibility(View.GONE);
        activate_5.setVisibility(View.GONE);
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.activate1, R.id.activate2, R.id.activate3, R.id.activate4, R.id.activate5, R.id.activate6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activate1:
                activate1.setBackgroundResource(R.mipmap.activate_bg);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.VISIBLE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);
                break;
            case R.id.activate2:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.VISIBLE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);
                break;
            case R.id.activate3:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.VISIBLE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);
                break;
            case R.id.activate4:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.VISIBLE);
                activate_5.setVisibility(View.GONE);
                break;
            case R.id.activate5:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.VISIBLE);
                break;
            case R.id.activate6:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);
                break;
        }
    }
}
