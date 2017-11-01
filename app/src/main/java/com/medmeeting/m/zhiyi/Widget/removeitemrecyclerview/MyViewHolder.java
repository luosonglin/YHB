package com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView content;
    public LinearLayout layout;
    public ImageView background;
    public RelativeLayout delete;
    public RelativeLayout update;
    public TextView liveNumber;

    public TextView price;
    public TextView sum;

    public MyViewHolder(View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.item_content);
        layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        background = (ImageView) itemView.findViewById(R.id.background);
        delete = (RelativeLayout) itemView.findViewById(R.id.item_delete);
        update = (RelativeLayout) itemView.findViewById(R.id.item_update);
        liveNumber = (TextView) itemView.findViewById(R.id.item_number);

        price = (TextView) itemView.findViewById(R.id.price);
        sum = (TextView) itemView.findViewById(R.id.sum);

    }
}
