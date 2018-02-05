package com.medmeeting.m.zhiyi.UI.UserInfoView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserGetInfoEntity;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class UpdateUserInfoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.des)
    TextView des;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.hospital)
    EditText hospital;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.position)
    TextView position;
    @BindView(R.id.company)
    EditText company;
    @BindView(R.id.position2)
    EditText position2;
    @BindView(R.id.school)
    EditText school;
    @BindView(R.id.major)
    EditText major;
    @BindView(R.id.education)
    TextView education;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.activate_save)
    TextView activateSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        ButterKnife.bind(this);
        toolBar();
        getUserInfoService();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getUserInfoService() {
        HttpData.getInstance().HttpDataGetUserInfo2(new Observer<HttpResult3<Object, UserGetInfoEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, UserGetInfoEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(UpdateUserInfoActivity.this, data.getMsg());
                    return;
                }

                Glide.with(UpdateUserInfoActivity.this)
                        .load(data.getEntity().getUserPic())
                        .crossFade()
                        .placeholder(R.mipmap.avator_default)
                        .transform(new GlideCircleTransform(UpdateUserInfoActivity.this))
                        .into(avatar);
                name.setText(data.getEntity().getName());
                nickname.setText(data.getEntity().getNickName());
                des.setText(data.getEntity().getDes());
                city.setText(data.getEntity().getCity());
                hospital.setText(data.getEntity().getCompany());
                department.setText(data.getEntity().getDepartment());
                title.setText(data.getEntity().getTitle());
                position.setText(data.getEntity().getPostion());
                name.setText(data.getEntity().getName());
                company.setText(data.getEntity().getCompany());
                position2.setText(data.getEntity().getPostion());
                school.setText(data.getEntity().getCompany());
                major.setText(data.getEntity().getDepartment());
                education.setText(data.getEntity().getDiploma());
                year.setText(data.getEntity().getEntranceDate());
                sex.setText(data.getEntity().getSex());
                email.setText(data.getEntity().getEmail());
            }
        });
    }


    @OnClick({R.id.nickname, R.id.des, R.id.city, R.id.hospital, R.id.department, R.id.title, R.id.position, R.id.education, R.id.year, R.id.sex, R.id.activate_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nickname:
                break;
            case R.id.des:
                break;
            case R.id.city:
                break;
            case R.id.hospital:
                break;
            case R.id.department:
                break;
            case R.id.title:
                break;
            case R.id.position:
                break;
            case R.id.education:
                break;
            case R.id.year:
                break;
            case R.id.sex:
                break;
            case R.id.activate_save:
                break;
        }
    }
}
