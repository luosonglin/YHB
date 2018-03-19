package com.medmeeting.m.zhiyi.UI.Adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.TimeUtils;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 21/11/2017 1:52 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BlogAdapter extends BaseQuickAdapter<Blog> {

    public BlogAdapter(List<Blog> data) {
        super(R.layout.item_blog, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Blog blog) {
        setGone(baseViewHolder);

        switch (blog.getBlogType()) {   //1图文类  2图集类    3视频类
            case "1":
                if (!TextUtils.isEmpty(blog.getImages())) {
                    //单图片文章
                    Glide.with(mContext)
                            .load(blog.getImages())
                            .crossFade()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.mipmap.news_bg)
                            .into((ImageView) baseViewHolder.getView(R.id.ivRightImg1));
                    baseViewHolder.setVisible(R.id.rlRightImg, true)
                            .setVisible(R.id.viewFill, true);
                }
                break;
            case "2":
                baseViewHolder.setVisible(R.id.llCenterImg, true);
                if (blog.getImages().contains(",")) {
                    String[] images = blog.getImages().split(",");

                    Glide.with(mContext)
                            .load(images[0])
                            .crossFade()
                            .dontAnimate()
                            .placeholder(R.mipmap.news_bg)
                            .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg1));
                    Glide.with(mContext)
                            .load(images[1])
                            .crossFade()
                            .dontAnimate()
                            .placeholder(R.mipmap.news_bg)
                            .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg2));

                    if (images.length > 2)
                        Glide.with(mContext)
                                .load(images[2])
                                .crossFade()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.mipmap.news_bg)
                                .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg3));
                }
                break;
            case "3":
                Glide.with(mContext)
                        .load(blog.getVideoImages())
                        .crossFade()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.video_bg)
                        .into((ImageView) baseViewHolder.getView(R.id.ivRightImg2));
                baseViewHolder.setVisible(R.id.rlRightImg, false)
                        .setVisible(R.id.viewFill, false)
                        .setVisible(R.id.llVideo, false)
                        .setVisible(R.id.videoRlyt, true);
                baseViewHolder.setText(R.id.tvDuration1, TimeUtils.getTimeFromSecond(blog.getTimeSecond()));
                break;
        }

        if (blog.getNewsStatus().equals("top")) {
            baseViewHolder.getView(R.id.news_top).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.news_top).setVisibility(View.GONE);
        }

        baseViewHolder.setText(R.id.tvTitle, blog.getTitle())
                .setText(R.id.tvAuthorName, " " + blog.getAuthorOrg() + " " + blog.getAuthorName())
                .setText(R.id.tvCommentCount, "评论" + blog.getCommentCount())
                .setText(R.id.tvTime, " " + DateUtils.formatDate(blog.getPushDate(), DateUtils.TYPE_04));
    }

    private void setGone(BaseViewHolder baseViewHolder) {
        baseViewHolder.setVisible(R.id.viewFill, false)
                .setVisible(R.id.llCenterImg, false)
                .setVisible(R.id.rlBigImg, false)
                .setVisible(R.id.llVideo, false)
                .setVisible(R.id.rlRightImg, false)
                .setVisible(R.id.videoRlyt, false);
    }
}
