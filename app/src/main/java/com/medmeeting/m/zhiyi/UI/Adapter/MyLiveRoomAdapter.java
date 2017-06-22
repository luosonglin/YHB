package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class MyLiveRoomAdapter extends BaseQuickAdapter<BannerDto.BannersBean> {
    public MyLiveRoomAdapter(int layoutResId, List<BannerDto.BannersBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerDto.BannersBean item) {
//        Glide.with(mContext)
//                .load("http://www.medmeeting.com/upload/banner/" + item.getBanner())
//                .crossFade()
//                .placeholder(R.mipmap.ic_launcher)
//                .into((ImageView) helper.getView(R.id.book_info_image_url));
        helper.setText(R.id.item_content,item.getId()+"");
    }
}
