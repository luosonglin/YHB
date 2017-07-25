package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.activity.CaptureActivity;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveStream;
import com.medmeeting.m.zhiyi.UI.LiveView.live.Config;
import com.medmeeting.m.zhiyi.UI.LiveView.live.SWCodecCameraStreamingActivity;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * 主播用的直播节目详情页
 */
public class LiveProgramDetailAuthorActivity extends AppCompatActivity {

    private static final String TAG = LiveProgramDetailAuthorActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView titleTv, name, title;
    private ImageView backgroundIv, userPic;
    private Integer roomId = 0;
    private Integer programId = 0;
    private TextView detailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail_author);
        toolBar();
        initView();
        initShare(getIntent().getExtras().getInt("programId"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("coverPhote"),
                getIntent().getExtras().getString("description"));

        //qq微信新浪授权防杀死, 在onCreate中再设置一次回调
        UMShareAPI.get(this).fetchAuthResultWithBundle(this, savedInstanceState, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        });

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
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.live_point));
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getIntent().getExtras().getString("title"));

        roomId = getIntent().getExtras().getInt("roomId");
        programId = getIntent().getExtras().getInt("programId");

        backgroundIv = (ImageView) findViewById(R.id.img);
        Glide.with(LiveProgramDetailAuthorActivity.this)
                .load(getIntent().getExtras().getString("coverPhoto"))
                .crossFade()
                .into(backgroundIv);

        userPic = (ImageView) findViewById(R.id.live_user_pic);
        Glide.with(LiveProgramDetailAuthorActivity.this)
                .load(getIntent().getExtras().getString("userPic"))
                .crossFade()
                .transform(new GlideCircleTransform(LiveProgramDetailAuthorActivity.this))
                .placeholder(R.mipmap.avator_default)
                .into(userPic);

        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getExtras().getString("authorName"));
        title = (TextView) findViewById(R.id.author_title);
        title.setText(getIntent().getExtras().getString("authorTitle"));

        detailTv = (TextView) findViewById(R.id.detail);
        detailTv.setText("");

        findViewById(R.id.to_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LiveProgramDetailAuthorActivity.this)
                        .setTitle("请用PC浏览器打开")
                        .setMessage(" zb.medmeeting.com 进行扫码登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, REQ_CODE);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        findViewById(R.id.to_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpData.getInstance().HttpDataGetLiveStream(new Observer<HttpResult3<Object, LiveStream>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3<Object, LiveStream> objectLiveStreamHttpResult3) {
                        if (objectLiveStreamHttpResult3.getStatus().equals("success")) {
                            Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, SWCodecCameraStreamingActivity.class);
                            startStreamingActivity(intent, objectLiveStreamHttpResult3.getEntity().getPushUrl(), programId);
                        }
                    }
                }, programId);
            }
        });
    }

    private final static int REQ_CODE = 1102;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {

            if (data == null) return;

            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);

            Log.e(TAG, "扫码结果：" + result + " " + bitmap);

            if (result != null) {
                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveLoginWebActivity.class);
                intent.putExtra("QRCode", result);
                intent.putExtra("extend", programId + "");
                startActivity(intent);
            }
        }
    }


    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.live_share_analysis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.letter:
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                mShareAction.open(config);
                return true;
            case R.id.analyse:
                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveTicketActivity.class);
                intent.putExtra("programId", getIntent().getExtras().getInt("programId"));
                startActivity(intent);
                return true;
            case R.id.delete:
                new AlertDialog.Builder(LiveProgramDetailAuthorActivity.this)
                        .setMessage("确定删除该节目？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                HttpData.getInstance().HttpDataDeleteProgram(new Observer<HttpResult3>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(HttpResult3 httpResult3) {
                                        if (httpResult3.getStatus().equals("success")) {
                                            dialogInterface.dismiss();
                                            onResume();
                                        }
                                    }
                                }, programId);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享
     *
     * @param programId
     * @param title
     * @param photo
     * @param description
     */
    public void initShare(final int programId, final String title, final String photo, final String description) {
        Log.e(TAG, "0 url" + url
                + "\n" + "title" + title
                + "\n" + "photo" + photo
                + "\n" + "description" + description);

        mShareListener = new LiveProgramDetailAuthorActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(LiveProgramDetailAuthorActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        UMWeb web = new UMWeb("http://wap.medmeeting.com/#!/live/room/show/" + programId);
                        web.setTitle(title);//标题
//                        web.setThumb(new UMImage(LiveProgramDetailAuthorActivity.this, photo));  //缩略图
                        web.setDescription(description);//描述

                        Log.e(TAG, "1 url" + "http://wap.medmeeting.com/#!/live/room/show/" + programId
                                + "\n" + "title" + title
                                + "\n" + "photo" + photo
                                + "\n" + "description" + description);

                        new ShareAction(LiveProgramDetailAuthorActivity.this)
                                .withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<LiveProgramDetailAuthorActivity> mActivity;

        private CustomShareListener(LiveProgramDetailAuthorActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE) {
                Toast.makeText(mActivity.get(), platform + "分享失败啦~~ \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.e(TAG, "umeng throw:" + t.getMessage());
                }
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //qq微信新浪授权防杀死
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存泄漏，在使用分享或者授权的Activity中，重写onDestory()方法：
        UMShareAPI.get(this).release();
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    /**
     * 直播推流
     */
    private String mSelectedInputType = null;
    private boolean mPermissionEnabled = false;
    private static final String INPUT_TYPE_STREAM_JSON = "StreamJson";
    private static final String INPUT_TYPE_AUTHORIZED_URL = "AuthorizedUrl";
    private static final String INPUT_TYPE_UNAUTHORIZED_URL = "UnauthorizedUrl";
    private static final String[] mInputTypeList = {    // "Please select input type of publish url:",
            INPUT_TYPE_STREAM_JSON,
            INPUT_TYPE_AUTHORIZED_URL,
            INPUT_TYPE_UNAUTHORIZED_URL
    };
    private static final String url = "Your app server url which get StreamJson";
    private static final String url2 = "Your app server url which get PublishUrl";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    private void startStreamingActivity(final Intent intent, String pushUrl1, final Integer programId) {
        if (!isPermissionOK()) {
            return;
        }

        final String pushUrl = pushUrl1;    //推流地址

        new Thread(new Runnable() {
            @Override
            public void run() {
                String publishUrl = null;
                mSelectedInputType = mInputTypeList[0];
                Log.i(TAG, "mSelectedInputType:" + mSelectedInputType + ",pushUrl:" + pushUrl);
                if (!"".equalsIgnoreCase(pushUrl)) {
                    publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + pushUrl;
                } else {
                    if (mSelectedInputType != null) {
                        if (INPUT_TYPE_STREAM_JSON.equalsIgnoreCase(mSelectedInputType)) {
                            publishUrl = requestStream(url);
                            if (publishUrl != null) {
                                publishUrl = Config.EXTRA_PUBLISH_JSON_PREFIX + publishUrl;
                            }
                        } else if (INPUT_TYPE_AUTHORIZED_URL.equalsIgnoreCase(mSelectedInputType)) {
                            publishUrl = requestStream(url2);
                            if (publishUrl != null) {
                                publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + publishUrl;
                            }
                        } else if (INPUT_TYPE_UNAUTHORIZED_URL.equalsIgnoreCase(mSelectedInputType)) {
                            // just for test
                            publishUrl = requestStream(url2);
                            try {
                                URI u = new URI(publishUrl);
                                publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + String.format("rtmp://401.qbox.net%s?%s", u.getPath(), u.getRawQuery());
                            } catch (Exception e) {
                                e.printStackTrace();
                                publishUrl = null;
                            }
                        } else {
                            throw new IllegalArgumentException("Illegal input type");
                        }
                    }
                }

                if (publishUrl == null) {
                    showToast("Publish Url Got Fail!");
                    return;
                }
                intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
                intent.putExtra("programId", programId);
                startActivity(intent);
            }
        }).start();
    }

    private static boolean isSupportHWEncode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    private static boolean isSupportScreenCapture() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean isPermissionOK() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionEnabled = true;
            return true;
        } else {
            return checkPermission();
        }
    }

    private String requestStream(String appServerUrl) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(appServerUrl).openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setConnectTimeout(5000);
            httpConn.setReadTimeout(10000);
            int responseCode = httpConn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int length = httpConn.getContentLength();
            if (length <= 0) {
                length = 16 * 1024;
            }
            InputStream is = httpConn.getInputStream();
            byte[] data = new byte[length];
            int read = is.read(data);
            is.close();
            if (read <= 0) {
                return null;
            }
            return new String(data, 0, read);
        } catch (Exception e) {
            showToast("Network error!");
        }
        return null;
    }

    void showToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveProgramDetailAuthorActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        boolean ret = true;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("CAMERA");
        }
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
            permissionsNeeded.add("MICROPHONE");
        }
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write external storage");
        }

        if (permissionsNeeded.size() > 0) {
            // Need Rationale
            String message = "You need to grant access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++) {
                message = message + ", " + permissionsNeeded.get(i);
            }
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permissionsList.get(0))) {
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            ret = false;
        }

        return ret;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean ret = true;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            ret = false;
        }
        return ret;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LiveProgramDetailAuthorActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (verifyPermissions(grantResults)) {
                    // All Permissions Granted
                    mPermissionEnabled = true;
                } else {
                    // Permission Denied
                    mPermissionEnabled = false;
                    showToast("Some Permission is Denied");
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
