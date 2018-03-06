package com.medmeeting.m.zhiyi.UI.LiveView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
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
import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuTokenDto;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

public class LiveUpdateProgramActivity extends AppCompatActivity {

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

    private static final String TAG = LiveUpdateProgramActivity.class.getSimpleName();

    //新增直播间
    private int userId;  //用户ID
    private String vidoTitle = "";  //直播间标题
    private long expectBeginTime;  //预计开始时间（预约模式为马上直播时，可传null）
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

    LiveDto mLiveDto = new LiveDto();

    @BindView(R.id.progress)
    View mProgressView;

    private long createTime;
    private long updateTime;

    private Integer programId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_update_program);
        ButterKnife.bind(this);
        toolBar();
//        showProgress(true);
        initView();
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("修改直播节目");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        programId = getIntent().getExtras().getInt("programId");
        HttpData.getInstance().HttpDataGetProgramDetail(new Observer<HttpResult3<Object, LiveDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<Object, LiveDto> data) {
                Glide.with(LiveUpdateProgramActivity.this)
                        .load(data.getEntity().getCoverPhoto())
//                        .crossFade()
//                        .transform(new GlideCircleTransform(LiveUpdateProgramActivity.this))
                        .into(new GlideDrawableImageViewTarget(livePic) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                //在这里添加一些图片加载完成的操作
                                super.onResourceReady(resource, animation);
                                showProgress(false);
                                livePicTipTv.setText("修改直播封面");
                            }
                        });

                videoPhoto = data.getEntity().getCoverPhoto();
                theme.setText(data.getEntity().getTitle());
                name.setText(data.getEntity().getAuthorName());
                title.setText(data.getEntity().getAuthorTitle());
                if (DateUtils.formatDate(data.getEntity().getStartTime(), DateUtils.TYPE_01).equals("1970-01-01 08:00:00")) {
                    startTime.setText(DateUtils.formatDate(System.currentTimeMillis(), DateUtils.TYPE_01));
                } else {
                    startTime.setText(DateUtils.formatDate(data.getEntity().getStartTime(), DateUtils.TYPE_01));
                }

                if (DateUtils.formatDate(data.getEntity().getEndTime(), DateUtils.TYPE_01).equals("1970-01-01 08:00:00")) {
                    endTime.setText("");
                } else {
                    endTime.setText(DateUtils.formatDate(data.getEntity().getEndTime(), DateUtils.TYPE_01));
                }
                if (data.getEntity().getChargeType().equals("no")) {
                    free.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                    free.setTextColor(getResources().getColor(R.color.white));
                    charge.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                    charge.setTextColor(getResources().getColor(R.color.black));
                    chargeAmount.setVisibility(View.GONE);
                } else if (data.getEntity().getChargeType().equals("yes")) {
                    free.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                    free.setTextColor(getResources().getColor(R.color.black));
                    charge.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                    charge.setTextColor(getResources().getColor(R.color.white));
                    chargeAmount.setVisibility(View.VISIBLE);
                    chargeAmount.setText(data.getEntity().getPrice() + "");
                }
                if (data.getEntity().getPrivacyType().equals("public")) {
                    open.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                    open.setTextColor(getResources().getColor(R.color.white));
                    close.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                    close.setTextColor(getResources().getColor(R.color.black));
                } else if (data.getEntity().getPrivacyType().equals("private")) {
                    open.setBackground(getResources().getDrawable(R.drawable.button_live_gray));
                    open.setTextColor(getResources().getColor(R.color.black));
                    close.setBackground(getResources().getDrawable(R.drawable.button_live_free));
                    close.setTextColor(getResources().getColor(R.color.white));
                }
                introduction.setText(data.getEntity().getDes());
                createTime = data.getEntity().getCreateTime();
                updateTime = data.getEntity().getUpdateTime();
                privacyType = data.getEntity().getPrivacyType();
                mLiveDto = data.getEntity();
                expectBeginTime = data.getEntity().getStartTime();
                expectEndTime = data.getEntity().getEndTime();
            }
        }, programId);
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
                        .start(LiveUpdateProgramActivity.this);
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
                        .show()
                        .setCanceledOnTouchOutside(true);
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
//                et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
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
                                ToastUtils.show(LiveUpdateProgramActivity.this, "金额不能为0，请重新设定");
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
                vidoTitle = theme.getText().toString().trim();
                authorName = name.getText().toString().trim();
                authorTitle = title.getText().toString().trim();
                vidoDesc = introduction.getText().toString().trim();
                if (vidoTitle.equals("")) {
                    ToastUtils.show(LiveUpdateProgramActivity.this, "请填写节目主题");
                    return;
                }
                if (authorName.equals("")) {
                    ToastUtils.show(LiveUpdateProgramActivity.this, "请填写主讲人姓名");
                    return;
                }
                if (authorTitle.equals("")) {
                    ToastUtils.show(LiveUpdateProgramActivity.this, "请填写主讲人职称");
                    return;
                }
                if (vidoDesc.equals("")) {
                    ToastUtils.show(LiveUpdateProgramActivity.this, "请填写直播节目介绍");
                    return;
                }
                buildllyt.setClickable(false);
//                LiveDto liveDto = new LiveDto(vidoTitle, videoPhoto, expectBeginTime, expectEndTime, authorName, authorTitle, privacyType, chargeType, Float.parseFloat(price), vidoDesc);
                mLiveDto.setTitle(vidoTitle);
                mLiveDto.setCoverPhoto(videoPhoto);
                mLiveDto.setAuthorName(name.getText().toString().trim());
                mLiveDto.setStartTime(expectBeginTime);
                mLiveDto.setEndTime(expectEndTime);
                mLiveDto.setAuthorTitle(authorTitle);
                mLiveDto.setPrivacyType(privacyType);
                mLiveDto.setChargeType(chargeType);
                mLiveDto.setPrice(Float.parseFloat(price));
                mLiveDto.setDes(vidoDesc);
                mLiveDto.setId(programId);
                mLiveDto.setUserId(Data.getUserId());
                mLiveDto.setCreateTime(createTime);
                mLiveDto.setUpdateTime(updateTime);
                HttpData.getInstance().HttpDataUpdataLiveProgram(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LiveUpdateProgramActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if ("success".equals(httpResult3.getStatus())) {
                            buildllyt.setClickable(false);
                            finish();
                        } else {
                            ToastUtils.show(LiveUpdateProgramActivity.this, httpResult3.getMsg());
                            buildllyt.setClickable(true);
                            if (httpResult3.getMsg().equals("invalid_token")) {
                                ToastUtils.show(LiveUpdateProgramActivity.this, "账号有问题，请测试人员重新登录");
                                startActivity(new Intent(LiveUpdateProgramActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }
                }, mLiveDto);
                break;
        }
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
//            showProgress(true);
//            ToastUtils.show(LiveUpdateProgramActivity.this, "正在上传封面图片...");
            getQiniuToken(photos.get(0));

            Glide.with(LiveUpdateProgramActivity.this)
                    .load(photos.get(0))
                    .crossFade()
                    .into(new GlideDrawableImageViewTarget(livePic) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            //在这里添加一些图片加载完成的操作
                            super.onResourceReady(resource, animation);
//                            showProgress(false);
                            livePicTipTv.setText("修改直播封面");
                        }
                    });
        }
    }

    private List<String> qiniuData = new ArrayList<>();
    private String qiniuKey;
    private String qiniuToken;
    private String images = "";

    private void getQiniuToken(final String file) {
        HttpData.getInstance().HttpDataGetQiniuToken(new Observer<QiniuTokenDto>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(QiniuTokenDto q) {
                if (q.getCode() != 200 || q.getData().getUploadToken() == null || q.getData().getUploadToken().equals("")) {
                    showProgress(false);
                    return;
                }
                qiniuToken = q.getData().getUploadToken();

                // 设置图片名字
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                qiniuKey = "android_live_" + sdf.format(new Date());

                int i = new Random().nextInt(1000) + 1;

                Log.e(TAG, "File对象、或 文件路径、或 字节数组: " + file);
                Log.e(TAG, "指定七牛服务上的文件名，或 null: " + qiniuKey + i);
                Log.e(TAG, "从服务端SDK获取: " + qiniuToken);
                Log.e(TAG, "http://ono5ms5i0.bkt.clouddn.com/" + qiniuKey + i);

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

//                                    ToastUtils.show(LiveUpdateProgramActivity.this, "封面正在上传，上传速度取决于当前网络，请耐心等待...");
                            } else {
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + res);

                            videoPhoto = "http://ono5ms5i0.bkt.clouddn.com/" + key1;
                        }, null);
            }
        }.start();
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
        month = c.get(Calendar.MONTH) + 1;  //Calendar.getInstance().get(Calendar.MONTH) 月份少1，因为Calendar api月份是从0开始计数的
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.e(TAG, "begin " + year + "-" + month + "-" + day);
        datePicker.init(year, month, day, (datePicker1, i, i1, i2) -> {
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
        Log.e(TAG, "hour " + hour + " minute " + minute);

        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            hour = i;
            minute = i1;
        });

        confirm.setOnClickListener(v -> {

            if ("START".equals(sign)) {

                startDateTime = year + "-" + judgeDecade(month) + "-" + judgeDecade(day) + " " + judgeDecade(hour) + ":" + judgeDecade(minute);

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
                    ToastUtils.show(LiveUpdateProgramActivity.this, "开始时间不能在当前时间之前，请重新设置");
                    return;
                }
                mLiveSettingPopupWindow.dismiss();
                startTime.setText(startDateTime);
//                    expectBeginTime = DateUtils.stringToLong(startDateTime, DateUtils.TYPE_01);
                expectBeginTime = DateUtils.dateToLong(mStartDate);

            } else if ("END".equals(sign)) {

                if (!expectType.equals("liveNow")) {
                    if (mStartDate == null) {
                        ToastUtils.show(LiveUpdateProgramActivity.this, "请先设置开始时间，再设置结束时间");
                        mLiveSettingPopupWindow.dismiss();
                        return;
                    }
                }
                endDateTime = year + "-" + judgeDecade(month) + "-" + judgeDecade(day) + " " + judgeDecade(hour) + ":" + judgeDecade(minute);

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
                        ToastUtils.show(LiveUpdateProgramActivity.this, "结束时间应该晚于开始时间，请重新设置");
                        return;
                    }
                }
                mLiveSettingPopupWindow.dismiss();
                endTime.setText(endDateTime);
//                    expectEndTime = DateUtils.stringToLong(endDateTime, DateUtils.TYPE_01);
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
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    //判断时候是小于10，如果小于10，则前面加0
    private String judgeDecade(int i){
        if (i >= 10)
            return Integer.toString(i);
        else
            return "0"+i;
    }
}
