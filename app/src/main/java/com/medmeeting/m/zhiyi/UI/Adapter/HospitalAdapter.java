package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HospitalInfo;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/03/2018 2:07 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class HospitalAdapter extends BaseQuickAdapter<HospitalInfo> {
    public HospitalAdapter(int layoutResId, List<HospitalInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalInfo item) {
        helper.setText(R.id.name, item.getHsName());
    }
}
