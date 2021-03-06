package com.medmeeting.m.zhiyi.UI.UserInfoView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.UI.Entity.UserEditEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserGetInfoEntity;
import com.medmeeting.m.zhiyi.UI.MineView.ChooseDepartmentActivity;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.StringUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @BindView(R.id.code1)
    LinearLayout code1;
    @BindView(R.id.code2)
    LinearLayout code2;
    @BindView(R.id.code3)
    LinearLayout code3;

    @BindView(R.id.company4)
    EditText company4;
    @BindView(R.id.department4)
    EditText department4;
    @BindView(R.id.position4)
    EditText position4;
    @BindView(R.id.code4)
    LinearLayout code4;

    private UserGetInfoEntity userGetInfoEntity;
    private UserEditEntity userEditEntity = new UserEditEntity();
    private String code; //data.getEntity().getMedical()

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

                userGetInfoEntity = data.getEntity();

                Glide.with(UpdateUserInfoActivity.this)
                        .load(data.getEntity().getUserPic())
                        .crossFade()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.avator_default)
                        .transform(new GlideCircleTransform(UpdateUserInfoActivity.this))
                        .into(avatar);
                name.setText(data.getEntity().getName());
                nickname.setText(data.getEntity().getNickName());
                des.setText(data.getEntity().getDes());

                if (data.getEntity().getProvinceName() == null && data.getEntity().getCityName() == null) {
                    city.setText("");
                } else {
                    city.setText(data.getEntity().getProvinceName() + " - " + data.getEntity().getCityName());
                }

                code = data.getEntity().getMedical();

                switch (data.getEntity().getMedical()) {
                    case "ASSOCIATION": //医疗协会
                        code1.setVisibility(View.GONE);
                        code2.setVisibility(View.VISIBLE);
                        code3.setVisibility(View.GONE);
                        code4.setVisibility(View.GONE);
                        break;
                    case "MEDICAL_STAFF": //医护人员
                        code1.setVisibility(View.VISIBLE);
                        code2.setVisibility(View.GONE);
                        code3.setVisibility(View.GONE);
                        code4.setVisibility(View.GONE);
                        break;
                    case "MEDICAL_COMPANY": //药械企业
                        code1.setVisibility(View.GONE);
                        code2.setVisibility(View.GONE);
                        code3.setVisibility(View.GONE);
                        code4.setVisibility(View.VISIBLE);
                        break;
                    case "MEDICO": //医学生
                        code1.setVisibility(View.GONE);
                        code2.setVisibility(View.GONE);
                        code3.setVisibility(View.VISIBLE);
                        code4.setVisibility(View.GONE);
                        break;
                    case "EDUCATION_SCIENCE": //医药教科研人员
                        code1.setVisibility(View.GONE);
                        code2.setVisibility(View.VISIBLE);
                        code3.setVisibility(View.GONE);
                        code4.setVisibility(View.GONE);
                        break;
                    case "OTHER": //其他人员
                        code1.setVisibility(View.GONE);
                        code2.setVisibility(View.GONE);
                        code3.setVisibility(View.GONE);
                        code4.setVisibility(View.GONE);
                        break;
                }
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

                company4.setText(data.getEntity().getCompany());
                department4.setText(data.getEntity().getDepartment());
                position4.setText(data.getEntity().getPostion());

                sex.setText(data.getEntity().getSex());
                email.setText(data.getEntity().getEmail());

                //修改API用的
                userEditEntity.setUserPic(data.getEntity().getUserPic());
                userEditEntity.setName(data.getEntity().getName());
                userEditEntity.setNickName(data.getEntity().getNickName());
                userEditEntity.setDes(data.getEntity().getDes());
                userEditEntity.setCity(data.getEntity().getCity());
                switch (data.getEntity().getMedical()) {
                    case "ASSOCIATION": //医疗协会
                        userEditEntity.setCompany(data.getEntity().getCompany());
                        userEditEntity.setPostion(data.getEntity().getPostion());
                        break;
                    case "MEDICAL_STAFF": //医护人员
                        userEditEntity.setCompany(data.getEntity().getCompany());
                        userEditEntity.setDepartment(data.getEntity().getDepartment());
                        userEditEntity.setTitle(data.getEntity().getTitle());
                        userEditEntity.setPostion(data.getEntity().getPostion());
                        break;
                    case "MEDICAL_COMPANY": //药械企业
                        userEditEntity.setCompany(data.getEntity().getCompany());
                        userEditEntity.setDepartment(data.getEntity().getDepartment());
                        userEditEntity.setPostion(data.getEntity().getPostion());
                        break;
                    case "MEDICO": //医学生
                        userEditEntity.setCompany(data.getEntity().getCompany());
                        userEditEntity.setDepartment(data.getEntity().getDepartment());
                        userEditEntity.setDiploma(data.getEntity().getDiploma());
                        userEditEntity.setEntranceDate(data.getEntity().getEntranceDate());
                        break;
                    case "EDUCATION_SCIENCE": //医药教科研人员
                        userEditEntity.setCompany(data.getEntity().getCompany());
                        userEditEntity.setDepartment(data.getEntity().getDepartment());
                        userEditEntity.setPostion(data.getEntity().getPostion());
                        break;
                }
                userEditEntity.setSex(data.getEntity().getSex());
                userEditEntity.setEmail(data.getEntity().getEmail());
            }
        });
    }


    @OnClick({R.id.avatar, R.id.nickname, R.id.des, R.id.city, R.id.hospital, R.id.department, R.id.title, R.id.position, R.id.education, R.id.year, R.id.sex, R.id.activate_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(UpdateUserInfoActivity.this);
                break;
            case R.id.nickname:
                startActivityForResult(new Intent(UpdateUserInfoActivity.this, UpdateUserInfoNickNameActivity.class), 1);
                break;
            case R.id.des:
                Intent intent = new Intent(UpdateUserInfoActivity.this, UpdateUserInfoDescriptionActivity.class);
                intent.putExtra("des2", des.getText().toString().trim());
                startActivityForResult(intent, 2);
                break;
            case R.id.city:
                startActivityForResult(new Intent(UpdateUserInfoActivity.this, UpdateUserInfoAreaActivity.class), 3);
                break;
            case R.id.hospital:
                break;
            case R.id.department:
                startActivityForResult(new Intent(UpdateUserInfoActivity.this, ChooseDepartmentActivity.class), 0);
                break;
            case R.id.title:
                showPositionPopupwindow();
                break;
            case R.id.position:
                showTitlePopupwindow();
                break;
            case R.id.education:
                showEduPopupwindow();
                break;
            case R.id.year:
                showYearPopupwindow();
                break;
            case R.id.sex:
                showSexPopupwindow();
                break;
            case R.id.activate_save:
                userEditEntity.setName(name.getText().toString().trim());
                userEditEntity.setNickName(nickname.getText().toString().trim());
                userEditEntity.setDes(des.getText().toString().trim());

                userEditEntity.setSex(sex.getText().toString().trim());

                if (!Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$").matcher(email.getText().toString().trim()).matches()) {
                    ToastUtils.show(UpdateUserInfoActivity.this, "请填写正确的邮箱格式");
                    return;
                }
                userEditEntity.setEmail(email.getText().toString().trim());

                switch (code) {
                    case "ASSOCIATION": //医疗协会
                        userEditEntity.setCompany(company.getText().toString().trim());
                        userEditEntity.setPostion(position2.getText().toString().trim());
                        break;
                    case "MEDICAL_STAFF": //医护人员
                        userEditEntity.setCompany(hospital.getText().toString().trim());
                        userEditEntity.setDepartment(department.getText().toString().trim());
                        userEditEntity.setTitle(title.getText().toString().trim());
                        userEditEntity.setPostion(position.getText().toString().trim());
                        break;
                    case "MEDICAL_COMPANY": //药械企业
                        userEditEntity.setCompany(company4.getText().toString().trim());
                        userEditEntity.setDepartment(department4.getText().toString().trim());
                        userEditEntity.setPostion(position4.getText().toString().trim());
                        break;
                    case "MEDICO": //医学生
                        userEditEntity.setCompany(school.getText().toString().trim());
                        userEditEntity.setDepartment(major.getText().toString().trim());
                        userEditEntity.setDiploma(education.getText().toString().trim());
                        userEditEntity.setEntranceDate(year.getText().toString().trim());
                        break;
                    case "EDUCATION_SCIENCE": //医药教科研人员
                        userEditEntity.setCompany(company.getText().toString().trim());
//                        userEditEntity.setDepartment(department.getText().toString().trim());
                        userEditEntity.setPostion(position2.getText().toString().trim());
                        break;
                }
                HttpData.getInstance().HttpDataEditUserInfo(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(UpdateUserInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(UpdateUserInfoActivity.this, data.getMsg());
                            return;
                        }
                        ToastUtils.show(UpdateUserInfoActivity.this, "保存成功");

//                        onResume();
                        finish();
                    }
                }, userEditEntity);
                break;
        }
    }

    /**
     * 以下为头像上传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {

            if (requestCode == 0) {
                if (resultCode == 0) {
                    department.setText(data.getExtras().getString("department"));
                    userEditEntity.setDepartment(data.getExtras().getString("department"));
                    return;
                }

            } else if (requestCode == 1) {
                if (resultCode == 1) {
                    nickname.setText(data.getExtras().getString("nickname"));
                    userEditEntity.setNickName(data.getExtras().getString("nickname"));
                    return;
                }

            } else if (requestCode == 2) {
                if (resultCode == 2) {
                    des.setText(data.getExtras().getString("des"));
                    userEditEntity.setDes(data.getExtras().getString("des"));
                    return;
                }
            } else if (requestCode == 3) {
                if (resultCode == 3) {
                    city.setText(data.getExtras().getString("provinceName") + " - " + data.getExtras().getString("cityName"));
                    userEditEntity.setProvince(data.getExtras().getInt("province") + "");
                    userEditEntity.setCity(data.getExtras().getInt("city") + "");
                    return;
                }
            }

            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String i : photos) {
                Log.e(getLocalClassName(), i);
            }
            ToastUtils.show(UpdateUserInfoActivity.this, "正在上传封面图片...");

            File file = new File(photos.get(0));
            // creates RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            // MultipartBody.Part is used to send also the actual filename
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Log.e("mediaType is  ", requestFile.contentType().toString());

            // adds another part within the multipart request
            String descriptionString = "Sample description";
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString); //multipart/form-data

            HttpData.getInstance().HttpUploadFile(new Observer<HttpResult6>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult6 data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(UpdateUserInfoActivity.this, data.getMsg());
                        return;
                    }

                    Glide.with(UpdateUserInfoActivity.this)
                            .load(data.getExtra().getAbsQiniuImgHash())
                            .crossFade()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .transform(new GlideCircleTransform(UpdateUserInfoActivity.this))
                            .into(avatar);

                    //run API
                    userEditEntity.setUserPic(data.getExtra().getAbsQiniuImgHash());

                }
            }, body, description);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //此处避免you cannot start a load for a destroyed activity，因为glide不在主线程
        Glide.with(getApplicationContext()).pauseRequests();
    }


    /**
     * 填写职务的弹出窗
     */
    private PopupWindow academicPopupWindow;
    private String[] academic = new String[]{"院长", "副院长", "科室/部门主任", "科室/部门副主任", "临床医师", "药师", "护士", "其他医技人员"};
    private String mChooseAcademic = "科室/部门主任"; //用户选择的学历

    private void showTitlePopupwindow() {
        View academicPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        academicPopupWindow = new PopupWindow(academicPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView academicConfirmTv = (TextView) academicPopupwindowView.findViewById(R.id.academic_confirm);
        academicConfirmTv.setOnClickListener(v -> {
            academicPopupWindow.dismiss();
            position.setText(mChooseAcademic);

            userEditEntity.setPostion(mChooseAcademic);
        });

        NumberPicker academicPicker = (NumberPicker) academicPopupwindowView.findViewById(R.id.academic_picker);
        final TextView academicDisplayTv = (TextView) academicPopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(academic))) {
            academicPicker.setDisplayedValues(academic);//test data
            Log.d("hahaha", academic.length + "");

            academicPicker.setMinValue(0);
            if (academic.length <= 1) {
                academicPicker.setMaxValue(1);
            } else {
                academicPicker.setMaxValue(academic.length - 1);
            }
        } else {
            academicPicker.setMinValue(0);
            academicPicker.setDisplayedValues(new String[]{"暂无数据"});
            academicPicker.setMaxValue(0);
        }

        academicPicker.setValue(2);

        academicPicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        academicPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        academicPicker.setFocusable(true);
        academicPicker.setFocusableInTouchMode(true);

        academicPicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > academic.length) {
                    mChooseAcademic = academic[academic.length];
                } else {
                    mChooseAcademic = academic[numberPicker.getValue()];
                }
                Log.d("mChooseAcademic", mChooseAcademic);
            }
            academicDisplayTv.setText(mChooseAcademic);
        });

        LinearLayout academicPopupParentLayout = (LinearLayout) academicPopupwindowView.findViewById(R.id.popup_parent);
        academicPopupParentLayout.setOnClickListener(v -> {
            if (academicPopupWindow != null && academicPopupWindow.isShowing()) {
                academicPopupWindow.dismiss();
            }
        });

        academicPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        academicPopupWindow.setBackgroundDrawable(dw);
        academicPopupWindow.showAtLocation(academicPopupwindowView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 填写职称的弹出窗
     */
    private PopupWindow positionPopupWindow;
    private String[] positions = new String[]{"未定级（含研究生在读）", "初级职称", "中级职称", "副高级职称", "高级职称"};
    private String mChoosePosition = "中级职称"; //用户选择的职称

    private void showPositionPopupwindow() {
        View positionPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        positionPopupWindow = new PopupWindow(positionPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView positionConfirmTv = (TextView) positionPopupwindowView.findViewById(R.id.academic_confirm);
        positionConfirmTv.setOnClickListener(v -> {
            positionPopupWindow.dismiss();
            title.setText(mChoosePosition);
            userEditEntity.setTitle(mChoosePosition);
        });

        NumberPicker positionPicker = (NumberPicker) positionPopupwindowView.findViewById(R.id.academic_picker);
        final TextView positionDisplayTv = (TextView) positionPopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(positions))) {
            positionPicker.setDisplayedValues(positions);//test data
            Log.d("hahaha", positions.length + "");

            positionPicker.setMinValue(0);
            if (positions.length <= 1) {
                positionPicker.setMaxValue(1);
            } else {
                positionPicker.setMaxValue(positions.length - 1);
            }
        } else {
            positionPicker.setMinValue(0);
            positionPicker.setDisplayedValues(new String[]{"暂无数据"});
            positionPicker.setMaxValue(0);
        }

        positionPicker.setValue(2);

        positionPicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        positionPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        positionPicker.setFocusable(true);
        positionPicker.setFocusableInTouchMode(true);

        positionPicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > positions.length) {
                    mChoosePosition = positions[positions.length];
                } else {
                    mChoosePosition = positions[positionPicker.getValue()];
                }
                Log.d("mChoosePosition", mChoosePosition);
            }
            positionDisplayTv.setText(mChoosePosition);
        });

        LinearLayout positionPopupParentLayout = (LinearLayout) positionPopupwindowView.findViewById(R.id.popup_parent);
        positionPopupParentLayout.setOnClickListener(v -> {
            if (positionPopupWindow != null && positionPopupWindow.isShowing()) {
                positionPopupWindow.dismiss();
            }
        });

        positionPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        positionPopupWindow.setBackgroundDrawable(dw);
        positionPopupWindow.showAtLocation(positionPopupwindowView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 填写学历的弹出窗
     */
    private PopupWindow eduPopupWindow;
    private String[] edus = new String[]{"大专以下", "大专", "本科", "硕士", "博士", "博士后"};
    private String mChooseEdu = "本科"; //用户选择的职称

    private void showEduPopupwindow() {
        View eduPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        eduPopupWindow = new PopupWindow(eduPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView eduConfirmTv = (TextView) eduPopupwindowView.findViewById(R.id.academic_confirm);
        eduConfirmTv.setOnClickListener(v -> {
            eduPopupWindow.dismiss();
            education.setText(mChooseEdu);
            userEditEntity.setDiploma(mChooseEdu);

        });

        NumberPicker eduPicker = (NumberPicker) eduPopupwindowView.findViewById(R.id.academic_picker);
        final TextView eduDisplayTv = (TextView) eduPopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(edus))) {
            eduPicker.setDisplayedValues(edus);//test data
            Log.d("hahaha", edus.length + "");

            eduPicker.setMinValue(0);
            if (edus.length <= 1) {
                eduPicker.setMaxValue(1);
            } else {
                eduPicker.setMaxValue(edus.length - 1);
            }
        } else {
            eduPicker.setMinValue(0);
            eduPicker.setDisplayedValues(new String[]{"暂无数据"});
            eduPicker.setMaxValue(0);
        }

        eduPicker.setValue(2);

        eduPicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        eduPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        eduPicker.setFocusable(true);
        eduPicker.setFocusableInTouchMode(true);

        eduPicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > edus.length) {
                    mChooseEdu = edus[edus.length];
                } else {
                    mChooseEdu = edus[eduPicker.getValue()];
                }
                Log.d("mChooseEdu", mChooseEdu);
            }
            eduDisplayTv.setText(mChooseEdu);
        });

        LinearLayout eduPopupParentLayout = (LinearLayout) eduPopupwindowView.findViewById(R.id.popup_parent);
        eduPopupParentLayout.setOnClickListener(v -> {
            if (eduPopupWindow != null && eduPopupWindow.isShowing()) {
                eduPopupWindow.dismiss();
            }
        });

        eduPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        eduPopupWindow.setBackgroundDrawable(dw);
        eduPopupWindow.showAtLocation(eduPopupwindowView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 填写性别的弹出窗
     */
    private PopupWindow sexPopupWindow;
    private String[] sexs = new String[]{"男", "女"};
    private String mChooseSex = "女"; //用户选择的职称

    private void showSexPopupwindow() {
        View sexPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        sexPopupWindow = new PopupWindow(sexPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView sexConfirmTv = (TextView) sexPopupwindowView.findViewById(R.id.academic_confirm);
        sexConfirmTv.setOnClickListener(v -> {
            sexPopupWindow.dismiss();
            sex.setText(mChooseSex);
        });

        NumberPicker eduPicker = (NumberPicker) sexPopupwindowView.findViewById(R.id.academic_picker);
        final TextView eduDisplayTv = (TextView) sexPopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(sexs))) {
            eduPicker.setDisplayedValues(sexs);//test data
            Log.d("hahaha", sexs.length + "");

            eduPicker.setMinValue(0);
            if (sexs.length <= 1) {
                eduPicker.setMaxValue(1);
            } else {
                eduPicker.setMaxValue(sexs.length - 1);
            }
        } else {
            eduPicker.setMinValue(0);
            eduPicker.setDisplayedValues(new String[]{"暂无数据"});
            eduPicker.setMaxValue(0);
        }

        eduPicker.setValue(2);

        eduPicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        eduPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        eduPicker.setFocusable(true);
        eduPicker.setFocusableInTouchMode(true);

        eduPicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > sexs.length) {
                    mChooseSex = sexs[sexs.length];
                } else {
                    mChooseSex = sexs[eduPicker.getValue()];
                }
                Log.d("mChooseEdu", mChooseSex);
            }
            eduDisplayTv.setText(mChooseSex);
        });
        eduPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mChooseSex = sexs[numberPicker.getValue()];
                eduDisplayTv.setText(mChooseSex);
            }
        });


        LinearLayout eduPopupParentLayout = (LinearLayout) sexPopupwindowView.findViewById(R.id.popup_parent);
        eduPopupParentLayout.setOnClickListener(v -> {
            if (sexPopupWindow != null && sexPopupWindow.isShowing()) {
                sexPopupWindow.dismiss();
            }
        });

        sexPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        sexPopupWindow.setBackgroundDrawable(dw);
        sexPopupWindow.showAtLocation(sexPopupwindowView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 填写入学年份的弹出窗
     */
    private PopupWindow yearPopupWindow;
    private List<String> yearls = new ArrayList<>();
    private String mChooseYear = (Calendar.getInstance().get(Calendar.YEAR) - 3) + ""; //用户选择的职称

    private void showYearPopupwindow() {
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i > Calendar.getInstance().get(Calendar.YEAR) - 100; i--) {
            yearls.add(i + "");
        }
        String[] years = yearls.toArray(new String[yearls.size()]);


        View yearPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        yearPopupWindow = new PopupWindow(yearPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView yearConfirmTv = (TextView) yearPopupwindowView.findViewById(R.id.academic_confirm);
        yearConfirmTv.setOnClickListener(v -> {
            yearPopupWindow.dismiss();
            year.setText(mChooseYear);
            userEditEntity.setEntranceDate(mChooseYear);
        });

        NumberPicker yearPicker = (NumberPicker) yearPopupwindowView.findViewById(R.id.academic_picker);
        final TextView yearDisplayTv = (TextView) yearPopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(years))) {
            yearPicker.setDisplayedValues(years);//test data
            Log.d("hahaha", years.length + "");

            yearPicker.setMinValue(0);
            if (years.length <= 1) {
                yearPicker.setMaxValue(1);
            } else {
                yearPicker.setMaxValue(years.length - 1);
            }
        } else {
            yearPicker.setMinValue(0);
            yearPicker.setDisplayedValues(new String[]{"暂无数据"});
            yearPicker.setMaxValue(0);
        }

        yearPicker.setValue(2);

        yearPicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        yearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        yearPicker.setFocusable(true);
        yearPicker.setFocusableInTouchMode(true);

        yearPicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > years.length) {
                    mChooseYear = years[years.length];
                } else {
                    mChooseYear = years[yearPicker.getValue()];
                }
                Log.d("mChooseEdu", mChooseYear);
            }
            yearDisplayTv.setText(mChooseYear);
        });
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mChooseYear = years[numberPicker.getValue()];
                yearDisplayTv.setText(mChooseYear);
            }
        });


        LinearLayout yearPopupParentLayout = (LinearLayout) yearPopupwindowView.findViewById(R.id.popup_parent);
        yearPopupParentLayout.setOnClickListener(v -> {
            if (yearPopupWindow != null && yearPopupWindow.isShowing()) {
                yearPopupWindow.dismiss();
            }
        });

        yearPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        yearPopupWindow.setBackgroundDrawable(dw);
        yearPopupWindow.showAtLocation(yearPopupwindowView, Gravity.BOTTOM, 0, 0);
    }

}
