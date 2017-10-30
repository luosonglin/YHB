package com.medmeeting.m.zhiyi.UI.MineView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.LiveView.MyLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.MyPayLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.MeetingView.MyMeetingActivity;
import com.medmeeting.m.zhiyi.UI.SignInAndSignUpView.LoginActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.MyCollectActivity;
import com.medmeeting.m.zhiyi.UI.WalletView.MyWalletActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.snappydb.SnappydbException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.img)
    ImageView imageView;
    @Bind(R.id.setting)
    TextView setting;
    @Bind(R.id.identity)
    TextView identity;
    @Bind(R.id.head_ic)
    ImageView headIv;
    @Bind(R.id.name)
    TextView nameTv;
    @Bind(R.id.title)
    TextView titleTv;
    @Bind(R.id.hospital)
    TextView hospitalTv;
    @Bind(R.id.doctor_llyt)
    LinearLayout doctorLlyt;
    @Bind(R.id.user_flyt)
    RelativeLayout userLlyt;
    @Bind(R.id.specialist)
    ImageView specialistIv;

    private static final String TAG = MineFragment.class.getSimpleName();

    @Bind(R.id.wodecanhui)
    RelativeLayout wodecanhui;
    @Bind(R.id.wodeqianbao)
    RelativeLayout wodeqianbao;
    @Bind(R.id.wodeshoucang)
    RelativeLayout wodeshoucang;
    @Bind(R.id.wodebingli)
    RelativeLayout wodebingli;
    @Bind(R.id.wodexuefen)
    RelativeLayout wodexuefen;
    @Bind(R.id.wodejianli)
    RelativeLayout wodejianli;
    @Bind(R.id.wodezhibo)
    RelativeLayout wodezhibo;
    @Bind(R.id.wodewendang)
    RelativeLayout wodewendang;
    @Bind(R.id.wodefufeizhibo)
    RelativeLayout wodefufeizhibo;

    private String identityHtml;
    private String userId = null;
    private String userAvatar;

    @Bind(R.id.progress)
    View mProgressView;

    private OnFragmentInteractionListener mListener;


    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;

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

        if (userId == null && "".equals(userId)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;

    }

    private void initView() {

        showProgress(true);

        if (userId == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            HttpData.getInstance().HttpDataGetMyInfo(new Observer<MyInfoDto>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: " + e.getMessage()
                            + "\n" + e.getCause()
                            + "\n" + e.getLocalizedMessage()
                            + "\n" + e.getStackTrace());
                }

                @Override
                public void onNext(MyInfoDto item) {
                    userAvatar = item.getData().getUser().getUserPic();
                    Glide.with(getActivity())
                            .load(item.getData().getUser().getUserPic())
                            .crossFade()
//                            .placeholder(R.mipmap.avator_default)
                            .into(headIv);

                    try {
                        DBUtils.put(getActivity(), "authentication", item.getData().getUser().getAuthenStatus() + "");
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                    //A:已认证'',''B:待审核'',''C:大咖认证'',''''X:未认证'
                    switch (item.getData().getUser().getAuthenStatus()) {
                        case "A":
                            identity.setVisibility(View.GONE);
                            specialistIv.setVisibility(View.VISIBLE);
                            specialistIv.setImageResource(R.mipmap.specialis_yellow);
                            nameTv.setText(item.getData().getUser().getName());
                            hospitalTv.setText(item.getData().getUser().getDepartment());
                            titleTv.setText(item.getData().getUser().getTitle() + " ");

                            wodezhibo.setVisibility(View.GONE);
                            break;
                        case "B":
                            identityHtml = "&nbsp;"
                                    + "<font size=\"10\" color=\"#000000\"> 您已实名认证，请静候相关人员核实 </font>"
                                    + "<font size=\"38\" color=\"#32A2F8\">" + "</font>";
                            identity.setText(Html.fromHtml(identityHtml));
                            identity.setClickable(false);
                            specialistIv.setVisibility(View.GONE);
                            nameTv.setText(item.getData().getUser().getName());
                            hospitalTv.setText(item.getData().getUser().getDepartment());
                            titleTv.setText(item.getData().getUser().getTitle() + " ");

                            wodezhibo.setVisibility(View.GONE);
                            break;
                        case "C":
                            identity.setVisibility(View.GONE);
                            specialistIv.setVisibility(View.VISIBLE);
                            specialistIv.setImageResource(R.mipmap.specialist_red);
                            nameTv.setText(item.getData().getUser().getName());
                            hospitalTv.setText(item.getData().getUser().getDepartment());
                            titleTv.setText(item.getData().getUser().getTitle() + " ");

                            wodezhibo.setVisibility(View.VISIBLE);
                            break;
                        default:
                            identityHtml = "&nbsp;"
                                    + "<font size=\"10\" color=\"#000000\"> 你还没有实名认证，点击前往认证 </font>"
                                    + "<font size=\"38\" color=\"#32A2F8\">" + " >>" + "</font>";
                            identity.setVisibility(View.VISIBLE);
                            identity.setText(Html.fromHtml(identityHtml));
                            identity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(), IdentityActivity.class));
                                }
                            });
                            specialistIv.setVisibility(View.GONE);
                            nameTv.setText(item.getData().getUser().getNickName());
                            hospitalTv.setText(" ");
                            titleTv.setText(" ");


                            wodezhibo.setVisibility(View.GONE);
                            break;
                    }
                    showProgress(false);
                }
            }, Integer.parseInt(userId));
        }

        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        // 设置图片初始大小 这里我设为满屏的16:9
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        imageView.setLayoutParams(lp);

        // 监听滚动事件
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView
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
                        lp.width = metric.widthPixels + distance;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        imageView.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });
    }

    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView
                .getLayoutParams();
        final float w = imageView.getLayoutParams().width;// 图片当前宽度
        final float h = imageView.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F)
                .setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                imageView.setLayoutParams(lp);
            }
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
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.setting, R.id.user_flyt, R.id.wodecanhui, R.id.wodeqianbao, R.id.wodeshoucang, R.id.wodebingli, R.id.wodexuefen, R.id.wodejianli, R.id.wodezhibo, R.id.wodewendang, R.id.wodefufeizhibo})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("avatar", userAvatar);
                startActivity(intent);
                break;
            case R.id.user_flyt:
                break;
            case R.id.wodecanhui:
                intent = new Intent(getActivity(), MyMeetingActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.wodeqianbao:
                intent = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.wodeshoucang:
                intent = new Intent(getActivity(), MyCollectActivity.class);
                startActivity(intent);
                break;
            case R.id.wodebingli:
                break;
            case R.id.wodexuefen:
                break;
            case R.id.wodejianli:
                break;
            case R.id.wodezhibo:
                startActivity(new Intent(getActivity(), MyLiveRoomActivity.class));
                break;
            case R.id.wodewendang:
                break;
            case R.id.wodefufeizhibo:
                startActivity(new Intent(getActivity(), MyPayLiveRoomActivity.class));
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
