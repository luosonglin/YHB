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
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
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

public class BankAccountNumberAddActivity extends AppCompatActivity {
    private static final String TAG = BankAccountNumberAddActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountName)
    EditText accountName;
    @BindView(R.id.bankName)
    TextView bankName;
    @BindView(R.id.bankAddress)
    EditText bankAddress;
    @BindView(R.id.accountNumber)
    EditText accountNumber;
    @BindView(R.id.mobilePhone)
    EditText mobilePhone;
    @BindView(R.id.identityNumber)
    EditText identityNumber;
    @BindView(R.id.identityImage)
    ImageView identityImage;
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
    @BindView(R.id.confirm)
    TextView confirm;

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
        setContentView(R.layout.activity_add_account_number);
        ButterKnife.bind(this);
        toolBar();

        if (getIntent().getStringExtra("publicPrivateType").equals("PUBLIC")) {
            tip.setText("请先绑定公司的银行卡");
            identityRlyt.setVisibility(View.GONE);
            identityImageLyt.setVisibility(View.GONE);
            accountName0.setText("账户名称");
        } else if (getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            tip.setText("请先绑定个人的银行卡");
            accountName0.setText("姓名");
        }
    }

    private void initView() {

        if (accountName.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入收款人姓名");
            return;
        } else if (bankName.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入银行名称");
            return;
        } else if (bankAddress.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入开户支行");
            return;
        } else if (accountNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入卡号");
            return;
        } else if (mobilePhone.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入手机号");
            return;
        } else if (identityNumber.getText().toString().trim().equals("") && getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon)) && getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请上传身份证正面照");
            return;
        } else {
            EditBankCardReqEntity bankCard = new EditBankCardReqEntity();
            bankCard.setAccountName(accountName.getText().toString().trim());
            bankCard.setBankName(bankName.getText().toString().trim());
            bankCard.setBankAddress(bankAddress.getText().toString().trim());
            bankCard.setAccountNumber(accountNumber.getText().toString().trim());
            bankCard.setMobilePhone(mobilePhone.getText().toString().trim());
            bankCard.setIdentityNumber(identityNumber.getText().toString().trim());
            bankCard.setIdentityImage(imageUrl + "");
            bankCard.setVerCode(code.getText().toString().trim());
            bankCard.setPublicPrivateType(getIntent().getStringExtra("publicPrivateType"));

            HttpData.getInstance().HttpDataSetBankCard(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(BankAccountNumberAddActivity.this, "" + e.getMessage());
                }

                @Override
                public void onNext(HttpResult3 httpResult3) {
                    if (!httpResult3.getStatus().equals("success")) {
                        ToastUtils.show(BankAccountNumberAddActivity.this, httpResult3.getMsg());
                        return;
                    }
                    ToastUtils.show(BankAccountNumberAddActivity.this, "绑定成功");
                    finish();
                }
            }, bankCard);
        }
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.bankName, R.id.identityImage, R.id.get_code_textview, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bankName:
                startActivityForResult(new Intent(BankAccountNumberAddActivity.this, BankListActivity.class), 0);
                break;
            case R.id.identityImage:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(BankAccountNumberAddActivity.this);
                break;
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.confirm:
                initView();
                break;
        }
    }

    private void getPhoneCode() {

        if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "手机号格式不正确,请重新输入");
            return;
        }
        timer.start();

        HttpData.getInstance().HttpDataGetAuthMessage(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(BankAccountNumberAddActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(BankAccountNumberAddActivity.this, httpResult3.getMsg());
                    return;
                }
                ToastUtils.show(BankAccountNumberAddActivity.this, httpResult3.getMsg());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            String bank = data.getStringExtra("bank");
            bankName.setText(bank);
        } else {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String i : photos) {
                    Log.e(TAG, i);
                }
                ToastUtils.show(BankAccountNumberAddActivity.this, "正在上传...");

                File file = new File(photos.get(0));
                // creates RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
                // MultipartBody.Part is used to send also the actual filename
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                Log.e("mediaType is  ", requestFile.contentType().toString());

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
                            ToastUtils.show(BankAccountNumberAddActivity.this, data.getMsg());
                            return;
                        }

                        imageUrl = data.getExtra().getAbsQiniuImgHash();
                        Glide.with(BankAccountNumberAddActivity.this)
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




}
