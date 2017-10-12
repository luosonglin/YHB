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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.SignUpCodeDto;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class BankAccountNumberModifyActivity extends AppCompatActivity {
    private static final String TAG = BankAccountNumberModifyActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.accountName)
    EditText accountName;
    @Bind(R.id.bankName)
    TextView bankName;
    @Bind(R.id.bankAddress)
    EditText bankAddress;
    @Bind(R.id.accountNumber)
    EditText accountNumber;
    @Bind(R.id.mobilePhone)
    EditText mobilePhone;
    @Bind(R.id.identityNumber)
    EditText identityNumber;
    @Bind(R.id.identityImage)
    ImageView identityImage;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_textview)
    TextView mGetCodeView;
    @Bind(R.id.confirm)
    TextView confirm;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account_number);
        ButterKnife.bind(this);
        toolBar();
        initView();
    }

    private void toolBar() {
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
    }

    private void initView() {
        walletAccountDto = (WalletAccountDto) getIntent().getSerializableExtra("walletAccount");
        accountName.setText(walletAccountDto.getAccountName());
        bankName.setText(walletAccountDto.getBankName());
        if (walletAccountDto.getBankAddress() == null) {
            bankAddress.setHint("请输入开户支行");
        } else {
            bankAddress.setText(walletAccountDto.getBankAddress());
        }
        accountNumber.setText(walletAccountDto.getAccountNumber());
        if (walletAccountDto.getMobilePhone() == null) {
            mobilePhone.setHint("请输入手机号");
        } else {
            mobilePhone.setText(walletAccountDto.getMobilePhone());
        }
        identityNumber.setText(walletAccountDto.getIdentityNumber());
        Glide.with(BankAccountNumberModifyActivity.this)
                .load(walletAccountDto.getIdentityImage())
                .crossFade()
                .placeholder(R.mipmap.wallet_add_identity_number_icon)
                .into(identityImage);
    }

    @OnClick({R.id.bankName, R.id.get_code_textview, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bankName:
                startActivityForResult(new Intent(BankAccountNumberModifyActivity.this ,BankListActivity.class), 0);
                break;
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.confirm:
                EditBankCardReqEntity bankCard = new EditBankCardReqEntity();
                bankCard.setAccountName(accountName.getText().toString().trim());
                bankCard.setBankName(bankName.getText().toString().trim());
                bankCard.setBankAddress(bankAddress.getText().toString().trim());
                bankCard.setAccountNumber(accountNumber.getText().toString().trim());
                bankCard.setMobilePhone(mobilePhone.getText().toString().trim());
                bankCard.setIdentityNumber(identityNumber.getText().toString().trim());
                bankCard.setIdentityImage(walletAccountDto.getIdentityImage());
                bankCard.setVerCode(code.getText().toString().trim());
                bankCard.setPublicPrivateType(walletAccountDto.getPublicPrivateType());

                HttpData.getInstance().HttpDataSetBankCard(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(BankAccountNumberModifyActivity.this, "" + e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(BankAccountNumberModifyActivity.this, "绑定成功");
                        }
                    }
                }, bankCard);
                break;
        }
    }

    private void getPhoneCode() {

        if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
            ToastUtils.show(BankAccountNumberModifyActivity.this, "手机号格式不正确,请重新输入");
            return;
        }
        timer.start();
        HttpData.getInstance().HttpDataGetPhoneCode(new Observer<SignUpCodeDto>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //设置页面为加载错误
                ToastUtils.show(BankAccountNumberModifyActivity.this, e.getMessage());
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(SignUpCodeDto signUpCodeDto) {
                Log.e(TAG, "onNext sms " + signUpCodeDto.getData().getMsg() + " " + signUpCodeDto.getCode());
            }
        }, mobilePhone.getText().toString().trim());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            String bank = data.getStringExtra("bank");
            bankName.setText(bank);
        }
    }
}
