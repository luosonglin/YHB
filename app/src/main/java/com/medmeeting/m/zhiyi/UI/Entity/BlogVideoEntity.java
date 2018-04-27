package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 30/11/2017 10:16 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BlogVideoEntity {
    private Blog blog;
    private VideoDetailsEntity videoDetailsEntity;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public VideoDetailsEntity getVideoDetailsEntity() {
        return videoDetailsEntity;
    }

    public void setVideoDetailsEntity(VideoDetailsEntity videoDetailsEntity) {
        this.videoDetailsEntity = videoDetailsEntity;
    }
}
