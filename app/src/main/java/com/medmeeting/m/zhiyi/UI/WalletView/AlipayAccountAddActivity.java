package com.medmeeting.m.zhiyi.UI.WalletView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

public class AlipayAccountAddActivity extends AppCompatActivity {
    private static final String TAG = AlipayAccountAddActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountName)
    EditText accountName;
    @BindView(R.id.accountNumber)
    EditText accountNumber;
    @BindView(R.id.identityNumber)
    EditText identityNumber;
    @BindView(R.id.identityImage)
    ImageView identityImage;
    @BindView(R.id.mobilePhone)
    EditText mobilePhone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code_textview)
    TextView mGetCodeView;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.identity_rlyt)
    LinearLayout identityRlyt;
    @BindView(R.id.identity_image_lyt)
    LinearLayout identityImageLyt;
    @BindView(R.id.accountName0)
    TextView accountName0;

    private WalletAccountDto walletAccountDto = null;
    // timer
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            mGetCodeView.setEnabled(false);
            mGetCodeView.setText("剩余" + l / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            mGetCodeView.setEnabled(true);
            mGetCodeView.setText("获取验证码");
        }
    };

    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alipay_account);
        ButterKnife.bind(this);

        toolBar();

        if (getIntent().getStringExtra("publicPrivateType").equals("PUBLIC")) {
            tip.setText("请先绑定公司的支付宝帐号");
            identityRlyt.setVisibility(View.GONE);
            identityImageLyt.setVisibility(View.GONE);
            accountName0.setText("收款方");
        } else if (getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            tip.setText("请先绑定个人的支付宝帐号");
            accountName0.setText("姓名");
        }
    }

    private void initView() {

        if (accountName.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入收款人姓名");
            return;
        } else if (accountNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入支付宝账号／手机号码");
            return;
        } else if (identityNumber.getText().toString().trim().equals("") && getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon)) && getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请上传身份证正面照");
            return;
        } else if (code.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入手机验证码");
            return;
        } else {
            EditAlipayReqEntity alipay = new EditAlipayReqEntity();
            alipay.setAccountName(accountName.getText().toString().trim());
            alipay.setAccountNumber(accountNumber.getText().toString().trim());
            alipay.setIdentityNumber(identityNumber.getText().toString().trim());
            alipay.setIdentityImage("" + imageUrl);
            alipay.setPublicPrivateType(getIntent().getStringExtra("publicPrivateType"));
            alipay.setVerCode(code.getText().toString().trim());

            HttpData.getInstance().HttpDataSetAlipay(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(AlipayAccountAddActivity.this, "" + e.getMessage());
                }

                @Override
                public void onNext(HttpResult3 httpResult3) {
                    if (!httpResult3.getStatus().equals("success")) {
                        ToastUtils.show(AlipayAccountAddActivity.this, httpResult3.getMsg());
                        return;
                    }
                    ToastUtils.show(AlipayAccountAddActivity.this, "绑定成功");
                    finish();
                }
            }, alipay);
        }
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.identityImage, R.id.get_code_textview, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identityImage:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(AlipayAccountAddActivity.this);
                break;
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.next_btn:
                initView();
        }
    }

    private void getPhoneCode() {

//        if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
//            ToastUtils.show(AlipayAccountAddActivity.this, "手机号格式不正确,请重新输入");
//            return;
//        }
        timer.start();
        HttpData.getInstance().HttpDataGetAuthMessage(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(AlipayAccountAddActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(AlipayAccountAddActivity.this, httpResult3.getMsg());
                    return;
                }
                ToastUtils.show(AlipayAccountAddActivity.this, httpResult3.getMsg());
            }
        });
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
            ToastUtils.show(AlipayAccountAddActivity.this, "正在上传...");
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
                    return;
                }
                qiniuToken = q.getData().getUploadToken();

                // 设置图片名字
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
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
                        (key1, info, res) -> {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                Log.i("qiniu", "Upload Success");
                                Glide.with(AlipayAccountAddActivity.this)
                                        .load("http://ono5ms5i0.bkt.clouddn.com/" + key1)
                                        .crossFade()
                                        .into(identityImage);
                                imageUrl = "http://ono5ms5i0.bkt.clouddn.com/" + key1;
                            } else {
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);
                        }, null);
            }
        }.start();
    }
}
