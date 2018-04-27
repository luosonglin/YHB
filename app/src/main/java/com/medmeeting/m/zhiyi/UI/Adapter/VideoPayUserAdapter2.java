package com.medmeeting.m.zhiyi.UI.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.VideoPayUserEntity;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 19/01/2018 6:53 PM
 * @describe 视频、付费用户adapter
 * @email iluosonglin@gmail.com
 * @org null
 */
public class VideoPayUserAdapter2 extends BaseQuickAdapter<VideoPayUserEntity> {
    public VideoPayUserAdapter2(int layoutResId, List<VideoPayUserEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final VideoPayUserEntity item) {
        Glide.with(mContext)
                .load(item.getUserPic())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.name, item.getUserName()) //+ " " + item.getPostion()
                .setText(R.id.hospital, item.getCompany()) //item.getDepartment() + " " +
                .setText(R.id.amount, item.getAmount() + "元");

        switch (item.getTradeStatus()) {
            case "WAIT_PAY":
                helper.setText(R.id.status, "等待付款");
                break;
            case "SUCCESS":
                helper.setText(R.id.status, "支付完成");
                break;
            case "CLOSED":
                helper.setText(R.id.status, "交易关闭，发生退款");
                break;
            case "FINISHED":
                helper.setText(R.id.status, "交易完成");
                break;
            case "TIMEOUT":
                helper.setText(R.id.status, "订单超时");
                break;
        }
    }
}

