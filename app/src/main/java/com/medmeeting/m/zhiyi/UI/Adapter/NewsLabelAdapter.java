package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 07/12/2017 10:41 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsLabelAdapter extends BaseQuickAdapter<String> {
    public NewsLabelAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.setText(R.id.name, item);

        helper.setOnClickListener(R.id.name, new OnItemChildClickListener());

    }
}
