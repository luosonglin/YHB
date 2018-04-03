package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.AccessToken;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserInfoDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserOpenIdEntity;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
import cn.jpush.android.api.JPushInterface;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/3/2018
 * @describe 登录
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class Login_v2Activity extends AppCompatActivity {


    @BindView(R.id.login_progress)
    ProgressBar mProgressView;
    @BindView(R.id.login_form)
    LinearLayout mLoginFormView;
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


    @OnClick({R.id.fast_sign_up, R.id.login_by_password, R.id.login_by_code, R.id.forget_password, R.id.login1, R.id.get_code, R.id.login2, R.id.wechat, R.id.qq, R.id.agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fast_sign_up:
                startActivity(new Intent(Login_v2Activity.this, SignUp_v2Activity.class));
                break;
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
                startActivity(new Intent(Login_v2Activity.this, ResetPassword_v2Activity.class));
                break;
            case R.id.login1:
                if (!PhoneUtils.isMobile(phone.getText().toString().trim())) {
                    ToastUtils.show(Login_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                showProgress(true);

                Map<String, Object> map = new HashMap<>();
                map.put("phone", phone.getText().toString().trim());
                map.put("pwd", password.getText().toString().trim());
                HttpData.getInstance().HttpDataLoginByPwd(new Observer<HttpResult3<Object, AccessToken>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(Login_v2Activity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, AccessToken> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(Login_v2Activity.this, data.getMsg());
                            showProgress(false);
                            return;
                        }

                        Data.setUserToken(data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                        try {
                            DBUtils.put(Login_v2Activity.this, "userToken", data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                            DBUtils.put(Login_v2Activity.this, "phone", phone.getText().toString().trim());
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }

                        Data.setPhone(phone.getText().toString().trim());

                        //极光推送 别名设置
                        JPushInterface.setAlias(Login_v2Activity.this, 1, phone.getText().toString().trim());

                        HttpData.getInstance().HttpDataGetUserInfo(new Observer<HttpResult3<Object, UserInfoDto>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(Login_v2Activity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserInfoDto> data2) {
                                if (!data2.getStatus().equals("success")) {
                                    ToastUtils.show(Login_v2Activity.this, data2.getMsg());
                                    return;
                                }

                                Data.setUserId(data2.getEntity().getId());
                                try {
                                    DBUtils.put(Login_v2Activity.this, "userId", data2.getEntity().getId() + "");
                                    DBUtils.put(Login_v2Activity.this, "userName", data2.getEntity().getName() + "");
                                    DBUtils.put(Login_v2Activity.this, "userNickName", data2.getEntity().getNickName() + "");
                                    DBUtils.put(Login_v2Activity.this, "authentication", data2.getEntity().getAuthenStatus() + "");
                                    DBUtils.put(Login_v2Activity.this, "confirmNumber", data2.getEntity().getConfirmNumber() + "");
                                    DBUtils.put(Login_v2Activity.this, "tokenId", data2.getEntity().getTokenId() + "");
                                } catch (SnappydbException e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.d(getLocalClassName(), "Login succeed!");
                                    finish();
                                    startActivity(new Intent(Login_v2Activity.this, MainActivity.class));
                                }
                            }
                        });
                    }
                }, map);

                break;
            case R.id.get_code:
                if (phone2.getText().toString().trim().equals("")) {
                    ToastUtils.show(Login_v2Activity.this, "请先填写手机号");
                    return;
                }
                if (!PhoneUtils.isMobile(phone2.getText().toString().trim())) {
                    ToastUtils.show(Login_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }
                initCodePopupwindow();
                new WorkThread().start();
                break;
            case R.id.login2:
                if (!PhoneUtils.isMobile(phone2.getText().toString().trim())) {
                    ToastUtils.show(Login_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }

                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm2 != null) {
                    imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                showProgress(true);

                Map<String, Object> map2 = new HashMap<>();
                map2.put("phone", phone2.getText().toString().trim());
                map2.put("code", code.getText().toString().trim());
                map2.put("source", "android");
                HttpData.getInstance().HttpDataLoginByCode(new Observer<HttpResult3<Object, AccessToken>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(Login_v2Activity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, AccessToken> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(Login_v2Activity.this, data.getMsg());
                            showProgress(false);
                            return;
                        }

                       loginSuccess(data.getEntity());
                    }
                }, map2);

                break;
            case R.id.wechat:
                UMShareAPI.get(this).getPlatformInfo(Login_v2Activity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() { //doOauthVerify //getPlatformInfo
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了

                        ToastUtils.show(Login_v2Activity.this, "微信登录授权成功");
                        Log.e(getLocalClassName(), map.get("openid"));
                        for (Object key : map.keySet())
                            Log.e(getLocalClassName(), key + " "+ map.get(key));

                        if (map != null) {
                            UserOpenIdEntity userOpenIdEntity = new UserOpenIdEntity();
                            userOpenIdEntity.setQqOpenId(null);
                            userOpenIdEntity.setWxOpenId(map.get("openid"));
                            HttpData.getInstance().HttpDataGetTokenByOpenid(new Observer<HttpResult3<Object, AccessToken>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtils.show(Login_v2Activity.this, e.getMessage());
                                }

                                @Override
                                public void onNext(HttpResult3<Object, AccessToken> data) {
                                    if (!data.getStatus().equals("success")) {
                                        ToastUtils.show(Login_v2Activity.this, data.getMsg());
                                        return;
                                    }
                                    if (data.getEntity() != null) {
                                        loginSuccess(data.getEntity());
                                    } else {
                                        Intent intent = new Intent(Login_v2Activity.this, BindPhone_v2Activity.class);
                                        intent.putExtra("nickname", map.get("name"));
                                        intent.putExtra("iconurl", map.get("iconurl"));
                                        intent.putExtra("openid", map.get("openid"));
                                        intent.putExtra("source", "qq");
                                        startActivity(intent);
                                    }
                                }
                            }, userOpenIdEntity);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        ToastUtils.show(Login_v2Activity.this, "微信登录失败，失败原因：" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        ToastUtils.show(Login_v2Activity.this, "取消微信登录");
                    }
                });
                break;
            case R.id.qq:
                UMShareAPI.get(this).getPlatformInfo(Login_v2Activity.this, SHARE_MEDIA.QQ, new UMAuthListener() { //doOauthVerify //getPlatformInfo
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了

                        ToastUtils.show(Login_v2Activity.this, "QQ登录授权成功");
                        if (map != null) {
                            UserOpenIdEntity userOpenIdEntity = new UserOpenIdEntity();
                            userOpenIdEntity.setQqOpenId(map.get("openid"));
                            userOpenIdEntity.setWxOpenId(null);
                            HttpData.getInstance().HttpDataGetTokenByOpenid(new Observer<HttpResult3<Object, AccessToken>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtils.show(Login_v2Activity.this, e.getMessage());
                                }

                                @Override
                                public void onNext(HttpResult3<Object, AccessToken> data) {
                                    if (!data.getStatus().equals("success")) {
                                        ToastUtils.show(Login_v2Activity.this, data.getMsg());
                                        return;
                                    }
                                    if (data.getEntity() != null) {
                                        loginSuccess(data.getEntity());
                                    } else {
                                        Intent intent = new Intent(Login_v2Activity.this, BindPhone_v2Activity.class);
                                        intent.putExtra("nickname", map.get("name"));
                                        intent.putExtra("iconurl", map.get("iconurl"));
                                        intent.putExtra("openid", map.get("openid"));
                                        intent.putExtra("source", "qq");
                                        startActivity(intent);
                                    }
                                }
                            }, userOpenIdEntity);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        ToastUtils.show(Login_v2Activity.this, "微信登录失败，失败原因：" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        ToastUtils.show(Login_v2Activity.this, "取消微信登录");
                    }
                });
                break;
            case R.id.agreement:
                BrowserActivity.launch(Login_v2Activity.this, "http://webview.medmeeting.com/#/page/user-protocol", "《登录协议》");
                break;
        }
    }


    /**
     * 验证码弹窗
     */
    View codeView;// = LayoutInflater.from(this).inflate(R.layout.popupwindow_get_code_in_login, null);
    PopupWindow codePopupwindow;// = new PopupWindow(codeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

    EditText codeEt;// = (EditText) codeView.findViewById(R.id.code_et);
    ImageView codeIv;// = (ImageView) codeView.findViewById(R.id.code_iv);
    TextView codeTv;// = (TextView) codeView.findViewById(R.id.code_tv);
    TextView codeConfirm;// = (TextView) codeView.findViewById(R.id.confirm);

    private void initCodePopupwindow() {
        codeView = LayoutInflater.from(this).inflate(R.layout.popupwindow_get_code_in_login, null);
        codePopupwindow = new PopupWindow(codeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        codeEt = (EditText) codeView.findViewById(R.id.code_et);
        codeIv = (ImageView) codeView.findViewById(R.id.code_iv);
        codeTv = (TextView) codeView.findViewById(R.id.code_tv);
        codeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WorkThread().start();
            }
        });

        codeConfirm = (TextView) codeView.findViewById(R.id.confirm);
        codeConfirm.setOnClickListener(view -> {
            //获取验证码API
            Map<String, Object> options = new HashMap<>();
            options.put("phone", phone2.getText().toString().trim());
            options.put("imgCode", codeEt.getText().toString().trim());
            HttpData.getInstance().HttpDataGetCode(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(Login_v2Activity.this, e.getMessage());
                    timer.cancel();
                }

                @Override
                public void onNext(HttpResult3 data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(Login_v2Activity.this, data.getMsg());

                        timer.cancel();

                        getCode.setEnabled(true);
                        getCode.setText("获取验证码");
                        return;
                    }
                    timer.start();
                    codePopupwindow.dismiss();
                    ToastUtils.show(Login_v2Activity.this, data.getMsg());
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


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     *
     * @param accessToken
     */
    private void loginSuccess(AccessToken accessToken) {
        Data.setUserToken(accessToken.getTokenType() + "_" + accessToken.getAccessToken());
        try {
            DBUtils.put(Login_v2Activity.this, "userToken", accessToken.getTokenType() + "_" + accessToken.getAccessToken());
            DBUtils.put(Login_v2Activity.this, "phone", phone2.getText().toString().trim());
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        Data.setPhone(phone2.getText().toString().trim());

        //极光推送 别名设置
        JPushInterface.setAlias(Login_v2Activity.this, 1, phone2.getText().toString().trim());

        HttpData.getInstance().HttpDataGetUserInfo(new Observer<HttpResult3<Object, UserInfoDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(Login_v2Activity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, UserInfoDto> data2) {
                if (!data2.getStatus().equals("success")) {
                    ToastUtils.show(Login_v2Activity.this, data2.getMsg());
                    showProgress(false);
                    return;
                }

                Data.setUserId(data2.getEntity().getId());
                try {
                    DBUtils.put(Login_v2Activity.this, "userId", data2.getEntity().getId() + "");
                    DBUtils.put(Login_v2Activity.this, "userName", data2.getEntity().getName() + "");
                    DBUtils.put(Login_v2Activity.this, "userNickName", data2.getEntity().getNickName() + "");
                    DBUtils.put(Login_v2Activity.this, "authentication", data2.getEntity().getAuthenStatus() + "");
                    DBUtils.put(Login_v2Activity.this, "confirmNumber", data2.getEntity().getConfirmNumber() + "");
                    DBUtils.put(Login_v2Activity.this, "tokenId", data2.getEntity().getTokenId() + "");
                } catch (SnappydbException e) {
                    e.printStackTrace();
                } finally {
                    Log.d(getLocalClassName(), "Login succeed!");
                    finish();
                    startActivity(new Intent(Login_v2Activity.this, MainActivity.class));
                }
            }
        });
    }
}
