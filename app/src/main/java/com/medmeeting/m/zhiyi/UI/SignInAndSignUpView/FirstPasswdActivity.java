package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class FirstPasswdActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirm)
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_passwd);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.confirm)
    public void onClick() {
        if (password.getText().toString().equals("")) {
            ToastUtils.show(FirstPasswdActivity.this, "密码不能为空");
            return;
        }
        HttpData.getInstance().HttpDatafirstPwd(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(FirstPasswdActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(FirstPasswdActivity.this, data.getMsg());
                    return;
                }
                startActivity(new Intent(FirstPasswdActivity.this, BindSubject_v2Activity.class));
            }
        }, password.getText().toString());

    }
}
