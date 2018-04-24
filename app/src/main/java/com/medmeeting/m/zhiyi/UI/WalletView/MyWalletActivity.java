package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletInfoDto;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class MyWalletActivity extends AppCompatActivity {
    private static final String TAG = MyWalletActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.balance)
    TextView balanceTv;
    @BindView(R.id.apply_btn)
    Button applyBtn;
    @BindView(R.id.trade_detail_btn)
    Button tradeDetailBtn;
    @BindView(R.id.agreement)
    Button radioBtn;
    @BindView(R.id.agreement_llyt)
    LinearLayout agreementLlyt;

    private Handler mHandler2;
    private Runnable mRunnable;

    private Boolean isAgreement = false;
    private String balance;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);

        toolBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpData.getInstance().HttpDataGetWalletInfo(new Observer<HttpResult3<Object, WalletInfoDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                balanceTv.setText("出错啦，" + e.getMessage() + "请联系开发团队");
            }

            @Override
            public void onNext(HttpResult3<Object, WalletInfoDto> walletInfo) {
                if (walletInfo.getStatus().equals("success")) {
                    password = walletInfo.getEntity().getPassword();
                } else {
                    ToastUtils.show(MyWalletActivity.this, walletInfo.getMsg() + "");
                }
            }
        });
    }

    private void initView() {
        mHandler2 = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                balanceTv.setText(String.format("¥ " + new DecimalFormat("0.00").format(Math.random() * 10000)));
                mHandler2.postDelayed(this, 10);
            }
        };
        mHandler2.post(mRunnable);

        HttpData.getInstance().HttpDataGetWalletInfo(new Observer<HttpResult3<Object, WalletInfoDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                balanceTv.setText("出错啦，" + e.getMessage() + "请联系开发团队");
            }

            @Override
            public void onNext(HttpResult3<Object, WalletInfoDto> walletInfo) {
                if (walletInfo.getStatus().equals("success")) {
                    mHandler2.removeCallbacks(mRunnable);
                    balanceTv.setText("¥ " + walletInfo.getEntity().getBalance());
                    balance = walletInfo.getEntity().getBalance() + "";
                    password = walletInfo.getEntity().getPassword();
                } else {
                    ToastUtils.show(MyWalletActivity.this, walletInfo.getMsg() + "");
                }
            }
        });

        radioBtn.setOnClickListener(view -> {
            isAgreement = !isAgreement;
            if (isAgreement)
                radioBtn.setBackground(getResources().getDrawable(R.mipmap.my_wallet_radio_button2));
            else
                radioBtn.setBackground(getResources().getDrawable(R.mipmap.my_wallet_radio_button));
            ToastUtils.show(MyWalletActivity.this, isAgreement + "");
        });

        agreementLlyt.setOnClickListener(view ->
                BrowserActivity.launch(MyWalletActivity.this, "http://webview.medmeeting.com/#/page/payment-protocol", "《代收付协议》"));
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.live_point));
        toolbar.setOnMenuItemClickListener(item -> {
            Intent intent = null;
            switch (item.getItemId()) {
                case R.id.action_setting_apply:
                    intent = new Intent(MyWalletActivity.this, SettingWithdrawActivity.class);
                    break;
                case R.id.action_setting_pay:
                    if (password == null) {
                        intent = new Intent(MyWalletActivity.this, WalletPasswordFirstSettingActivity.class);
                    } else {
                        intent = new Intent(MyWalletActivity.this, WalletPasswordActivity.class);
                    }
                    break;
            }
            startActivity(intent);
            return true;
        });
    }

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wallets, menu);
        return true;
    }

    /**
     * 解决Toolbar中无法显示optionMenu中icon的问题
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler2.removeCallbacks(mRunnable);
    }

    @OnClick({R.id.apply_btn, R.id.trade_detail_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_btn:
                if (password == null) {
                    Intent intent = new Intent(MyWalletActivity.this, WalletPasswordFirstSettingActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyWalletActivity.this, WithdrawActivity.class);
                    intent.putExtra("balance", balance);
                    startActivity(intent);
                }
                break;
            case R.id.trade_detail_btn:
//                BrowserActivity.launch(MyWalletActivity.this, "http://webview.medmeeting.com/#/wallet/record-list", "交易明细");
//                RecordActivity.launch(MyWalletActivity.this, "http://webview.medmeeting.com/#/wallet/record-list", "交易明细");

                startActivity(new Intent(MyWalletActivity.this, WalletWebActivity.class)
                        .putExtra("url", "http://webview.medmeeting.com/#/wallet/record-list"));

                break;
        }
    }
}
