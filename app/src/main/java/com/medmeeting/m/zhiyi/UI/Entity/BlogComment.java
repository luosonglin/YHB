package com.medmeeting.m.zhiyi.UI.Entity;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 28/11/2017 2:48 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BlogComment {
    /**
     * blogId : 0
     * commentId : 0
     * content : string
     * createdAt : 2017-11-28T05:09:27.369Z
     * deletedAt : 2017-11-28T05:09:27.369Z
     * id : 0
     * userId : 0
     * userName : string
     * userPic : string
     */

    private int blogId;
    private int commentId;
    private String content;
    private String createdAt;
    private String deletedAt;
    private int id;
    private int userId;
    private String userName;
    private String userPic;

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }


    /**
     * 添加评论只需
     * {
     "blogId": 22,
     "content": " 乌鸦坐飞机，松哥撩妹子"
     }
     */


}
