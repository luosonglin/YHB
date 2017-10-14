package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.ExtractEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.TallageDto;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class WithdrawActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.extractType)
    TextView extractType;
    @Bind(R.id.extract_type_rlyt)
    RelativeLayout extractTypeRlyt;
    @Bind(R.id.amount)
    EditText amount;
    @Bind(R.id.all_extract_action)
    TextView allExtractAction;
    @Bind(R.id.actual_arrival)
    TextView actualArrival;
    @Bind(R.id.tax)
    TextView tax;
    @Bind(R.id.extractPassword)
    EditText extractPassword;
    @Bind(R.id.confirm)
    Button confirm;

    private ExtractEntity extractEntity = new ExtractEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);

        initToolbar();
        initView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_help:
                    BrowserActivity.launch(WithdrawActivity.this, "http://webview.medmeeting.com/#/wallet/help", "帮助");
                    break;
            }
            return true;
        });
    }

    private void initView() {
        amount.setHint("可提现 " + getIntent().getStringExtra("balance") + "元");
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 输入前的监听
                Log.e("输入前确认执行该方法", "开始输入");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 输入的内容变化的监听
                Log.e("输入过程中执行该方法", "文字变化");

                if (!charSequence.toString().equals("")) {
                    HttpData.getInstance().HttpDataGetTallage(new Observer<HttpResult3<Object, TallageDto>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpResult3<Object, TallageDto> result) {
                            if (!result.getStatus().equals("success")) {
                                ToastUtils.show(WithdrawActivity.this, result.getMsg());
                                return;
                            }
                            actualArrival.setText("实际到账：" + Html.fromHtml("<font color='#00BFFF'>" + result.getEntity().getAmount() + "</font>" + "元"));
                            tax.setText("扣税：" + Html.fromHtml("<font color='#00BFFF'>" + result.getEntity().getTallages() + "</font>" + "元"));
                        }
                    }, Double.parseDouble(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 输入后的监听
                Log.e("输入结束执行该方法", "输入结束");
            }
        });
    }

    /**
     * 菜单栏 修改器下拉刷新模式
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet_withdraw_help, menu);
        return true;
    }

    @OnClick({R.id.extract_type_rlyt, R.id.all_extract_action, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.extract_type_rlyt:
                startActivityForResult(new Intent(WithdrawActivity.this, ExtractTypeActivity.class), 0);
                break;
            case R.id.all_extract_action:
                amount.setText(getIntent().getStringExtra("balance"));
                break;
            case R.id.confirm:
                extractEntity.setUserId(Data.getUserId());
                extractEntity.setAmount(Double.parseDouble(amount.getText().toString().trim()));
                extractEntity.setExtractPassword(extractPassword.getText().toString().trim());
                HttpData.getInstance().HttpDataWithdraw(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!httpResult3.getStatus().equals("success")) {
                            ToastUtils.show(WithdrawActivity.this, httpResult3.getMsg());
                            return;
                        }
                        Intent i = new Intent(WithdrawActivity.this, WithdrawStatusActivity.class);
                        i.putExtra("amount",  amount.getText().toString().trim());
                        startActivity(i);
                    }
                }, extractEntity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 1) {
            extractType.setText(data.getStringExtra("withdraw_bank"));

            extractEntity.setExtractNumber(data.getStringExtra("extractNumber"));
            extractEntity.setExtractType(data.getStringExtra("extractType"));
            extractEntity.setWithdrawType(data.getStringExtra("withdrawType"));
            extractEntity.setWalletId(data.getIntExtra("walletId", 0));
        }

    }
}
