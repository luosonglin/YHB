package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LiveProgramDetailInfoFragment  extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Bind(R.id.live_user_pic)
    CircleImageView liveUserPic;
    @Bind(R.id.name)
    TextView nameTv;
    @Bind(R.id.hospital)
    TextView hospitalTv;
    @Bind(R.id.detail)
    TextView detailTv;
    @Bind(R.id.start_time_llyt)
    LinearLayout startTimeLlyt;


    private String TAG = LiveProgramDetailInfoFragment.class.getSimpleName();

    private static String name, hospital, userPic, detail;

    public LiveProgramDetailInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LiveProgramDetailInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveProgramDetailInfoFragment newInstance(String name1, String hospital1, String userPic1, String detail1) {
        LiveProgramDetailInfoFragment fragment = new LiveProgramDetailInfoFragment();
        Bundle args = new Bundle();
        args.putString("name", name1);
        args.putString("hospital", hospital1);
        args.putString("userPic", userPic1);
        args.putString("detail", detail1);

        name = name1;
        hospital = hospital1;
        userPic = userPic1;
        detail = detail1;

        Log.e("LiveReadyForDetail", "newInstance--->>>" + name+" "+hospital+" "+" "+userPic+" "+detail);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            hospital = getArguments().getString("hospital");
            userPic = getArguments().getString("userPic");
            detail = getArguments().getString("detail");
        }

        Log.e(TAG, "onCreate--->>>" + name+" "+hospital+" "+userPic+" "+detail);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_program_detail_info, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView() {
        nameTv.setText(name);
        hospitalTv.setText(hospital);

        Picasso.with(getActivity()).load(userPic + "?imageMogr/v2/thumbnail/140x70")
                .error(R.mipmap.avator_default)
                .into(liveUserPic);
        detailTv.setText(detail);
        Log.e(TAG+"111", userPic + "?imageMogr/v2/thumbnail/360x140");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
