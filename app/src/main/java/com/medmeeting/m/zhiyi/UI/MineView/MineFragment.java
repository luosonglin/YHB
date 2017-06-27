package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.MyInfoDto;
import com.medmeeting.m.zhiyi.UI.LiveView.MyLiveRoomActivity;
import com.medmeeting.m.zhiyi.UI.MeetingView.MyMeetingActivity;
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

    @Bind(R.id.setting)
    TextView setting;
//    @Bind(R.id.identity)
//    TextView identity;
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
    @Bind(R.id.wodetiezi)
    RelativeLayout wodetiezi;
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

//    private String identityHtml;
    private String userId = null;

    private OnFragmentInteractionListener mListener;

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
        try {
            userId = DBUtils.get(getActivity(), "userId");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        HttpData.getInstance().HttpDataGetMyInfo(new Observer<MyInfoDto>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage()
                        +"\n"+e.getCause()
                        +"\n"+e.getLocalizedMessage()
                        +"\n"+e.getStackTrace());
            }

            @Override
            public void onNext(MyInfoDto item) {
                Glide.with(getActivity())
                        .load(item.getData().getUser().getUserPic())
                        .crossFade()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(headIv);
                nameTv.setText(item.getData().getUser().getName() == null ?
                        item.getData().getUser().getNickName() : item.getData().getUser().getName());
                hospitalTv.setText(item.getData().getUser().getHospital() + " " + item.getData().getUser().getDepartment());
                titleTv.setText(item.getData().getUser().getTitle()+" ");
            }
        }, Integer.parseInt(userId));
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

    @OnClick({R.id.setting, R.id.user_flyt, R.id.wodecanhui, R.id.wodeqianbao, R.id.wodetiezi, R.id.wodebingli, R.id.wodexuefen, R.id.wodejianli, R.id.wodezhibo, R.id.wodewendang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                break;
            case R.id.user_flyt:
                break;
            case R.id.wodecanhui:
                Intent intent = new Intent(getActivity(), MyMeetingActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.wodeqianbao:
                break;
            case R.id.wodetiezi:
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


}
