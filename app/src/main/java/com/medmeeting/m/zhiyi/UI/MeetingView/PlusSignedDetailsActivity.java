package com.medmeeting.m.zhiyi.UI.MeetingView;

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
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.snappydb.SnappydbException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议签到 页面
 */
public class PlusSignedDetailsActivity extends AppCompatActivity {

    @Bind(R.id.meeting_name)
    TextView meetingName;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.share)
    TextView share;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.qrcode_llyt)
    LinearLayout qrcodeLlyt;
    @Bind(R.id.meeting_name2)
    TextView meetingName2;
    @Bind(R.id.info)
    LinearLayout info;
    @Bind(R.id.name_icon)
    TextView nameIcon;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.time_icon)
    TextView timeIcon;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.location_icon)
    TextView locationIcon;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.phone_icon)
    TextView phoneIcon;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.email_icon)
    TextView emailIcon;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.type_icon)
    TextView typeIcon;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.money_icon)
    TextView moneyIcon;
    @Bind(R.id.money)
    TextView money;
    @Bind(R.id.hottopics)
    LinearLayout hottopics;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.yimatong)
    RelativeLayout yimatong;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.yimatong_content)
    RelativeLayout yimatongContent;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.content_plus_signed_details)
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
