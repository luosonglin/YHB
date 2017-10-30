package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveDetailActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class LiveAdapter extends BaseQuickAdapter<LiveDto> {
    public LiveAdapter(int layoutResId, List<LiveDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveDto item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into((ImageView) helper.getView(R.id.image));

        Glide.with(mContext)
                .load(item.getUserPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.sum, item.getOnlineCount() + "")
                .setText(R.id.title, item.getRoomTitle() + "")
                .setText(R.id.name, item.getTitle())
//                .setText(R.id.time, DateUtil.formatDate(item.getStartTime(), DateUtil.TYPE_06));
                .setText(R.id.time, item.getAuthorName() + " | "+item.getAuthorTitle());

        if (item.getChargeType().equals("no")) {
            helper.getView(R.id.price).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.price, "¥ " + item.getPrice());
        }
        switch (item.getLiveStatus()) {
            case "ready":
                helper.setText(R.id.status, "预告");//准备
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_blue);
                break;
            case "play":
                helper.setText(R.id.status, "直播");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_red);
                break;
            case "wait":
                helper.setText(R.id.status, "离开");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_yellow);
                break;
            case "end":
                helper.setText(R.id.status, "结束");
                helper.setBackgroundRes(R.id.status, R.mipmap.icon_live_adapter_status_grey);
                break;
        }
        helper.getView(R.id.image).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, LiveProgramDetailActivity.class);
//                Intent intent = new Intent(mContext, VideoDetailActivity.class);
            intent.putExtra("authorName", item.getAuthorName());
            intent.putExtra("userPic", item.getUserPic());
            intent.putExtra("programId", item.getId());
            intent.putExtra("roomId", item.getRoomId());
            intent.putExtra("coverPhoto", item.getCoverPhoto());
            intent.putExtra("title", item.getTitle());
            mContext.startActivity(intent);
        });


        helper.getView(R.id.r2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LiveDetailActivity.class);
                intent.putExtra("roomId", item.getRoomId());
                intent.putExtra("coverPhote", item.getCoverPhoto());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("authorName", item.getAuthorName());
                intent.putExtra("description", item.getDes());
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.l1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LiveDetailActivity.class);
                intent.putExtra("roomId", item.getRoomId());
                intent.putExtra("coverPhote", item.getCoverPhoto());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("authorName", item.getAuthorName());
                intent.putExtra("description", item.getDes());
                mContext.startActivity(intent);
            }
        });


    }
}
