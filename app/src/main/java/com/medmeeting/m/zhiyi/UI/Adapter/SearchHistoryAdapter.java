package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 3:55 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String> {
    public SearchHistoryAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        helper.setText(R.id.name,item);
    }
}



