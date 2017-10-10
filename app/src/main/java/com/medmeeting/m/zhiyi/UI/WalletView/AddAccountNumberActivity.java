package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditBankCardReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class AddAccountNumberActivity extends AppCompatActivity {
    private static final String TAG = AddAccountNumberActivity.class.getSimpleName();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_number);
        ButterKnife.bind(this);
        toolBar();

        initView();
    }

    private void initView() {

        if (accountName.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入收款人姓名");
            return;
        } else if (bankName.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入银行名称");
            return;
        } else if (bankAddress.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入开户支行");
            return;
        } else if (accountNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入卡号");
            return;
        } else if (mobilePhone.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入手机号");
            return;
        } else if (identityNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAccountNumberActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.IDNumberBGImage))) {
            ToastUtils.show(AddAccountNumberActivity.this, "请上传身份证正面照");
            return;
        } else {
            EditBankCardReqEntity bankCard = new EditBankCardReqEntity();
            bankCard.setAccountName(accountName.getText().toString().trim());
            bankCard.setBankName(bankName.getText().toString().trim());
            bankCard.setBankAddress(bankAddress.getText().toString().trim());
            bankCard.setAccountNumber(accountNumber.getText().toString().trim());
            bankCard.setMobilePhone(mobilePhone.getText().toString().trim());
            bankCard.setIdentityNumber(identityNumber.getText().toString().trim());
            bankCard.setIdentityImage("");

            HttpData.getInstance().HttpDataSetBankCard(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(AddAccountNumberActivity.this, ""+e.getMessage());
                }

                @Override
                public void onNext(HttpResult3 httpResult3) {
                    if (httpResult3.getStatus().equals("success")) {
                        ToastUtils.show(AddAccountNumberActivity.this, "绑定成功");
                    }
                }
            }, bankCard);
        }
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

    @OnClick(R.id.identityImage)
    public void onClick() {
    }
}
