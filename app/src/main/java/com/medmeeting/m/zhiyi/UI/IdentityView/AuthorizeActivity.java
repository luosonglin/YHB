package com.medmeeting.m.zhiyi.UI.IdentityView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IdentityTypeAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserAddAuthenEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserIdentity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * 认证页（设置基本信息）
 */
public class AuthorizeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.identity_name)
    TextView identityName;

    List<UserIdentity> userIdentities = new ArrayList<>();
    @BindView(R.id.identity_1)
    LinearLayout identity1;
    @BindView(R.id.identity_2)
    LinearLayout identity2;
    @BindView(R.id.identity_3)
    LinearLayout identity3;


    @BindView(R.id.authorize1)
    LinearLayout authorize1;
    @BindView(R.id.authorize2)
    LinearLayout authorize2;
    @BindView(R.id.authorize3)
    LinearLayout authorize3;
    @BindView(R.id.authorize4)
    LinearLayout authorize4;
    @BindView(R.id.authorize5)
    LinearLayout authorize5;

    private int type = 0;

    private UserAddAuthenEntity userAddAuthenEntity = new UserAddAuthenEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        ButterKnife.bind(this);

        toolBar();

        identity1.setVisibility(View.VISIBLE);
        identity2.setVisibility(View.GONE);
        identity3.setVisibility(View.GONE);

        authorize1.setVisibility(View.VISIBLE);
        authorize2.setVisibility(View.GONE);
        authorize3.setVisibility(View.GONE);
        authorize4.setVisibility(View.GONE);
        authorize5.setVisibility(View.GONE);
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.rights, R.id.identity_name, R.id.next1, R.id.next21, R.id.next22, R.id.next31, R.id.next32})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rights:
                break;
            case R.id.identity_name:
                chooseIdentityType();
                break;
            case R.id.next1:
                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.VISIBLE);
                identity3.setVisibility(View.GONE);
                break;
            case R.id.next21:
                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.GONE);
                identity3.setVisibility(View.VISIBLE);
                break;
            case R.id.next22:
                identity1.setVisibility(View.VISIBLE);
                identity2.setVisibility(View.GONE);
                identity3.setVisibility(View.GONE);
                break;
            case R.id.next31:

//                userAddAuthenEntity.setCompany();
//                userAddAuthenEntity.setDepartment();
//                userAddAuthenEntity.setDiploma();
//                userAddAuthenEntity.setEmail();
//                userAddAuthenEntity.setEntranceDate();
//                userAddAuthenEntity.setIdentityPhoto();
//                userAddAuthenEntity.setPostion();
//                userAddAuthenEntity.setTitle();
//                userAddAuthenEntity.setUserName();
//                userAddAuthenEntity.setWorkPhoto();
//                userAddAuthenEntity.setWorkPhotoType();

                HttpData.getInstance().HttpDataAuthorize(new Observer<HttpResult3>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult3 data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(AuthorizeActivity.this, data.getMsg());
                            return;
                        }

                        ToastUtils.show(AuthorizeActivity.this, "提交");
                    }
                }, userAddAuthenEntity);

                break;
            case R.id.next32:
                identity1.setVisibility(View.GONE);
                identity2.setVisibility(View.VISIBLE);
                identity3.setVisibility(View.GONE);
                break;
        }
    }


    BaseQuickAdapter mBaseQuickAdapter = new IdentityTypeAdapter(R.layout.item_identity_type, null);

    private void chooseIdentityType() {
        final View popupView = this.getLayoutInflater().inflate(R.layout.popupwindow_identity_type, null);

        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.types);
        recyclerView.setLayoutManager(new LinearLayoutManager(popupView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(AuthorizeActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mBaseQuickAdapter);


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
        window.showAtLocation(identityName, Gravity.CENTER, 0, 0);


        ImageView cancelIv = (ImageView) popupView.findViewById(R.id.cancel);
        cancelIv.setOnClickListener(view -> window.dismiss());


        if (userIdentities.size() == 0) {
            HttpData.getInstance().HttpDataGetAuthorizationTypes(new Observer<HttpResult3<UserIdentity, Object>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3<UserIdentity, Object> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(AuthorizeActivity.this, data.getMsg());
                        return;
                    }
                    userIdentities.clear();
                    userIdentities.addAll(data.getData());
                    mBaseQuickAdapter.setNewData(data.getData());
                    mBaseQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                        identityName.setText(data.getData().get(position).getTitle());

                        userAddAuthenEntity.setCategory(data.getData().get(position).getCode());
                        userAddAuthenEntity.setCategoryName(data.getData().get(position).getTitle());

                        type = position + 1;
                        if (type == 1) {
                            authorize1.setVisibility(View.VISIBLE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (type == 2) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.VISIBLE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (type == 3) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.VISIBLE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.GONE);
                        } else if (type == 4) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.VISIBLE);
                            authorize5.setVisibility(View.GONE);
                        } else if (type == 5) {
                            authorize1.setVisibility(View.GONE);
                            authorize2.setVisibility(View.GONE);
                            authorize3.setVisibility(View.GONE);
                            authorize4.setVisibility(View.GONE);
                            authorize5.setVisibility(View.VISIBLE);
                        }

                        window.dismiss();
                    });
                }
            });
        } else {
            mBaseQuickAdapter.setNewData(userIdentities);
            mBaseQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                identityName.setText(userIdentities.get(position).getTitle());
                window.dismiss();
            });
        }

    }
}
