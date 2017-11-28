package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.UI.OtherVIew.NewsActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<BlogDto.BlogBean.ListBean> {
    public NewsAdapter(int layoutResId, List<BlogDto.BlogBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BlogDto.BlogBean.ListBean item) {
        String[] images =item.getImages().split(";");
        if (images.length > 1) {
            Glide.with(mContext)
                    .load(images[0])
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.image));
        } else {
            Glide.with(mContext)
                    .load(item.getImages())
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.image));
        }

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.author, item.getName())
                .setText(R.id.time, DateUtils.formatDate(item.getCreatedAt(), DateUtils.TYPE_06));

        Glide.with(mContext)
                .load(item.getUserPic())
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.avator_default)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.getView(R.id.item_news_cv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("blogId", item.getId()+"");
                bundle.putSerializable("blog", item);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
