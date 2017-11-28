package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.FollowFinishedEvent;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class FinishedEventAdapter extends BaseQuickAdapter<FollowFinishedEvent> {
    public FinishedEventAdapter(int layoutResId, List<FollowFinishedEvent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FollowFinishedEvent item) {
        Glide.with(mContext)
                .load("http://www.medmeeting.com/upload/banner/" + item.getEventBanner())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getEventName())
                .setText(R.id.address, item.getEventAddress()+"")
                .setText(R.id.ha1, DateUtils.formatDate(item.getEventStartTime(), DateUtils.TYPE_07));

        helper.getView(R.id.item_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", item.getEventId()+"");
                bundle.putString("eventTitle", item.getEventName());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
