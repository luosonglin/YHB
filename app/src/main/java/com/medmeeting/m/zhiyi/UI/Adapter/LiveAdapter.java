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
import com.medmeeting.m.zhiyi.Util.DateUtil;
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
//                .placeholder(R.mipmap.live_background)
                .into((ImageView) helper.getView(R.id.image));

        Glide.with(mContext)
                .load(item.getUserPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
//                .placeholder(R.mipmap.avator_default)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.sum, item.getOnlineCount()+"")
                .setText(R.id.title,item.getRoomTitle()+"")
                .setText(R.id.name,item.getTitle())
                .setText(R.id.time, DateUtil.formatDate(item.getStartTime(), DateUtil.TYPE_06));

        switch (item.getLiveStatus()) {
            case "ready":
                helper.setText(R.id.status, "准备中");
                break;
            case "play":
                helper.setText(R.id.status, "直播中");
                break;
            case "wait":
                helper.setText(R.id.status, "离开");
                break;
            case "end":
                helper.setText(R.id.status, "已结束");
                break;
        }
        helper.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LiveProgramDetailActivity.class);
                intent.putExtra("authorName", item.getAuthorName());
                intent.putExtra("userPic", item.getUserPic());
                intent.putExtra("programId", item.getId());
                intent.putExtra("roomId", item.getRoomId());
                intent.putExtra("coverPhoto", item.getCoverPhoto());
                intent.putExtra("title", item.getTitle());
                mContext.startActivity(intent);
            }
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
