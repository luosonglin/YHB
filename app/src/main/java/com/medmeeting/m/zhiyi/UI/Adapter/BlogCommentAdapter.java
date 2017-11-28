package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BlogComment;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 28/11/2017 3:08 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BlogCommentAdapter extends BaseQuickAdapter<BlogComment> {
    public BlogCommentAdapter(int layoutResId, List<BlogComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlogComment item) {
        Glide.with(mContext)
                .load(item.getUserPic())
                .crossFade()
                .placeholder(R.mipmap.avator_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.name, item.getUserName())
                .setText(R.id.time, DateUtils.formatDate(item.getCreatedAt(), DateUtils.TYPE_06))
                .setText(R.id.content, item.getContent());
    }
}
