package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.extractType)
    TextView extractType;
    @Bind(R.id.extract_type_rlyt)
    RelativeLayout extractTypeRlyt;
    @Bind(R.id.amount)
    EditText amount;
    @Bind(R.id.all_extract_action)
    TextView allExtractAction;
    @Bind(R.id.actual_arrival)
    TextView actualArrival;
    @Bind(R.id.tax)
    TextView tax;
    @Bind(R.id.extractPassword)
    EditText extractPassword;
    @Bind(R.id.confirm)
    Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);

        initToolbar();
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

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet_withdraw_help, menu);
        return true;
    }

    @OnClick({R.id.extract_type_rlyt, R.id.all_extract_action, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.extract_type_rlyt:
                startActivityForResult(new Intent(WithdrawActivity.this, ExtractTypeActivity.class), 0);
                break;
            case R.id.all_extract_action:
                break;
            case R.id.confirm:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String a1 = data.getStringExtra("a1");
        String a2 = data.getStringExtra("a2");

    }
}
