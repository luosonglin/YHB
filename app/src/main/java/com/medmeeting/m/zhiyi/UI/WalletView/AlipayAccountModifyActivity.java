package com.medmeeting.m.zhiyi.UI.WalletView;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;

public class AlipayAccountModifyActivity extends AppCompatActivity {
    private static final String TAG = AlipayAccountModifyActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.mobilePhone)
    EditText mobilePhone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code_textview)
    TextView mGetCodeView;
    @BindView(R.id.next_btn)
    TextView nextBtn;
    @BindView(R.id.identityNumber)
    EditText identityNumber;
    @BindView(R.id.identity_rlyt)
    LinearLayout identityRlyt;
    @BindView(R.id.identityImage)
    ImageView identityImage;
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
        setContentView(R.layout.activity_modify_alipay_account);
        ButterKnife.bind(this);

        toolBar();
        initView();
    }

    private void initView() {
        walletAccountDto = (WalletAccountDto) getIntent().getSerializableExtra("walletAccount");
        name.setHint(walletAccountDto.getAccountName());
        account.setHint(walletAccountDto.getAccountNumber());

        if (walletAccountDto.getPublicPrivateType().equals("PUBLIC")) {
            identityRlyt.setVisibility(View.GONE);
            identityImageLyt.setVisibility(View.GONE);
            accountName0.setText("收款方");
        } else {
            accountName0.setText("姓名");
            identityNumber.setHint(walletAccountDto.getIdentityNumber());
            imageUrl = walletAccountDto.getIdentityImage();
            Log.e(getLocalClassName(), imageUrl+"");
            Glide.with(AlipayAccountModifyActivity.this)
                    .load(imageUrl)
                    .crossFade()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.wallet_add_identity_number_icon)
                    .into(identityImage);
        }
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.get_code_textview, R.id.identityImage, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.identityImage:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(AlipayAccountModifyActivity.this);
                break;
            case R.id.next_btn:
                if (name.getText().toString().trim().equals("")) {
                    ToastUtils.show(AlipayAccountModifyActivity.this, "请输入收款人姓名");
                    return;
                } else if (account.getText().toString().trim().equals("")) {
                    ToastUtils.show(AlipayAccountModifyActivity.this, "请输入支付宝账号／手机号码");
                    return;
                }
                if (!walletAccountDto.getPublicPrivateType().equals("PUBLIC")) {
                    if (identityNumber.getText() == null || identityNumber.getText().toString().trim().equals("")) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, "请输入身份证号");
                        return;
                    } else if (imageUrl.equals("****")){//(identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon))) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, "请上传身份证正面照");
                        return;
                    } else if (code.getText().toString().trim().equals("")) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, "请输入手机验证码");
                        return;
                    }
                }

                EditAlipayReqEntity alipay = new EditAlipayReqEntity();
                alipay.setAccountName(name.getText().toString().trim());
                alipay.setAccountNumber(account.getText().toString().trim());
                if (!walletAccountDto.getPublicPrivateType().equals("PUBLIC")) {
                    alipay.setIdentityNumber(identityNumber.getText().toString().trim());
                    alipay.setIdentityImage(imageUrl + "");
                }
                alipay.setVerCode(code.getText().toString().trim());
                alipay.setPublicPrivateType(walletAccountDto.getPublicPrivateType());

                HttpData.getInstance().HttpDataSetAlipay(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, "" + e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(AlipayAccountModifyActivity.this, httpResult3.getMsg());
                            return;
                        }
                        ToastUtils.show(AlipayAccountModifyActivity.this, "修改成功");
                        finish();
                    }
                }, alipay);

                break;
        }
    }

    private void getPhoneCode() {

//        if (walletAccountDto.getPublicPrivateType().equals("PUBLIC")) {
//            Log.e(getLocalClassName(), " 1 "+name.getText().toString().trim());
//            if (!PhoneUtils.isMobile(name.getText().toString().trim())) {
//                ToastUtils.show(AlipayAccountModifyActivity.this, "手机号格式不正确,请重新输入");
//                return;
//            }
//        } else {
//
//            Log.e(getLocalClassName(), " 2 "+mobilePhone.getText().toString().trim());
//            if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
//                ToastUtils.show(AlipayAccountModifyActivity.this, "手机号格式不正确,请重新输入");
//                return;
//            }
//        }
        timer.start();
        HttpData.getInstance().HttpDataGetAuthMessage(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(AlipayAccountModifyActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(AlipayAccountModifyActivity.this, httpResult3.getMsg());
                    return;
                }
                ToastUtils.show(AlipayAccountModifyActivity.this, httpResult3.getMsg());
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
            ToastUtils.show(AlipayAccountModifyActivity.this, "正在上传...");

            File file = new File(photos.get(0));
            // creates RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            // MultipartBody.Part is used to send also the actual filename
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            // adds another part within the multipart request
            String descriptionString = "Sample description";
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString); //multipart/form-data

            HttpData.getInstance().HttpUploadFile(new Observer<HttpResult6>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult6 data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, data.getMsg());
                        return;
                    }

                    imageUrl  = data.getExtra().getAbsQiniuImgHash();
                    Glide.with(AlipayAccountModifyActivity.this)
                            .load(imageUrl)
                            .crossFade()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(identityImage);
                }
            }, body, description);
        }
    }

}
