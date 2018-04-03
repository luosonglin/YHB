package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.Login_v2Activity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.snappydb.SnappydbException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议签到 页面
 */
public class PlusSignedDetailsActivity extends AppCompatActivity {

    @BindView(R.id.meeting_name)
    TextView meetingName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.qrcode_llyt)
    LinearLayout qrcodeLlyt;
    @BindView(R.id.meeting_name2)
    TextView meetingName2;
    @BindView(R.id.info)
    LinearLayout info;
    @BindView(R.id.name_icon)
    TextView nameIcon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time_icon)
    TextView timeIcon;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.location_icon)
    TextView locationIcon;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.phone_icon)
    TextView phoneIcon;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.email_icon)
    TextView emailIcon;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.type_icon)
    TextView typeIcon;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.money_icon)
    TextView moneyIcon;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.hottopics)
    LinearLayout hottopics;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.yimatong)
    RelativeLayout yimatong;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.yimatong_content)
    RelativeLayout yimatongContent;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.content_plus_signed_details)
    RelativeLayout contentPlusSignedDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_signed_details);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(view -> finish());

        initView();

        //设置屏幕亮度最大
        setWindowBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        make();
    }


    //生成二维码 可以设置Logo
    public void make() {
        String input = null;
        try {
            input = DBUtils.get(PlusSignedDetailsActivity.this, "confirmNumber");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        if (input == null) {
            finish();
            startActivity(new Intent(PlusSignedDetailsActivity.this, Login_v2Activity.class));
            return;
        }
        if (input.equals("")){
            Toast.makeText(this,"请先登录", Toast.LENGTH_SHORT).show();
        }else{
//            Bitmap qrCode = EncodingUtils.createQRCode(input, 1000,1000, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

            try {
                Bitmap qrCode = BitmapUtils.create2DCode(input);
                imageView.setImageBitmap(qrCode);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.share, R.id.yimatong, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
//                showSharePopupwindow();
                break;
            case R.id.yimatong:
                if (yimatongContent.getVisibility() == View.GONE) {
                    yimatongContent.setVisibility(View.VISIBLE);
                    content.setMovementMethod(ScrollingMovementMethod.getInstance());   //配合android:singleLine="false"  android:maxLines="5" android:scrollbars="vertical"  让 TextView 自带滚动条
                    yimatong.setBackground(this.getResources().getDrawable(R.drawable.textview_half_fillet_up));
                    yimatongContent.setBackground(this.getResources().getDrawable(R.drawable.textview_half_fillet_down));
                } else {
                    yimatongContent.setVisibility(View.GONE);
                    yimatong.setBackground(this.getResources().getDrawable(R.drawable.textview_radius_grey));
                }
                break;
            case R.id.button:
                break;
        }
    }

    /**
     * 设置当前窗口亮度
     * @param brightness
     */
    private void setWindowBrightness(float brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消屏幕最亮
        setWindowBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
    }
}
