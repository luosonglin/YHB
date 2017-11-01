package com.medmeeting.m.zhiyi.UI.VideoView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditVideoEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/11/2017 2:00 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoUpdateActivity extends AppCompatActivity {
    @Bind(R.id.buildllyt)
    LinearLayout buildllyt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.live_pic)
    ImageView livePic;
    @Bind(R.id.live_pic_tip_tv)
    TextView livePicTipTv;
    @Bind(R.id.live_pic_tip)
    LinearLayout livePicTip;
    @Bind(R.id.theme)
    EditText theme;
    @Bind(R.id.free)
    Button free;
    @Bind(R.id.charge)
    Button charge;
    @Bind(R.id.charge_amount)
    TextView chargeAmount;
    @Bind(R.id.introduction)
    EditText introduction;
    @Bind(R.id.progress)
    View mProgressView;

    private Integer videoId;
    private EditVideoEntity videoEntity;
    private String videoPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_update);
        ButterKnife.bind(this);

        videoId = getIntent().getIntExtra("videoId", 0);

        toolBar();

        getVideoDetail(videoId);

        videoEntity = new EditVideoEntity();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getVideoDetail(Integer videoId) {
        showProgress(true);
        HttpData.getInstance().HttpDataGetVideoDetail(new Observer<HttpResult3<Object, VideoDetailsEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, VideoDetailsEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(VideoUpdateActivity.this, data.getMsg());
                    return;
                }
                Glide.with(VideoUpdateActivity.this)
                        .load(data.getEntity().getCoverPhoto())
                        .crossFade()
                        .into(new GlideDrawableImageViewTarget(livePic) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                //在这里添加一些图片加载完成的操作
                                super.onResourceReady(resource, animation);
                                showProgress(false);
//                                livePicTipTv.setText("点击可修改封面");
                            }
                        });
                theme.setText(data.getEntity().getTitle());
                if (data.getEntity().getChargeType().equals("no")) {
                    free.setBackgroundResource(R.drawable.button_live_free);
                    free.setTextColor(getResources().getColor(R.color.white));
                    charge.setBackgroundResource(R.drawable.button_live_gray);
                    charge.setTextColor(getResources().getColor(R.color.black));
                    chargeAmount.setVisibility(View.GONE);
                } else if (data.getEntity().getChargeType().equals("yes")) {
                    free.setBackgroundResource(R.drawable.button_live_gray);
                    free.setTextColor(getResources().getColor(R.color.black));
                    charge.setBackgroundResource(R.drawable.button_live_free);
                    charge.setTextColor(getResources().getColor(R.color.white));
                    chargeAmount.setVisibility(View.VISIBLE);
                    chargeAmount.setText(data.getEntity().getPrice()+" 元");
                }
                introduction.setText(data.getEntity().getDes()+"");

                videoEntity.setVideoId(videoId);
                videoEntity.setTitle(data.getEntity().getTitle());
                videoEntity.setCoverPhoto(data.getEntity().getCoverPhoto());
                videoEntity.setDes(data.getEntity().getDes());
//                videoEntity.setPrivacyType(data.getEntity().getp);//无该字段，隐私类型跟直播时候一样
                videoEntity.setChargeType(data.getEntity().getChargeType());
                videoEntity.setPrice(data.getEntity().getPrice());
            }
        }, videoId);
    }

    @OnClick({R.id.buildllyt, R.id.live_pic_tip, R.id.free, R.id.charge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buildllyt:
                buildllyt.setClickable(false);
                videoEntity.setCoverPhoto(videoPhoto);
                HttpData.getInstance().HttpDataUpdateVideo(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!"success".equals(httpResult3.getStatus())) {
                            ToastUtils.show(VideoUpdateActivity.this, httpResult3.getMsg());
                            buildllyt.setClickable(true);
                            if (httpResult3.getMsg().equals("invalid_token")) {
                                ToastUtils.show(VideoUpdateActivity.this, "账号有问题，请测试人员重新登录");
                                startActivity(new Intent(VideoUpdateActivity.this, LoginActivity.class));
                                finish();
                            }
                            return;
                        }
                        buildllyt.setClickable(false);
                        ToastUtils.show(VideoUpdateActivity.this, "修改成功");
                        finish();
                    }
                },videoEntity);

                break;
            case R.id.live_pic_tip:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(VideoUpdateActivity.this);
                break;
            case R.id.free:
                free.setBackgroundResource(R.drawable.button_live_free);
                free.setTextColor(getResources().getColor(R.color.white));
                charge.setBackgroundResource(R.drawable.button_live_gray);
                charge.setTextColor(getResources().getColor(R.color.black));
                chargeAmount.setVisibility(View.GONE);
                videoEntity.setChargeType("no");
                break;
            case R.id.charge:
                free.setBackgroundResource(R.drawable.button_live_gray);
                free.setTextColor(getResources().getColor(R.color.black));
                charge.setBackgroundResource(R.drawable.button_live_free);
                charge.setTextColor(getResources().getColor(R.color.white));
                chargeAmount.setVisibility(View.VISIBLE);

                final EditText et = new EditText(this);
                et.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);//弹出数字键盘，只能输入数字
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setIcon(R.mipmap.logo)
                        .setMessage("填写金额")
                        .setView(et)
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            String amount = et.getText().toString();
                            if (amount.equals("")) {
                                Toast.makeText(getApplicationContext(), "金额不能为空，请重新设定" + amount, Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (Float.parseFloat(amount) == 0) {
                                ToastUtils.show(VideoUpdateActivity.this, "金额不能为0，请重新设定");
                                return;
                            }
                            chargeAmount.setText("费用： " + amount + "元");
                            videoEntity.setChargeType("yes");
                            videoEntity.setPrice(Double.parseDouble(amount));
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            showProgress(true);
            ToastUtils.show(VideoUpdateActivity.this, "正在上传封面图片...");
            getQiniuToken(photos.get(0));
        }
    }

    private String qiniuKey;
    private String qiniuToken;

    private void getQiniuToken(final String file) {
        HttpData.getInstance().HttpDataGetQiniuToken(new Observer<QiniuTokenDto>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
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

    private void upload(final String data, final String key, final String token) {
        new Thread() {
            public void run() {
                // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                UploadManager uploadManager = new UploadManager(config);
                uploadManager.put(data, key, token,
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                if (info.isOK()) {
                                    Log.i("qiniu", "Upload Success");


                                } else {
                                    Log.i("qiniu", "Upload Fail");
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                }
                                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);

                                videoPhoto = "http://ono5ms5i0.bkt.clouddn.com/" + key;
                            }
                        }, null);
            }
        }.start();
        Glide.with(VideoUpdateActivity.this)
                .load("http://ono5ms5i0.bkt.clouddn.com/" + key)
                .crossFade()
                .into(new GlideDrawableImageViewTarget(livePic) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        //在这里添加一些图片加载完成的操作
                        super.onResourceReady(resource, animation);
                        showProgress(false);
                        livePicTipTv.setText("再次点击仍可修改直播间封面");
                        ToastUtils.show(VideoUpdateActivity.this, "封面正在上传，上传速度取决于当前网络，请耐心等待...");
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
