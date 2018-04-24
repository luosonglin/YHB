package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.BaseArea;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 06/03/2018 1:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class CityAdapter extends BaseQuickAdapter<BaseArea> {
    public CityAdapter(int layoutResId, List<BaseArea> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseArea item) {
        helper.setText(R.id.name, item.getName());
    }
}
