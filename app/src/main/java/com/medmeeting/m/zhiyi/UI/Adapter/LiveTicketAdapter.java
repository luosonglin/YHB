package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveTicketDto;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class LiveTicketAdapter extends BaseQuickAdapter<LiveTicketDto.PayListBean> {
    public LiveTicketAdapter(int layoutResId, List<LiveTicketDto.PayListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveTicketDto.PayListBean item) {

        Glide.with(mContext)
                .load(item.getUserPic())
                .crossFade()
                .dontAnimate()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.avator_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.title, item.getDepartment() + "")
                .setText(R.id.money, "已支付 ¥" + item.getAmount());

        helper.setText(R.id.name, item.getUserName() == null ? item.getNickName() : item.getUserName());
    }
}
