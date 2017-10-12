package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawStatusActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.a)
    ImageView a;
    @Bind(R.id.a2)
    TextView a2;
    @Bind(R.id.a3)
    TextView a3;
    @Bind(R.id.confirm)
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_status);
        ButterKnife.bind(this);

        initToolbar();
        initView();
    }

    private void initView() {
        a3.setText("¥  " + getIntent().getStringExtra("amount"));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        break;
                }
                return true;
            }
        });
    }

    @OnClick(R.id.confirm)
    public void onClick() {
        finish();
    }
}
