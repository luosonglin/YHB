package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HospitalInfo;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddActivationEntity;
import com.medmeeting.m.zhiyi.UI.MineView.ChooseDepartmentActivity;
import com.medmeeting.m.zhiyi.Util.NetworkUtils;
import com.medmeeting.m.zhiyi.Util.StringUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;


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

    @BindView(R.id.name_et)
    EditText name;
    @BindView(R.id.company1)
    EditText company1;
    @BindView(R.id.position1)
    EditText position1;
    @BindView(R.id.hospital)
    AutoCompleteTextView hospital;
    @BindView(R.id.department2)
    TextView department2;
    @BindView(R.id.position2)
    TextView position2;
    @BindView(R.id.title2)
    TextView title2;
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
    @BindView(R.id.activate_save)
    TextView activateSave;
    private Toolbar toolbar;

    UserAddActivationEntity userAddActivationEntity;

    private int type = 1;

    private String[] res = {"beijing1", "beijing2", "beijing3", "shanghai1", "shanghai2", "guangzhou1", "shenzhen"};
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        ButterKnife.bind(this);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        toolBar();

        activate_1.setVisibility(View.VISIBLE);
        activate_2.setVisibility(View.GONE);
        activate_3.setVisibility(View.GONE);
        activate_4.setVisibility(View.GONE);
        activate_5.setVisibility(View.GONE);
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.activate1, R.id.activate2, R.id.department2, R.id.position2, R.id.title2, R.id.activate3, R.id.activate4, R.id.activate5, R.id.activate6, R.id.activate_save})
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

                type = 1;

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

                type = 2;


                hospital.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.e(getLocalClassName() + " beforeTextChanged ", charSequence.toString().trim());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        Log.e(getLocalClassName() + " onTextChanged ", charSequence.toString().trim());

                        new ShowPopwindow().execute(charSequence.toString().trim());
                        /*if (charSequence.toString().trim().length() >= 1) {
                            Log.e(getLocalClassName() + " onTextChanged ", charSequence.toString().trim().length() + "");
                            HttpData.getInstance().HttpDataGetHospitalInfo(new Observer<HttpResult3<HospitalInfo, Object>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HttpResult3<HospitalInfo, Object> data) {
                                    if (!data.getStatus().equals("success")) {
                                        ToastUtils.show(ActivateActivity.this, data.getMsg());
                                        return;
                                    }
                                    List<String> hospitalNames = new ArrayList<>();
                                    for (HospitalInfo i : data.getData()) {
                                        hospitalNames.add(i.getHsName());
                                    }
                                    res = hospitalNames.toArray(new String[hospitalNames.size()]);

                                    for (int i = 0; i < res.length; i++)
                                        Log.e(getLocalClassName(), res[i]);
                                    //edittext联想提示弹窗
                                    adapter = new ArrayAdapter<>(ActivateActivity.this, android.R.layout.simple_list_item_1, res);
                                    hospital.setAdapter(adapter);

                                }
                            }, charSequence.toString().trim());//"上海");//
                        }*/
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        Log.e(getLocalClassName() + " afterTextChanged ", editable.toString().trim());
                    }
                });


                break;
            case R.id.department2:
                Intent intent = new Intent(ActivateActivity.this, ChooseDepartmentActivity.class);
                int requestCode = 0;
                startActivityForResult(intent, requestCode);
                break;
            case R.id.position2:
                showPositionPopupwindow();
                break;
            case R.id.title2:
                showTitlePopupwindow();
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

                type = 3;

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

                type = 4;
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

                type = 5;
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

                type = 6;
                break;
            case R.id.activate_save:
                if (type == 0) {
                    ToastUtils.show(ActivateActivity.this, "请选择一种身份");
                    return;
                } else if (type == 1) {
                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("ASSOCIATION");
                    userAddActivationEntity.setIdentityTitle("医疗协会");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                    userAddActivationEntity.setCompany(company1.getText().toString().trim());
                    userAddActivationEntity.setPostion(position1.getText().toString().trim());
                } else if (type == 2) {
                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("MEDICAL_STAFF");
                    userAddActivationEntity.setIdentityTitle("医护人员");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                    userAddActivationEntity.setCompany(hospital.getText().toString().trim());
                    userAddActivationEntity.setDepartment(department2.getText().toString().trim());
                    userAddActivationEntity.setPostion(position2.getText().toString().trim());
                    userAddActivationEntity.setTitle(title2.getText().toString().trim());
                } else if (type == 3) {

                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("MEDICAL_COMPANY");
                    userAddActivationEntity.setIdentityTitle("药械企业");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                    userAddActivationEntity.setCompany(company3.getText().toString().trim());
                    userAddActivationEntity.setDepartment(department3.getText().toString().trim());
                    userAddActivationEntity.setPostion(position3.getText().toString().trim());
                } else if (type == 4) {
                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("MEDICO");
                    userAddActivationEntity.setIdentityTitle("医学生");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                    userAddActivationEntity.setCompany(school.getText().toString().trim());
                    userAddActivationEntity.setDepartment(major.getText().toString().trim());
                    userAddActivationEntity.setDiploma(education.getText().toString().trim());
                    userAddActivationEntity.setEntranceDate(year.getText().toString().trim());
                } else if (type == 5) {
                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("EDUCATION_SCIENCE");
                    userAddActivationEntity.setIdentityTitle("医药教科研人员");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                    userAddActivationEntity.setCompany(company5.getText().toString().trim());
                    userAddActivationEntity.setDepartment(department5.getText().toString().trim());
                    userAddActivationEntity.setPostion(position5.getText().toString().trim());
                } else if (type == 6) {
                    userAddActivationEntity = new UserAddActivationEntity();
                    userAddActivationEntity.setIdentityCode("OTHER");
                    userAddActivationEntity.setIdentityTitle("其他人员");

                    userAddActivationEntity.setName(name.getText().toString().trim());
                }

                final View popupView = this.getLayoutInflater().inflate(R.layout.popupwindow_activate, null);

                // 创建PopupWindow对象，指定宽度和高度
                final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, 1600); //ViewGroup.LayoutParams.MATCH_PARENT);
                // 设置动画
                window.setAnimationStyle(R.style.popup_window_anim);
                // 设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0DFFFFFF")));

                // 设置可以获取焦点
                window.setFocusable(false);
                // 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);

                // 更新popupwindow的状态
                window.update();
                // 以下拉的方式显示，并且可以设置显示的位置
                window.showAtLocation(activateSave, Gravity.CENTER, 0, 0);

                LinearLayout know = (LinearLayout) popupView.findViewById(R.id.know);
                know.setOnClickListener(view1 -> {
                    window.dismiss();
                    HttpData.getInstance().HttpDataActivate(new Observer<HttpResult3>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.show(ActivateActivity.this, e.getMessage());
                        }

                        @Override
                        public void onNext(HttpResult3 data) {
                            if (!data.getStatus().equals("success")) {
                                ToastUtils.show(ActivateActivity.this, data.getMsg());
                                return;
                            }
                            Intent intent = new Intent(ActivateActivity.this, ActivatedActivity.class);
                            intent.putExtra("code", userAddActivationEntity.getIdentityCode());
                            intent.putExtra("title", userAddActivationEntity.getIdentityTitle());
                            startActivity(intent);
                            finish();
                        }
                    }, userAddActivationEntity);
                });

                break;
        }
    }

    //选择科室
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.equals(""))
            return;
        String department = data.getStringExtra("department");
        switch (resultCode) {
            case 0:
                department2.setText(department);
                break;
            default:
                break;
        }
    }

    /**
     * 填写职务的弹出窗
     */
    private PopupWindow titlePopupWindow;
    private String[] titles = new String[]{"未定级（含研究生在读）", "初级职称", "中级职称", "副高级职称", "高级职称"};
    private String mChooseTitle = "中级职称"; //用户选择的学历

    private void showTitlePopupwindow() {
        View titlePopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        titlePopupWindow = new PopupWindow(titlePopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        final TextView titleConfirmTv = (TextView) titlePopupwindowView.findViewById(R.id.academic_confirm);
        titleConfirmTv.setOnClickListener(v -> {
            titlePopupWindow.dismiss();
            title2.setText(mChooseTitle);
        });

        NumberPicker titlePicker = (NumberPicker) titlePopupwindowView.findViewById(R.id.academic_picker);
        final TextView titleDisplayTv = (TextView) titlePopupwindowView.findViewById(R.id.academic_display);

        if (!StringUtils.isEmpty(Arrays.toString(titles))) {
            titlePicker.setDisplayedValues(titles);//test data
            Log.d("hahaha", titles.length + "");

            titlePicker.setMinValue(0);
            if (titles.length <= 1) {
                titlePicker.setMaxValue(1);
            } else {
                titlePicker.setMaxValue(titles.length - 1);
            }
        } else {
            titlePicker.setMinValue(0);
            titlePicker.setDisplayedValues(new String[]{"暂无数据"});
            titlePicker.setMaxValue(0);
        }

        titlePicker.setValue(2);

        titlePicker.setWrapSelectorWheel(false); //防止NumberPicker无限滚动
        titlePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入

        titlePicker.setFocusable(true);
        titlePicker.setFocusableInTouchMode(true);

        titlePicker.setOnScrollListener((numberPicker, scrollState) -> {
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                if (numberPicker.getValue() > titles.length) {
                    mChooseTitle = titles[titles.length];
                } else {
                    mChooseTitle = titles[numberPicker.getValue()];
                }
                Log.d("mChooseAcademic", mChooseTitle);
            }
            titleDisplayTv.setText(mChooseTitle);
        });

        LinearLayout titlePopupParentLayout = (LinearLayout) titlePopupwindowView.findViewById(R.id.popup_parent);
        titlePopupParentLayout.setOnClickListener(v -> {
            if (titlePopupWindow != null && titlePopupWindow.isShowing()) {
                titlePopupWindow.dismiss();
            }
        });

        titlePopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        titlePopupWindow.setBackgroundDrawable(dw);
        titlePopupWindow.showAtLocation(titlePopupwindowView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 填写职称的弹出窗
     */
    private PopupWindow positionPopupWindow;
    private String[] positions = new String[]{"院长", "副院长", "科室/部门主任", "科室/部门副主任", "临床医师", "药师", "护士", "其他医技人员"};
    private String mChoosePosition = "科室/部门主任"; //用户选择的职称

    private void showPositionPopupwindow() {
        View positionPopupwindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_academic, null);
        positionPopupWindow = new PopupWindow(positionPopupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        mChoosePosition = "科室/部门主任";

        final TextView positionConfirmTv = (TextView) positionPopupwindowView.findViewById(R.id.academic_confirm);
        positionConfirmTv.setOnClickListener(v -> {
            positionPopupWindow.dismiss();
            position2.setText(mChoosePosition);
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


    private class ShowPopwindow extends AsyncTask<String, Void, List<HospitalInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<HospitalInfo> doInBackground(String... strings) {
            if (NetworkUtils.isNetworkConnected(ActivateActivity.this)) {
                //获取数据
                return getHospitalService(strings[0]);
            } else {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<HospitalInfo> hospitalInfos) {
            Log.e(getLocalClassName(), hospitalInfos.size() + " onPostExecute");

            List<String> hospitalNames = new ArrayList<>();
            for (HospitalInfo i : hospitalInfos) {
                hospitalNames.add(i.getHsName());
            }
            res = hospitalNames.toArray(new String[hospitalNames.size()]);

            for (int i = 0; i < res.length; i++)
                Log.e(getLocalClassName(), res[i]);
            //edittext联想提示弹窗
            adapter = new ArrayAdapter<>(ActivateActivity.this, android.R.layout.simple_list_item_1, res);
            hospital.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<HospitalInfo> getHospitalService(String word) {
        List<HospitalInfo> hospitalInfoList = new ArrayList<>();

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.API_SERVER_LIVE_TEST + "/v1/app/hospital/A/getHospitalDetail?pojo=" + word);
//            HttpGet httpGet = new HttpGet(Constant.API_SERVER_LIVE + "/v1/app/hospital/A/getHospitalDetail?pojo=" + word);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            //HttpURLConnection.HTTP_OK即200响应码
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream ins = response.getEntity().getContent();

                //将输入流转换成字符串
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                String jsonString = baos.toString();
                baos.close();
                ins.close();

                String data = null;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);//json就是JSON字符串
                    Iterator iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = jsonObject.getString(key);
                        Log.e(getLocalClassName(), "key=" + key + "\tvalue=" + value);//键值对key value

                        if (key.equals("data")) {
                            data = jsonObject.getString("data");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //转换成json数据处理
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HospitalInfo hospitalInfo = new HospitalInfo();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    hospitalInfo.setHsId(jsonObject.getInt("hsId"));
                    hospitalInfo.setHsAddress(jsonObject.getString("hsAddress"));
                    hospitalInfo.setHsCity(jsonObject.getString("hsCity"));
                    hospitalInfo.setHsName(jsonObject.getString("hsName"));
                    hospitalInfo.setHsProvince(jsonObject.getString("hsProvince"));
//                    hospitalInfo.setSelectCount(jsonObject.getInt("selectCount"));

                    hospitalInfoList.add(hospitalInfo);
                }

                return hospitalInfoList;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    //医院弹窗
    PopupWindow hospitalPopupWindow;

    private void initPopWindow() {
        if (hospitalPopupWindow == null) {
            hospitalPopupWindow = new PopupWindow(LayoutInflater.from(ActivateActivity.this).inflate(R.layout.popupwindow_hospital, null), LinearLayout.LayoutParams.MATCH_PARENT, 600, true);

        }
        if (hospitalPopupWindow.isShowing()) hospitalPopupWindow.dismiss();
    }
}
