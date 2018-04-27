package com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.PushUserMessage;
import com.medmeeting.m.zhiyi.Util.DateUtils;

import java.util.ArrayList;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 13/12/2017 4:45 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<PushUserMessage> mList;

    public MessageAdapter(Context context, ArrayList<PushUserMessage> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(mInflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MessageViewHolder viewHolder = (MessageViewHolder) holder;
        viewHolder.content.setText(mList.get(position).getContent());

        viewHolder.time.setText(DateUtils.formatDate(mList.get(position).getCreateDate(), DateUtils.TYPE_01));
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
