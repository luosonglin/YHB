package com.medmeeting.m.zhiyi.UI.Adapter;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.UserIdentity;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.BaseViewHolder;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 01/02/2018 1:47 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org null
 */
public class IdentityTypeAdapter extends BaseQuickAdapter<UserIdentity> {
    public IdentityTypeAdapter(int layoutResId, List<UserIdentity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserIdentity item) {
        helper.setText(R.id.name, item.getTitle())
                .setText(R.id.des, item.getDes());
    }

}

