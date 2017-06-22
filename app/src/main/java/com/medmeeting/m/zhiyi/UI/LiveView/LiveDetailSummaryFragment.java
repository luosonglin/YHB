package com.medmeeting.m.zhiyi.UI.LiveView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

public class LiveDetailSummaryFragment extends Fragment {

    private TextView mSummaryTv;
    private static Integer classifys;

    public static LiveDetailSummaryFragment newInstance(Integer classifys1) {
        LiveDetailSummaryFragment fragment = new LiveDetailSummaryFragment();
        Bundle args = new Bundle();
        args.putInt("classifys", classifys1);
        fragment.setArguments(args);

        classifys = classifys1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            classifys = getArguments().getInt("classifys");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_detail_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSummaryTv = (TextView) view.findViewById(R.id.summary);
        mSummaryTv.setText(classifys+"");
    }

}

