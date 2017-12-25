package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.SearchView.SearchMeetingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/11/2017 5:14 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MeetingFragment2 extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MeetingFragment2.OnFragmentInteractionListener mListener;

    public MeetingFragment2() {
    }

    public static MeetingFragment2 newInstance() {
        MeetingFragment2 fragment = new MeetingFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting2, container, false);
        ButterKnife.bind(this, view);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        initTagsView();

        return view;
    }

    private void initTagsView() {
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() - 140 * 6;//800

        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE); //tabLayout MODE_FIXED
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).select();
    }

    private void setUpViewPager(ViewPager viewPager) {
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(getChildFragmentManager());//getActivity().getSupportFragmentManager

        mIndexChildAdapter.addFragment(EventFragment.newInstance(0), "全部");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(1), "年会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(2), "论坛");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(3), "峰会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(4), "交流会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(5), "研讨会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(6), "专题会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(7), "培训班");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(8), "学习班");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(9), "取证班");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(10), "研修班");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(11), "展会");
        mIndexChildAdapter.addFragment(EventFragment.newInstance(12), "其他");

        viewPager.setAdapter(mIndexChildAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MeetingFragment2.OnFragmentInteractionListener) {
            mListener = (MeetingFragment2.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    @OnClick({R.id.search_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon:
//                startActivity(new Intent(getActivity(), SearchActicity.class));
                startActivity(new Intent(getActivity(), SearchMeetingActivity.class));
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
