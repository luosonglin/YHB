package com.medmeeting.m.zhiyi.UI.IndexView;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.Base.BaseMvpFragment;
import com.medmeeting.m.zhiyi.MVP.Listener.OnChannelListener;
import com.medmeeting.m.zhiyi.MVP.Presenter.HomePresenter;
import com.medmeeting.m.zhiyi.MVP.View.IHomeView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.ChannelPagerAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Channel;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.CommonUtil;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.medmeeting.m.zhiyi.Widget.colortrackview.ColorTrackTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.medmeeting.m.zhiyi.Util.ConstanceValue.TITLE_SELECTED;
import static com.medmeeting.m.zhiyi.Util.ConstanceValue.TITLE_UNSELECTED;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/11/2017 1:21 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class IndexNewsFragment extends BaseMvpFragment<HomePresenter> implements IHomeView, OnChannelListener {

    private static final String TAG = IndexNewsFragment.class.getSimpleName();
    @Bind(R.id.tab)
    ColorTrackTabLayout tab;
    @Bind(R.id.icon_category)
    ImageView iconCategory;
    @Bind(R.id.vp)
    ViewPager mVp;

    private ChannelPagerAdapter mTitlePagerAdapter;

    public List<Channel> mSelectedDatas = new ArrayList<>();
    public List<Channel> mUnSelectedDatas = new ArrayList<>();
    private List<BaseFragment> mFragments;
    private Gson mGson = new Gson();


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    public IndexNewsFragment() {
    }

    public static IndexNewsFragment newInstance() {
        IndexNewsFragment fragment = new IndexNewsFragment();
        return fragment;
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_index_news, container);
    }

    @Override
    protected void bindViews(View view) {
        ButterKnife.bind(this, rootView);
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

    @Override
    protected void processLogic() {
        getTitleData();
        mFragments = new ArrayList<>();
        for (int i = 0; i < mSelectedDatas.size(); i++) {
            NewsFragment fragment = NewsFragment.newInstance(mSelectedDatas.get(i).TitleCode);
            mFragments.add(fragment);
        }
        mTitlePagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), mFragments, mSelectedDatas);
        mVp.setAdapter(mTitlePagerAdapter);
        mVp.setOffscreenPageLimit(mSelectedDatas.size());

        tab.setTabPaddingLeftAndRight(CommonUtil.dip2px(10), CommonUtil.dip2px(10));
        tab.setupWithViewPager(mVp);
        tab.post(new Runnable() {
            @Override
            public void run() {
                //设置最小宽度，使其可以在滑动一部分距离
                ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iconCategory.getMeasuredWidth());
            }
        });
        //隐藏指示器
        tab.setSelectedTabIndicatorHeight(0);
    }


    /**
     * 获取标题数据
     */
    private void getTitleData() {

        String selectTitle = SharedPreferencesMgr.getString(TITLE_SELECTED, "");
        String unselectTitle = SharedPreferencesMgr.getString(TITLE_UNSELECTED, "");
        if (TextUtils.isEmpty(selectTitle) || TextUtils.isEmpty(unselectTitle)) {
            //本地没有title
            String[] titleStr = getResources().getStringArray(R.array.home_title);
            String[] titlesCode = getResources().getStringArray(R.array.home_title_code);
            //默认添加了全部频道
            for (int i = 0; i < titlesCode.length; i++) {
                String t = titleStr[i];
                String code = titlesCode[i];
                mSelectedDatas.add(new Channel(t, code));
            }

            String selectedStr = mGson.toJson(mSelectedDatas);
            SharedPreferencesMgr.setString(TITLE_SELECTED, selectedStr);
        } else {
            //之前添加过
            List<Channel> selecteData = mGson.fromJson(selectTitle, new TypeToken<List<Channel>>() {
            }.getType());
            List<Channel> unselecteData = mGson.fromJson(unselectTitle, new TypeToken<List<Channel>>() {
            }.getType());
            mSelectedDatas.addAll(selecteData);
            mUnSelectedDatas.addAll(unselecteData);
        }
    }


    @Override
    protected void setListener() {
    }


    @OnClick({R.id.icon_category})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_category:
//                ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedDatas, mUnSelectedDatas);
//                dialogFragment.setOnChannelListener(this);
//                dialogFragment.show(getChildFragmentManager(), "CHANNEL");
//                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        mTitlePagerAdapter.notifyDataSetChanged();
//                        mVp.setOffscreenPageLimit(mSelectedDatas.size());
//                        tab.setCurrentItem(tab.getSelectedTabPosition());
//                        ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
//                        //注意：因为最开始设置了最小宽度，所以重新测量宽度的时候一定要先将最小宽度设置为0
//                        slidingTabStrip.setMinimumWidth(0);
//                        slidingTabStrip.measure(0, 0);
//                        slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iconCategory.getMeasuredWidth());
//
//                        //保存选中和未选中的channel
//                        SharedPreferencesMgr.setString(ConstanceValue.TITLE_SELECTED, mGson.toJson(mSelectedDatas));
//                        SharedPreferencesMgr.setString(ConstanceValue.TITLE_UNSELECTED, mGson.toJson(mUnSelectedDatas));
//
//                    }
//                });
                break;
        }
    }


    @Override
    public void onItemMove(int starPos, int endPos) {
        listMove(mSelectedDatas, starPos, endPos);
        listMove(mFragments, starPos, endPos);
    }


    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        //移动到我的频道
        Channel channel = mUnSelectedDatas.remove(starPos);
        mSelectedDatas.add(endPos, channel);
        mFragments.add(NewsFragment.newInstance(channel.TitleCode));
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        //移动到推荐频道
        mUnSelectedDatas.add(endPos, mSelectedDatas.remove(starPos));
        mFragments.remove(starPos);
    }

    private void listMove(List datas, int starPos, int endPos) {
        Object o = datas.get(starPos);
        //先删除之前的位置
        datas.remove(starPos);
        //添加到现在的位置
        datas.add(endPos, o);
    }

}
