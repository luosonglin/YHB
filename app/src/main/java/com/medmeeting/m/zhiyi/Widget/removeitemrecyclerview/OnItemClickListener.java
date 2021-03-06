package com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview;

import android.view.View;

public interface OnItemClickListener {
    /**
     * item点击回调
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position);

    /**
     * 更新按钮回调
     *
     * @param position
     */
    void onUpdateClick(int position);
}
