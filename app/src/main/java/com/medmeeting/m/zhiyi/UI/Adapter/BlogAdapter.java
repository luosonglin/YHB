package com.medmeeting.m.zhiyi.UI.Adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.Util.DateUtil;

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
                            .placeholder(R.mipmap.ic_launcher)
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
                            .placeholder(R.mipmap.ic_launcher)
                            .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg1));
                    Glide.with(mContext)
                            .load(images[1])
                            .crossFade()
                            .placeholder(R.mipmap.ic_launcher)
                            .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg2));

                    if (images.length > 2)
                        Glide.with(mContext)
                                .load(images[2])
                                .crossFade()
                                .placeholder(R.mipmap.ic_launcher)
                                .into((ImageView) baseViewHolder.getView(R.id.ivCenterImg3));
                }
                break;
            case "3":
                Glide.with(mContext)
                        .load(blog.getImages())
                        .crossFade()
                        .placeholder(R.mipmap.ic_launcher)
                        .into((ImageView) baseViewHolder.getView(R.id.ivRightImg2));
                baseViewHolder.setVisible(R.id.rlRightImg, false)
                        .setVisible(R.id.viewFill, false)
                        .setVisible(R.id.llVideo, false)
                        .setVisible(R.id.videoRlyt, true)
                        .setText(R.id.tvDuration1, blog.getBlogType());
                break;
        }

        baseViewHolder.setText(R.id.tvTitle, blog.getTitle())
                .setText(R.id.tvAuthorName, blog.getAuthorName())
                .setText(R.id.tvCommentCount, "评论" + blog.getCommentCount())
                .setText(R.id.tvTime, DateUtil.formatDate(blog.getCreatedAt(), DateUtil.TYPE_09));
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
