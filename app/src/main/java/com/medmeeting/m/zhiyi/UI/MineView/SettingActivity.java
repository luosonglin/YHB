package com.medmeeting.m.zhiyi.UI.MineView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView versionTv;
    private RelativeLayout clean;
    private TextView cache;
    private TextView logout;

    @Bind(R.id.progress)
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

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
//                        cache.setText(CleanUtils.getTotalCacheSize(SettingActivity.this));
                        cache.setText("0 MB");
                        showProgress(false);
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
                showProgress(true);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
