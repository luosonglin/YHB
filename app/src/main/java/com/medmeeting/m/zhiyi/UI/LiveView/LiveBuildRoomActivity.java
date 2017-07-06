package com.medmeeting.m.zhiyi.UI.LiveView;

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

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.TagAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
                break;
            case R.id.classify:
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
                        Log.e(TAG, "onError: "+e.getMessage()
                                +"\n"+e.getCause()
                                +"\n"+e.getLocalizedMessage()
                                +"\n"+e.getStackTrace());
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
                        ToastUtils.show(LiveBuildRoomActivity.this, tags.get(position).getLabelName());
                        window.dismiss();
                    }
                });
                break;
            case R.id.buildllyt:
                break;
        }
    }
}
