package com.medmeeting.m.zhiyi.UI.Adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.Util.DateUtil;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class MyLiveProgramAdapter extends BaseQuickAdapter<LiveDto> {
    public MyLiveProgramAdapter(int layoutResId, List<LiveDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveDto item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));
        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.time, DateUtil.formatDate(item.getStartTime(), DateUtil.TYPE_06))
                .setText(R.id.sum, "已报名：" + item.getPayCount()+" 人");
        if (item.getChargeType().equals("yes")) {
            helper.setText(R.id.money, "¥" + item.getPrice());
        } else if (item.getChargeType().equals("no")) {
            helper.getView(R.id.money).setVisibility(View.INVISIBLE);
        }
    }
}
