package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 23/10/2017 4:29 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoTagAdapter extends BaseQuickAdapter<TagDto> {
    public VideoTagAdapter(int layoutResId, List<TagDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, TagDto item) {
        helper.setText(R.id.name, item.getLabelName() + "");
        Glide.with(mContext)
                .load(item.getIconUrl())
                .dontAnimate()
                .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) helper.getView(R.id.image));
    }
}
