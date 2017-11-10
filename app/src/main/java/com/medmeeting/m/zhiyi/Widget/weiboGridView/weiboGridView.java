package com.medmeeting.m.zhiyi.Widget.weibogridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.github.mzule.ninegridlayout.AbstractNineGridLayout;
import com.medmeeting.m.zhiyi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * —————————————————————–—————————————————————–
 * Copyright (C) 2017，Luo Songlin，All rights reserved.
 * Created by luosonglin on 21/02/2017.
 * (Feature)
 * —————————————————————–—————————————————————–
 */

public class weiboGridView extends AbstractNineGridLayout<List<String>> {
    private ImageView[] imageViews;
    private View[] gifViews;

    public weiboGridView(Context context) {
        super(context);
    }

    public weiboGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void fill() {
        fill(R.layout.item_image_grid);
        imageViews = findInChildren(R.id.image, ImageView.class); // 在每个 child 里面进行 findViewById(id)，返回数组
        gifViews = findInChildren(R.id.gif, View.class);
    }

    @Override
    public void render(List<String> images) {
        setSingleModeSize(400, 400);//images.get(0).getWidth(), images.get(0).getHeight()); // 设置单图模式单图大小
        setDisplayCount(images.size()); // 设置展示个数，剩下的隐藏
        for (int i = 0; i < images.size(); i++) {
            String url = images.get(i);
            Picasso.with(getContext()).load(url).placeholder(R.color.gray).into(imageViews[i]);
            gifViews[i].setVisibility(url.endsWith(".gif") ? VISIBLE : INVISIBLE);
        }
    }

}
