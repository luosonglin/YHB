package com.medmeeting.m.zhiyi.UI.VideoView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 5:34 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailOtherFragment  extends Fragment {

    private TextView mSummaryTv;
    private static Integer videoId;

    public static VideoDetailInfomationFragment newInstance(Integer classifys1) {
        VideoDetailInfomationFragment fragment = new VideoDetailInfomationFragment();
        Bundle args = new Bundle();
        args.putInt("videoId", classifys1);
        fragment.setArguments(args);

        videoId = classifys1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            videoId = getArguments().getInt("videoId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail_other, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSummaryTv = (TextView) view.findViewById(R.id.summary);
        mSummaryTv.setText(videoId+"");
    }

}
