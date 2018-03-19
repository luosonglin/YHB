package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IdentityTypeAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddAuthenEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserIdentity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.snappydb.SnappydbException;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

/**
 * 认证页（设置基本信息）
 */
public class AuthorizeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.identity_name)
    TextView identityName;

    List<UserIdentity> userIdentities = new ArrayList<>();
    @BindView(R.id.identity_1)
    LinearLayout identity1;
    @BindView(R.id.identity_2)
    LinearLayout identity2;
    @BindView(R.id.identity_3)
    LinearLayout identity3;


    @BindView(R.id.authorize1)
    LinearLayout authorize1;
    @BindView(R.id.authorize2)
    LinearLayout authorize2;
    @BindView(R.id.authorize3)
    LinearLayout authorize3;
    @BindView(R.id.authorize4)
    LinearLayout authorize4;
    @BindView(R.id.authorize5)
    LinearLayout authorize5;


    @BindView(R.id.company)
    EditText company;
    @BindView(R.id.position)
    EditText position;
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


    @BindView(R.id.material)
    TextView material;
    @BindView(R.id.work_photo1)
    ImageView workPhoto1;
    @BindView(R.id.work_photo2)
    ImageView workPhoto2;


    private UserAddAuthenEntity userAddAuthenEntity = new UserAddAuthenEntity();
    private String Category;
    private String workPhoto;
    private String identityPhoto;
    private int photoType = 0; //0为上传的是工作证，1为身份证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        ButterKnife.bind(this);

        toolBar();

        identity1.setVisibility(View.VISIBLE);
        identity2.setVisibility(View.GONE);
        identity3.setVisibility(View.GONE);

        authorize1.setVisibility(View.VISIBLE);
        authorize2.setVisibility(View.GONE);
        authorize3.setVisibility(View.GONE);
        authorize4.setVisibility(View.GONE);
        authorize5.setVisibility(View.GONE);

//        phone.setText(Data.getPhone());
        try {
            phone.setText(DBUtils.get(AuthorizeActivity.this, "phone"));
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);

        if (getIntent().getExtras().getString("CategoryName").equals("")) {
            //从未认证过
            identityName.setText("医疗协会");
            Category = "ASSOCIATION";
            userAddAuthenEntity.setCategory(Category);
            userAddAuthenEntity.setCategoryName("医疗协会");
        } else {
            //已认证过
            identityName.setText(getIntent().getExtras().getString("CategoryName"));
            Category = getIntent().getExtras().getString("Category");
            userAddAuthenEntity.setCategory(Category);
            userAddAuthenEntity.setCategoryName(getIntent().getExtras().getString("CategoryName"));
        }
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.rights, R.id.identity_name, R.id.next1, R.id.next21, R.id.next22, R.id.material, R.id.work_photo1, R.id.work_photo2, R.id.next31, R.id.next32})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rights:
                startActivity(new Intent(AuthorizeActivity.this, RightsActivity.class));
                break;
            case R.id.identity_name:
                chooseIdentityType();
                break;
            case R.id.next1:
                //非空判断
                if (name.getText().toString().trim().equals("")) {
                    ToastUtils.show(AuthorizeActivity.this, "姓名不能为空");
                    return;
                }
//                else if (phone.getText().toString().trim().equals("")) {
//                    ToastUtils.show(AuthorizeActivity.this, "手机号不能为空");
//                    return;
//                }
                else if (email.getText().toString().trim().equals("")) {
                    ToastUtils.show(AuthorizeActivity.this, "邮箱不能为空");
                    return;
                }
                else if (!Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$").matcher(email.getText().toString().trim()).matches()) {
                    ToastUtils.show(AuthorizeActivity.this, "请填写正确的邮箱格式");
                    return;
                }

                else if (identityName.getText().toString().trim().equals("")) {
                    ToastUtils.show(AuthorizeActivity.this, "请选择你的身份");
                    return;
                }

                if (Category.equals("ASSOCIATION")) {
                    authorize1.setVisibility(View.VISIBLE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICAL_STAFF")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.VISIBLE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICAL_COMPANY")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.VISIBLE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICO")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.VISIBLE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("EDUCATION_SCIENCE")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.VISIBLE);
                }

                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.VISIBLE);
                identity3.setVisibility(View.GONE);
                break;
            case R.id.next21:
                //非空判断
                if (Category.equals("ASSOCIATION")) {
                    //医疗协会
                    if (company.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "单位不能为空");
                        return;
                    } else if (position.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "职务不能为空");
                        return;
                    }
                } else if (Category.equals("MEDICAL_STAFF")) {
                    //医护人员
                    if (hospital.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "医院不能为空");
                        return;
                    } else if (department2.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "科室不能为空");
                        return;
                    } else if (position2.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "职务不能为空");
                        return;
                    } else if (title2.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "职称不能为空");
                        return;
                    }
                } else if (Category.equals("MEDICAL_COMPANY")) {
                    //药械企业
                    if (company3.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "单位不能为空");
                        return;
                    } else if (department3.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "部门不能为空");
                        return;
                    } else if (position3.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "职务不能为空");
                        return;
                    }
                } else if (Category.equals("MEDICO")) {
                    //医学生
                    if (school.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "学校不能为空");
                        return;
                    } else if (major.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "专业不能为空");
                        return;
                    } else if (education.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "学历不能为空");
                        return;
                    } else if (year.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "入学年份不能为空");
                        return;
                    }
                } else if (Category.equals("EDUCATION_SCIENCE")) {
                    //医药教科研人员
                    if (company5.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "单位不能为空");
                        return;
                    } else if (department5.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "部门不能为空");
                        return;
                    } else if (position5.getText().toString().trim().equals("")) {
                        ToastUtils.show(AuthorizeActivity.this, "职务不能为空");
                        return;
                    }
                }
                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.GONE);
                identity3.setVisibility(View.VISIBLE);

                chooseWorkPhotoType(Category);
                break;
            case R.id.next22:
                identity1.setVisibility(View.VISIBLE);
                identity2.setVisibility(View.GONE);
                identity3.setVisibility(View.GONE);
                break;
            case R.id.material:
                chooseWorkPhotoType(Category);
                break;
            case R.id.work_photo1:
                photoType = 0;  //识别图片类型
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(AuthorizeActivity.this);
                break;
            case R.id.work_photo2:
                photoType = 1;  //识别图片类型
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(AuthorizeActivity.this);
                break;
            case R.id.next31:
                userAddAuthenEntity.setEmail(email.getText().toString().trim());
                userAddAuthenEntity.setUserName(name.getText().toString().trim());

                if (Category.equals("ASSOCIATION")) {
                    userAddAuthenEntity.setCompany(company.getText().toString().trim());
                    userAddAuthenEntity.setPostion(position.getText().toString().trim());
                } else if (Category.equals("MEDICAL_STAFF")) {
                    userAddAuthenEntity.setCompany(hospital.getText().toString().trim());
                    userAddAuthenEntity.setDepartment(department2.getText().toString().trim());
                    userAddAuthenEntity.setPostion(position2.getText().toString().trim());
                    userAddAuthenEntity.setTitle(title2.getText().toString().trim());
                } else if (Category.equals("MEDICAL_COMPANY")) {
                    userAddAuthenEntity.setCompany(company3.getText().toString().trim());
                    userAddAuthenEntity.setDepartment(department3.getText().toString().trim());
                    userAddAuthenEntity.setPostion(position3.getText().toString().trim());
                } else if (Category.equals("MEDICO")) {
                    userAddAuthenEntity.setCompany(school.getText().toString().trim());
                    userAddAuthenEntity.setDepartment(major.getText().toString().trim());
                    userAddAuthenEntity.setDiploma(education.getText().toString().trim());
                    userAddAuthenEntity.setEntranceDate(year.getText().toString().trim());
                } else if (Category.equals("EDUCATION_SCIENCE")) {
                    userAddAuthenEntity.setCompany(company5.getText().toString().trim());
                    userAddAuthenEntity.setDepartment(department5.getText().toString().trim());
                    userAddAuthenEntity.setPostion(position5.getText().toString().trim());
                }

                userAddAuthenEntity.setWorkPhoto(workPhoto);
                userAddAuthenEntity.setIdentityPhoto(identityPhoto);

                HttpData.getInstance().HttpDataAuthorize(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(AuthorizeActivity.this, data.getMsg());
                            return;
                        }

                        startActivity(new Intent(AuthorizeActivity.this, AuthorizedActivity.class));
                        finish();
                    }
                }, userAddAuthenEntity);

                break;
            case R.id.next32:
                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.VISIBLE);
                identity3.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 选择身份弹窗
     */
    BaseQuickAdapter mBaseQuickAdapter = new IdentityTypeAdapter(R.layout.item_identity_type, null);

    private void chooseIdentityType() {
        final View popupView = this.getLayoutInflater().inflate(R.layout.popupwindow_identity_type, null);

        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.types);
        recyclerView.setLayoutManager(new LinearLayoutManager(popupView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(AuthorizeActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mBaseQuickAdapter);


        // 创建PopupWindow对象，指定宽度和高度
        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, 1600);
        // 设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0DFFFFFF")));

        // 设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);

        // 更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
        window.showAtLocation(identityName, Gravity.CENTER, 0, 0);


        ImageView cancelIv = (ImageView) popupView.findViewById(R.id.cancel);
        cancelIv.setOnClickListener(view -> window.dismiss());


        if (userIdentities.size() == 0) {
            HttpData.getInstance().HttpDataGetAuthorizationTypes(new Observer<HttpResult3<UserIdentity, Object>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3<UserIdentity, Object> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(AuthorizeActivity.this, data.getMsg());
                        return;
                    }
                    userIdentities.clear();
                    userIdentities.addAll(data.getData());
                    mBaseQuickAdapter.setNewData(data.getData());
                    mBaseQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                        identityName.setText(data.getData().get(position).getTitle());

                        userAddAuthenEntity.setCategory(data.getData().get(position).getCode());
                        userAddAuthenEntity.setCategoryName(data.getData().get(position).getTitle());

                        Category = data.getData().get(position).getCode();
                        if (Category.equals("ASSOCIATION")) {
                            authorize1.setVisibility(View.VISIBLE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (Category.equals("MEDICAL_STAFF")) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.VISIBLE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (Category.equals("MEDICAL_COMPANY")) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.VISIBLE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (Category.equals("MEDICO")) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.VISIBLE);
                            authorize5.setVisibility(View.GONE);
                        } else if (Category.equals("EDUCATION_SCIENCE")) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.VISIBLE);
                        }

                        window.dismiss();
                    });
                }
            });
        } else {
            mBaseQuickAdapter.setNewData(userIdentities);
            mBaseQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                identityName.setText(userIdentities.get(position).getTitle());

                userAddAuthenEntity.setCategory(userIdentities.get(position).getCode());
                userAddAuthenEntity.setCategoryName(userIdentities.get(position).getTitle());

                Category = userIdentities.get(position).getCode();
                if (Category.equals("ASSOCIATION")) {
                    authorize1.setVisibility(View.VISIBLE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICAL_STAFF")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.VISIBLE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICAL_COMPANY")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.VISIBLE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("MEDICO")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.VISIBLE);
                    authorize5.setVisibility(View.GONE);
                } else if (Category.equals("EDUCATION_SCIENCE")) {
                    authorize1.setVisibility(View.GONE);
                    authorize2.setVisibility(View.GONE);
                    authorize3.setVisibility(View.GONE);
                    authorize4.setVisibility(View.GONE);
                    authorize5.setVisibility(View.VISIBLE);
                }

                window.dismiss();
            });
        }
    }


    /**
     * 选择上传证件类型弹窗
     */
    private void chooseWorkPhotoType(String category) {
//        if (type == 0) {
//            ToastUtils.show(AuthorizeActivity.this, "请上2步选择身份类型");
//            return;
//        }
        final View popupView = this.getLayoutInflater().inflate(R.layout.popupwindow_work_photo_type, null);

        CheckBox checkBox1 = (CheckBox) popupView.findViewById(R.id.checkbox1);
        CheckBox checkBox2 = (CheckBox) popupView.findViewById(R.id.checkbox2);
        CheckBox checkBox3 = (CheckBox) popupView.findViewById(R.id.checkbox3);
        CheckBox checkBox4 = (CheckBox) popupView.findViewById(R.id.checkbox4);
        CheckBox checkBox5 = (CheckBox) popupView.findViewById(R.id.checkbox5);
        CheckBox checkBox6 = (CheckBox) popupView.findViewById(R.id.checkbox6);
        CheckBox checkBox7 = (CheckBox) popupView.findViewById(R.id.checkbox7);

        if (Category.equals("ASSOCIATION")) {
            checkBox1.setVisibility(View.VISIBLE);
            checkBox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
            checkBox4.setVisibility(View.GONE);
            checkBox5.setVisibility(View.GONE);
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.GONE);
        } else if (Category.equals("MEDICAL_STAFF")) {
            checkBox1.setVisibility(View.GONE);
            checkBox2.setVisibility(View.VISIBLE);
            checkBox3.setVisibility(View.VISIBLE);
            checkBox4.setVisibility(View.VISIBLE);
            checkBox5.setVisibility(View.VISIBLE);
            checkBox6.setVisibility(View.VISIBLE);
            checkBox7.setVisibility(View.GONE);
        } else if (Category.equals("MEDICAL_COMPANY")) {
            checkBox1.setVisibility(View.VISIBLE);
            checkBox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
            checkBox4.setVisibility(View.GONE);
            checkBox5.setVisibility(View.GONE);
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.GONE);
        } else if (Category.equals("MEDICO")) {
            checkBox1.setVisibility(View.GONE);
            checkBox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
            checkBox4.setVisibility(View.GONE);
            checkBox5.setVisibility(View.GONE);
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.VISIBLE);
        } else if (Category.equals("EDUCATION_SCIENCE")) {
            checkBox1.setVisibility(View.VISIBLE);
            checkBox2.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
            checkBox4.setVisibility(View.GONE);
            checkBox5.setVisibility(View.GONE);
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.GONE);
        }


        // 创建PopupWindow对象，指定宽度和高度
        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, 1600);
        // 设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // 设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
        window.showAtLocation(identityName, Gravity.CENTER, 0, 0);

        ImageView cancelIv = (ImageView) popupView.findViewById(R.id.cancel);
        cancelIv.setOnClickListener(view -> window.dismiss());

        checkBox1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox1.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox1.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox2.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox2.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox3.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox3.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox3.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox4.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox4.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox4.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox5.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox5.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox5.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox6.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox6.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox6.getText().toString().trim());
                window.dismiss();
            }
        });
        checkBox7.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                material.setText(checkBox7.getText().toString().trim());
                userAddAuthenEntity.setWorkPhotoType(checkBox7.getText().toString().trim());
                window.dismiss();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String i : photos) {
                Log.e(getLocalClassName(), i);
            }
            ToastUtils.show(AuthorizeActivity.this, "正在上传...");
            getQiniuToken(photos.get(0));

            if (photoType == 0) {
                Glide.with(AuthorizeActivity.this)
                        .load(photos.get(0))
                        .crossFade()
                        .dontAnimate()
                        .into(workPhoto1);
            } else if (photoType == 1) {
                Glide.with(AuthorizeActivity.this)
                        .load(photos.get(0))
                        .crossFade()
                        .dontAnimate()
                        .into(workPhoto2);
            }
        }
    }

    private String qiniuKey;
    private String qiniuToken;

    private void getQiniuToken(final String file) {
        HttpData.getInstance().HttpDataGetQiniuToken(new Observer<QiniuTokenDto>() {
            @Override
            public void onCompleted() {
                Log.e(getLocalClassName(), "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(getLocalClassName(), "onError: " + e.getMessage());
            }

            @Override
            public void onNext(QiniuTokenDto q) {
                if (q.getCode() != 200 || q.getData().getUploadToken() == null || q.getData().getUploadToken().equals("")) {
                    return;
                }
                qiniuToken = q.getData().getUploadToken();

                // 设置图片名字
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                qiniuKey = "android_live_" + sdf.format(new Date());

                int i = new Random().nextInt(1000) + 1;

                Log.e(getLocalClassName(), "File对象、或 文件路径、或 字节数组: " + file);
                Log.e(getLocalClassName(), "指定七牛服务上的文件名，或 null: " + qiniuKey + i);
                Log.e(getLocalClassName(), "从服务端SDK获取: " + qiniuToken);
                Log.e(getLocalClassName(), "http://ono5ms5i0.bkt.clouddn.com/" + qiniuKey + i);

                upload(file, qiniuKey + i, qiniuToken);
            }
        }, "android");
    }

    private Configuration config = new Configuration.Builder()
            .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
            .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
            .connectTimeout(10) // 链接超时。默认10秒
            .responseTimeout(60) // 服务器响应超时。默认60秒
//            .zone(Zone.zone1) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
            .build();

    private void upload(final String data, final String key, final String token) {
        new Thread() {
            public void run() {
                // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                UploadManager uploadManager = new UploadManager(config);
                uploadManager.put(data, key, token,
                        (key1, info, res) -> {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                Log.i("qiniu", "Upload Success");

//                                    ToastUtils.show(LiveBuildProgramActivity.this, "封面正在上传，上传速度取决于当前网络，请耐心等待...");
                            } else {
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);

                            if (photoType == 0) {
                                workPhoto = "http://ono5ms5i0.bkt.clouddn.com/" + key1;
                            } else if (photoType == 1) {
                                identityPhoto = "http://ono5ms5i0.bkt.clouddn.com/" + key1;
                            }
                        }, null);
            }
        }.start();
    }
}
