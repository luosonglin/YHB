package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/3/2018
 * @describe 登录
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class Login_v2Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login_by_password)
    TextView loginByPassword;
    @BindView(R.id.login_by_code)
    TextView loginByCode;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.login1)
    TextView login1;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.phone2)
    EditText phone2;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.login2)
    TextView login2;
    @BindView(R.id.ll2)
    LinearLayout ll2;

    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);
        ButterKnife.bind(this);

        toolBar();

        initView();

        initEye();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);

        Drawable drawable = getResources().getDrawable(R.mipmap.login_ban);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        loginByPassword.setCompoundDrawables(null, null, null, drawable);
        loginByCode.setCompoundDrawables(null, null, null, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEye()  {
        final Drawable[] drawables = password.getCompoundDrawables() ;
        final int eyeWidth = drawables[2].getBounds().width() ;// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.mipmap.eye_open) ;
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    // getWidth,getHeight必须在这里处理
                    float passwordMinX = v.getWidth() - eyeWidth - password.getPaddingRight() ;
                    float passwordMaxX = v.getWidth() ;
                    float passwordMinY = 0 ;
                    float passwordMaxY = v.getHeight();
                    float x = event.getX() ;
                    float y = event.getY() ;
                    if(x < passwordMaxX && x > passwordMinX && y > passwordMinY && y < passwordMaxY){
                        // 点击了眼睛图标的位置
                        isHidePwd = !isHidePwd;
                        if(isHidePwd){
//                            password.setCompoundDrawables(drawables[0],
//                                    drawables[2] ,
//                                    drawables[3]);
                            password.setCompoundDrawables(null, null, drawables[2], null);

                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        else {
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

    @OnClick({R.id.login_by_password, R.id.login_by_code, R.id.forget_password, R.id.login1, R.id.get_code, R.id.login2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_by_password:
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);

                loginByPassword.setTextColor(getResources().getColor(R.color.cornflowerblue));
                loginByCode.setTextColor(getResources().getColor(R.color.black));

                Drawable drawable1 = getResources().getDrawable(R.mipmap.login_ban);
                // 这一步必须要做,否则不会显示.
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                loginByPassword.setCompoundDrawables(null, null, null, drawable1);
                loginByCode.setCompoundDrawables(null, null, null, null);

                break;
            case R.id.login_by_code:
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);

                loginByCode.setTextColor(getResources().getColor(R.color.cornflowerblue));
                loginByPassword.setTextColor(getResources().getColor(R.color.black));

                Drawable drawable2 = getResources().getDrawable(R.mipmap.login_ban);
                // 这一步必须要做,否则不会显示.
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                loginByCode.setCompoundDrawables(null, null, null, drawable2);
                loginByPassword.setCompoundDrawables(null, null, null, null);

                break;
            case R.id.forget_password:
                break;
            case R.id.login1:
                break;
            case R.id.get_code:
                break;
            case R.id.login2:
                break;
        }
    }
}
