package com.medmeeting.m.zhiyi.UI.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * —————————————————————–
 * Copyright (C) 2016，上海知醫有限公司， All rights reserved.
 * —————————————————————–
 * Created by luosonglin on 06/12/2016.
 * —————————————————————–
 * (Feature)
 * —————————————————————–
 */

public class BlogDto {

    /**
     * msg : success
     * blog : {"pageNum":0,"pageSize":0,"size":3,"startRow":1,"endRow":3,"total":3,"pages":0,"list":[{"id":7,"userId":7,"title":"周六打球","content":"冠亚季军，就是我们！哈哈哈哈哈","commentCount":9,"likeCount":5,"createdAt":1482222369000,"deletedAt":null,"tagId":null,"isHot":1,"images":"http://wx2.sinaimg.cn/mw1024/a601622bly1fat2boqnsvj20zk0qojwx0.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"},{"id":3,"userId":7,"title":"hahah","content":"微博feed系统架构的起点","commentCount":0,"likeCount":1,"createdAt":1480387445000,"deletedAt":null,"tagId":null,"isHot":1,"images":"http://ww4.sinaimg.cn/mw1024/a601622bgw1f42dva8tt5j20qo0qotc2.jpg;http://ww1.sinaimg.cn/mw1024/a601622bgw1eyo6z2u77lj20p018g44b.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"},{"id":1,"userId":7,"title":"NapoleonRohaha_Songlin","content":"既然Instagram 13个人创造出10亿美金的产品，\n既然Whatsapp 50个人130亿美金，\n为何一个人不能攻下千万市值的产品呢？！\n","commentCount":18,"likeCount":2,"createdAt":1480323830000,"deletedAt":null,"tagId":"0,2,3","isHot":1,"images":"http://ww3.sinaimg.cn/mw1024/a601622bgw1fa1c5g33zwj20zk0qoq9k.jpg;http://wx4.sinaimg.cn/mw1024/a601622bgy1fcf5pqtsozj23oq2f8b2b.jpg;http://wx3.sinaimg.cn/mw1024/a601622bly1fbtdzwas60j20dw099755.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"}],"prePage":0,"nextPage":0,"isFirstPage":false,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[],"navigateFirstPage":0,"navigateLastPage":0,"firstPage":0,"lastPage":0}
     */

    private String msg;
    private BlogBean blog;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BlogBean getBlog() {
        return blog;
    }

    public void setBlog(BlogBean blog) {
        this.blog = blog;
    }

    public static class BlogBean {
        /**
         * pageNum : 0
         * pageSize : 0
         * size : 3
         * startRow : 1
         * endRow : 3
         * total : 3
         * pages : 0
         * list : [{"id":7,"userId":7,"title":"周六打球","content":"冠亚季军，就是我们！哈哈哈哈哈","commentCount":9,"likeCount":5,"createdAt":1482222369000,"deletedAt":null,"tagId":null,"isHot":1,"images":"http://wx2.sinaimg.cn/mw1024/a601622bly1fat2boqnsvj20zk0qojwx0.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"},{"id":3,"userId":7,"title":"hahah","content":"微博feed系统架构的起点","commentCount":0,"likeCount":1,"createdAt":1480387445000,"deletedAt":null,"tagId":null,"isHot":1,"images":"http://ww4.sinaimg.cn/mw1024/a601622bgw1f42dva8tt5j20qo0qotc2.jpg;http://ww1.sinaimg.cn/mw1024/a601622bgw1eyo6z2u77lj20p018g44b.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"},{"id":1,"userId":7,"title":"NapoleonRohaha_Songlin","content":"既然Instagram 13个人创造出10亿美金的产品，\n既然Whatsapp 50个人130亿美金，\n为何一个人不能攻下千万市值的产品呢？！\n","commentCount":18,"likeCount":2,"createdAt":1480323830000,"deletedAt":null,"tagId":"0,2,3","isHot":1,"images":"http://ww3.sinaimg.cn/mw1024/a601622bgw1fa1c5g33zwj20zk0qoq9k.jpg;http://wx4.sinaimg.cn/mw1024/a601622bgy1fcf5pqtsozj23oq2f8b2b.jpg;http://wx3.sinaimg.cn/mw1024/a601622bly1fbtdzwas60j20dw099755.jpg","name":"罗崧麟","nickName":"医宝123644","company":"草帽海贼团","userPic":"http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg","authenStatus":"A"}]
         * prePage : 0
         * nextPage : 0
         * isFirstPage : false
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : []
         * navigateFirstPage : 0
         * navigateLastPage : 0
         * firstPage : 0
         * lastPage : 0
         */

        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int firstPage;
        private int lastPage;
        private List<ListBean> list;
        private List<?> navigatepageNums;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<?> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<?> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean implements Serializable {
            /**
             * id : 7
             * userId : 7
             * title : 周六打球
             * content : 冠亚季军，就是我们！哈哈哈哈哈
             * commentCount : 9
             * likeCount : 5
             * createdAt : 1482222369000
             * deletedAt : null
             * tagId : null
             * isHot : 1
             * images : http://wx2.sinaimg.cn/mw1024/a601622bly1fat2boqnsvj20zk0qojwx0.jpg
             * name : 罗崧麟
             * nickName : 医宝123644
             * company : 草帽海贼团
             * userPic : http://oimas2nso.bkt.clouddn.com/webwxgeticon.jpeg
             * authenStatus : A
             */

            private int id;
            private int userId;
            private String title;
            private String content;
            private int commentCount;
            private int likeCount;
            private long createdAt;
            private Object deletedAt;
            private Object tagId;
            private int isHot;
            private String images;
            private String name;
            private String nickName;
            private String company;
            private String userPic;
            private String authenStatus;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public long getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(long createdAt) {
                this.createdAt = createdAt;
            }

            public Object getDeletedAt() {
                return deletedAt;
            }

            public void setDeletedAt(Object deletedAt) {
                this.deletedAt = deletedAt;
            }

            public Object getTagId() {
                return tagId;
            }

            public void setTagId(Object tagId) {
                this.tagId = tagId;
            }

            public int getIsHot() {
                return isHot;
            }

            public void setIsHot(int isHot) {
                this.isHot = isHot;
            }

            public String getImages() {
                return images + "?imageMogr/v2/thumbnail/540x540";
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getUserPic() {
                return userPic + "?imageMogr/v2/thumbnail/120x120";
            }

            public void setUserPic(String userPic) {
                this.userPic = userPic;
            }

            public String getAuthenStatus() {
                return authenStatus;
            }

            public void setAuthenStatus(String authenStatus) {
                this.authenStatus = authenStatus;
            }
        }
    }
}
