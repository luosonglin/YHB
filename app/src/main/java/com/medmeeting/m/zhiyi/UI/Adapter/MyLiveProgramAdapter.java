package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailAuthorActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class MyLiveProgramAdapter extends BaseQuickAdapter<LiveDto> {
    public MyLiveProgramAdapter(int layoutResId, List<LiveDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveDto item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));
        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.time, DateUtils.formatDate(item.getStartTime(), DateUtils.TYPE_06))
                .setText(R.id.sum, "已报名：" + item.getPayCount() + " 人")
                .setText(R.id.status, item.getLiveStatus());

        switch (item.getLiveStatus()) {
            case "end":
                helper.setText(R.id.status, "已结束");
                break;
            case "wait":
//                if () startTime
                helper.setText(R.id.status, "断开中");
                break;
            case "ready":
                helper.setText(R.id.status, "准备中");
                break;
            case "play":
                helper.setText(R.id.status, "直播中");
                break;
        }

        if (item.getChargeType().equals("yes")) {
            helper.setText(R.id.money, "¥" + item.getPrice());
        } else if (item.getChargeType().equals("no")) {
            helper.getView(R.id.money).setVisibility(View.INVISIBLE);
        }

        if (item.getPrivacyType().equals("private")) {
            helper.getView(R.id.lock).setVisibility(View.VISIBLE);
        } else if (item.getPrivacyType().equals("public")) {
            helper.getView(R.id.lock).setVisibility(View.GONE);
        }

        helper.getView(R.id.item_news_cv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LiveProgramDetailAuthorActivity.class);
                intent.putExtra("userPic", item.getUserPic());
                intent.putExtra("authorTitle", item.getAuthorTitle());
                intent.putExtra("authorName", item.getAuthorName());
                intent.putExtra("programId", item.getId());
                intent.putExtra("roomId", item.getRoomId());
                intent.putExtra("coverPhoto", item.getCoverPhoto());
                intent.putExtra("title", item.getTitle());
                mContext.startActivity(intent);
            }
        });
    }
}
