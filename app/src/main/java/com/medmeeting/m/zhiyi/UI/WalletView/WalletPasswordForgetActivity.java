package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordForgetDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class WalletPasswordForgetActivity extends AppCompatActivity {
    private static final String TAG = WalletPasswordForgetActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.modify)
    RelativeLayout modify;
    @Bind(R.id.forget)
    RelativeLayout forget;
    @Bind(R.id.pwd)
    EditText newPwd;
    @Bind(R.id.pwd2)
    EditText newPwd2;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_tv)
    TextView getCodeTv;
    @Bind(R.id.next_btn)
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_password_forget);
        ButterKnife.bind(this);
        toolBar();
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

    @OnClick({R.id.get_code_tv, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                HttpData.getInstance().HttpDataGetAuthMessage(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(WalletPasswordForgetActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(WalletPasswordForgetActivity.this, httpResult3.getMsg());
                            return;
                        }
                        ToastUtils.show(WalletPasswordForgetActivity.this, httpResult3.getMsg());
                    }
                });
                break;
            case R.id.next_btn:
                if (!newPwd.getText().toString().trim().equals(newPwd2.getText().toString().trim())) {
                    ToastUtils.show(WalletPasswordForgetActivity.this, "两次密码不一致，请重新输入");
                    return;
                }
                if (code.getText().toString().trim().equals("")) {
                    ToastUtils.show(WalletPasswordForgetActivity.this, "手机验证码不能为空");
                    return;
                }

                WalletPasswordForgetDto walletPasswordForgetDto = new WalletPasswordForgetDto(newPwd.getText().toString().trim(), code.getText().toString().trim());
                HttpData.getInstance().HttpDataForgetWalletPassword(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(WalletPasswordForgetActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(WalletPasswordForgetActivity.this, httpResult3.getMsg());
                            return;
                        }
                        ToastUtils.show(WalletPasswordForgetActivity.this, "已重新修改支付密码");
                    }
                },walletPasswordForgetDto);
                break;
        }
    }
}