package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/11/2017 4:23 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventAdapter extends BaseQuickAdapter<Event> {
    public EventAdapter(int layoutResId, List<Event> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Event item) {
        Glide.with(mContext)
                .load(item.getBanner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .placeholder(R.mipmap.meeting_bg)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.address, item.getEventProvince() + item.getEventCity())
                .setText(R.id.ha1, DateUtils.formatDate(item.getStartDate(), DateUtils.TYPE_07))
                .setText(R.id.ha2, " ~ " + DateUtils.formatDate(item.getEndDate(), DateUtils.TYPE_07));
    }
}
