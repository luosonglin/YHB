package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.medmeeting.m.zhiyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
}
