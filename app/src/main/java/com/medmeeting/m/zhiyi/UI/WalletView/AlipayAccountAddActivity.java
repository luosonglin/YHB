package com.medmeeting.m.zhiyi.UI.WalletView;

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
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class AlipayAccountAddActivity extends AppCompatActivity {
    private static final String TAG = AlipayAccountAddActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.accountName)
    EditText accountName;
    @Bind(R.id.accountNumber)
    EditText accountNumber;
    @Bind(R.id.identityNumber)
    EditText identityNumber;
    @Bind(R.id.identityImage)
    ImageView identityImage;
    @Bind(R.id.mobilePhone)
    EditText mobilePhone;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_textview)
    TextView mGetCodeView;
    @Bind(R.id.tip)
    TextView tip;

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
        setContentView(R.layout.activity_add_alipay_account);
        ButterKnife.bind(this);

        toolBar();

        if (getIntent().getStringExtra("publicPrivateType").equals("PUBLIC")) {
            tip.setText("请先绑定公司的支付宝帐号");
        } else if (getIntent().getStringExtra("publicPrivateType").equals("PRIVATE")) {
            tip.setText("请先绑定个人的支付宝帐号");
        }
    }

    private void initView() {

        if (accountName.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入收款人姓名");
            return;
        } else if (accountNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入支付宝账号／手机号码");
            return;
        } else if (identityNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon))) {
            ToastUtils.show(AlipayAccountAddActivity.this, "请上传身份证正面照");
            return;
        } else {
            EditAlipayReqEntity alipay = new EditAlipayReqEntity();
            alipay.setAccountName(accountName.getText().toString().trim());
            alipay.setAccountNumber(accountNumber.getText().toString().trim());
            alipay.setIdentityNumber(identityNumber.getText().toString().trim());
            alipay.setIdentityImage("");
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.identityImage, R.id.get_code_textview, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identityImage:
                break;
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.next_btn:
                initView();
        }
    }

    private void getPhoneCode() {

        if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
            ToastUtils.show(AlipayAccountAddActivity.this, "手机号格式不正确,请重新输入");
            return;
        }
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
}
