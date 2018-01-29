package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.medmeeting.m.zhiyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletPasswordActivity extends AppCompatActivity {
    private static final String TAG = WalletPasswordActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.modify)
    RelativeLayout modify;
    @BindView(R.id.forget)
    RelativeLayout forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_password);
        ButterKnife.bind(this);
        toolBar();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.modify, R.id.forget})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.modify:
                intent = new Intent(WalletPasswordActivity.this, WalletPasswordModifyActivity.class);
                break;
            case R.id.forget:
                intent = new Intent(WalletPasswordActivity.this, WalletPasswordForgetActivity.class);
                break;
        }
        startActivity(intent);
    }
}
