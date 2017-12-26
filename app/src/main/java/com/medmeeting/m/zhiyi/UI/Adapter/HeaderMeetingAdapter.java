package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 27/11/2017 1:19 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class HeaderMeetingAdapter extends BaseQuickAdapter<Event> {
    public HeaderMeetingAdapter(int layoutResId, List<Event> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Event item) {

        Glide.with(mContext)
                .load(item.getBanner()+"?imageView/1/w/150/h/90")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.meeting_bg)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle());

    }
}
