package com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 13/12/2017 4:51 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView content;
    public LinearLayout layout;
    public ImageView background;
    public RelativeLayout delete;
    public TextView liveNumber;

    public TextView time;

    public TextView price;
    public TextView sum;

    public MessageViewHolder(View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.item_content);
        layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        background = (ImageView) itemView.findViewById(R.id.background);
        delete = (RelativeLayout) itemView.findViewById(R.id.item_delete);
        liveNumber = (TextView) itemView.findViewById(R.id.item_number);

        time = (TextView) itemView.findViewById(R.id.time);

        price = (TextView) itemView.findViewById(R.id.price);
        sum = (TextView) itemView.findViewById(R.id.sum);

    }
}
