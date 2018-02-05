package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 激活完成页
 */
public class ActivatedActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activated);
        ButterKnife.bind(this);
        toolBar();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.back, R.id.apply_authorization})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.apply_authorization:
                startActivity(new Intent(ActivatedActivity.this, AuthorizeActivity.class));
                break;
        }
    }
}
