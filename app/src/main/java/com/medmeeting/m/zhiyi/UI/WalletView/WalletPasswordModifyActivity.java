package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordModifyDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class WalletPasswordModifyActivity extends AppCompatActivity {
    private static final String TAG = WalletPasswordModifyActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pwd0)
    EditText oldPwd;
    @Bind(R.id.pwd1)
    EditText newPwd;
    @Bind(R.id.pwd2)
    EditText newPwd2;
    @Bind(R.id.confirm)
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_password_modify);
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

    @OnClick(R.id.confirm)
    public void onClick() {
        if (!newPwd.getText().toString().trim().equals(newPwd2.getText().toString().trim())) {
            ToastUtils.show(WalletPasswordModifyActivity.this, "两次密码不一致，请重新输入");
            return;
        }
        WalletPasswordModifyDto walletPasswordModifyDto = new WalletPasswordModifyDto(oldPwd.getText().toString().trim(), newPwd.getText().toString().trim());
        HttpData.getInstance().HttpDataModifyWalletPassword(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(WalletPasswordModifyActivity.this, e.getMessage()+"");
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(WalletPasswordModifyActivity.this, httpResult3.getMsg());
                    return;
                }
                ToastUtils.show(WalletPasswordModifyActivity.this, "修改支付密码成功");
            }
        }, walletPasswordModifyDto);
    }
}
