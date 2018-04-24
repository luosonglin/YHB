package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserForgetPwdEntity;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/3/2018
 * @describe 重置密码
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class ResetPassword_v2Activity extends AppCompatActivity {

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
    @BindView(R.id.confirm)
    TextView confirm;

    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true

    // timer
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            getCode.setEnabled(false);
            getCode.setText("剩余" + l / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            getCode.setEnabled(true);
            getCode.setText("获取验证码");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_v2);
        ButterKnife.bind(this);

        toolBar();
        initEye();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.get_code, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                if (phone.getText().toString().trim().equals("")) {
                    ToastUtils.show(ResetPassword_v2Activity.this, "请先填写手机号");
                    return;
                }
                if (!PhoneUtils.isMobile(phone.getText().toString().trim())) {
                    ToastUtils.show(ResetPassword_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }
                initCodePopupwindow();
                new ResetPassword_v2Activity.WorkThread().start();
                break;
            case R.id.confirm:
                if (!PhoneUtils.isMobile(phone.getText().toString().trim())) {
                    ToastUtils.show(ResetPassword_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }

                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm2 != null) {
                    imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                UserForgetPwdEntity userEditPwdEntity = new UserForgetPwdEntity();
                userEditPwdEntity.setCode(code.getText().toString().trim());
                userEditPwdEntity.setPassword(password.getText().toString().trim());
                userEditPwdEntity.setPhone( phone.getText().toString().trim());
                HttpData.getInstance().HttpDateForgetPwd(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(ResetPassword_v2Activity.this, data.getMsg());
                            return;
                        }
                        finish();
                        startActivity(new Intent(ResetPassword_v2Activity.this, Login_v2Activity.class));
                    }
                }, userEditPwdEntity);


                break;
        }
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
                            password.setCompoundDrawables(null, null, drawables[2], null);

                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            password.setCompoundDrawables(null, null, drawableEyeOpen, null);

                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }


    /**
     * 验证码弹窗
     */
    View codeView;
    PopupWindow codePopupwindow;

    EditText codeEt;
    ImageView codeIv;
    TextView codeTv;
    TextView codeConfirm;

    private void initCodePopupwindow() {
        codeView = LayoutInflater.from(this).inflate(R.layout.popupwindow_get_code_in_login, null);
        codePopupwindow = new PopupWindow(codeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        codeEt = (EditText) codeView.findViewById(R.id.code_et);
        codeIv = (ImageView) codeView.findViewById(R.id.code_iv);
        codeTv = (TextView) codeView.findViewById(R.id.code_tv);
        codeTv.setOnClickListener(view -> new WorkThread().start());

        codeConfirm = (TextView) codeView.findViewById(R.id.confirm);
        codeConfirm.setOnClickListener(view -> {
            //获取验证码API
            Map<String, Object> options = new HashMap<>();
            options.put("phone", phone.getText().toString().trim());
            options.put("imgCode", codeEt.getText().toString().trim());
            HttpData.getInstance().HttpDataGetCode(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(ResetPassword_v2Activity.this, e.getMessage());
                    timer.cancel();
                }

                @Override
                public void onNext(HttpResult3 data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(ResetPassword_v2Activity.this, data.getMsg());

                        timer.cancel();

                        getCode.setEnabled(true);
                        getCode.setText("获取验证码");
                        return;
                    }
                    timer.start();
                    codePopupwindow.dismiss();
                    ToastUtils.show(ResetPassword_v2Activity.this, data.getMsg());
                }
            }, options);
        });

        codePopupwindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x000ff000);
        codePopupwindow.setBackgroundDrawable(dw);
        codePopupwindow.showAtLocation(codeView, Gravity.BOTTOM, 0, 0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                codeIv.setImageBitmap(pic);
            }
        }
    };

    //工作线程
    private class WorkThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();

            //......处理比较耗时的操作
            getBitmapFromServer(Constant.API_SERVER_LIVE + "/" + "v1/token/imageCode/read?v=" + System.currentTimeMillis());
//            getBitmapFromServer(Constant.API_SERVER_LIVE_TEST + "/" + "v1/token/imageCode/read?v=" + System.currentTimeMillis());

            //处理完成后给handler发送消息
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            Looper.loop();
        }
    }

    Bitmap pic = null;

    public void getBitmapFromServer(String imagePath) {

        HttpGet get = new HttpGet(imagePath);
        HttpClient client = new DefaultHttpClient();

        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            pic = BitmapFactory.decodeStream(is);   // 关键是这句代码
            Log.e("aaa ", pic + "");
            Log.e("aaa ", response.getEntity() + "");
            Log.e("aaa ", response.getFirstHeader("Set-Cookie") + "");//Set-Cookie: JSESSIONID=B24E8934E24E9CF953D946BAC196BEF6; Path=/; HttpOnly

            Data.setSession(response.getFirstHeader("Set-Cookie").getValue().split(";")[0] + "");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return pic;
    }
}
