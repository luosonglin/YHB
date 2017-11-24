package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.AccessToken;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserInfoDto;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.PhoneUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private ImageView mBackgroundImageView;
    private AutoCompleteTextView mPhoneView;
    private EditText mCodeView;
    private TextView mGetCodeView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mForgetPasswordView;
    private TextView mTurnPasswordView;
    private boolean isCode = true;

    //图形验证码
    private RelativeLayout mTxRlyt;
    private EditText mTxCodeView;
    private static ImageView mGetTxCodeView;

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

    /**
     * print some tag in log.
     */
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set full screen 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        mBackgroundImageView = (ImageView) findViewById(R.id.login_background_image);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_background_translate_anim);
                mBackgroundImageView.startAnimation(animation);
            }
        }, 1000);

        // Set up the login form.
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        populateAutoComplete();

        mTxRlyt = (RelativeLayout) findViewById(R.id.tx_rlyt);
        mTxRlyt.setVisibility(View.VISIBLE);
        mTxCodeView = (EditText) findViewById(R.id.tx_code);
        mGetTxCodeView = (ImageView) findViewById(R.id.get_tx_code_textview);

        new WorkThread().start();

        getTxCodeView();
        mGetTxCodeView.setOnClickListener(view -> {
//            getTxCodeView()
            new WorkThread().start();
        });

        mCodeView = (EditText) findViewById(R.id.code);
        mCodeView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            } else if (!isCode) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mGetCodeView = (TextView) findViewById(R.id.get_code_textview);
        mGetCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneCode();
            }
        });

        Button mPhoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mPhoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mForgetPasswordView = (TextView) findViewById(R.id.forget_password_textview);
        mForgetPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        mTurnPasswordView = (TextView) findViewById(R.id.turn_password);
        mTurnPasswordView.setOnClickListener(view -> {
            isCode = !isCode;
            if (!isCode) {
                mTurnPasswordView.setText("切换验证码登录");
                mGetCodeView.setVisibility(View.GONE);
                mCodeView.setHint("请输入密码");
                mCodeView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mTxRlyt.setVisibility(View.GONE);
                return;
            }
            mTurnPasswordView.setText("切换密码登录");
            mGetCodeView.setVisibility(View.VISIBLE);
            mCodeView.setHint("请输入验证码");
            mCodeView.setInputType(InputType.TYPE_CLASS_NUMBER);
            mTxRlyt.setVisibility(View.VISIBLE);
            return;
        });
    }

    public static void getTxCodeView() {
//        String url = Constant.API_SERVER_LIVE_TEST + "/"+"v1/token/imageCode/read?v=" + System.currentTimeMillis();
//        GlideUrl glideUrl = new GlideUrl(url);
//        Glide.with(LoginActivity.this)
//                .load(glideUrl)
//                .crossFade()
//                .listener(new RequestListener<GlideUrl, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        Log.e(getLocalClassName(), model.getHeaders().get("Set-Cookie"));
//                        Log.e(getApplicationContext().toString(),"资源加载异常");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                        Log.e(getLocalClassName(), model.getHeaders().get("HOST"));
//                        try {
//                            Log.e(getLocalClassName(), model.toURL().getUserInfo()+"");
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//                        Log.e(getApplicationContext().toString(),"图片加载完成");
//                        return false;
//                    }
//                })
//                .into(mGetTxCodeView);

//        Map<String, Object> map = new HashMap<>();
//        map.put("v", System.currentTimeMillis());
//        HttpData.getInstance().HttpDataReadImageCode(new Observer<ResponseBody>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("aae ", e.getMessage());
//
//                Bitmap pic = null;
//                InputStream is = Data.getInputStream();
//                Log.e("is ", is + "");
//                if (is != null) {
////                    pic = BitmapFactory.decodeStream(is);   // 关键是这句代码
////                    Log.e("pic ", pic+"");
////                    mGetTxCodeView.setImageBitmap(pic);
//                    getBitmap(is);
//                }
//            }
//
//            @Override
//            public void onNext(ResponseBody d) {
////                Glide.with(LoginActivity.this)
////                        .load(d)
////                        .crossFade()
////                        .into(mGetTxCodeView);
//                Log.e("aa ", d.toString());
//            }
//        }, map);
////
//        ResponseBody responseBody = Data.getResponseBody();
//        if (responseBody != null)
//            Glide.with(LoginActivity.this)
//                    .load(responseBody)
//                    .crossFade()
//                    .into(mGetTxCodeView);

//        mGetTxCodeView.setImageBitmap(bitmap);


    }

    public static void getBitmap(InputStream inputStream) {
        Bitmap pic = null;
        try {
            pic = BitmapFactory.decodeStream(inputStream);   // 关键是这句代码
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mGetTxCodeView.setImageBitmap(pic);
            }
        }
    };

    //工作线程
    private class WorkThread extends Thread {
        @Override
        public void run() {
            //......处理比较耗时的操作
            getBitmapFromServer(Constant.API_SERVER_LIVE + "/" + "v1/token/imageCode/read?v=" + System.currentTimeMillis());

            //处理完成后给handler发送消息
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
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

    private void getPhoneCode() {
        if (!PhoneUtils.isMobile(mPhoneView.getText().toString().trim())) {
            ToastUtils.show(LoginActivity.this, "手机号格式不正确,请重新输入");
            return;
        }
        timer.start();
//        HttpData.getInstance().HttpDataGetPhoneCode(new Observer<SignUpCodeDto>() {
//            @Override
//            public void onCompleted() {
//                Log.e(TAG, "onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                //设置页面为加载错误
//                ToastUtils.show(LoginActivity.this, e.getMessage());
//            }
//
//            @Override
//            public void onNext(SignUpCodeDto signUpCodeDto) {
//                Log.e(TAG, "onNext sms " + signUpCodeDto.getData().getMsg() + " " + signUpCodeDto.getCode());
//            }
//        }, mPhoneView.getText().toString().trim());

        Map<String, Object> options = new HashMap<>();
        options.put("phone", mPhoneView.getText().toString().trim());
        options.put("imgCode", mTxCodeView.getText().toString().trim());

        HttpData.getInstance().HttpDataGetCode(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(LoginActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(LoginActivity.this, data.getMsg());
//                    getTxCodeView();
                    timer.cancel();
//                    new WorkThread().start();
                    mGetCodeView.setEnabled(true);
                    mGetCodeView.setText("获取验证码");
                    return;
                }
                ToastUtils.show(LoginActivity.this, data.getMsg());
            }
        }, options);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mPhoneView, "Contacts permissions are needed for providing email completions.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mCodeView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = mCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError("手机号不能为空");
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError("手机号格式不正确");
            focusView = mPhoneView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            if (isCode) {
                mCodeView.setError("验证码不能为空");
            } else {
                mCodeView.setError("密码不能为空");
            }
            focusView = mCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(phone, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPhoneValid(String phone) {
        return PhoneUtils.isMobile(phone);
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only phone addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();//?
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mPhoneView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mPassword;
        private Map<String, Object> map = new HashMap<>();

        UserLoginTask(String phone, String password) {
            mPhone = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mPhone)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
//            showProgress(false);

            map.put("phone", mPhone);

            if (isCode) {
                map.put("code", mPassword);
                map.put("source", "android");
                HttpData.getInstance().HttpDataLoginByCode(new Observer<HttpResult3<Object, AccessToken>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LoginActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, AccessToken> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(LoginActivity.this, data.getMsg());
                            showProgress(false);
                            return;
                        }

                        Data.setUserToken(data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                        try {
                            DBUtils.put(LoginActivity.this, "userToken", data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }

                        HttpData.getInstance().HttpDataGetUserInfo(new Observer<HttpResult3<Object, UserInfoDto>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(LoginActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserInfoDto> data2) {
                                if (!data2.getStatus().equals("success")) {
                                    ToastUtils.show(LoginActivity.this, data2.getMsg());
                                    return;
                                }

                                Data.setUserId(data2.getEntity().getId());
                                try {
                                    DBUtils.put(LoginActivity.this, "userId", data2.getEntity().getId() + "");
                                    DBUtils.put(LoginActivity.this, "userName", data2.getEntity().getName() + "");
                                    DBUtils.put(LoginActivity.this, "userNickName", data2.getEntity().getNickName() + "");
                                    DBUtils.put(LoginActivity.this, "authentication", data2.getEntity().getAuthenStatus() + "");
                                    DBUtils.put(LoginActivity.this, "confirmNumber", data2.getEntity().getConfirmNumber() + "");
                                    DBUtils.put(LoginActivity.this, "tokenId", data2.getEntity().getTokenId() + "");
                                } catch (SnappydbException e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.d(TAG, "Login succeed!");
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            }
                        });
                    }
                }, map);


            } else {
                map.put("pwd", mPassword);
                HttpData.getInstance().HttpDataLoginByPwd(new Observer<HttpResult3<Object, AccessToken>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LoginActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3<Object, AccessToken> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(LoginActivity.this, data.getMsg());
                            showProgress(false);
                            return;
                        }

                        Data.setUserToken(data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                        try {
                            DBUtils.put(LoginActivity.this, "userToken", data.getEntity().getTokenType() + "_" + data.getEntity().getAccessToken());
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }

                        HttpData.getInstance().HttpDataGetUserInfo(new Observer<HttpResult3<Object, UserInfoDto>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(LoginActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserInfoDto> data2) {
                                if (!data2.getStatus().equals("success")) {
                                    ToastUtils.show(LoginActivity.this, data2.getMsg());
                                    return;
                                }

                                Data.setUserId(data2.getEntity().getId());
                                try {
                                    DBUtils.put(LoginActivity.this, "userId", data2.getEntity().getId() + "");
                                    DBUtils.put(LoginActivity.this, "userName", data2.getEntity().getName() + "");
                                    DBUtils.put(LoginActivity.this, "userNickName", data2.getEntity().getNickName() + "");
                                    DBUtils.put(LoginActivity.this, "authentication", data2.getEntity().getAuthenStatus() + "");
                                    DBUtils.put(LoginActivity.this, "confirmNumber", data2.getEntity().getConfirmNumber() + "");
                                    DBUtils.put(LoginActivity.this, "tokenId", data2.getEntity().getTokenId() + "");
                                } catch (SnappydbException e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.d(TAG, "Login succeed!");
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            }
                        });
                    }
                }, map);
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pKeyEvent) {
        switch (pKeyCode) {
            case KeyEvent.KEYCODE_BACK:

                return false;   //
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return true;
            default:
                return super.onKeyDown(pKeyCode, pKeyEvent);
        }
    }
}

