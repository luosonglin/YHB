package com.medmeeting.m.zhiyi.UI.LiveView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.TagAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult6;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.MineView.MyLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;

public class LiveBuildRoomActivity extends AppCompatActivity {

    @BindView(R.id.live_pic)
    ImageView livePic;
    @BindView(R.id.live_pic_tip_tv)
    TextView livePicTipTv;
    @BindView(R.id.live_pic_tip)
    LinearLayout livePicTip;
    @BindView(R.id.theme)
    EditText theme;
    @BindView(R.id.classify_tv)
    TextView classifyTv;
    @BindView(R.id.classify)
    LinearLayout classify;
    @BindView(R.id.introduction)
    EditText introduction;
    @BindView(R.id.buildllyt)
    LinearLayout buildllyt;
    private Toolbar toolbar;
    private static final String TAG = LiveBuildRoomActivity.class.getSimpleName();
    private TagAdapter mBaseQuickAdapter;
    //新增直播间
    private int userId;  //用户ID
    private String videoTitle = "";  //直播间标题
    private String videoLabel = "";  //直播间标题
    private String videoLabelIds = "";

    private String videoDesc = "";  //直播间描述
    private String videoPhoto = "";  //直播间封面图片

    @BindView(R.id.progress)
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_build_room);
        ButterKnife.bind(this);
        toolBar();
        initAgreementView();

//        //第一次创建直播间，弹出直播间协议弹窗
//        if (getIntent().getStringExtra("times").equals("0") || getIntent().getStringExtra("times").equals("")) {
//            View codeView = LayoutInflater.from(this).inflate(R.layout.popupwindow_live_agreement, null);
//            PopupWindow codePopupwindow = new PopupWindow(codeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
//
//            TextView confirmTv = (TextView) codeView.findViewById(R.id.confirm);
//            confirmTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    codePopupwindow.dismiss();
//                }
//            });
//            TextView cancelTv = (TextView) codeView.findViewById(R.id.cancel);
//            cancelTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    codePopupwindow.dismiss();
//                    finish();
//                }
//            });
//            TextView agreementTv = (TextView) codeView.findViewById(R.id.blue);
//            cancelTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });
//
//            codePopupwindow.setOutsideTouchable(true);
//            ColorDrawable dw = new ColorDrawable(0x000ff000);
//            codePopupwindow.setBackgroundDrawable(dw);
//            codePopupwindow.showAtLocation(codeView, Gravity.BOTTOM, 0, 0);
//        }
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 第一次创建直播间，弹出直播协议弹窗
     */
    private void initAgreementView() {
        View codeView = LayoutInflater.from(this).inflate(R.layout.popupwindow_live_agreement, null);
        PopupWindow codePopupwindow = new PopupWindow(codeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        TextView confirmTv = (TextView) codeView.findViewById(R.id.confirm);
        confirmTv.setOnClickListener(view ->
                HttpData.getInstance().HttpDataAgree(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LiveBuildRoomActivity.this, e.getMessage());
                        codePopupwindow.dismiss();
                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(LiveBuildRoomActivity.this, data.getMsg());
                            return;
                        }
                        codePopupwindow.dismiss();
                    }
                }));
        TextView cancelTv = (TextView) codeView.findViewById(R.id.cancel);
        cancelTv.setOnClickListener(view -> {
            codePopupwindow.dismiss();
            finish();
        });
        TextView agreementTv = (TextView) codeView.findViewById(R.id.blue);
        agreementTv.setOnClickListener(view -> BrowserActivity.launch(LiveBuildRoomActivity.this, "http://webview.medmeeting.com/#/page/live-protocol", "《直播协议》"));


        HttpData.getInstance().HttpDataGetAgreement(new Observer<HttpResult3<Object, Boolean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(LiveBuildRoomActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, Boolean> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(LiveBuildRoomActivity.this, data.getMsg());
                    return;
                }
                if (data.getEntity())
                    return;

                codePopupwindow.setOutsideTouchable(true);
                ColorDrawable dw = new ColorDrawable(0x000ff000);
                codePopupwindow.setBackgroundDrawable(dw);
                codePopupwindow.showAtLocation(codeView, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    @OnClick({R.id.agreement_llyt, R.id.live_pic_tip, R.id.classify, R.id.buildllyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agreement_llyt:
                BrowserActivity.launch(LiveBuildRoomActivity.this, "http://webview.medmeeting.com/#/page/live-protocol", "《直播协议》");
                break;
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
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(theme.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(introduction.getWindowToken(), 0);
                break;
            case R.id.buildllyt:
                videoTitle = theme.getText().toString().trim();
                videoLabel = classifyTv.getText().toString().trim();
                videoDesc = introduction.getText().toString().trim();
                if (videoTitle.equals("")) {
                    ToastUtils.show(LiveBuildRoomActivity.this, "请填写标题");
                    return;
                }
                if (videoLabel.equals("")) {
                    ToastUtils.show(LiveBuildRoomActivity.this, "请选择分类标签");
                    return;
                }
                if (videoDesc.equals("")) {
                    ToastUtils.show(LiveBuildRoomActivity.this, "请填写直播介绍");
                    return;
                }
                LiveRoomDto liveRoomDto = new LiveRoomDto(videoTitle, videoPhoto, videoLabelIds, videoDesc);

                buildllyt.setClickable(false);
                HttpData.getInstance().HttpDataAddLiveRoom(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LiveBuildRoomActivity.this, e.getMessage());
                        Log.e(TAG, "onError" + e.getMessage() + " " + e.getStackTrace());
                    }

                    @Override
                    public void onNext(HttpResult3 httpResult3) {
                        if (!"success".equals(httpResult3.getStatus())) {
                            ToastUtils.show(LiveBuildRoomActivity.this, httpResult3.getMsg());
                            buildllyt.setClickable(true);
                            Log.e(TAG, "onNext");
                            return;
                        }
                        startActivity(new Intent(LiveBuildRoomActivity.this, MyLiveRoomActivity.class));
                        finish();
                        buildllyt.setClickable(false);
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
                        ToastUtils.show(LiveBuildRoomActivity.this, data.getMsg());
                        return;
                    }

                    videoPhoto = data.getExtra().getAbsQiniuImgHash();

                    Glide.with(LiveBuildRoomActivity.this)
                            .load(videoPhoto)
                            .crossFade()
                            .dontAnimate()
                            .into(new GlideDrawableImageViewTarget(livePic) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                    //在这里添加一些图片加载完成的操作
                                    super.onResourceReady(resource, animation);
                                    livePicTipTv.setText("修改直播间封面");
                                }
                            });
                }
            }, body, description);

        }
    }


    final List<TagDto> tags = new ArrayList<>();
    final List<TagDto> tags_confirm = new ArrayList<>();

    /**
     * 标签弹窗
     */
    private void initTagsPopupWindow() {
        final View popupView = LiveBuildRoomActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_live_classify, null);

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
                ToastUtils.show(LiveBuildRoomActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<TagDto, Object> tagDtoObjectHttpResult3) {
                mBaseQuickAdapter.addData(tagDtoObjectHttpResult3.getData());
                tags.addAll(tagDtoObjectHttpResult3.getData());
                Log.e(TAG, "onNext");
            }
        });

        // 创建PopupWindow对象，指定宽度和高度
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
        cancelIv.setOnClickListener(view -> window.dismiss());

        tags_confirm.clear();
        videoLabelIds = "";
        mBaseQuickAdapter.setOnRecyclerViewItemChildClickListener((adapter, view, position) -> {
            TagDto tagDto = (TagDto) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.name:
                    if (!tags_confirm.contains(tagDto)) {
                        tags_confirm.add(tagDto);
                        view.setBackgroundResource(R.drawable.textview_all_blue);
                    } else {
                        tags_confirm.remove(tagDto);
                        view.setBackgroundResource(R.drawable.textview_radius_grey);
                    }
                    break;
            }
        });

        TextView confirmTv = (TextView) popupView.findViewById(R.id.confirm_tag);
        confirmTv.setOnClickListener(view -> {
            if (tags_confirm.size() <= 3) {
                String classify1 = "";
                for (TagDto i : tags_confirm) {
                    videoLabelIds += i.getId() + ",";
                    classify1 += " " + i.getLabelName();
                    Log.e(TAG, i.getLabelName());
                }
                classifyTv.setText(classify1);
                videoLabelIds = videoLabelIds.substring(0, videoLabelIds.length() - 1);
                window.dismiss();
            } else if (tags_confirm.size() == 0) {
                ToastUtils.show(LiveBuildRoomActivity.this, "请选择直播分类标签");
            } else {
                ToastUtils.show(LiveBuildRoomActivity.this, "只能选3个标签，请重新筛选");
            }
        });
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //此处避免you cannot start a load for a destroyed activity，因为glide不在主线程
        Glide.with(getApplicationContext()).pauseRequests();
    }
}
