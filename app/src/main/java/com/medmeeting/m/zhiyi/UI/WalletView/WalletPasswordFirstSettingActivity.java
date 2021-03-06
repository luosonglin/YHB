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
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletPasswordDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class WalletPasswordFirstSettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.pwd2)
    EditText pwd2;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code_tv)
    TextView mGetCodeView;
    @BindView(R.id.next_btn)
    TextView nextBtn;

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
        setContentView(R.layout.activity_wallet_password_first_setting);
        ButterKnife.bind(this);
        toolBar();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.get_code_tv, R.id.next_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                getPhoneCode();
                break;
            case R.id.next_btn:

                HttpData.getInstance().HttpDataSetWalletPassword(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(WalletPasswordFirstSettingActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(WalletPasswordFirstSettingActivity.this, httpResult3.getMsg());
                            return;
                        }
                        ToastUtils.show(WalletPasswordFirstSettingActivity.this, "设置成功");
                        finish();
                    }
                }, new WalletPasswordDto(pwd.getText().toString().trim(), code.getText().toString().trim()));
                break;
        }
    }

    private void getPhoneCode() {
        timer.start();
        HttpData.getInstance().HttpDataGetAuthMessage(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(WalletPasswordFirstSettingActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(WalletPasswordFirstSettingActivity.this, httpResult3.getMsg());
                    return;
                }
                ToastUtils.show(WalletPasswordFirstSettingActivity.this, httpResult3.getMsg());
            }
        });
    }
}
