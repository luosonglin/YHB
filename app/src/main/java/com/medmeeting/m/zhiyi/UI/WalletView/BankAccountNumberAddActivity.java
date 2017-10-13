package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class BankAccountNumberAddActivity extends AppCompatActivity {
    private static final String TAG = BankAccountNumberAddActivity.class.getSimpleName();
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
    @Bind(R.id.tip)
    TextView tip;

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
        setContentView(R.layout.activity_add_account_number);
        ButterKnife.bind(this);
        toolBar();

        if (getIntent().getStringExtra("publicPrivateType").equals("PUBLIC")) {
            tip.setText("请先绑定公司的银行卡");
        } else if (getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            tip.setText("请先绑定个人的银行卡");
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
        } else if (identityNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(BankAccountNumberAddActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon))) {
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
            bankCard.setIdentityImage("");
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.bankName, R.id.identityImage, R.id.get_code_textview, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bankName:
                startActivityForResult(new Intent(BankAccountNumberAddActivity.this, BankListActivity.class), 0);
                break;
            case R.id.identityImage:
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
        }
    }
}
