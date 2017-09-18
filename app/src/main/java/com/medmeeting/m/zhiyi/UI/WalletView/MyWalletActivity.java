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
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveUpdateProgramActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class MyWalletActivity extends AppCompatActivity {
    private static final String TAG = MyWalletActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.apply_btn)
    Button applyBtn;
    @Bind(R.id.trade_detail_btn)
    Button tradeDetailBtn;
    private TextView balanceTv;
    private Handler mHandler2;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);

        toolBar();
        initView();
    }

    private void initView() {
        balanceTv = (TextView) findViewById(R.id.balance);

        mHandler2 = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                balanceTv.setText(String.format("¥ " + new DecimalFormat("0.00").format(Math.random() * 10000)));
                mHandler2.postDelayed(this, 10);
            }
        };
        mHandler2.post(mRunnable);


        HttpData.getInstance().HttpDataGetWalletInfo(new Observer<HttpResult3<Object, WalletDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, WalletDto> walletInfo) {
                if (walletInfo.getStatus().equals("success")) {
                    mHandler2.removeCallbacks(mRunnable);
                    balanceTv.setText("¥ " + walletInfo.getEntity().getBalance());
                } else {
                    ToastUtils.show(MyWalletActivity.this, walletInfo.getMsg() + "");
                }
            }
        });
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.live_point));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_setting_apply:
                        Intent intent = new Intent(MyWalletActivity.this, SettingWithdrawActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_setting_pay:
                        Intent intent2 = new Intent(MyWalletActivity.this, LiveUpdateProgramActivity.class);
                        intent2.putExtra("programId", getIntent().getExtras().getInt("programId"));
                        startActivity(intent2);
                        break;
                }
                return true;
            }
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
                startActivity(new Intent(MyWalletActivity.this, WithdrawActivity.class));
                break;
            case R.id.trade_detail_btn:
                break;
        }
    }
}
