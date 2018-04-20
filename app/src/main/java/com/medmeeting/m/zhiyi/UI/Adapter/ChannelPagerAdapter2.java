package com.medmeeting.m.zhiyi.UI.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.UI.MeetingView.EventFragment;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 20/04/2018 11:31 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class ChannelPagerAdapter2  extends FragmentStatePagerAdapter {

    private final FragmentManager mFm;
    private List<EventFragment> fragments;
    private List<TagDto> mChannels;
    private int mChildCount;
    private boolean[] fragmentsUpdateFlag;

    public ChannelPagerAdapter2(FragmentManager fm, List<EventFragment> fragments, List<TagDto> channels) {
        super(fm);
        mFm = fm;
        this.fragments = fragments;
        this.mChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {
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
