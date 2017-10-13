package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
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

public class AlipayAccountModifyActivity extends AppCompatActivity {
    private static final String TAG = AlipayAccountModifyActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.mobilePhone)
    EditText mobilePhone;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_textview)
    TextView mGetCodeView;
    @Bind(R.id.next_btn)
    TextView nextBtn;

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
        setContentView(R.layout.activity_modify_alipay_account);
        ButterKnife.bind(this);

        toolBar();
        initView();
    }

    private void initView() {
        walletAccountDto = (WalletAccountDto) getIntent().getSerializableExtra("walletAccount");
        name.setText(walletAccountDto.getAccountName());
        account.setText(walletAccountDto.getAccountNumber());
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.get_code_textview, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_textview:
                getPhoneCode();
                break;
            case R.id.next_btn:
                EditAlipayReqEntity alipay = new EditAlipayReqEntity();
                alipay.setAccountName(name.getText().toString().trim());
                alipay.setAccountNumber(account.getText().toString().trim());
                alipay.setVerCode(code.getText().toString().trim());
                alipay.setPublicPrivateType(walletAccountDto.getPublicPrivateType());

                HttpData.getInstance().HttpDataSetAlipay(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(AlipayAccountModifyActivity.this, ""+e.getMessage());
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

        if (!PhoneUtils.isMobile(mobilePhone.getText().toString().trim())) {
            ToastUtils.show(AlipayAccountModifyActivity.this, "手机号格式不正确,请重新输入");
            return;
        }
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
}
