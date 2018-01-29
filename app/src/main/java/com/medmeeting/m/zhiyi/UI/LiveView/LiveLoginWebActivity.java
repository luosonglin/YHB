package com.medmeeting.m.zhiyi.UI.LiveView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveLoginWebDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class LiveLoginWebActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_live_login_web)
    LinearLayout contentLiveLoginWeb;
    @BindView(R.id.live_login_web_confirm)
    Button liveLoginWebConfirm;
    @BindView(R.id.live_login_web_cancel)
    Button liveLoginWebCancel;

    private String QRCode;
    private String extend;
    private static final String TAG = LiveLoginWebActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_login_web);
        ButterKnife.bind(this);

        initToolbar("直播登陆");

        initView();
    }

    private void initToolbar(String title) {
        toolbarTitleTv.setText(title);
        toolbarTitleTv.setTextColor(Color.BLACK);
        toolbarTitleTv.setFocusable(true);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        QRCode = getIntent().getExtras().getString("QRCode");
        extend = getIntent().getExtras().getString("extend");
    }

    @OnClick({R.id.live_login_web_confirm, R.id.live_login_web_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_login_web_confirm:
//                Map<String, Object> map = new HashMap<>();
//                map.put("token", QRCode);
//                HttpManager.generate(LiveService.class, LiveLoginWebActivity.this)
//                        .loginWeb(map)
//                        .enqueue(new Callback<ResponseQRWeb>() {
//                            @Override
//                            public void onResponse(Call<ResponseQRWeb> call, Response<ResponseQRWeb> response) {
//                                if (!response.body().getReturnCode().equals("success")) {
//                                    ToastUtils.show(LiveLoginWebActivity.this, response.body().getReturnMsg());
//                                    return;
//                                }
//                                ToastUtils.show(LiveLoginWebActivity.this, "登录成功");
//                                LiveLoginWebActivity.this.finish();
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseQRWeb> call, Throwable t) {
//                                ToastUtils.show(LiveLoginWebActivity.this, "登录失败 " + t.getMessage());
//                                Log.e(TAG, t.getMessage());
//                                LiveLoginWebActivity.this.finish();
//                            }
//                        });
                if (QRCode == null || extend == null) return;
                LiveLoginWebDto liveLoginWebDto = new LiveLoginWebDto(extend, QRCode);
                HttpData.getInstance().HttpDataLoginWeb(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(LiveLoginWebActivity.this, "登录成功");
                            finish();
                        } else {
                            ToastUtils.show(LiveLoginWebActivity.this, httpResult3.getMsg());
                        }
                    }
                }, liveLoginWebDto);
                break;
            case R.id.live_login_web_cancel:
                LiveLoginWebActivity.this.finish();
                break;
        }
    }
}
