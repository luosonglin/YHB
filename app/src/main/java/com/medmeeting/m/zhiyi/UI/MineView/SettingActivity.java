package com.medmeeting.m.zhiyi.UI.MineView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VersionDto;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.Util.CleanUtils;
import com.medmeeting.m.zhiyi.Util.CustomUtils;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.UpdataDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.snappydb.SnappydbException;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView versionTv;
    private RelativeLayout clean;
    private RelativeLayout update;
    private TextView cache;
    private TextView logout;

    private ImageView avatarIv;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;

    @Bind(R.id.progress)
    View mProgressView;

    private UpdataDialog updataDialog;
    private String versionName = "";
    private int versioncode;
    private String oldVersion, newVersion, versionmsg, url, channelid;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        avatarIv = (ImageView) findViewById(R.id.avatar);
        Glide.with(SettingActivity.this)
                .load(getIntent().getExtras().getString("avatar"))
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(avatarIv);
        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(SettingActivity.this);
            }
        });

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
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpData.getInstance().HttpDataGetLatestAndroidVersion(new Observer<HttpResult<VersionDto>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult<VersionDto> versionDtoHttpResult) {
                        newVersion = versionDtoHttpResult.getData().getVersion().getVersion();
                        versionmsg = versionDtoHttpResult.getData().getVersion().getLog();
                        url = versionDtoHttpResult.getData().getVersion().getUrl();
                        Log.e(TAG, newVersion + " " + oldVersion);
                        if (!newVersion.equals(oldVersion)) {
                            updataDialog.show();

                            tvmsg = (TextView) updataDialog.findViewById(R.id.updataversion_msg);
                            tvcode = (TextView) updataDialog.findViewById(R.id.updataversioncode);
                            updateDeletIv = (ImageView) updataDialog.findViewById(R.id.delete);
                            tvcode.setText(newVersion);
                            tvmsg.setText(versionmsg);
                            updateDeletIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updataDialog.dismiss();
                                }
                            });
                            updataDialog.setOnCenterItemClickListener(new UpdataDialog.OnCenterItemClickListener() {
                                @Override
                                public void OnCenterItemClick(UpdataDialog dialog, View view) {
                                    switch (view.getId()) {
                                        case R.id.dialog_sure:
                                            /**调用系统自带的浏览器去下载最新apk*/
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(url);
                                            intent.setData(content_url);
                                            startActivity(intent);
                                            break;
                                    }
                                    updataDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String i : photos) {
                Log.e(TAG, i);
            }
            showProgress(true);
            ToastUtils.show(SettingActivity.this, "正在上传封面图片...");
            getQiniuToken(photos.get(0));
        }
    }

    private List<String> qiniuData = new ArrayList<>();
    private String qiniuKey;
    private String qiniuToken;
    private String images = "";
    private void getQiniuToken(final String file) {
        HttpData.getInstance().HttpDataGetQiniuToken(new Observer<QiniuTokenDto>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(QiniuTokenDto q) {
                if (q.getCode() != 200 || q.getData().getUploadToken() == null || q.getData().getUploadToken().equals("")) {
                    showProgress(false);
                    return;
                }
                qiniuToken = q.getData().getUploadToken();

                // 设置图片名字
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                qiniuKey = "android_live_" + sdf.format(new Date());

                int i = new Random().nextInt(1000) + 1;

                Log.e(TAG, "File对象、或 文件路径、或 字节数组: " + file);
                Log.e(TAG, "指定七牛服务上的文件名，或 null: " + qiniuKey + i);
                Log.e(TAG, "从服务端SDK获取: " + qiniuToken);
                Log.e(TAG, "http://ono5ms5i0.bkt.clouddn.com/" + qiniuKey + i);

                upload(file, qiniuKey + i, qiniuToken);
            }
        }, "android");
    }

    private Configuration config = new Configuration.Builder()
            .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
            .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
            .connectTimeout(10) // 链接超时。默认10秒
            .responseTimeout(60) // 服务器响应超时。默认60秒
//            .zone(Zone.zone1) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();
    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
    UploadManager uploadManager = new UploadManager(config);

    private void upload(final String data, final String key, final String token) {
        new Thread() {
            public void run() {
                uploadManager.put(data, key, token,
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                if (info.isOK()) {
                                    Log.i("qiniu", "Upload Success");
                                    Glide.with(SettingActivity.this)
                                            .load("http://ono5ms5i0.bkt.clouddn.com/" + key)
                                            .crossFade()
                                            .into(avatarIv);
                                    showProgress(false);
                                    Map<String, Object> maps = new HashMap<>();
                                    try {
                                        maps.put("userId", DBUtils.get(SettingActivity.this, "userId"));
                                    } catch (SnappydbException e) {
                                        e.printStackTrace();
                                    }
                                    maps.put("avatar", "http://ono5ms5i0.bkt.clouddn.com/" + key);
                                    HttpData.getInstance().HttpDataUpdateAvatar(new Observer<MyInfoDto>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(MyInfoDto myInfoDto) {
                                            if (myInfoDto.getCode() == 200) {
                                                ToastUtils.show(SettingActivity.this, "修改成功");
                                            }
                                        }
                                    }, maps);
                                } else {
                                    Log.i("qiniu", "Upload Fail");
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                }
                                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);

                            }
                        }, null);
            }
        }.start();
//        Glide.with(SettingActivity.this)
//                .load("http://ono5ms5i0.bkt.clouddn.com/" + key +"/thumbnail/140x140")
//                .crossFade()
//                .into(new GlideDrawableImageViewTarget(avatarIv) {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                        //在这里添加一些图片加载完成的操作
//                        super.onResourceReady(resource, animation);
//                        showProgress(false);
//                        ToastUtils.show(SettingActivity.this, "封面正在上传，上传速度取决于当前网络，请耐心等待...");
//                    }
//                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //此处避免you cannot start a load for a destroyed activity，因为glide不在主线程
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
