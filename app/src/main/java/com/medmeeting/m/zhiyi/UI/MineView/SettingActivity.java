package com.medmeeting.m.zhiyi.UI.MineView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.Version;
import com.medmeeting.m.zhiyi.Util.CleanUtils;
import com.medmeeting.m.zhiyi.Util.CustomUtils;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.UpdataDialog;
import com.snappydb.SnappydbException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView versionTv;
    private RelativeLayout clean;
    private RelativeLayout update;
    private TextView cache;
    private TextView logout;

    @BindView(R.id.progress)
    View mProgressView;

    private UpdataDialog updataDialog;
    private String oldVersion, newVersion;
    private TextView tvmsg, tvcode;
    private ImageView updateDeletIv;

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
        toolbar.setNavigationOnClickListener(v -> finish());
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

        //初始化弹窗 布局 点击事件的id
        updataDialog = new UpdataDialog(SettingActivity.this, R.layout.dialog_updataversion, new int[]{R.id.dialog_sure});
        oldVersion = CustomUtils.getVersion(SettingActivity.this) + "";
        update = (RelativeLayout) findViewById(R.id.update);
        update.setOnClickListener(view ->

                HttpData.getInstance().HttpDataGetAndroidVersion(new Observer<HttpResult3<Object, Version>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3<Object, Version> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(SettingActivity.this, "网络错误，请稍后再试");
                            return;
                        }
                        newVersion = data.getEntity().getVersion();
                        Log.e(getLocalClassName(), oldVersion+" "+newVersion);


                        String[] yours = oldVersion.split("\\.");   //已经安装的版本
                        String[] news = newVersion.split("\\.");
                        Log.e(getLocalClassName(), yours[0] + " " + yours[1] + " " + yours[2]);
                        Log.e(getLocalClassName(), news[0] + " " + news[1] + " " + news[2]);

                        if (Integer.parseInt(yours[0]) >= Integer.parseInt(news[0])) { //&&  &&
                            ToastUtils.show(SettingActivity.this, "已经是最新版本");

                        } else if (Integer.parseInt(yours[0]) < Integer.parseInt(news[0])) {
                            if (Integer.parseInt(yours[1]) >= Integer.parseInt(news[1])) {
                                ToastUtils.show(SettingActivity.this, "已经是最新版本");

                            } else if (Integer.parseInt(yours[1]) < Integer.parseInt(news[1])) {
                                if (Integer.parseInt(yours[2]) >= Integer.parseInt(news[2])) {
                                    ToastUtils.show(SettingActivity.this, "已经是最新版本");

                                } else {
                                    updataDialog.show();

                                    tvmsg = (TextView) updataDialog.findViewById(R.id.updataversion_msg);
                                    tvcode = (TextView) updataDialog.findViewById(R.id.updataversioncode);
                                    updateDeletIv = (ImageView) updataDialog.findViewById(R.id.delete);
                                    tvcode.setText(newVersion);
                                    tvmsg.setText(data.getEntity().getLog());

                                    updateDeletIv.setOnClickListener(view1 -> updataDialog.dismiss());
                                    updataDialog.setOnCenterItemClickListener((dialog, view12) -> {
                                        switch (view12.getId()) {
                                            case R.id.dialog_sure:
                                                Intent intent = new Intent();
                                                intent.setAction("android.intent.action.VIEW");
                                                Uri content_url = Uri.parse(data.getEntity().getUrl());
                                                intent.setData(content_url);
                                                startActivity(intent);
                                                break;
                                        }
                                        updataDialog.dismiss();
                                    });
                                }
                            }
                        }


                    }
                })
        );

        clean = (RelativeLayout) findViewById(R.id.clean);
        clean.setOnClickListener(view -> dialog());

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            Data.clearUserToken();
            Data.clearUserId();
            Data.clearPayType();
            Data.clearSession();
            Data.clearPhone();

            SharedPreferencesMgr.clearAll();

            try {
                DBUtils.del(SettingActivity.this, "userToken");
                DBUtils.del(SettingActivity.this, "phone");
                DBUtils.del(SettingActivity.this, "userId");
                DBUtils.del(SettingActivity.this, "userName");
                DBUtils.del(SettingActivity.this, "userNickName");
                DBUtils.del(SettingActivity.this, "authentication");
                DBUtils.del(SettingActivity.this, "confirmNumber");
                DBUtils.del(SettingActivity.this, "tokenId");
            } catch (SnappydbException e) {
                e.printStackTrace();
            }finally {
                finish();
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

    };

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("确认清理缓存吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", (dialog, which) -> {
            dialog.dismiss();
            showProgress(true);
            new clearCacheRunnable().run();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
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
