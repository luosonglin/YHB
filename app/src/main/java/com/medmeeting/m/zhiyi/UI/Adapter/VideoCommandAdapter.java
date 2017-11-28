package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.VideoCommentUserEntity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 25/10/2017 11:33 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoCommandAdapter extends BaseQuickAdapter<VideoCommentUserEntity> {
    public VideoCommandAdapter(int layoutResId, List<VideoCommentUserEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoCommentUserEntity item) {
        Glide.with(mContext)
                .load(item.getUserPic())
                .crossFade()
                .placeholder(R.mipmap.avator_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.avatar));

        if (item.getName() != null) {
            if (!item.getName().equals("")) {
                helper.setText(R.id.name, item.getName());
            }
        } else {
            helper.setText(R.id.name, item.getNickName());
        }
        helper.setText(R.id.time, DateUtils.formatDate(item.getCreateTime(), DateUtils.TYPE_06));
        helper.setText(R.id.content, item.getContent());
    }
}
