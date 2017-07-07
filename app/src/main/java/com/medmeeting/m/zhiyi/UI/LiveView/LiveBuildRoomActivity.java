package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.TagAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.QiniuToken;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import rx.Observer;

public class LiveBuildRoomActivity extends AppCompatActivity {

    @Bind(R.id.live_pic)
    ImageView livePic;
    @Bind(R.id.live_pic_tip_tv)
    TextView livePicTipTv;
    @Bind(R.id.live_pic_tip)
    LinearLayout livePicTip;
    @Bind(R.id.theme)
    EditText theme;
    @Bind(R.id.classify_tv)
    TextView classifyTv;
    @Bind(R.id.classify)
    LinearLayout classify;
    @Bind(R.id.introduction)
    EditText introduction;
    @Bind(R.id.buildllyt)
    LinearLayout buildllyt;
    private Toolbar toolbar;
    private static final String TAG = LiveBuildRoomActivity.class.getSimpleName();
    private TagAdapter mBaseQuickAdapter;
    //新增直播间
    private int userId;  //用户ID
    private String videoTitle = "";  //直播间标题
    private String videoLabel = "";  //直播间标题
    private String videoDesc = "";  //直播间描述
    private String videoPhoto = "";  //直播间封面图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_build_room);
        ButterKnife.bind(this);
        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }

    @OnClick({R.id.live_pic_tip, R.id.classify, R.id.buildllyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_pic_tip:
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(LiveBuildRoomActivity.this);
                break;
            case R.id.classify:
                initTagsPopupWindow();
                break;
            case R.id.buildllyt:
                videoTitle = theme.getText().toString().trim();
                videoLabel = classifyTv.getText().toString().trim();
                videoDesc = introduction.getText().toString().trim();
                LiveRoomDto liveRoomDto = new LiveRoomDto(videoTitle, videoPhoto, videoLabel, videoDesc);

                HttpData.getInstance().HttpDataAddLiveRoom(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage()
                                +"\n"+e.getCause()
                                +"\n"+e.getLocalizedMessage()
                                +"\n"+e.getStackTrace());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if ("success".equals(httpResult3.getStatus())) {
                            startActivity(new Intent(LiveBuildRoomActivity.this, MyLiveRoomActivity.class));
                            finish();
                        } else {
                            ToastUtils.show(LiveBuildRoomActivity.this, httpResult3.getMsg());
                        }
                    }
                }, liveRoomDto);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (data != null) {
            photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
        }

        for (String i : photos) {
            Log.e(TAG, i);
        }
        getQiniuToken(photos.get(0));
    }

    private List<String> qiniuData = new ArrayList<>();
    private String qiniuKey;
    private String qiniuToken;
    private String images = "";

    private void getQiniuToken(final String file) {
        HttpData.getInstance().HttpDataGetQiniuToken(new Observer<QiniuToken>() {
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
            public void onNext(QiniuToken q) {
                if (q.getCode() != 200 || q.getData().getUploadToken() == null || q.getData().getUploadToken().equals("")) {
                    return;
                }
                qiniuToken = q.getData().getUploadToken();

                // 设置图片名字
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
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
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                if (info.isOK()) {
                                    Log.i("qiniu", "Upload Success");
                                    Glide.with(LiveBuildRoomActivity.this)
                                            .load("http://ono5ms5i0.bkt.clouddn.com/" + key)
                                            .crossFade()
                                            .placeholder(R.mipmap.live_title_pic)
                                            .into(livePic);
                                    livePicTipTv.setText("修改直播封面");
                                } else {
                                    Log.i("qiniu", "Upload Fail");
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                }
                                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);

                                videoPhoto = "http://ono5ms5i0.bkt.clouddn.com/" + key;
                            }
                        }, null);
            }
        }.start();
    }

    /**
     * 标签弹窗
     */
    private void initTagsPopupWindow() {
        final View popupView = LiveBuildRoomActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_live_classify, null);

        final List<TagDto> tags = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.classify);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mBaseQuickAdapter = new TagAdapter(R.layout.item_tag, null);
        recyclerView.setAdapter(mBaseQuickAdapter);

        HttpData.getInstance().HttpDataGetLiveTags(new Observer<HttpResult3<TagDto, Object>>() {
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
            public void onNext(HttpResult3<TagDto, Object> tagDtoObjectHttpResult3) {
                mBaseQuickAdapter.addData(tagDtoObjectHttpResult3.getData());
                tags.addAll(tagDtoObjectHttpResult3.getData());
                Log.e(TAG, "onNext");
            }
        });

        // 创建PopupWindow对象，指定宽度和高度
//                PopupWindow window = new PopupWindow(popupView, 400, 600);
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
        window.showAsDropDown(buildllyt, 0, 20);

        ImageView cancelIv = (ImageView) popupView.findViewById(R.id.cancel);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });

        mBaseQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                classifyTv.setText(tags.get(position).getLabelName());
                window.dismiss();
            }
        });
    }
}
