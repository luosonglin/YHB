package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveIndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveIndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveIndexFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView searchLiveBtn;

    private OnFragmentInteractionListener mListener;

    public LiveIndexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveIndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveIndexFragment newInstance(String param1, String param2) {
        LiveIndexFragment fragment = new LiveIndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) { //判断原来的rootView是否为null、若不为null则直接进行显示
            rootView = inflater.inflate(R.layout.fragment_live_index, container, false);

            tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
            viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

            //为ViewPager设置高度
            ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//            params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight();//800

            viewPager.setLayoutParams(params);

            setUpViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
            tabLayout.setupWithViewPager(viewPager);

            searchLiveBtn = (ImageView) rootView.findViewById(R.id.search_live_btn);
            searchLiveBtn.setOnClickListener(view -> startActivity(new Intent(getActivity(), LiveSearchActivity.class)));
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView); //防止Fragment被销毁
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

    private void setUpViewPager(ViewPager viewPager) {

         //如果是在fragment中使用viewpager, 记得要用getChildFragmentManager, 否则你会发现fragment异常的生命周期.
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(LiveIndexFragment.this.getChildFragmentManager());//.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(LiveIndexTabFragment1.newInstance(), "直播");
        mIndexChildAdapter.addFragment(LiveIndexTabFragment2.newInstance(), "视频");

        viewPager.setOffscreenPageLimit(2);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }

    public class IndexChildAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public IndexChildAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
