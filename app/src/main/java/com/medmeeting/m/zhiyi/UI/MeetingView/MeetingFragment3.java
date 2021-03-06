package com.medmeeting.m.zhiyi.UI.MeetingView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Listener.OnChannelListener;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.ChannelPagerAdapter2;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.IndexView.ChannelDialogFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.CommonUtil;
import com.medmeeting.m.zhiyi.UI.SearchView.SearchMeetingActivity;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.colortrackview.ColorTrackTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;

import static com.medmeeting.m.zhiyi.Util.ConstanceValue.MEETING_SELECTED;
import static com.medmeeting.m.zhiyi.Util.ConstanceValue.MEETING_UNSELECTED;

public class MeetingFragment3 extends Fragment implements OnChannelListener {

    private static final String TAG = MeetingFragment3.class.getSimpleName();
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.new_category_tip)
    ImageView newCategoryTip;
    @BindView(R.id.tab)
    ColorTrackTabLayout tab;
    @BindView(R.id.icon_category)
    ImageView iconCategory;
    @BindView(R.id.vp)
    ViewPager vp;
    private ChannelPagerAdapter2 mTitlePagerAdapter;

    public List<TagDto> mSelectedDatas = new ArrayList<>();
    public List<TagDto> mUnSelectedDatas = new ArrayList<>();
    private List<EventFragment> mFragments;
    private Gson mGson = new Gson();
    private OnFragmentInteractionListener mListener;

    Unbinder unbinder;

    public MeetingFragment3() {
    }

    public static MeetingFragment3 newInstance() {
        MeetingFragment3 fragment = new MeetingFragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting3, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);

        getTitleData();

        return view;
    }

    /**
     * 获取标题数据
     */
    private void getTitleData() {
        String selectTitle = SharedPreferencesMgr.getString(MEETING_SELECTED, "");
        String unselectTitle = SharedPreferencesMgr.getString(MEETING_UNSELECTED, "");

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
            Map<String, Object> options = new HashMap<>();
            options.put("type", "EVENT");
            HttpData.getInstance().HttpDataGetVideoTags(new Observer<HttpResult3<TagDto, Object>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpResult3<TagDto, Object> data) {
                    if (!data.getStatus().equals("success")) {
                        ToastUtils.show(getActivity(), data.getMsg());
                        return;
                    }
                    //默认添加频道
                    List<TagDto> liveLabels = new ArrayList<>();
                    liveLabels.add(new TagDto(0, 0,"全部1"));
                    liveLabels.add(new TagDto(0, 48,"内科"));
                    liveLabels.add(new TagDto(0, 7,"外科"));
                    liveLabels.add(new TagDto(0, 9,"骨科"));

                    for(TagDto i: data.getData()) {
                        Log.e(getActivity().getLocalClassName(), i.getLabelName()+" "+i.getId()+" "+i.getItemType());
                    }

                    mSelectedDatas.addAll(liveLabels);
                    String selectedStr = mGson.toJson(mSelectedDatas);
                    SharedPreferencesMgr.setString(MEETING_SELECTED, selectedStr);

                    liveLabels.clear();
                    liveLabels.addAll(data.getData());
                    mUnSelectedDatas.addAll(liveLabels);
                    String unselectTitle = mGson.toJson(mUnSelectedDatas);
                    SharedPreferencesMgr.setString(MEETING_UNSELECTED, unselectTitle);

                    initView();
                }
            }, options);

        } else {
            //之前添加过
            List<TagDto> selecteData = mGson.fromJson(selectTitle, new TypeToken<List<TagDto>>() {
            }.getType());
            List<TagDto> unselecteData = mGson.fromJson(unselectTitle, new TypeToken<List<TagDto>>() {
            }.getType());
            mSelectedDatas.addAll(selecteData);
            mUnSelectedDatas.addAll(unselecteData);

            initView();
        }
    }


    private void initView() {

        mFragments = new ArrayList<>();
        for (int i = 0; i < mSelectedDatas.size(); i++) {
//            NewsFragment fragment = NewsFragment.newInstance(mSelectedDatas.get(i).getId(), mSelectedDatas.get(i).getLabelName());
            EventFragment fragment = EventFragment.newInstance(mSelectedDatas.get(i).getId());
            mFragments.add(fragment);
        }
        mTitlePagerAdapter = new ChannelPagerAdapter2(getChildFragmentManager(), mFragments, mSelectedDatas);
        vp.setAdapter(mTitlePagerAdapter);
        vp.setOffscreenPageLimit(mSelectedDatas.size());

        tab.setTabPaddingLeftAndRight(CommonUtil.dip2px(10), CommonUtil.dip2px(10));
        tab.setupWithViewPager(vp);
        tab.post(() -> {
            //设置最小宽度，使其可以在滑动一部分距离
            ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
            slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iconCategory.getMeasuredWidth());
        });
        //隐藏指示器时候为0
        tab.setSelectedTabIndicatorHeight(6);//设置当前被选中标签的高度
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
        unbinder.unbind();
    }

    @OnClick({R.id.search_icon, R.id.icon_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon:
                startActivity(new Intent(getActivity(), SearchMeetingActivity.class));
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
                    SharedPreferencesMgr.setString(ConstanceValue.MEETING_SELECTED, mGson.toJson(mSelectedDatas));
                    SharedPreferencesMgr.setString(ConstanceValue.MEETING_UNSELECTED, mGson.toJson(mUnSelectedDatas));

                    initView();
                });
                mTitlePagerAdapter.notifyDataSetChanged();
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
        TagDto liveLabel = mUnSelectedDatas.remove(starPos);
        mSelectedDatas.add(endPos, liveLabel);
        mFragments.add(EventFragment.newInstance(liveLabel.getId()));
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
