package com.medmeeting.m.zhiyi.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.VideoInfoUserEntity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.MyViewHolder;

import java.util.ArrayList;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 5:46 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyVideoAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<VideoInfoUserEntity> mList;

    public MyVideoAdapter(Context context, ArrayList<VideoInfoUserEntity> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_my_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.content.setText(mList.get(position).getTitle());

        Glide.with(mContext)
                .load(mList.get(position).getCoverPhoto())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(viewHolder.background);

        viewHolder.liveNumber.setText(DateUtils.formatDate(mList.get(position).getCreateTime(), DateUtils.TYPE_08));

        if (mList.get(position).getChargeType().equals("no")) {
            viewHolder.price.setText("免费");
            viewHolder.sum.setVisibility(View.GONE);
        } else if (mList.get(position).getChargeType().equals("yes")) {
            viewHolder.price.setText("¥ " + mList.get(position).getPrice());
            viewHolder.sum.setText("已付费: " + mList.get(position).getPayCount() + " 人");
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }
}
