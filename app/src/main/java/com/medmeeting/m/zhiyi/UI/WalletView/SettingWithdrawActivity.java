package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingWithdrawActivity extends AppCompatActivity {
    private static final String TAG = SettingWithdrawActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.account_number_add_status)
    TextView accountNumberAddStatus;
    @Bind(R.id.account_number_rlyt)
    RelativeLayout accountNumberRlyt;
    @Bind(R.id.alipay_add_status)
    TextView alipayAddStatus;
    @Bind(R.id.alipay_rlyt)
    RelativeLayout alipayRlyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_withdraw);
        ButterKnife.bind(this);

        toolBar();

        initView();
    }

    private void initView() {
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.account_number_add_status, R.id.account_number_rlyt, R.id.alipay_add_status, R.id.alipay_rlyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_number_add_status:
                startActivity(new Intent(SettingWithdrawActivity.this, AddAccountNumberActivity.class));
                break;
            case R.id.account_number_rlyt:
                startActivity(new Intent(SettingWithdrawActivity.this, ModifyAccountNumberActivity.class));
                break;
            case R.id.alipay_add_status:
                startActivity(new Intent(SettingWithdrawActivity.this, AddAlipayAccountActivity.class));
                break;
            case R.id.alipay_rlyt:
                startActivity(new Intent(SettingWithdrawActivity.this, ModifyAlipayAccountActivity.class));
                break;
        }
    }
}
