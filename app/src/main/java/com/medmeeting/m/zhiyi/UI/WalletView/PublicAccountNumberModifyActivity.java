package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.WalletAccountDto;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicAccountNumberModifyActivity extends AppCompatActivity {
    private static final String TAG = PublicAccountNumberModifyActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.accountName)
    EditText accountName;
    @Bind(R.id.bankName)
    EditText bankName;
    @Bind(R.id.bankAddress)
    EditText bankAddress;
    @Bind(R.id.accountNumber)
    EditText accountNumber;
    @Bind(R.id.mobilePhone)
    EditText mobilePhone;
    @Bind(R.id.identityNumber)
    EditText identityNumber;
    @Bind(R.id.identityImage)
    ImageView identityImage;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_textview)
    TextView getCodeTextview;
    @Bind(R.id.confirm)
    TextView confirm;

    private WalletAccountDto walletAccountDto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account_number);
        ButterKnife.bind(this);
        toolBar();
        initView();
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

    private void initView() {
        walletAccountDto = (WalletAccountDto) getIntent().getSerializableExtra("walletAccount");
        accountName.setHint(walletAccountDto.getAccountName());
        bankName.setHint(walletAccountDto.getBankName());
        bankAddress.setHint(walletAccountDto.getBankAddress());
        accountNumber.setHint(walletAccountDto.getAccountNumber());
        mobilePhone.setHint(walletAccountDto.getMobilePhone());
        identityNumber.setHint(walletAccountDto.getIdentityNumber());
        Glide.with(PublicAccountNumberModifyActivity.this)
                .load(walletAccountDto.getIdentityImage())
                .crossFade()
                .placeholder(R.mipmap.wallet_add_identity_number_icon)
                .into(identityImage);
    }

    @OnClick({R.id.get_code_textview, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_textview:
                break;
            case R.id.confirm:
                break;
        }
    }
}
