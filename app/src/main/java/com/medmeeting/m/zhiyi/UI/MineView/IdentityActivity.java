package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.DoctorAuthentication;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserAuthorEntity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.FontHelper;
import com.medmeeting.m.zhiyi.Util.StringUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

//身份认证页面
public class IdentityActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name_tip)
    TextView nameTip;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.name_wrap)
    RelativeLayout nameWrap;
    @BindView(R.id.hospital)
    EditText hospital;
    @BindView(R.id.department)
    TextView mDepartmentTv;
    @BindView(R.id.department_tip)
    TextView departmentTip;
    @BindView(R.id.department_rlyt)
    RelativeLayout departmentRlyt;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_tip)
    TextView titleTip;
    @BindView(R.id.title_wrap)
    RelativeLayout titleWrap;
    @BindView(R.id.next_step)
    TextView nextStep;
    @BindView(R.id.content_identity)
    RelativeLayout contentIdentity;


    private static final String TAG = IdentityActivity.class.getSimpleName();
    private DoctorAuthentication.DataBean.UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initFont();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initFont() {
        FontHelper.applyFont(IdentityActivity.this, nameTip, FontHelper.ICONFONT);
        FontHelper.applyFont(IdentityActivity.this, departmentTip, FontHelper.ICONFONT);
        FontHelper.applyFont(IdentityActivity.this, titleTip, FontHelper.ICONFONT);
    }

    @OnClick({R.id.name_tip, R.id.department_rlyt, R.id.title_wrap, R.id.next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_tip:
                showNameTipPopupwindow();
                break;
            case R.id.department_rlyt:
                Intent intent = new Intent(IdentityActivity.this, ChooseDepartmentActivity.class);
                int requestCode = 0;
                startActivityForResult(intent, requestCode);
                break;
            case R.id.title_wrap:
                showTitlePopupwindow();
                break;
            case R.id.next_step:
                InAuthentication();
                break;
        }
    }

    //填写姓名的提示
    private PopupWindow popupwindow;

    private void showNameTipPopupwindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_identity_name_tip, null);
        popupwindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);


        LinearLayout academicPopupParentLayout = (LinearLayout) view.findViewById(R.id.popup_parent);
        academicPopupParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
            }
        });

        int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
        int[] location = new int[2];

        // 允许点击外部消失
        popupwindow.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
        popupwindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
        popupwindow.setFocusable(true);
        // 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
        name.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        popupwindow.showAtLocation(name, Gravity.NO_GRAVITY, (location[0] + name.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }

    //选择科室
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.equals(""))
            return;
        String department = data.getStringExtra("department");
        switch (resultCode) {
            case 0:
                mDepartmentTv.setText(department);
                mDepartmentTv.setTextColor(this.getResources().getColor(R.color.item_background_translucent_two));
                break;
            default:
                break;
        }
    }

    private Map<String, Object> mOptions = new HashMap<>();

    private void InAuthentication() {

        String userId;
        try {
            userId = DBUtils.get(IdentityActivity.this, "userId");
        } catch (SnappydbException e) {
            e.printStackTrace();
            return;
        }

        if (StringUtils.isEmpty(name.getText().toString().trim())) {
            ToastUtils.show(IdentityActivity.this, "请重新输入真实姓名");
            return;
        }
        if (StringUtils.isEmpty(hospital.getText().toString().trim())) {
            ToastUtils.show(IdentityActivity.this, "请重新输入所在医院名称");
            return;
        }
        if (StringUtils.isEmpty(mDepartmentTv.getText().toString().trim())) {
            ToastUtils.show(IdentityActivity.this, "请重新选择所在科室");
            return;
        }
        if (StringUtils.isEmpty(title.getText().toString().trim())) {
            ToastUtils.show(IdentityActivity.this, "请重新选择你的职称");
            return;
        }

        UserAuthorEntity userAuthorEntity = new UserAuthorEntity();
        userAuthorEntity.setName(name.getText().toString().trim());
        userAuthorEntity.setTitle(title.getText().toString().trim());
        userAuthorEntity.setHospital(hospital.getText().toString().trim());
        userAuthorEntity.setDepartment(mDepartmentTv.getText().toString().trim());
        HttpData.getInstance().HttpDataAuthorization(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(IdentityActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(IdentityActivity.this, data.getMsg());
                    return;
                }
                ToastUtils.show(IdentityActivity.this, "您已成功提交认证信息，请等待相关人员审核信息");
                finish();
            }
        }, userAuthorEntity);
    }

    /**
     * 填写职称的弹出窗
     */
    private PopupWindow academicPopupWindow;
    private final List<String> academics = new ArrayList<>();
    private String[] academic = new String[]{"主任医师", "副主任医师", "主治医师", "医师", "助理医师", "教授", "副教授", "讲师", "助教"};
    private String mChooseAcademic = "主任医师"; //用户选择的学历

    private void showTitlePopupwindow() {
        View academicPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        academicPopupWindow = new PopupWindow(academicPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView academicConfirmTv = (TextView) academicPopupwindowView.findViewById(R.id.academic_confirm);
        academicConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                academicPopupWindow.dismiss();
                title.setText(mChooseAcademic);
            }
        });

        NumberPicker academicPicker = (NumberPicker) academicPopupwindowView.findViewById(R.id.academic_picker);
        final TextView academicDisplayTv = (TextView) academicPopupwindowView.findViewById(R.id.academic_display);

        for (String i : academic) {
            Log.d("academic", i);
        }

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

        academicPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (numberPicker.getValue() > academic.length) {
                        mChooseAcademic = academic[academic.length];
                    } else {
                        mChooseAcademic = academic[numberPicker.getValue()];
                    }
                    Log.d("mChooseAcademic", mChooseAcademic);
                }
                academicDisplayTv.setText(mChooseAcademic);
            }
        });

        LinearLayout academicPopupParentLayout = (LinearLayout) academicPopupwindowView.findViewById(R.id.popup_parent);
        academicPopupParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (academicPopupWindow != null && academicPopupWindow.isShowing()) {
                    academicPopupWindow.dismiss();
                }
            }
        });

        academicPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        academicPopupWindow.setBackgroundDrawable(dw);
        academicPopupWindow.showAtLocation(academicPopupwindowView, Gravity.BOTTOM, 0, 0);

    }
}
