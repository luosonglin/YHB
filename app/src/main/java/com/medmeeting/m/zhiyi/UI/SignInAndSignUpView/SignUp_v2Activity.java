package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/3/2018
 * @describe 快速注册
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SignUp_v2Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.repassword)
    EditText repassword;
    @BindView(R.id.agree)
    CheckBox agree;
    @BindView(R.id.agreement)
    TextView agreement;
    @BindView(R.id.confirm)
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_v2);
        ButterKnife.bind(this);

        toolBar();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.get_code, R.id.agreement, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                break;
            case R.id.agreement:
                break;
            case R.id.confirm:
                break;
        }
    }
}
