package com.medmeeting.m.zhiyi.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 06/12/2017 10:28 AM
 * @describe 重写TextView用的Html带<img>标签时候的Html.ImageGetter
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class TextViewHtmlImageGetter implements Html.ImageGetter {

    Context mContext;
    TextView mTv;

    public TextViewHtmlImageGetter(Context mContext, TextView mTv) {
        this.mContext = mContext;
        this.mTv = mTv;
    }

    @Override
    public Drawable getDrawable(String source) {
        final LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(mContext)
                .load(source)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                            drawable.addLevel(1, 1, bitmapDrawable);
                            drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                            drawable.setLevel(1);

                            mTv.invalidate();
                            mTv.setText(mTv.getText());
                        }
                    }
                });

        return drawable;
    }
}
