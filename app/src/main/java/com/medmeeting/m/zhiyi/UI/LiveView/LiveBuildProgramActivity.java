package com.medmeeting.m.zhiyi.UI.LiveView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;

public class LiveBuildProgramActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.live_pic)
    ImageView livePic;
    @BindView(R.id.live_pic_tip_tv)
    TextView livePicTipTv;
    @BindView(R.id.live_pic_tip)
    LinearLayout livePicTip;
    @BindView(R.id.theme)
    EditText theme;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.start_time_llyt)
    LinearLayout startTimeLlyt;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.end_time_llyt)
    LinearLayout endTimeLlyt;
    @BindView(R.id.free)
    Button free;
    @BindView(R.id.charge)
    Button charge;
    @BindView(R.id.charge_amount)
    TextView chargeAmount;
    @BindView(R.id.open)
    Button open;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.introduction)
    EditText introduction;
    @BindView(R.id.buildllyt)
    LinearLayout buildllyt;

    private static final String TAG = LiveBuildProgramActivity.class.getSimpleName();

    //新增直播间
    private int userId;  //用户ID
    private String vidoTitle = "";  //直播间标题
    private long expectBeginTime = System.currentTimeMillis();  //预计开始时间（预约模式为马上直播时，可传null）
    private long expectEndTime;  //预计结束时间
    private String vidoLabel = "";  //直播间标题
    private String chargeType = "no";  //付费方式（免费：yes，收费：no）
    private String price = "0";  //价格
    private String privacyType = "public";  //是否公开(公开:public,隐私:private)
    private String vidoDesc = "";  //直播间描述
    private String expectType = "";  //预约模式(liveNow:马上直播,expect:预约直播)
    private String videoPhoto = "";  //直播间封面图片
    private String authorName;
    private String authorTitle;

    @BindView(R.id.progress)
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_build_program);
        ButterKnife.bind(this);
        toolBar();
        initView();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("新建直播节目");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        theme.setHorizontallyScrolling(true);
    }

    @OnClick({R.id.live_pic_tip, R.id.start_time_llyt, R.id.end_time_llyt, R.id.free, R.id.charge, R.id.open, R.id.close, R.id.buildllyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_pic_tip:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(LiveBuildProgramActivity.this);
                break;
            case R.id.start_time_llyt:
                new AlertDialog.Builder(this)
                        .setTitle(" ")
                        .setCancelable(false)
                        .setIcon(getResources().getDrawable(R.mipmap.logo))
                        .setMessage("选择您想要直播的时间")
                        .setNegativeButton("马上直播", (dialogInterface, i) -> {
                            expectType = "liveNow";
                            startTime.setText("现在直播");
                            endTime.setText("");
                            expectBeginTime = System.currentTimeMillis();
                        })
                        .setPositiveButton("预约时间", (dialogInterface, i) -> {
                            expectType = "expect";
                            showDateTimePopupwindow("START");
                        })
                        .show();
                break;
            case R.id.end_time_llyt:
                showDateTimePopupwindow("END");
                break;
            case R.id.free:
                free.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                free.setTextColor(getResources().getColor(R.color.white));
                charge.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                charge.setTextColor(getResources().getColor(R.color.black));
                chargeAmount.setVisibility(View.GONE);

                chargeType = "no";
                price = "0";
                break;
            case R.id.charge:
                free.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                free.setTextColor(getResources().getColor(R.color.black));
                charge.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                charge.setTextColor(getResources().getColor(R.color.white));
                chargeAmount.setVisibility(View.VISIBLE);
                final EditText et = new EditText(this);
                et.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);//弹出数字键盘，只能输入数字
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setIcon(R.mipmap.logo)
                        .setMessage("填写金额")
                        .setView(et)
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            String amount = et.getText().toString();
                            if (amount.equals("")) {
                                Toast.makeText(getApplicationContext(), "金额不能为空，请重新设定" + amount, Toast.LENGTH_LONG).show();
                                return;
                            }

                            Log.e(TAG, "eee1 " + amount + amount.matches(regEx));
                            if (Float.parseFloat(amount) == 0) {
                                ToastUtils.show(LiveBuildProgramActivity.this, "金额不能为0，请重新设定");
                                return;
                            }
                            chargeAmount.setText("费用： " + amount + "元");
                            chargeType = "yes";
                            price = amount;
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.open:
                open.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                open.setTextColor(getResources().getColor(R.color.white));
                close.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                close.setTextColor(getResources().getColor(R.color.black));
                privacyType = "public";
                break;
            case R.id.close:
                open.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                open.setTextColor(getResources().getColor(R.color.black));
                close.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                close.setTextColor(getResources().getColor(R.color.white));
                privacyType = "private";
                break;
            case R.id.buildllyt:
                BuildNewLiveProgramService();
                break;
        }
    }

    private void BuildNewLiveProgramService() {
        vidoTitle = theme.getText().toString().trim();
        authorName = name.getText().toString().trim();
        authorTitle = title.getText().toString().trim();
        vidoDesc = introduction.getText().toString().trim();
        if (vidoTitle.equals("")) {
            ToastUtils.show(LiveBuildProgramActivity.this, "请填写节目主题");
            return;
        }
        if (authorName.equals("")) {
            ToastUtils.show(LiveBuildProgramActivity.this, "请填写主讲人姓名");
            return;
        }
        if (authorTitle.equals("")) {
            ToastUtils.show(LiveBuildProgramActivity.this, "请填写主讲人职称");
            return;
        }
        if (vidoDesc.equals("")) {
            ToastUtils.show(LiveBuildProgramActivity.this, "请填写直播节目介绍");
            return;
        }
        LiveDto liveDto = new LiveDto(vidoTitle, videoPhoto, expectBeginTime, expectEndTime, authorName, authorTitle, privacyType, chargeType, Float.parseFloat(price), vidoDesc);
        buildllyt.setClickable(false);
        HttpData.getInstance().HttpDataAddLiveProgram(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if ("success".equals(httpResult3.getStatus())) {
                    finish();
                    buildllyt.setClickable(false);
                } else {
                    ToastUtils.show(LiveBuildProgramActivity.this, httpResult3.getMsg());
                    buildllyt.setClickable(true);
                    if (httpResult3.getMsg().equals("invalid_token")) {
                        ToastUtils.show(LiveBuildProgramActivity.this, "账号有问题，请测试人员重新登录");
                        startActivity(new Intent(LiveBuildProgramActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }, getIntent().getExtras().getInt("roomId"), liveDto);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String i : photos) {
                Log.e(TAG, i);
            }

            File file = new File(photos.get(0));
            // creates RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            // MultipartBody.Part is used to send also the actual filename
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
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
                        ToastUtils.show(LiveBuildProgramActivity.this, data.getMsg());
                        return;
                    }

                    videoPhoto = data.getExtra().getAbsQiniuImgHash();

                    Glide.with(LiveBuildProgramActivity.this)
                            .load(videoPhoto)
                            .crossFade()
                            .dontAnimate()
                            .into(new GlideDrawableImageViewTarget(livePic) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                    //在这里添加一些图片加载完成的操作
                                    super.onResourceReady(resource, animation);
                                    livePicTipTv.setText("修改直播封面");
                                }
                            });
                }
            }, body, description);


        }
    }


    /**
     * 弹窗
     */
    private PopupWindow mLiveSettingPopupWindow;
    private int year;
    private int month;
    private int day;
    private int hour = 0;
    private int minute = 0;
    //用于给textview赋值
    private String startDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute; //默认开始时间为当前时间
    private String endDateTime;
    //用于比较
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Date mStartDate;
    private Date mEndDate;
    private Date currentDate;

    @TargetApi(Build.VERSION_CODES.M)
    private void showDateTimePopupwindow(final String sign) {
        final View mLiveSettingPopupWindowView = LayoutInflater.from(this).inflate(R.layout.popupwindow_date_time, null);
        mLiveSettingPopupWindow = new PopupWindow(mLiveSettingPopupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        TextView back = (TextView) mLiveSettingPopupWindowView.findViewById(R.id.back);
        TextView confirm = (TextView) mLiveSettingPopupWindowView.findViewById(R.id.confirm);
        final DatePicker datePicker = (DatePicker) mLiveSettingPopupWindowView.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) mLiveSettingPopupWindowView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        LinearLayout popupDateTime = (LinearLayout) mLiveSettingPopupWindowView.findViewById(R.id.popup_date_time);

        //set the current date in a DatePicker
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;  //Calendar.getInstance().get(Calendar.MONTH) 月份少1，因为Calendar api月份是从0开始计数的
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.e(TAG, "begin " + year + "-" + month + "-" + day);
        datePicker.init(year, month-1, day, (datePicker1, i, i1, i2) -> {
            year = i;
            month = i1 + 1;
            day = i2;
        });

        //以下为版本兼容
        //timepick的getHour() 最低要求api >= 23
        final int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        Log.e(TAG, "hour " + hour + " minute "+ minute);

        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            hour = i;
            minute = i1;
        });

        confirm.setOnClickListener(v -> {

            if ("START".equals(sign)) {

                startDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;

                //判断开始时间是否早于当前时间
                try {
                    mStartDate = mSimpleDateFormat.parse(startDateTime + ":" + 59);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentDate = Calendar.getInstance().getTime();
                Log.e(TAG, "START " + startDateTime);
                Log.e(TAG, " " + mStartDate);
                Log.e(TAG, "  >>> " + currentDate);

                if (mStartDate.before(currentDate)) {
                    ToastUtils.show(LiveBuildProgramActivity.this, "开始时间不能在当前时间之前，请重新设置");
                    return;
                }
                mLiveSettingPopupWindow.dismiss();
                startTime.setText(startDateTime);
                expectBeginTime = DateUtils.dateToLong(mStartDate);

            } else if ("END".equals(sign)) {

                if (!expectType.equals("liveNow")) {
                    if (mStartDate == null) {
                        ToastUtils.show(LiveBuildProgramActivity.this, "请先设置开始时间，再设置结束时间");
                        mLiveSettingPopupWindow.dismiss();
                        return;
                    }
                }
                endDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;

                //判断结束时间是否早于开始时间
                try {
                    mEndDate = mSimpleDateFormat.parse(endDateTime + ":" + 59);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "END " + endDateTime);
                Log.e(TAG, "END " + mStartDate);
                Log.e(TAG, "END " + "  >>> " + mEndDate);

                if (!expectType.equals("liveNow")) {
                    if (mStartDate.after(mEndDate)) {
                        ToastUtils.show(LiveBuildProgramActivity.this, "结束时间应该晚于开始时间，请重新设置");
                        return;
                    }
                }
                mLiveSettingPopupWindow.dismiss();
                endTime.setText(endDateTime);
                expectEndTime = DateUtils.dateToLong(mEndDate);
            }
        });

        back.setOnClickListener(view -> mLiveSettingPopupWindow.dismiss());

        popupDateTime.setOnClickListener(v -> {
            if (mLiveSettingPopupWindow != null && mLiveSettingPopupWindow.isShowing()) {
                mLiveSettingPopupWindow.dismiss();
            }
        });

        mLiveSettingPopupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mLiveSettingPopupWindow.setBackgroundDrawable(dw);
        mLiveSettingPopupWindow.showAtLocation(mLiveSettingPopupWindowView, Gravity.BOTTOM, 0, 0);
    }

    // 所有特殊字符
    private String regEx = "[`~!@#$%^&*()+-=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
