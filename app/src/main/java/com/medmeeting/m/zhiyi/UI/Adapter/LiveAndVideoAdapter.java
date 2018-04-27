package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/10/2017 5:10 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LiveAndVideoAdapter extends BaseQuickAdapter<LiveRoomDto> {
    public LiveAndVideoAdapter(int layoutResId, List<LiveRoomDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveRoomDto item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle());
    }
}

