package com.medmeeting.m.zhiyi.UI.Adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private final FragmentManager mFm;
    private List<BaseFragment> fragments;
    private List<TagDto> mChannels;
    private int mChildCount;
    private boolean[] fragmentsUpdateFlag;

    public ChannelPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, List<TagDto> channels) {
        super(fm);
        mFm = fm;
        this.fragments = fragments;
        this.mChannels = channels;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels == null ? "" : mChannels.get(position).getLabelName();
    }

//    @Override
//    public void notifyDataSetChanged() {
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }



    @Override
    public int getItemPosition(Object object) {
//        if (mChildCount > 0) {
//            mChildCount--;
            return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
    }

}
