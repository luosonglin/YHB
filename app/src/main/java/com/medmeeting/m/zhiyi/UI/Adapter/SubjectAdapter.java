package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/03/2018 11:22 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class SubjectAdapter extends BaseQuickAdapter<TagDto> {
    public SubjectAdapter(int layoutResId, List<TagDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, TagDto item) {
        helper.setText(R.id.name, item.getLabelName() + "");

        helper.setOnClickListener(R.id.name, new OnItemChildClickListener());
    }
}
