package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserAuthenRecord;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * 认证完成页
 */
public class AuthorizedActivity extends AppCompatActivity {
    @BindView(R.id.name_et)
    TextView name;
    @BindView(R.id.type1)
    TextView type;
    @BindView(R.id.phone1)
    TextView phone;
    @BindView(R.id.update)
    TextView update;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.rejected)
    TextView rejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized);
        ButterKnife.bind(this);

        toolBar();

        getLastAuthorizeInfoService();
    }

    private void getLastAuthorizeInfoService() {
        HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(AuthorizedActivity.this, data.getMsg());
                    return;
                }

                name.setText(data.getEntity().getUserName());
                type.setText(data.getEntity().getCategoryName());
                phone.setText(data.getEntity().getMobilePhone());

                switch (data.getEntity().getStatus()) {     //认证状态（A:已认证，B:待认证,X:未通过）
                    case "A":
                        info.setText("  您的实名认证已审核通过");
                        rejected.setVisibility(View.GONE);
                        tip.setText(Html.fromHtml("<font color='#000000'>您已获得认证权益，</font><font color='#87CEEB'>点击查看</font>"));
                        tip.setOnClickListener(view -> startActivity(new Intent(AuthorizedActivity.this, RightsActivity.class)));
                        break;
                    case "B":
                        info.setText("  您的医会宝认证申请已提交");
                        tip.setText(Html.fromHtml("我们将在5个工作日内进行审核，请您留意电话、短息、消息中心或邮件通知"));
                        rejected.setVisibility(View.GONE);
                        break;
                    case "X":
                        info.setText("很抱歉，您的认证申请因为以下原因未审核通过");
                        info.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.authorize_status2), null, null, null);
                        rejected.setVisibility(View.VISIBLE);
                        tip.setText(Html.fromHtml("<font color='#000000'>通过后，您将获得认证权益，</font><font color='#87CEEB'>点击查看</font>"));
                        tip.setOnClickListener(view -> startActivity(new Intent(AuthorizedActivity.this, RightsActivity.class)));
                        break;
                }

            }
        });
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
