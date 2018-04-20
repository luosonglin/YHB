package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.FirstPwd;
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
    @BindView(R.id.inviteNumber)
    EditText inviteNumber;
    @BindView(R.id.confirm)
    TextView confirm;

    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_passwd);
        ButterKnife.bind(this);

        initEye();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEye() {
        final Drawable[] drawables = password.getCompoundDrawables();
        final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.mipmap.eye_open);
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // getWidth,getHeight必须在这里处理
                    float passwordMinX = v.getWidth() - eyeWidth - password.getPaddingRight();
                    float passwordMaxX = v.getWidth();
                    float passwordMinY = 0;
                    float passwordMaxY = v.getHeight();
                    float x = event.getX();
                    float y = event.getY();
                    if (x < passwordMaxX && x > passwordMinX && y > passwordMinY && y < passwordMaxY) {
                        // 点击了眼睛图标的位置
                        isHidePwd = !isHidePwd;
                        if (isHidePwd) {
//                            password.setCompoundDrawables(drawables[0],
//                                    drawables[2] ,
//                                    drawables[3]);
                            password.setCompoundDrawables(null, null, drawables[2], null);

                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
//                            password.setCompoundDrawables(drawables[0],
//                                    drawableEyeOpen,
//                                    drawables[3]);
                            password.setCompoundDrawables(null, null, drawableEyeOpen, null);

                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }

    @OnClick(R.id.confirm)
    public void onClick() {
        if (password.getText().toString().equals("")) {
            ToastUtils.show(FirstPasswdActivity.this, "密码不能为空");
            return;
        }
        FirstPwd firstPwd = new FirstPwd(password.getText().toString());
        firstPwd.setInviteNumber(inviteNumber.getText().toString().trim());
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
        }, firstPwd);

    }
}
