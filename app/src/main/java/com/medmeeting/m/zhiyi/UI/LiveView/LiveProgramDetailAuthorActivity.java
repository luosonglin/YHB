package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.activity.CaptureActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;

/**
 * 主播用的直播节目详情页
 */
public class LiveProgramDetailAuthorActivity extends AppCompatActivity {

    private static final String TAG = LiveProgramDetailAuthorActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView titleTv, name, title;
    private ImageView backgroundIv, userPic;
    private Integer programId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_program_detail_author);
        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(getIntent().getExtras().getString("title"));

        programId = getIntent().getExtras().getInt("programId");

        backgroundIv = (ImageView) findViewById(R.id.img);
        Glide.with(LiveProgramDetailAuthorActivity.this)
                .load(getIntent().getExtras().getString("coverPhoto"))
                .crossFade()
                .into(backgroundIv);

        userPic = (ImageView) findViewById(R.id.live_user_pic);
        Glide.with(LiveProgramDetailAuthorActivity.this)
                .load(getIntent().getExtras().getString("userPic"))
                .crossFade()
                .transform(new GlideCircleTransform(LiveProgramDetailAuthorActivity.this))
                .placeholder(R.mipmap.avator_default)
                .into(userPic);

        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getExtras().getString("authorName"));
        title = (TextView) findViewById(R.id.author_title);
        title.setText(getIntent().getExtras().getString("authorTitle"));

        findViewById(R.id.invitation_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveInvitationLetterActivity.class);
                intent.putExtra("roomId", getIntent().getExtras().getInt("roomId"));
                startActivity(intent);
            }
        });

        findViewById(R.id.to_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }
    private final static int REQ_CODE = 1102;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {

            if (data == null) return;

            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);

            Log.e(TAG, "扫码结果：" + result + bitmap);

//            if (result != null) {
//                Intent intent = new Intent(LiveProgramDetailAuthorActivity.this, LiveLoginWebActivity.class);
//                intent.putExtra("QRCode", result + userId);
//                startActivity(intent);
//            }
        }
    }
}
