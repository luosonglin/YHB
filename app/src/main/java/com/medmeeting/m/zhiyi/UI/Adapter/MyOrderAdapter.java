package com.medmeeting.m.zhiyi.UI.Adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/11/2017 5:32 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyOrderAdapter extends BaseQuickAdapter<VideoListEntity> {
    public MyOrderAdapter(int layoutResId, List<VideoListEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final VideoListEntity item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.sum, "观看 " + item.getPlayCount() + "    收藏 " + item.getCollectCount())
                .setText(R.id.time, DateUtils.formatDate(item.getCreateTime(), DateUtils.TYPE_06));

        if (item.getChargeType().equals("no")) {
            helper.getView(R.id.price).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.price, "¥ " + item.getPrice());
        }

        if (item.getVideoStatus() != null) {
            if (item.getVideoStatus().equals("ready")) {
                helper.setText(R.id.status, "预告");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_blue);
            }
        } else {
            helper.getView(R.id.status).setVisibility(View.GONE);
        }
    }
}
