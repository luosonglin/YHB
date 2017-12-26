package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class MeetingAdapter extends BaseQuickAdapter<MeetingDto> {
    public MeetingAdapter(int layoutResId, List<MeetingDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MeetingDto item) {
        Glide.with(mContext)
                .load(item.getBanner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
//                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.address, item.getAddress() + "")
                .setText(R.id.ha1, DateUtils.formatDate(item.getStartDate(), DateUtils.TYPE_07))
                .setText(R.id.ha2, "~ " + DateUtils.formatDate(item.getEndDate(), DateUtils.TYPE_07));

        Glide.with(mContext)
                .load(item.getBanner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.avator_default)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.getView(R.id.item_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("eventId", item.getId());
                bundle.putString("eventTitle", item.getTitle());
//                bundle.putString("sourceType", item.getSourceType());
                bundle.putString("photo", item.getBanner());
                bundle.putString("description", "大会时间：" + DateUtils.formatDate(item.getStartDate(), DateUtils.TYPE_02)
                        + " 至 " + DateUtils.formatDate(item.getEndDate(), DateUtils.TYPE_02)
                        + " 欢迎参加： " + item.getTitle());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
