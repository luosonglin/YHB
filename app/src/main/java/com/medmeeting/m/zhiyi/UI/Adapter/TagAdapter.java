package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

public class TagAdapter extends BaseQuickAdapter<TagDto> {
    public TagAdapter(int layoutResId, List<TagDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagDto item) {
        helper.setText(R.id.name,item.getLabelName()+"");
    }
}
