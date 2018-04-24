package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.VAppMyEvents;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * 待参会adapter
 */
public class WaitMeetingAdapter extends BaseQuickAdapter<VAppMyEvents> {
    public WaitMeetingAdapter(int layoutResId, List<VAppMyEvents> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final VAppMyEvents item) {
        Glide.with(mContext)
                .load(item.getBanner())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
//                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.address, item.getAddress() + "")
                .setText(R.id.ha1, DateUtils.formatDate(item.getStateDate(), DateUtils.TYPE_07))
                .setText(R.id.ha2, "~ " + DateUtils.formatDate(item.getEndDate(), DateUtils.TYPE_07));

        helper.getView(R.id.item_meeting).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("eventId", item.getEventId());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
    }
}
