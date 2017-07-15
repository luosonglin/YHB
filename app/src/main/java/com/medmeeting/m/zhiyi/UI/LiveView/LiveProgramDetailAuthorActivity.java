package com.medmeeting.m.zhiyi.UI.LiveView;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 主播用的直播节目详情页
 */
public class LiveProgramDetailAuthorActivity extends AppCompatActivity {

    private static final String TAG = LiveProgramDetailAuthorActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView titleTv, name, title;
    private ImageView backgroundIv, userPic;
    private Integer programId = 0;
    private TextView detailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail_author);
        toolBar();
        initView();
        initShare(savedInstanceState, getIntent().getExtras().getInt("roomId"),
                getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("coverPhote"),
                getIntent().getExtras().getString("description"));

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
                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
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

            Log.e(TAG, "扫码结果：" + result + bitmap);

//            if (result != null) {
//                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveLoginWebActivity.class);
//                intent.putExtra("QRCode", result + userId);
//                startActivity(intent);
//            }
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
//                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveInvitationLetterActivity.class);
//                intent.putExtra("roomId", getIntent().getExtras().getInt("roomId"));
//                startActivity(intent);
                ToastUtils.show(LiveProgramDetailAuthorActivity.this, "稍后开启");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initShare(Bundle savedInstanceState, final int roomId, final String title, final String phone, final String description){
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
        //因为分享授权中需要使用一些对应的权限，如果你的targetSdkVersion设置的是23或更高，需要提前获取权限。
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new LiveProgramDetailAuthorActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(LiveProgramDetailAuthorActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.MORE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        UMWeb web = new UMWeb("http://wap.medmeeting.com/#!/live/room/show/" + roomId);
                        web.setTitle(title);//标题
//                        web.setThumb(new UMImage(LiveProgramDetailAuthorActivity.this, phone));  //缩略图
                        web.setDescription(description);//描述
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
}
