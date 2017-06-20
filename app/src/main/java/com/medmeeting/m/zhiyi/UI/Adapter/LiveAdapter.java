package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class LiveAdapter extends BaseQuickAdapter<LiveDto.DataBean> {
    public LiveAdapter(int layoutResId, List<LiveDto.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveDto.DataBean item) {
        Glide.with(mContext)
                .load("http://www.medmeeting.com/upload/banner/" + item.getCoverPhoto())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        Glide.with(mContext)
                .load("http://www.medmeeting.com/upload/banner/" + item.getUserPic())
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.sum, item.getId()+"")
                .setText(R.id.title,item.getRoomTitle()+"")
                .setText(R.id.name,item.getTitle())
                .setText(R.id.time, DateUtils.formatDate(item.getStartTime(), DateUtils.TYPE_06));
    }
}
