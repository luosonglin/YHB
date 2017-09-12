package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyAlipayAccountActivity extends AppCompatActivity {
    private static final String TAG = ModifyAlipayAccountActivity.class.getSimpleName();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alipay_account);
        ButterKnife.bind(this);

        toolBar();
    }


    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    @OnClick(R.id.next_btn)
    public void onClick() {
        startActivity(new Intent(ModifyAlipayAccountActivity.this, SettingPayPasswordActivity.class));
    }
}
