package com.medmeeting.m.zhiyi.UI.IndexView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnChannelListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.ChannelPagerAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.IndexLabel;
import com.medmeeting.m.zhiyi.UI.Entity.LiveLabel;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.CommonUtil;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.colortrackview.ColorTrackTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

import static com.medmeeting.m.zhiyi.Util.ConstanceValue.TITLE_SELECTED;
import static com.medmeeting.m.zhiyi.Util.ConstanceValue.TITLE_UNSELECTED;

public class IndexFragment2 extends Fragment implements OnChannelListener {

    private static final String TAG = IndexFragment2.class.getSimpleName();
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.new_category_tip)
    ImageView newCategoryTip;
    @Bind(R.id.tab)
    ColorTrackTabLayout tab;
    @Bind(R.id.icon_category)
    ImageView iconCategory;
    @Bind(R.id.vp)
    ViewPager vp;
    private ChannelPagerAdapter mTitlePagerAdapter;

    public List<LiveLabel> mSelectedDatas = new ArrayList<>();
    public List<LiveLabel> mUnSelectedDatas = new ArrayList<>();
    private List<BaseFragment> mFragments;
    private Gson mGson = new Gson();
    private OnFragmentInteractionListener mListener;

    public IndexFragment2() {
    }

    public static IndexFragment2 newInstance() {
        IndexFragment2 fragment = new IndexFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index2, container, false);
        ButterKnife.bind(this, view);

        getTitleData();

        return view;
    }

    private void initView() {

        mFragments = new ArrayList<>();
        for (int i = 0; i < mSelectedDatas.size(); i++) {
            NewsFragment fragment = NewsFragment.newInstance(mSelectedDatas.get(i).getId());
            mFragments.add(fragment);
        }
        mTitlePagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), mFragments, mSelectedDatas);
        vp.setAdapter(mTitlePagerAdapter);
        vp.setOffscreenPageLimit(mSelectedDatas.size());

        tab.setTabPaddingLeftAndRight(CommonUtil.dip2px(10), CommonUtil.dip2px(10));
        tab.setupWithViewPager(vp);
        tab.post(new Runnable() {
            @Override
            public void run() {
                //设置最小宽度，使其可以在滑动一部分距离
                ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iconCategory.getMeasuredWidth());
            }
        });
        //隐藏指示器
        tab.setSelectedTabIndicatorHeight(6);

        //test
//        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    /**
     * 获取标题数据
     */
    private void getTitleData() {
        String selectTitle = SharedPreferencesMgr.getString(TITLE_SELECTED, "");
        String unselectTitle = SharedPreferencesMgr.getString(TITLE_UNSELECTED, "");

//        Log.e(IndexFragment2.this.getActivity().getLocalClassName(), "333 "+selectTitle);
//        Log.e(IndexFragment2.this.getActivity().getLocalClassName(), unselectTitle);

        if (TextUtils.isEmpty(selectTitle) || TextUtils.isEmpty(unselectTitle)) {
            //本地没有title
//            String[] titleStr = getResources().getStringArray(R.array.home_title);
//            String[] titlesCode = getResources().getStringArray(R.array.home_title_code);
//            //默认添加了全部频道
//            for (int i = 0; i < titlesCode.length; i++) {
//                String t = titleStr[i];
//                String code = titlesCode[i];
//                mSelectedDatas.add(new Channel(t, code));
//            }

            //获取label
            HttpData.getInstance().HttpDataGetLabels(new Observer<HttpResult3<Object, IndexLabel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.show(getActivity(), e.getMessage());
                }

                @Override
                public void onNext(HttpResult3<Object, IndexLabel> data) {
                    //默认添加频道
                    List<LiveLabel> liveLabels = new ArrayList<>();
                    liveLabels.add(new LiveLabel(0, "推荐", 0, 0, 0, ""));
                    liveLabels.addAll(data.getEntity().getDefaultList());

                    mSelectedDatas.addAll(liveLabels);
                    String selectedStr = mGson.toJson(mSelectedDatas);
                    SharedPreferencesMgr.setString(TITLE_SELECTED, selectedStr);

                    mUnSelectedDatas.addAll(data.getEntity().getSysList());
                    String unselectTitle = mGson.toJson(mUnSelectedDatas);
                    SharedPreferencesMgr.setString(TITLE_UNSELECTED, unselectTitle);

                    initView();
                }
            });


        } else {
            //之前添加过
            List<LiveLabel> selecteData = mGson.fromJson(selectTitle, new TypeToken<List<LiveLabel>>() {
            }.getType());
            List<LiveLabel> unselecteData = mGson.fromJson(unselectTitle, new TypeToken<List<LiveLabel>>() {
            }.getType());
            mSelectedDatas.addAll(selecteData);
            mUnSelectedDatas.addAll(unselecteData);

            initView();
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
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.search_icon, R.id.icon_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon:
                startActivity(new Intent(getActivity(), SearchActicity.class));
                break;
            case R.id.icon_category:
                ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedDatas, mUnSelectedDatas);
                dialogFragment.setOnChannelListener(this);
                dialogFragment.show(getChildFragmentManager(), "CHANNEL");
                dialogFragment.setOnDismissListener(dialog -> {
                    mTitlePagerAdapter.notifyDataSetChanged();
                    vp.setOffscreenPageLimit(mSelectedDatas.size());
                    tab.setCurrentItem(tab.getSelectedTabPosition());
                    ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
                    //注意：因为最开始设置了最小宽度，所以重新测量宽度的时候一定要先将最小宽度设置为0
                    slidingTabStrip.setMinimumWidth(0);
                    slidingTabStrip.measure(0, 0);
                    slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iconCategory.getMeasuredWidth());

                    //保存选中和未选中的channel
                    SharedPreferencesMgr.setString(ConstanceValue.TITLE_SELECTED, mGson.toJson(mSelectedDatas));
                    SharedPreferencesMgr.setString(ConstanceValue.TITLE_UNSELECTED, mGson.toJson(mUnSelectedDatas));
                });
                break;
        }
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        listMove(mSelectedDatas, starPos, endPos);
        listMove(mFragments, starPos, endPos);
    }

    private void listMove(List datas, int starPos, int endPos) {
        Object o = datas.get(starPos);
        //先删除之前的位置
        datas.remove(starPos);
        //添加到现在的位置
        datas.add(endPos, o);
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        //移动到我的频道
        LiveLabel liveLabel = mUnSelectedDatas.remove(starPos);
        mSelectedDatas.add(endPos, liveLabel);
        mFragments.add(NewsFragment.newInstance(liveLabel.getId()));
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        //移动到推荐频道
        mUnSelectedDatas.add(endPos, mSelectedDatas.remove(starPos));
        mFragments.remove(starPos);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
