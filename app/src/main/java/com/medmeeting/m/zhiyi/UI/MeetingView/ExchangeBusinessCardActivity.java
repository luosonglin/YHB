package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.common.BitmapUtils;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult5;
import com.medmeeting.m.zhiyi.UI.Entity.UserInfoDto;
import com.medmeeting.m.zhiyi.Util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;

public class ExchangeBusinessCardActivity extends AppCompatActivity {


    private static final String TAG = ExchangeBusinessCardActivity.class.getSimpleName();
    @BindView(R.id.QRflyt)
    FrameLayout QRflyt;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.company_info)
    TextView companyInfo;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.info)
    LinearLayout info;

    private final static int REQ_CODE = 1102;
    private static final String TAG_CARD = "001";
    private String cardType;
    private UserInfoDto userInfo;
    private Map<String, Object> options = new HashMap<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.hospital)
    TextView hospital;
    @BindView(R.id.doctor_llyt)
    LinearLayout doctorLlyt;
    @BindView(R.id.pic)
    CircleImageView pic;
    @BindView(R.id.QRImage)
    ImageView QRImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card);
        ButterKnife.bind(this);

        initToolbar();

        initQRcodeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initToolbar() {
        toolbar.setTitle("二维码名片");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initQRcodeView() {

        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        String confirmNumber = bundle.getString("confirmNumber");
        cardType = bundle.getString("card_type");

        options.put("confirmNum", confirmNumber);

        HttpData.getInstance().HttpDataGetUserInfoByConfirmNum(new Observer<HttpResult5<UserInfoDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult5<UserInfoDto> userInfoDtoHttpResult4) {
                userInfo = userInfoDtoHttpResult4.getEntity();
                Log.e(TAG, userInfo.getId() + " ");

                if (userInfo != null) {
                    if (cardType.equals("001")) {
                        QRflyt.setVisibility(View.GONE);
                        info.setVisibility(View.VISIBLE);

                        name.setText(StringUtils.isEmpty(userInfo.getName()) ? userInfo.getNickName() : userInfo.getName());
                        Picasso.with(ExchangeBusinessCardActivity.this).load(userInfo.getUserPic())
                                .error(R.mipmap.avator_default)
                                .into(pic);

                        companyInfo.setText("公司");
                        company.setText(userInfo.getHospital()+" "+userInfo.getDepartment());
                        nickname.setText(userInfo.getNickName());
                        phone.setText(userInfo.getMobilePhone());

                        return;
                    }

                    QRflyt.setVisibility(View.VISIBLE);
                    tip.setVisibility(View.VISIBLE);

                    info.setVisibility(View.GONE);

                    name.setText(StringUtils.isEmpty(userInfo.getName()) ? userInfo.getNickName() : userInfo.getName());
                    hospital.setText(userInfo.getHospital()+" "+userInfo.getDepartment());

                    Picasso.with(ExchangeBusinessCardActivity.this).load(userInfo.getUserPic())
                            .error(R.mipmap.avator_default)
                            .into(pic);
                }
            }
        },options);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapUtils.create2DCode(confirmNumber);
            QRImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {

            if (data == null) return;

            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);

//            ToastUtils.show(getActivity(), "扫码结果：" + result + bitmap);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("confirmNumber", result);
            bundle.putString("card_type", TAG_CARD);//扫描出来的，别人
            intent.putExtras(bundle);
            intent.setClass(ExchangeBusinessCardActivity.this, ExchangeBusinessCardActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({ R.id.communication})//R.id.plus,
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.plus:
//                break;
            case R.id.communication:
                Intent intent = new Intent(ExchangeBusinessCardActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
                break;
        }
    }
}
