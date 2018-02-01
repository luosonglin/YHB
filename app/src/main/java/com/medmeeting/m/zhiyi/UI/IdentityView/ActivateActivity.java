package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddActivationEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 激活页
 */
public class ActivateActivity extends AppCompatActivity {

    @BindView(R.id.activate1)
    TextView activate1;
    @BindView(R.id.activate2)
    TextView activate2;
    @BindView(R.id.activate3)
    TextView activate3;
    @BindView(R.id.activate4)
    TextView activate4;
    @BindView(R.id.activate5)
    TextView activate5;
    @BindView(R.id.activate6)
    TextView activate6;
    @BindView(R.id.activate_1)
    LinearLayout activate_1;
    @BindView(R.id.activate_2)
    LinearLayout activate_2;
    @BindView(R.id.activate_3)
    LinearLayout activate_3;
    @BindView(R.id.activate_4)
    LinearLayout activate_4;
    @BindView(R.id.activate_5)
    LinearLayout activate_5;

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.company1)
    EditText company1;
    @BindView(R.id.position1)
    EditText position1;
    @BindView(R.id.hospital)
    EditText hospital;
    @BindView(R.id.department2)
    EditText department2;
    @BindView(R.id.position2)
    EditText position2;
    @BindView(R.id.title2)
    EditText title2;
    @BindView(R.id.company3)
    EditText company3;
    @BindView(R.id.department3)
    EditText department3;
    @BindView(R.id.position3)
    EditText position3;
    @BindView(R.id.school)
    EditText school;
    @BindView(R.id.major)
    EditText major;
    @BindView(R.id.education)
    EditText education;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.company5)
    EditText company5;
    @BindView(R.id.department5)
    EditText department5;
    @BindView(R.id.position5)
    EditText position5;
    private Toolbar toolbar;

    UserAddActivationEntity userAddActivationEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        ButterKnife.bind(this);
        toolBar();

        activate_1.setVisibility(View.VISIBLE);
        activate_2.setVisibility(View.GONE);
        activate_3.setVisibility(View.GONE);
        activate_4.setVisibility(View.GONE);
        activate_5.setVisibility(View.GONE);

        new AlertDialog.Builder(ActivateActivity.this)
                .setIcon(R.mipmap.logo)
                .setTitle("")
                .setMessage("您填写的信息仅用于激活医会宝账号。医会宝将严格保密您的个人信息，激活成功后可正常参与评论点赞，转发；关注大咖。")
                .setPositiveButton("知道了", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnClick({R.id.activate1, R.id.activate2, R.id.activate3, R.id.activate4, R.id.activate5, R.id.activate6, R.id.activate_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activate1:
                activate1.setBackgroundResource(R.mipmap.activate_bg);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.VISIBLE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("ASSOCIATION");
                userAddActivationEntity.setIdentityTitle("医疗协会");

                userAddActivationEntity.setName(name.getText().toString().trim());
                userAddActivationEntity.setCompany(company1.getText().toString().trim());
                userAddActivationEntity.setPostion(position1.getText().toString().trim());

                break;
            case R.id.activate2:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.VISIBLE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("MEDICAL_STAFF");
                userAddActivationEntity.setIdentityTitle("医护人员");

                userAddActivationEntity.setName(name.getText().toString().trim());
                userAddActivationEntity.setCompany(hospital.getText().toString().trim());
                userAddActivationEntity.setDepartment(department2.getText().toString().trim());
                userAddActivationEntity.setPostion(position2.getText().toString().trim());
                userAddActivationEntity.setTitle(title2.getText().toString().trim());
                break;
            case R.id.activate3:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.VISIBLE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("MEDICAL_COMPANY");
                userAddActivationEntity.setIdentityTitle("药械企业");

                userAddActivationEntity.setName(name.getText().toString().trim());
                userAddActivationEntity.setCompany(company3.getText().toString().trim());
                userAddActivationEntity.setDepartment(department3.getText().toString().trim());
                userAddActivationEntity.setPostion(position3.getText().toString().trim());

                break;
            case R.id.activate4:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.VISIBLE);
                activate_5.setVisibility(View.GONE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("MEDICO");
                userAddActivationEntity.setIdentityTitle("医学生");

                userAddActivationEntity.setName(name.getText().toString().trim());
                userAddActivationEntity.setCompany(school.getText().toString().trim());
                userAddActivationEntity.setDepartment(major.getText().toString().trim());
                userAddActivationEntity.setDiploma(education.getText().toString().trim());
                userAddActivationEntity.setEntranceDate(year.getText().toString().trim());
                break;
            case R.id.activate5:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg);
                activate6.setBackgroundResource(R.mipmap.activate_bg2);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.VISIBLE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("EDUCATION_SCIENCE");
                userAddActivationEntity.setIdentityTitle("医药教科研人员");

                userAddActivationEntity.setName(name.getText().toString().trim());
                userAddActivationEntity.setCompany(company5.getText().toString().trim());
                userAddActivationEntity.setDepartment(department5.getText().toString().trim());
                userAddActivationEntity.setPostion(position5.getText().toString().trim());
                break;
            case R.id.activate6:
                activate1.setBackgroundResource(R.mipmap.activate_bg2);
                activate2.setBackgroundResource(R.mipmap.activate_bg2);
                activate3.setBackgroundResource(R.mipmap.activate_bg2);
                activate4.setBackgroundResource(R.mipmap.activate_bg2);
                activate5.setBackgroundResource(R.mipmap.activate_bg2);
                activate6.setBackgroundResource(R.mipmap.activate_bg);

                activate_1.setVisibility(View.GONE);
                activate_2.setVisibility(View.GONE);
                activate_3.setVisibility(View.GONE);
                activate_4.setVisibility(View.GONE);
                activate_5.setVisibility(View.GONE);

                userAddActivationEntity = new UserAddActivationEntity();
                userAddActivationEntity.setIdentityCode("OTHER");
                userAddActivationEntity.setIdentityTitle("其他人员");

                userAddActivationEntity.setName(name.getText().toString().trim());
                break;
            case R.id.activate_save:
//                HttpData.getInstance().HttpDataActivate(new Observer<HttpResult3>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.show(ActivateActivity.this, e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(HttpResult3 data) {
//                        if (!data.getStatus().equals("success")) {
//                            ToastUtils.show(ActivateActivity.this, data.getMsg());
//                            return;
//                        }
//                        startActivity(new Intent(ActivateActivity.this, ActivatedActivity.class ));
//                    }
//                }, userAddActivationEntity);

                startActivity(new Intent(ActivateActivity.this, ActivatedActivity.class ));
                break;
        }
    }
}
