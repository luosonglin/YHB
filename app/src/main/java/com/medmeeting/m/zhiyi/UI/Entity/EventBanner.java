package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 06/12/2017 4:12 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class EventBanner {


    private List<BannersBean> banners;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * id : 249
         * typeId : 1
         * createDate : 1512525463000
         * title : 会议
         * banner : http://on5q2lha8.bkt.clouddn.com/FnpbNYwveM81lX2XLJHfHOMw43df
         * url : http://localhost:8085/#/c/homePage/column/add
         * type : event
         * sort : 4
         * pushPlace : EVENT
         */

        private int id;
        private int typeId;
        private long createDate;
        private String title;
        private String banner;
        private String url;
        private String type;
        private int sort;
        private String pushPlace;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getPushPlace() {
            return pushPlace;
        }

        public void setPushPlace(String pushPlace) {
            this.pushPlace = pushPlace;
        }
    }
}
