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

public class ExtractTypeActivity extends AppCompatActivity {
    private static final String TAG = ExtractTypeActivity.class.getSimpleName();
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
    @Bind(R.id.private_bank)
    TextView privateBankTv;
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
        setContentView(R.layout.activity_extract_type);
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

                if (publicAlipay != null) {
                    alipayAddStatus.setText(publicAlipay.getAccountName());
                }

                if (privateAlipay != null) {
                    alipayAddPrivateStatus.setText(privateAlipay.getAccountName());
                }

                if (publicBank != null) {
                    accountNumberAddStatus.setText(publicBank.getBankName() + "("
                            + publicBank.getAccountNumber().substring((publicBank.getAccountNumber().length() - 4), publicBank.getAccountNumber().length())
                            + ")");
                }

                if (privateBank != null) {
                    privateBankTv.setText(privateBank.getBankName() + "("
                            + privateBank.getAccountNumber().substring((privateBank.getAccountNumber().length() - 4), privateBank.getAccountNumber().length())
                            + ")");
                }
            }
        });
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.account_number_rlyt, R.id.alipay_rlyt, R.id.account_number_private_rlyt, R.id.alipay_private_rlyt})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.account_number_rlyt:
                Intent intent = new Intent();
                if (publicBank == null) {
                    intent.setClass(ExtractTypeActivity.this, BankAccountNumberAddActivity.class);
                    intent.putExtra("publicPrivateType", "PUBLIC");
                    startActivity(intent);
                    return;
                }
                intent.putExtra("withdraw_bank", accountNumberAddStatus.getText().toString().trim());
                intent.putExtra("extractNumber", publicBank.getAccountNumber());
                intent.putExtra("extractType", "CARD");
                intent.putExtra("withdrawType", "public");
                intent.putExtra("walletId", publicBank.getId());
                setResult(1, intent);
                finish();
                break;
            case R.id.alipay_rlyt:
                Intent intent2 = new Intent();
                if (publicAlipay == null) {
                    intent2.setClass(ExtractTypeActivity.this, AlipayAccountAddActivity.class);
                    intent2.putExtra("publicPrivateType", "PUBLIC");
                    startActivity(intent2);
                    return;
                }
                intent2.putExtra("withdraw_bank", alipayAddStatus.getText().toString().trim());
                intent2.putExtra("extractNumber", publicAlipay.getAccountNumber());
                intent2.putExtra("extractType", "ALIPAY");
                intent2.putExtra("withdrawType", "public");
                intent2.putExtra("walletId", publicAlipay.getId());
                setResult(1, intent2);
                finish();
                break;
            case R.id.account_number_private_rlyt:
                Intent intent3 = new Intent();
                if (privateBank == null) {
                    intent3.setClass(ExtractTypeActivity.this, BankAccountNumberAddActivity.class);
                    intent3.putExtra("publicPrivateType", "PRIVATE");
                    startActivity(intent3);
                    return;
                }
                intent3.putExtra("withdraw_bank", privateBankTv.getText().toString().trim());
                intent3.putExtra("extractNumber", privateBank.getAccountNumber());
                intent3.putExtra("extractType", "CARD");
                intent3.putExtra("withdrawType", "private");
                intent3.putExtra("walletId", privateBank.getId());
                setResult(1, intent3);
                finish();
                break;
            case R.id.alipay_private_rlyt:
                Intent intent4 = new Intent();
                if (privateAlipay == null) {
                    intent4.setClass(ExtractTypeActivity.this, AlipayAccountModifyActivity.class);
                    intent4.putExtra("publicPrivateType", "PRIVATE");
                    startActivity(intent4);
                    return;
                }
                intent4.putExtra("withdraw_bank", alipayAddPrivateStatus.getText().toString().trim());
                intent4.putExtra("extractNumber", privateAlipay.getAccountNumber());
                intent4.putExtra("extractType", "ALIPAY");
                intent4.putExtra("withdrawType", "private");
                intent4.putExtra("walletId", privateAlipay.getId());
                setResult(1, intent4);
                finish();
                break;
        }
    }
}
