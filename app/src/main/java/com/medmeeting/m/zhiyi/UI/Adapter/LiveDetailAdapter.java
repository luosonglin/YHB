package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailAuthorActivity;
import com.medmeeting.m.zhiyi.Util.DateUtil;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class LiveDetailAdapter extends BaseQuickAdapter<LiveDetailDto.EntityBean.ProgramListBean> {
    private String mUserId;

    public LiveDetailAdapter(int layoutResId, List<LiveDetailDto.EntityBean.ProgramListBean> data, String userId) {
        super(layoutResId, data, userId);
        mUserId = userId;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveDetailDto.EntityBean.ProgramListBean item) {
        Glide.with(mContext)
                .load(item.getCoverPhoto())
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.author, "主讲人：" + item.getAuthorName())
                .setText(R.id.time, DateUtil.formatDate(item.getStartTime(), DateUtil.TYPE_06))
                .setText(R.id.money, "¥" + item.getPrice())
                .setText(R.id.type, "直播");

        if ("no".equals(item.getChargeType())) helper.setText(R.id.money, "免费");

        helper.getView(R.id.item_news_cv).setOnClickListener(view -> {
            Intent intent;
            if (mUserId.equals(item.getUserId() + "")) {
                //主播进的直播节目详情页
                intent = new Intent(mContext, LiveProgramDetailAuthorActivity.class);
                Log.e(TAG, "1: " + mUserId + " " + item.getUserId());
            } else {
                //用户进的直播节目详情页
                intent = new Intent(mContext, LiveProgramDetailActivity2.class);
                Log.e(TAG, "2: " + mUserId + " " + item.getUserId());
            }
            intent.putExtra("programId", item.getId());

            mContext.startActivity(intent);
        });
    }
}

