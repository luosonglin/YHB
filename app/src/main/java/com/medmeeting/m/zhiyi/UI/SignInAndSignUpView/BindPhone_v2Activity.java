package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
import com.medmeeting.m.zhiyi.UI.Entity.UserOpenAddEntity;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
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
 * @describe 绑定手机号
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BindPhone_v2Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.agree)
    CheckBox agree;
    @BindView(R.id.agreement)
    TextView agreement;
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
        setContentView(R.layout.activity_bind_phone_v2);
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
                if (phone.getText().toString().trim().equals("")) {
                    ToastUtils.show(BindPhone_v2Activity.this, "请先填写手机号");
                    return;
                }
                if (!PhoneUtils.isMobile(phone.getText().toString().trim())) {
                    ToastUtils.show(BindPhone_v2Activity.this, "手机号格式不正确,请重新输入");
                    return;
                }
                initCodePopupwindow();
                new WorkThread().start();
                break;
            case R.id.agreement:
                BrowserActivity.launch(BindPhone_v2Activity.this, "http://webview.medmeeting.com/#/page/live-protocol", "《直播协议》");
                break;
            case R.id.confirm:
                UserOpenAddEntity userOpenAddEntity = new UserOpenAddEntity();
                userOpenAddEntity.setPhone(phone.getText().toString().trim());
                userOpenAddEntity.setCode(code.getText().toString().trim());
                userOpenAddEntity.setUserSource("android");



                HttpData.getInstance().HttpDataAddThird(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {

                    }
                }, userOpenAddEntity);
                break;
        }
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
        codeTv.setOnClickListener(view -> new BindPhone_v2Activity.WorkThread().start());

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
                    ToastUtils.show(BindPhone_v2Activity.this, e.getMessage());
                    timer.cancel();
                }

                @Override
                public void onNext(HttpResult3 data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(BindPhone_v2Activity.this, data.getMsg());

                        timer.cancel();

                        getCode.setEnabled(true);
                        getCode.setText("获取验证码");
                        return;
                    }
                    timer.start();
                    codePopupwindow.dismiss();
                    ToastUtils.show(BindPhone_v2Activity.this, data.getMsg());
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
