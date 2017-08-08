package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.Util.CleanUtils;
import com.medmeeting.m.zhiyi.Util.CustomUtils;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView versionTv;
    private RelativeLayout clean;
    private TextView cache;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        versionTv = (TextView) findViewById(R.id.versionTv);
        versionTv.setText(CustomUtils.getVersion(this) + "");

        cache = (TextView) findViewById(R.id.cacheTv);
        try {
            cache.setText(CleanUtils.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        clean = (RelativeLayout) findViewById(R.id.clean);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.clearUserToken();
                Data.clearUserId();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });
    }

    class clearCacheRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Toast.makeText(SettingActivity.this, "开始清理", Toast.LENGTH_SHORT).show();
                CleanUtils.clearAllCache(SettingActivity.this);
                Thread.sleep(3000);
                if (CleanUtils.getTotalCacheSize(SettingActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(SettingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                    try {
                        cache.setText(CleanUtils.getTotalCacheSize(SettingActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

        ;
    };

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("确认清理缓存吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new clearCacheRunnable().run();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
