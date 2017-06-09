package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.MeetingDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class MeetingAdapter extends BaseQuickAdapter<MeetingDto.PageInfoBean.ListBean> {
    public MeetingAdapter(int layoutResId, List<MeetingDto.PageInfoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingDto.PageInfoBean.ListBean item) {
        Glide.with(mContext)
                .load("http://www.medmeeting.com/upload/banner/" + item.getBanner())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.book_info_image_url));
        helper.setText(R.id.book_info_textview_name,item.getTitle());
        helper.setText(R.id.book_info_textview_author,item.getId()+"");
        helper.setText(R.id.book_info_textview_introduction,"简介:"+item.getStartDate());
    }
}
