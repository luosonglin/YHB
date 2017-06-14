package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<BlogDto.BlogBean.ListBean> {
    public NewsAdapter(int layoutResId, List<BlogDto.BlogBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlogDto.BlogBean.ListBean item) {
        Glide.with(mContext)
                .load(item.getImages())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.author, item.getName());

        Glide.with(mContext)
                .load(item.getUserPic())
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.avatar));
    }
}
