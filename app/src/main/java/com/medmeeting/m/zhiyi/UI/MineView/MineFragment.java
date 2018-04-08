package com.medmeeting.m.zhiyi.UI.MineView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserAuthenRecord;
import com.medmeeting.m.zhiyi.UI.Entity.UserGetInfoEntity;
import com.medmeeting.m.zhiyi.UI.IdentityView.ActivateActivity;
import com.medmeeting.m.zhiyi.UI.IdentityView.AuthorizeActivity;
import com.medmeeting.m.zhiyi.UI.IdentityView.AuthorizedActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.MyPayLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.Login_v2Activity;
import com.medmeeting.m.zhiyi.UI.UserInfoView.UpdateUserInfoActivity;
import com.medmeeting.m.zhiyi.UI.WalletView.MyWalletActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.head_ic)
    ImageView headIv;
    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.doctor_llyt)
    LinearLayout doctorLlyt;
    @BindView(R.id.user_flyt)
    RelativeLayout userLlyt;
    @BindView(R.id.specialist)
    ImageView specialistIv;

    private static final String TAG = MineFragment.class.getSimpleName();

    @BindView(R.id.wodecanhui)
    RelativeLayout wodecanhui;
    @BindView(R.id.wodeqianbao)
    RelativeLayout wodeqianbao;
    @BindView(R.id.wodeshoucang)
    RelativeLayout wodeshoucang;
    @BindView(R.id.wodexuefen)
    RelativeLayout wodexuefen;
    @BindView(R.id.wodejianli)
    RelativeLayout wodejianli;
    @BindView(R.id.my_live)
    LinearLayout my_live;
    @BindView(R.id.wodewendang)
    RelativeLayout wodewendang;
    @BindView(R.id.wodefufeizhibo)
    RelativeLayout wodefufeizhibo;
    @BindView(R.id.my_video)
    LinearLayout my_video;
    @BindView(R.id.activate)
    TextView activate;
    @BindView(R.id.authorize)
    TextView authorize;

    private String identityHtml;
    private String userId = null;
    private String userAvatar;

    @BindView(R.id.progress)
    View mProgressView;

    private OnFragmentInteractionListener mListener;

    private String code = ""; //身份类型

    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;

    Unbinder unbinder;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            userId = DBUtils.get(getActivity(), "userId");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

//        if (userId == null && "".equals(userId)) {
//            startActivity(new Intent(getActivity(), Login_v2Activity.class));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        showProgress(true);

        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        // 设置图片初始大小 这里我设为满屏的16:9
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        imageView.setLayoutParams(lp);

        // 监听滚动事件
        scrollView.setOnTouchListener((v, event) -> {
            ViewGroup.LayoutParams lp1 = imageView
                    .getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    // 手指离开后恢复图片
                    mScaling = false;
                    replyImage();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mScaling) {
                        if (scrollView.getScrollY() == 0) {
                            mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                        } else {
                            break;
                        }
                    }
                    int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                    if (distance < 0) { // 当前位置比记录位置要小，正常返回
                        break;
                    }

                    // 处理放大
                    mScaling = true;
                    lp1.width = metric.widthPixels + distance;
                    lp1.height = (metric.widthPixels + distance) * 9 / 16;
                    imageView.setLayoutParams(lp1);
                    return true; // 返回true表示已经完成触摸事件，不再处理
            }
            return false;
        });


        if (userId == null) {
//            startActivity(new Intent(getActivity(), Login_v2Activity.class));
            showProgress(false);
            return;
        }

        try {
            if (DBUtils.isSet(getActivity(), "userToken")) {
                HttpData.getInstance().HttpDataGetUserInfo2(new Observer<HttpResult3<Object, UserGetInfoEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(getActivity(), e.getMessage());
                        Log.e(getActivity().getLocalClassName(), e.getMessage());
                        showProgress(false);
                    }

                    @Override
                    public void onNext(HttpResult3<Object, UserGetInfoEntity> data) {
                        if (!data.getStatus().equals("success")) {
                            ToastUtils.show(getActivity(), data.getMsg());
                            showProgress(false);
//                    if (data.getMsg().equals("token校验错误")) {
                            if (data.getErrorCode().equals("11010")) {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), Login_v2Activity.class));
                            }
                            return;
                        }
                        userAvatar = data.getEntity().getUserPic();
                        Glide.with(getActivity())
                                .load(userAvatar)
                                .crossFade()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.mipmap.avator_default)
                                .into(headIv);
                        if (data.getEntity().getName() != null) {
                            nameTv.setText(data.getEntity().getName());
                        } else {
                            nameTv.setText(data.getEntity().getNickName());
                        }

                        //正式服，该字段暂无
                        switch (data.getEntity().getTocPortStatus()) {
                            case "wait_activation":
                                activate.setText("待激活");
                                authorize.setText("去激活");
                                specialistIv.setVisibility(View.GONE);
                                break;
                            case "done_activation":
                                activate.setText("未认证");//已激活
                                specialistIv.setVisibility(View.VISIBLE);
                                specialistIv.setImageResource(R.mipmap.red_v);
                                code = data.getEntity().getMedical();
                                authorize.setText("去认证");
                                break;
                            case "done_authen":
                                activate.setText("已认证");
                                specialistIv.setVisibility(View.VISIBLE);
                                specialistIv.setImageResource(R.mipmap.yellow_v);
                                authorize.setVisibility(View.GONE);
                                break;
                        }
                        try {
                            DBUtils.put(getActivity(), "tocPortStatus", data.getEntity().getTocPortStatus() + "");
                        } catch (SnappydbException e) {
                            e.printStackTrace();
                        }

                        showProgress(false);
                    }
                });

            } else {
                //退出登录后
                Glide.with(getActivity())
                        .load(R.mipmap.avator_default)
                        .crossFade()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.avator_default)
                        .into(headIv);
                nameTv.setText("");

                activate.setVisibility(View.GONE);
                authorize.setVisibility(View.GONE);
                specialistIv.setVisibility(View.GONE);

                showProgress(false);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }


    }

    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        final ViewGroup.LayoutParams lp = imageView
                .getLayoutParams();
        final float w = imageView.getLayoutParams().width;// 图片当前宽度
        final float h = imageView.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F)
                .setDuration(200);

        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            lp.width = (int) (w - (w - newW) * cVal);
            lp.height = (int) (h - (h - newH) * cVal);
            imageView.setLayoutParams(lp);
        });
        anim.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.setting, R.id.modify_userinfo, R.id.authorize,
            R.id.user_flyt, R.id.wodecanhui, R.id.wodeqianbao, R.id.my_video,
            R.id.wodeshoucang, R.id.wodedingdan, R.id.wodexuefen, R.id.wodejianli,
            R.id.my_live, R.id.wodewendang, R.id.wodefufeizhibo,
            R.id.name, R.id.activate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("avatar", userAvatar);
                startActivity(intent);
                break;
            case R.id.modify_userinfo:  //激活后就能修改
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                switch (activate.getText().toString().trim()) {
                    case "待激活":     //跳激活页
                        startActivity(new Intent(getActivity(), ActivateActivity.class));
                        break;
                    case "未认证":     //跳认证页
                        startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                       /* HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                                if (!data.getStatus().equals("success")) {
                                    ToastUtils.show(getActivity(), data.getMsg());
                                    return;
                                }
                                if (data.getEntity() == null) { //从来没有认证过
                                    ToastUtils.show(getActivity(), "请重新认证账户");
                                    Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                    if (code.equals("OTHER")) code = "ASSOCIATION"; //回到默认值，传参
                                    intent.putExtra("Category", code);
                                    switch (code) {
                                        case "ASSOCIATION":
                                            intent.putExtra("CategoryName", "医疗协会");
                                            break;
                                        case "MEDICAL_STAFF":
                                            intent.putExtra("CategoryName", "医护人员");
                                            break;
                                        case "MEDICAL_COMPANY":
                                            intent.putExtra("CategoryName", "药械企业");
                                            break;
                                        case "MEDICO":
                                            intent.putExtra("CategoryName", "医学生");
                                            break;
                                        case "EDUCATION_SCIENCE":
                                            intent.putExtra("CategoryName", "医药教科研人员");
                                            break;
                                    }
                                    startActivity(intent);
                                    return;
                                }
                                switch (data.getEntity().getStatus()) {
                                    case "A":   //已认证
                                        startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                                        break;
                                    case "B":   //待认证 (运营端未审核,弹出“资料提交成功”界面
                                        startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                                        break;
                                    case "X":   //未通过 (运营端拒绝,弹出“重新认证界面”界面
                                        ToastUtils.show(getActivity(), "请重新认证账户");
                                        Intent intent1 = new Intent(getActivity(), AuthorizeActivity.class);
                                        intent1.putExtra("Category", data.getEntity().getCategory());
                                        intent1.putExtra("CategoryName", data.getEntity().getCategoryName());
                                        startActivity(intent1);
                                        break;
                                }

                            }
                        });*/
                        break;
                    case "已认证":     //跳认证状态页
                        startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
                        break;
                }
                break;
            case R.id.authorize:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                switch (activate.getText().toString().trim()) {
                    case "待激活":     //跳激活页
                        Intent intent20 = new Intent(getActivity(), ActivateActivity.class);
                        startActivity(intent20);
                        break;
                    case "未认证":     //跳认证页
                        HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                                if (!data.getStatus().equals("success")) {
                                    ToastUtils.show(getActivity(), data.getMsg());
                                    return;
                                }

                                if (data.getEntity() == null) {
                                    //从未认证过
                                    Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                    if (code.equals("OTHER")) code = "ASSOCIATION"; //回到默认值，传参
                                    intent.putExtra("Category", code);
                                    switch (code) {
                                        case "ASSOCIATION":
                                            intent.putExtra("CategoryName", "医疗协会");
                                            break;
                                        case "MEDICAL_STAFF":
                                            intent.putExtra("CategoryName", "医护人员");
                                            break;
                                        case "MEDICAL_COMPANY":
                                            intent.putExtra("CategoryName", "药械企业");
                                            break;
                                        case "MEDICO":
                                            intent.putExtra("CategoryName", "医学生");
                                            break;
                                        case "EDUCATION_SCIENCE":
                                            intent.putExtra("CategoryName", "医药教科研人员");
                                            break;
                                    }
                                    startActivity(intent);
                                } else {
                                    //认证过
                                    switch (data.getEntity().getStatus()) {     //认证状态（A:已认证，B:待认证,X:未通过）
                                        case "A":
                                            break;
                                        case "B": //待认证 (运营端未审核,弹出“资料提交成功”界面
                                            startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                                            break;
                                        case "X":
                                            startActivity(new Intent(getActivity(), AuthorizedActivity.class));
//                                            Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
//                                            intent.putExtra("Category", data.getEntity().getCategory());
//                                            intent.putExtra("CategoryName", data.getEntity().getCategoryName());
//                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }
                        });
                        break;
                    case "已认证":     //跳认证状态页
                        Intent intent23 = new Intent(getActivity(), AuthorizedActivity.class);
                        startActivity(intent23);
                        break;
                }
                break;
            case R.id.wodecanhui:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                Intent intent30 = new Intent(getActivity(), MyMeetingActivity.class);
                startActivity(intent30);
                break;
            case R.id.wodeqianbao:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                switch (activate.getText().toString().trim()) {
                    case "待激活":     //跳激活页
                        ToastUtils.show(getActivity(), "请先激活账户");
                        Intent intent40 = new Intent(getActivity(), ActivateActivity.class);
                        startActivity(intent40);
                        break;
                    case "未认证":
                        HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                                if (!data.getStatus().equals("success")) {
                                    ToastUtils.show(getActivity(), data.getMsg());
                                    return;
                                }

                                if (data.getEntity() == null) { //从来没有认证过
                                    ToastUtils.show(getActivity(), "请重新认证账户");
                                    Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                    if (code.equals("OTHER")) code = "ASSOCIATION"; //回到默认值，传参
                                    intent.putExtra("Category", code);
                                    switch (code) {
                                        case "ASSOCIATION":
                                            intent.putExtra("CategoryName", "医疗协会");
                                            break;
                                        case "MEDICAL_STAFF":
                                            intent.putExtra("CategoryName", "医护人员");
                                            break;
                                        case "MEDICAL_COMPANY":
                                            intent.putExtra("CategoryName", "药械企业");
                                            break;
                                        case "MEDICO":
                                            intent.putExtra("CategoryName", "医学生");
                                            break;
                                        case "EDUCATION_SCIENCE":
                                            intent.putExtra("CategoryName", "医药教科研人员");
                                            break;
                                    }
                                    startActivity(intent);
                                    return;
                                }

                                switch (data.getEntity().getStatus()) {
                                    case "A":   //已认证
                                        startActivity(new Intent(getActivity(), MyWalletActivity.class));
                                        break;
                                    case "B":   //待认证 (运营端未审核,弹出“资料提交成功”界面
                                        startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                                        break;
                                    case "X":   //未通过 (运营端拒绝,弹出“重新认证界面”界面
                                        ToastUtils.show(getActivity(), "请重新认证账户");
                                        Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                        intent.putExtra("Category", data.getEntity().getCategory());
                                        intent.putExtra("CategoryName", data.getEntity().getCategoryName());
                                        startActivity(intent);
                                        break;
                                }

                            }
                        });
                        break;
                    case "已认证":     //跳认证状态页
                        startActivity(new Intent(getActivity(), MyWalletActivity.class));
                        break;
                }
                break;
            case R.id.my_live:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                switch (activate.getText().toString().trim()) {
                    case "待激活":     //跳激活页
                        ToastUtils.show(getActivity(), "请先激活账户");
                        Intent intent50 = new Intent(getActivity(), ActivateActivity.class);
                        startActivity(intent50);
                        break;
                    case "未认证":
                        HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                                if (!data.getStatus().equals("success")) {
                                    ToastUtils.show(getActivity(), data.getMsg());
                                    return;
                                }

                                if (data.getEntity() == null) { //从来没有认证过
                                    ToastUtils.show(getActivity(), "请重新认证账户");
                                    Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                    if (code.equals("OTHER")) code = "ASSOCIATION"; //回到默认值，传参
                                    intent.putExtra("Category", code);
                                    switch (code) {
                                        case "ASSOCIATION":
                                            intent.putExtra("CategoryName", "医疗协会");
                                            break;
                                        case "MEDICAL_STAFF":
                                            intent.putExtra("CategoryName", "医护人员");
                                            break;
                                        case "MEDICAL_COMPANY":
                                            intent.putExtra("CategoryName", "药械企业");
                                            break;
                                        case "MEDICO":
                                            intent.putExtra("CategoryName", "医学生");
                                            break;
                                        case "EDUCATION_SCIENCE":
                                            intent.putExtra("CategoryName", "医药教科研人员");
                                            break;
                                    }
                                    startActivity(intent);
                                    return;
                                }

                                switch (data.getEntity().getStatus()) {
                                    case "A":   //已认证
                                        startActivity(new Intent(getActivity(), MyLiveRoomActivity.class));
                                        break;
                                    case "B":   //待认证(运营端未审核,弹出“资料提交成功”界面
                                        startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                                        break;
                                    case "X":   //未通过 (运营端拒绝,弹出“重新认证界面”界面
                                        ToastUtils.show(getActivity(), "请重新认证账户");
                                        Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                        intent.putExtra("Category", data.getEntity().getCategory());
                                        intent.putExtra("CategoryName", data.getEntity().getCategoryName());
                                        startActivity(intent);
                                        break;
                                }

                            }
                        });
                        break;
                    case "已认证":     //跳认证状态页
                        startActivity(new Intent(getActivity(), MyLiveRoomActivity.class));
                        break;
                }
                break;
            case R.id.my_video:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                switch (activate.getText().toString().trim()) {
                    case "待激活":     //跳激活页
                        ToastUtils.show(getActivity(), "请先激活账户");
                        startActivity(new Intent(getActivity(), ActivateActivity.class));
                        break;
                    case "未认证":
                        HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                                if (!data.getStatus().equals("success")) {
                                    ToastUtils.show(getActivity(), data.getMsg());
                                    return;
                                }

                                if (data.getEntity() == null) { //从来没有认证过
                                    ToastUtils.show(getActivity(), "请重新认证账户");
                                    Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                    if (code.equals("OTHER")) code = "ASSOCIATION"; //回到默认值，传参
                                    intent.putExtra("Category", code);
                                    switch (code) {
                                        case "ASSOCIATION":
                                            intent.putExtra("CategoryName", "医疗协会");
                                            break;
                                        case "MEDICAL_STAFF":
                                            intent.putExtra("CategoryName", "医护人员");
                                            break;
                                        case "MEDICAL_COMPANY":
                                            intent.putExtra("CategoryName", "药械企业");
                                            break;
                                        case "MEDICO":
                                            intent.putExtra("CategoryName", "医学生");
                                            break;
                                        case "EDUCATION_SCIENCE":
                                            intent.putExtra("CategoryName", "医药教科研人员");
                                            break;
                                    }
                                    startActivity(intent);
                                    return;
                                }
                                switch (data.getEntity().getStatus()) {
                                    case "A":   //已认证
                                        startActivity(new Intent(getActivity(), MyVideoActivity.class));
                                        break;
                                    case "B":   //待认证 (运营端未审核,弹出“资料提交成功”界面
                                        startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                                        break;
                                    case "X":   //未通过 (运营端拒绝,弹出“重新认证界面”界面
                                        ToastUtils.show(getActivity(), "请重新认证账户");
                                        Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                                        intent.putExtra("Category", data.getEntity().getCategory());
                                        intent.putExtra("CategoryName", data.getEntity().getCategoryName());
                                        startActivity(intent);
                                        break;
                                }

                            }
                        });
                        break;
                    case "已认证":     //跳认证状态页
                        startActivity(new Intent(getActivity(), MyVideoActivity.class));
                        break;
                }
                break;

            case R.id.wodeshoucang:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.wodedingdan:  //非激活情况下也能使用
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.wodexuefen:
                break;
            case R.id.wodejianli:
                break;
            case R.id.wodewendang:
                break;
            case R.id.wodefufeizhibo:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                startActivity(new Intent(getActivity(), MyPayLiveRoomActivity.class));
                break;
            case R.id.user_flyt:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                if (activate.getText().toString().trim().equals("已认证")) {
                    startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                    /*HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                            if (!data.getStatus().equals("success")) {
                                ToastUtils.show(getActivity(), data.getMsg());
                                return;
                            }

                            Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                            intent.putExtra("Category", data.getEntity().getCategory());
                            intent.putExtra("CategoryName", data.getEntity().getCategoryName());
                            startActivity(intent);
                        }
                    });*/
                }
                break;
            case R.id.name:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                if (activate.getText().toString().trim().equals("已认证")) {
                    startActivity(new Intent(getActivity(), AuthorizedActivity.class));
                }
                break;
            case R.id.activate:
                if (userId == null) {
                    startActivity(new Intent(getActivity(), Login_v2Activity.class));
                    showProgress(false);
                    return;
                }
                if (activate.getText().toString().trim().equals("已认证")) {
                    startActivity(new Intent(getActivity(), AuthorizedActivity.class));

                    /*HttpData.getInstance().HttpDataGetLastAuthentizeStatus(new Observer<HttpResult3<Object, UserAuthenRecord>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HttpResult3<Object, UserAuthenRecord> data) {
                            if (!data.getStatus().equals("success")) {
                                ToastUtils.show(getActivity(), data.getMsg());
                                return;
                            }

                            Intent intent = new Intent(getActivity(), AuthorizeActivity.class);
                            intent.putExtra("Category", data.getEntity().getCategory());
                            intent.putExtra("CategoryName", data.getEntity().getCategoryName());
                            startActivity(intent);
                        }
                    });*/
                }
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }


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
                    if (mProgressView != null)
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
