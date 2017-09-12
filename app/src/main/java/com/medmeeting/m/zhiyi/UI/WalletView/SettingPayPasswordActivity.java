package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingPayPasswordActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_tv)
    TextView getCodeTv;
    @Bind(R.id.next_btn)
    TextView nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pay_password);
        ButterKnife.bind(this);
        toolBar();
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

    @OnClick({R.id.get_code_tv, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                break;
            case R.id.next_btn:
                break;
        }
    }
}
