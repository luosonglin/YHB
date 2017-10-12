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

                if (publicBank != null) {
                    accountNumberAddStatus.setText(publicBank.getBankName() + "(" + publicBank.getAccountNumber().substring((publicBank.getAccountNumber().length()-4), publicBank.getAccountNumber().length()) + ")");
                }
                if (publicAlipay != null) {
                    alipayAddStatus.setText(publicAlipay.getAccountName());
                }
                if (privateBank != null) {
                    accountNumberAddPrivateStatus.setText(privateBank.getBankName() + "(" + privateBank.getAccountNumber().substring((privateBank.getAccountNumber().length()-4), privateBank.getAccountNumber().length()) + ")");
                }
                if (privateAlipay != null) {
                    alipayAddPrivateStatus.setText(privateAlipay.getAccountName());
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
    }

    @OnClick({R.id.account_number_rlyt, R.id.alipay_rlyt, R.id.account_number_private_rlyt, R.id.alipay_private_rlyt})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.account_number_rlyt:
                intent.putExtra("extractType", accountNumberAddStatus.getText().toString().trim());
                intent.putExtra("extractNumber", publicBank.getAccountNumber());
                intent.putExtra("extractType", "CARD");
                intent.putExtra("withdrawType", "public");
                intent.putExtra("walletId", publicBank.getId());
                break;
            case R.id.alipay_rlyt:
                intent.putExtra("extractType", alipayAddStatus.getText().toString().trim());
                intent.putExtra("extractNumber", publicAlipay.getAccountNumber());
                intent.putExtra("extractType", "ALIPAY");
                intent.putExtra("withdrawType", "public");
                intent.putExtra("walletId", publicAlipay.getId());
                break;
            case R.id.account_number_private_rlyt:
                intent.putExtra("extractType", accountNumberAddPrivateStatus.getText().toString().trim());
                intent.putExtra("extractNumber", privateBank.getAccountNumber());
                intent.putExtra("extractType", "CARD");
                intent.putExtra("withdrawType", "private");
                intent.putExtra("walletId", privateBank.getId());
                break;
            case R.id.alipay_private_rlyt:
                intent.putExtra("extractType", alipayAddPrivateStatus.getText().toString().trim());
                intent.putExtra("extractNumber", privateAlipay.getAccountNumber());
                intent.putExtra("extractType", "ALIPAY");
                intent.putExtra("withdrawType", "private");
                intent.putExtra("walletId", privateAlipay.getId());
                break;
        }
        setResult(1, intent);
        finish();
    }
}
