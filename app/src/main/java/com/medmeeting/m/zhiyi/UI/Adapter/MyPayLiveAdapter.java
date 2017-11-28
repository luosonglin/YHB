package com.medmeeting.m.zhiyi.UI.Adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 2:11 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyPayLiveAdapter extends BaseQuickAdapter<LiveDto> {
    public MyPayLiveAdapter(int layoutResId, List<LiveDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveDto item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.sum, "收藏 "+ item.getCollectCount() + "")
                .setText(R.id.name, item.getTitle())
                .setText(R.id.time, DateUtils.formatDate(item.getStartTime(), DateUtils.TYPE_08));

        if (item.getChargeType().equals("no")) {
            helper.getView(R.id.price).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.price, "¥ " + item.getPrice());
        }
        switch (item.getLiveStatus()) {
            case "ready":
                helper.setText(R.id.status, "预告");//准备
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_blue);
                break;
            case "play":
                helper.setText(R.id.status, "直播");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_red);
                break;
            case "wait":
                helper.setText(R.id.status, "离开");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_yellow);
                break;
            case "end":
                helper.setText(R.id.status, "结束");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_grey);
                break;
        }
    }
}

