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
    protected void convert(final BaseViewHolder helper, TagDto item) {
        helper.setText(R.id.name, item.getLabelName() + "");

//        helper.getView(R.id.name).setTag("grey");
//        helper.getView(R.id.name).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (helper.getView(R.id.name).getTag().equals("grey")) {
//                    helper.getView(R.id.name).setTag("blue");
//                    helper.getView(R.id.name).setBackgroundResource(R.drawable.textview_all_blue);
//                } else {
//                    helper.getView(R.id.name).setTag("grey");
//                    helper.getView(R.id.name).setBackgroundResource(R.drawable.textview_radius_grey);
//                }
//            }
//        });

    }
}
