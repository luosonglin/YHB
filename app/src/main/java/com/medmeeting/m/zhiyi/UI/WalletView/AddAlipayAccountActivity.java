package com.medmeeting.m.zhiyi.UI.WalletView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.EditAlipayReqEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class AddAlipayAccountActivity extends AppCompatActivity {
    private static final String TAG = AddAlipayAccountActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.accountName)
    EditText accountName;
    @Bind(R.id.accountNumber)
    EditText accountNumber;
    @Bind(R.id.identityNumber)
    EditText identityNumber;
    @Bind(R.id.identityImage)
    ImageView identityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alipay_account);
        ButterKnife.bind(this);

        toolBar();
        
        initView();
    }


    private void initView() {

        if (accountName.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAlipayAccountActivity.this, "请输入收款人姓名");
            return;
        } else if (accountNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAlipayAccountActivity.this, "请输入支付宝账号／手机号码");
            return;
        } else if (identityNumber.getText().toString().trim().equals("")) {
            ToastUtils.show(AddAlipayAccountActivity.this, "请输入身份证号");
            return;
        } else if (identityImage.getDrawable().equals(getResources().getDrawable(R.mipmap.wallet_add_identity_number_icon))) {
            ToastUtils.show(AddAlipayAccountActivity.this, "请上传身份证正面照");
            return;
        } else {
            EditAlipayReqEntity alipay = new EditAlipayReqEntity();
            alipay.setAccountName(accountName.getText().toString().trim());
            alipay.setAccountNumber(accountNumber.getText().toString().trim());
            alipay.setIdentityNumber(identityNumber.getText().toString().trim());
            alipay.setIdentityImage("");

            HttpData.getInstance().HttpDataSetAlipay(new Observer<HttpResult3>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(AddAlipayAccountActivity.this, ""+e.getMessage());
                }

                @Override
                public void onNext(HttpResult3 httpResult3) {
                    if (httpResult3.getStatus().equals("success")) {
                        ToastUtils.show(AddAlipayAccountActivity.this, "绑定成功");
                    }
                }
            }, alipay);
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
