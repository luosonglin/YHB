package com.medmeeting.m.zhiyi.UI.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.DepartmentLevelTwo;
import com.medmeeting.m.zhiyi.Util.FontHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentLevelTwoAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int VIEWHOLDER_TAG = R.id.department_level_one_llyt;
    private static final int DEPARTMENT_LEVEL_ONE_TAG = R.id.name;
    private static final int POSITION_TAG = R.id.thumb;


    private Context mContext;
    private List<DepartmentLevelTwo> departmentLevelTwos = new ArrayList<>();
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public DepartmentLevelTwoAdapter(Context context) {
        this.mContext = context;
    }

    public void setupDepartmentLevelTwo(List<DepartmentLevelTwo> departmentLevelTwos, boolean isClear) {
        if (departmentLevelTwos == null) return;
        if (isClear) {
            this.departmentLevelTwos.clear();
        }
        this.departmentLevelTwos.addAll(departmentLevelTwos);

        notifyDataSetChanged();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_department_level_two, parent, false);

        return new DepartmentLevelTwoViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DepartmentLevelTwo departmentLevelTwo = departmentLevelTwos.get(position);
        final DepartmentLevelTwoViewHolder viewHolder = (DepartmentLevelTwoViewHolder) holder;

        FontHelper.applyFont(mContext, viewHolder.name, FontHelper.ICONFONT);

        viewHolder.departmentLevelTwoLlyt.setTag(VIEWHOLDER_TAG, viewHolder);
        viewHolder.departmentLevelTwoLlyt.setTag(DEPARTMENT_LEVEL_ONE_TAG, departmentLevelTwo);
        viewHolder.departmentLevelTwoLlyt.setTag(POSITION_TAG, position);
        viewHolder.departmentLevelTwoLlyt.setOnClickListener(this);

        viewHolder.name.setText(departmentLevelTwo.getName());
    }

    @Override
    public int getItemCount() {
        if (departmentLevelTwos == null) {
            return 0;
        }
        return departmentLevelTwos.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickListener == null) return;
        switch (v.getId()) {
            case R.id.department_level_one_llyt:
                onRecyclerViewItemClickListener.onItemClick(v,
                        (DepartmentLevelTwoViewHolder) v.getTag(VIEWHOLDER_TAG), (DepartmentLevelTwo) v.getTag(DEPARTMENT_LEVEL_ONE_TAG),
                        (Integer) v.getTag(POSITION_TAG));
                break;
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, DepartmentLevelTwoViewHolder viewHolder, DepartmentLevelTwo departmentLevelTwo, int position);
    }

    public static class DepartmentLevelTwoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.department_level_one_llyt)
        RelativeLayout departmentLevelTwoLlyt;

        public DepartmentLevelTwoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

