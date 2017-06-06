package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class BannerInfo {

    /**
     * code : 200
     * data : {"banners":[{"id":180,"title":"2017上海耳鼻咽喉头颈外科国际论坛暨汾阳苑耳鼻咽喉头颈外科学术月","banner":"466374f7-0870-4c7f-9bfd-a368430b5c13.jpg","startDate":1493792820000,"endDate":1495952880000},{"id":210,"title":"红房子论坛\u2014\u2014暨第四届复旦大学附属妇产科医院国际妇产科高峰论坛","banner":"b9ad727a-9595-4cec-ba7b-080c5295fbe0.jpg","startDate":1496365200000,"endDate":1496570400000},{"id":218,"title":"2017第二届全球精准医疗（中国）峰会","banner":"57cb691c-a4ae-4482-914d-ff04265dc1e7.jpg","startDate":1512086400000,"endDate":1512302400000}]}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
}
