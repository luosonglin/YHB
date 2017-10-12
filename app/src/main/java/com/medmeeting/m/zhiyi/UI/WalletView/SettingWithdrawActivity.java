package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class SettingWithdrawActivity extends AppCompatActivity {
    private static final String TAG = SettingWithdrawActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.account_number_add_status)
    TextView accountNumberAddStatus;
    @Bind(R.id.account_number_rlyt)
    RelativeLayout accountNumberRlyt;
    @Bind(R.id.alipay_add_status)
    TextView alipayAddStatus;
    @Bind(R.id.alipay_rlyt)
    RelativeLayout alipayRlyt;
    @Bind(R.id.account_number_add_private_status)
    TextView accountNumberAddPrivateStatus;
    @Bind(R.id.account_number_private_rlyt)
    RelativeLayout accountNumberPrivateRlyt;
    @Bind(R.id.alipay_add_private_status)
    TextView alipayAddPrivateStatus;
    @Bind(R.id.alipay_private_rlyt)
    RelativeLayout alipayPrivateRlyt;

    private WalletAccountDto publicBank = null;
    private WalletAccountDto publicAlipay = null;
    private WalletAccountDto privateBank = null;
    private WalletAccountDto privateAlipay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_withdraw);
        ButterKnife.bind(this);

        toolBar();

        initView();
    }

    private void initView() {
        HttpData.getInstance().HttpDataGetWalletAccount(new Observer<HttpResult3<WalletAccountDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<WalletAccountDto, Object> walletAccount) {

                publicBank = walletAccount.getData().get(2);
                publicAlipay = walletAccount.getData().get(3);
                privateBank = walletAccount.getData().get(0);
                privateAlipay = walletAccount.getData().get(1);

                if (publicBank != null) {
                    accountNumberAddStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
                    accountNumberAddStatus.setText("已添加");
                    accountNumberAddStatus.setTextColor(getResources().getColor(R.color.white));
                }
                if (publicAlipay != null) {
                    alipayAddStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
                    alipayAddStatus.setText("已添加");
                    alipayAddStatus.setTextColor(getResources().getColor(R.color.white));
                }
                if (privateBank != null) {
                    accountNumberAddPrivateStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
                    accountNumberAddPrivateStatus.setText("已添加");
                    accountNumberAddPrivateStatus.setTextColor(getResources().getColor(R.color.white));
                }
                if (privateAlipay != null) {
                    alipayAddPrivateStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
                    alipayAddPrivateStatus.setText("已添加");
                    alipayAddPrivateStatus.setTextColor(getResources().getColor(R.color.white));
                }
//                for (WalletAccountDto i: walletAccount.getData()) {
//                    if (i.getPublicPrivateType().equals("PUBLIC")) {
//                        if (i.getAccountType().equals("BANK")) {
//                            accountNumberAddStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
//                            accountNumberAddStatus.setText("已添加");
//                            accountNumberAddStatus.setTextColor(getResources().getColor(R.color.white));
//                        } else if (i.getAccountType().equals("ALIPAY")) {
//                            alipayAddStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
//                            alipayAddStatus.setText("已添加");
//                            alipayAddStatus.setTextColor(getResources().getColor(R.color.white));
//                        }
//                    } else if (i.getPublicPrivateType().equals("PRIVATE")) {
//                        if (i.getAccountType().equals("BANK")) {
//                            accountNumberAddPrivateStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
//                            accountNumberAddPrivateStatus.setText("已添加");
//                            accountNumberAddPrivateStatus.setTextColor(getResources().getColor(R.color.white));
//                        } else if (i.getAccountType().equals("ALIPAY")) {
//                            alipayAddPrivateStatus.setBackground(getResources().getDrawable(R.drawable.textview_all_blue2));
//                            alipayAddPrivateStatus.setText("已添加");
//                            alipayAddPrivateStatus.setTextColor(getResources().getColor(R.color.white));
//                        }
//                    }
//                }
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
    }

    @OnClick({R.id.account_number_rlyt, R.id.alipay_rlyt, R.id.account_number_private_rlyt, R.id.alipay_private_rlyt})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.account_number_rlyt:
                if (accountNumberAddStatus.getText().toString().equals("已添加")) {
                    intent = new Intent(SettingWithdrawActivity.this, BankAccountNumberModifyActivity.class);
                    intent.putExtra("walletAccount", publicBank);
                } else {
                    intent = new Intent(SettingWithdrawActivity.this, BankAccountNumberAddActivity.class);
                    intent.putExtra("publicPrivateType", "PUBLIC");
                }
                break;
            case R.id.alipay_rlyt:
                if (alipayAddStatus.getText().toString().equals("已添加")) {
                    intent = new Intent(SettingWithdrawActivity.this, AlipayAccountModifyActivity.class);
                    intent.putExtra("walletAccount", publicAlipay);
                } else {
                    intent = new Intent(SettingWithdrawActivity.this, AlipayAccountAddActivity.class);
                    intent.putExtra("publicPrivateType", "PUBLIC");
                }
                break;
            case R.id.account_number_private_rlyt:
                if (accountNumberAddPrivateStatus.getText().toString().equals("已添加")) {
                    intent = new Intent(SettingWithdrawActivity.this, BankAccountNumberModifyActivity.class);
                    intent.putExtra("walletAccount", privateBank);
                } else {
                    intent = new Intent(SettingWithdrawActivity.this, BankAccountNumberAddActivity.class);
                    intent.putExtra("publicPrivateType", "PRIVATE");
                }
                break;
            case R.id.alipay_private_rlyt:
                if (alipayAddPrivateStatus.getText().toString().equals("已添加")) {
                    intent = new Intent(SettingWithdrawActivity.this, AlipayAccountModifyActivity.class);
                    intent.putExtra("walletAccount", publicAlipay);
                } else {
                    intent = new Intent(SettingWithdrawActivity.this, AlipayAccountAddActivity.class);
                    intent.putExtra("publicPrivateType", "PRIVATE");
                }
                break;
        }
        startActivity(intent);
    }

}
