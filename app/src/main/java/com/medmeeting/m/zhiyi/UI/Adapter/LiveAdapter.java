package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
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
                .load("http://www.medmeeting.com/upload/banner/" + item.getCoverPhoto())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        Glide.with(mContext)
                .load("http://www.medmeeting.com/upload/banner/" + item.getUserPic())
                .crossFade()
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.avatar));

        helper.setText(R.id.sum, item.getId()+"")
                .setText(R.id.title,item.getRoomTitle()+"")
                .setText(R.id.name,item.getTitle())
                .setText(R.id.time, DateUtils.formatDate(item.getStartTime(), DateUtils.TYPE_06));

        helper.getView(R.id.item_live_flyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(mContext, );
                ToastUtils.show(mContext, "11");
            }
        });

        helper.getView(R.id.name).setOnClickListener(new View.OnClickListener() {
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
