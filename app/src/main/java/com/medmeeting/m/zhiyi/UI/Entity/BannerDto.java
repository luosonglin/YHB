package com.medmeeting.m.zhiyi.UI.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * test
 */
public class BannerDto implements Serializable {

    private List<BannersBean> banners;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * id : 180
         * title : 2017上海耳鼻咽喉头颈外科国际论坛暨汾阳苑耳鼻咽喉头颈外科学术月
         * banner : 466374f7-0870-4c7f-9bfd-a368430b5c13.jpg
         * startDate : 1493792820000
         * endDate : 1495952880000
         */

        private int id;
        private String title;
        private String banner;
        private long startDate;
        private long endDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }
    }
}
