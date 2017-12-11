package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedEntity;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 08/12/2017 5:56 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchUserRedAdapter extends BaseQuickAdapter<UserRedEntity> {
    public SearchUserRedAdapter(int layoutResId, List<UserRedEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final UserRedEntity item) {
        helper.setText(R.id.name, item.getName());

        Glide.with(mContext)
                .load(item.getUserPic())
                .asBitmap()
                .placeholder(R.mipmap.avator_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.avatar));
    }
}


