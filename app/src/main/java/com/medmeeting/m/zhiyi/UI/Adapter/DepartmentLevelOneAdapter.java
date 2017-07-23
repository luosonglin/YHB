package com.medmeeting.m.zhiyi.UI.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.DepartmentLevelOne;
import com.medmeeting.m.zhiyi.Util.FontHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * —————————————————————–
 * Copyright (C) 2016，上海知醫有限公司， All rights reserved.
 * —————————————————————–
 * Created by luosonglin on 11/11/2016.
 * —————————————————————–
 * (Feature)
 * —————————————————————–
 */

public class DepartmentLevelOneAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int VIEWHOLDER_TAG = R.id.department_level_one_llyt;
    private static final int DEPARTMENT_LEVEL_ONE_TAG = R.id.name;
    private static final int POSITION_TAG = R.id.thumb;


    private Context mContext;
    private List<DepartmentLevelOne> departmentLevelOnes = new ArrayList<>();
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public DepartmentLevelOneAdapter(Context context) {
        this.mContext = context;
    }

    public void setupDepartmentLevelOne(List<DepartmentLevelOne> departmentLevelOnes, boolean isClear) {
        if (departmentLevelOnes == null) return;
        if (isClear) {
            this.departmentLevelOnes.clear();
        }
        this.departmentLevelOnes.addAll(departmentLevelOnes);

//        notifyDataSetChanged();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_department_level_one, parent, false);

        return new DepartmentLevelOneViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DepartmentLevelOne departmentLevelOne = departmentLevelOnes.get(position);
        final DepartmentLevelOneViewHolder viewHolder = (DepartmentLevelOneViewHolder) holder;

        FontHelper.applyFont(mContext, viewHolder.name, FontHelper.ICONFONT);

        viewHolder.departmentLevelOneLlyt.setTag(VIEWHOLDER_TAG, viewHolder);
        viewHolder.departmentLevelOneLlyt.setTag(DEPARTMENT_LEVEL_ONE_TAG, departmentLevelOne);
        viewHolder.departmentLevelOneLlyt.setTag(POSITION_TAG, position);

        viewHolder.name.setText(Html.fromHtml("<font color=\"#000000\">"+ departmentLevelOne.getName() + "</font>"));
        viewHolder.line.setBackground(mContext.getResources().getDrawable(R.color.colorPrimaryDark));

        viewHolder.departmentLevelOneLlyt.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (departmentLevelOnes == null) {
            return 0;
        }
        return departmentLevelOnes.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickListener == null) return;
        switch (v.getId()) {
            case R.id.department_level_one_llyt:
//                notifyItemChanged((Integer) v.getTag(POSITION_TAG));
                onRecyclerViewItemClickListener.onItemClick(v,
                        (DepartmentLevelOneViewHolder) v.getTag(VIEWHOLDER_TAG), (DepartmentLevelOne) v.getTag(DEPARTMENT_LEVEL_ONE_TAG),
                        (Integer) v.getTag(POSITION_TAG));
                break;
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, DepartmentLevelOneViewHolder viewHolder, DepartmentLevelOne departmentLevelOne, int position);
    }

    public static class DepartmentLevelOneViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.line)
        View line;
        @Bind(R.id.department_level_one_llyt)
        RelativeLayout departmentLevelOneLlyt;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public View getLine() {
            return line;
        }

        public void setLine(View line) {
            this.line = line;
        }

        public RelativeLayout getDepartmentLevelOneLlyt() {
            return departmentLevelOneLlyt;
        }

        public void setDepartmentLevelOneLlyt(RelativeLayout departmentLevelOneLlyt) {
            this.departmentLevelOneLlyt = departmentLevelOneLlyt;
        }

        public DepartmentLevelOneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

