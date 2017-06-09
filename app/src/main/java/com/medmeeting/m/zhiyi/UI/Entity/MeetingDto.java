package com.medmeeting.m.zhiyi.UI.Entity;

import java.util.List;

public class MeetingDto {


    /**
     * amtOrder : 0.0
     * pageInfo : {"list":[{"address":"上海龙之梦万丽酒店","banner":"57cb691c-a4ae-4482-914d-ff04265dc1e7.jpg","endDate":1512302400000,"eventType":"3","hot":"","id":218,"startDate":1512086400000,"title":"2017第二届全球精准医疗（中国）峰会"},{"address":"山东会议中心","banner":"9b2826f5-1841-406f-a032-8d947fa8f50e.jpg","endDate":1500116400000,"eventType":"1","hot":"","id":216,"startDate":1499904000000,"title":"2017中国儿科医师年会暨齐鲁儿童医学发展论坛"},{"address":"河南.郑州","banner":"42e66a23-cbf7-4820-a118-d15d7b2dd329.jpg","endDate":1497776400000,"eventType":"7","hot":"","id":256,"startDate":1497657600000,"title":"\u201c全国儿童康复护理新进展\u201d培训班-- 郑州大学第三附属医院   河南省妇幼保健院"},{"address":"亚洲海湾大酒店","banner":"813c5c34-3a16-4bc4-b94d-92c710e73ec0.png","endDate":1496570400000,"eventType":"3","hot":"","id":254,"startDate":1496534400000,"title":"海峡两岸泌尿外科新技术新进展高峰论坛"},{"address":"第二军医大学长海医院","banner":"b70d80a7-389e-4e3c-913a-2f55ed89d467.png","endDate":1495447200000,"eventType":"1","hot":"","id":217,"startDate":1494982800000,"title":"2017年第三届长海麻醉学术周"},{"address":"上海国际会议中心   滨江大道2727号","banner":"b9ad727a-9595-4cec-ba7b-080c5295fbe0.jpg","endDate":1496570400000,"eventType":"2","hot":"true","id":210,"startDate":1496365200000,"title":"红房子论坛暨第四届复旦大学附属妇产科医院国际妇产科高峰论坛"},{"address":"复旦大学附属眼耳鼻喉科医院","banner":"466374f7-0870-4c7f-9bfd-a368430b5c13.jpg","endDate":1495952880000,"eventType":"2","hot":"","id":180,"startDate":1493792820000,"title":"第三轮通知  2017上海耳鼻咽喉头颈外科国际论坛暨汾阳苑耳鼻咽喉头颈外科学术月"},{"address":"上海光大会展中心国际大酒店","banner":"870349c5-8916-40cf-a8ca-b18d457a8b4a.jpg","endDate":1491724800000,"eventType":"2","hot":"true","id":207,"startDate":1491613200000,"title":"2017 世界骨科创新大会暨展览会"},{"address":"复旦大学附属中山医院18号楼","banner":"79fa7e41-fc96-4c41-961f-05cc2aa74cbb.jpg","endDate":1491710400000,"eventType":"5","hot":"true","id":209,"startDate":1491541200000,"title":"2017上海国际消化内镜研讨会暨第十届中日ESD高峰论坛"},{"address":"香格里拉大酒店","banner":"a0941fe1-13e6-490c-a985-f8a3db568e5b.jpg","endDate":1489903680000,"eventType":"2","hot":"true","id":129,"startDate":1489795200000,"title":"第三届国际智慧医疗创新论坛暨智创奖颁奖盛典"},{"address":"蓝宫大饭店（上海市嘉定区博乐南路125号）","banner":"43a8652c-0585-4256-b19d-ce81002a16f8.jpg","endDate":1492250400000,"eventType":"1","hot":"true","id":206,"startDate":1492124400000,"title":"2017华夏足踝外科大会 暨第八届上海国际足踝外科高峰论坛"},{"address":"上海光大会展中心国际大酒店","banner":"84ffc12c-73c2-4d96-a33e-bdfcc368002e.png","endDate":1492941600000,"eventType":"5","hot":"","id":229,"startDate":1492768800000,"title":"2017心理创伤治疗眼动脱敏与再加工第三届亚洲会议"},{"address":"龙之梦万丽大酒店","banner":"cf98af9a-eeb3-4efc-a305-7ed3761a2c9b.jpg","endDate":1491202320000,"eventType":"1","hot":"true","id":194,"startDate":1490943120000,"title":"2017国际气道管理学会（IAMS）气道培训"}],"pageEnd":false,"pages":1,"total":13}
     * returnCode : success
     * returnMsg : 获取热门会议列表成功！
     * status : 200
     */

    private double amtOrder;
    private PageInfoBean pageInfo;
    private String returnCode;
    private String returnMsg;
    private String status;

    public double getAmtOrder() {
        return amtOrder;
    }

    public void setAmtOrder(double amtOrder) {
        this.amtOrder = amtOrder;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class PageInfoBean {
        /**
         * list : [{"address":"上海龙之梦万丽酒店","banner":"57cb691c-a4ae-4482-914d-ff04265dc1e7.jpg","endDate":1512302400000,"eventType":"3","hot":"","id":218,"startDate":1512086400000,"title":"2017第二届全球精准医疗（中国）峰会"},{"address":"山东会议中心","banner":"9b2826f5-1841-406f-a032-8d947fa8f50e.jpg","endDate":1500116400000,"eventType":"1","hot":"","id":216,"startDate":1499904000000,"title":"2017中国儿科医师年会暨齐鲁儿童医学发展论坛"},{"address":"河南.郑州","banner":"42e66a23-cbf7-4820-a118-d15d7b2dd329.jpg","endDate":1497776400000,"eventType":"7","hot":"","id":256,"startDate":1497657600000,"title":"\u201c全国儿童康复护理新进展\u201d培训班-- 郑州大学第三附属医院   河南省妇幼保健院"},{"address":"亚洲海湾大酒店","banner":"813c5c34-3a16-4bc4-b94d-92c710e73ec0.png","endDate":1496570400000,"eventType":"3","hot":"","id":254,"startDate":1496534400000,"title":"海峡两岸泌尿外科新技术新进展高峰论坛"},{"address":"第二军医大学长海医院","banner":"b70d80a7-389e-4e3c-913a-2f55ed89d467.png","endDate":1495447200000,"eventType":"1","hot":"","id":217,"startDate":1494982800000,"title":"2017年第三届长海麻醉学术周"},{"address":"上海国际会议中心   滨江大道2727号","banner":"b9ad727a-9595-4cec-ba7b-080c5295fbe0.jpg","endDate":1496570400000,"eventType":"2","hot":"true","id":210,"startDate":1496365200000,"title":"红房子论坛暨第四届复旦大学附属妇产科医院国际妇产科高峰论坛"},{"address":"复旦大学附属眼耳鼻喉科医院","banner":"466374f7-0870-4c7f-9bfd-a368430b5c13.jpg","endDate":1495952880000,"eventType":"2","hot":"","id":180,"startDate":1493792820000,"title":"第三轮通知  2017上海耳鼻咽喉头颈外科国际论坛暨汾阳苑耳鼻咽喉头颈外科学术月"},{"address":"上海光大会展中心国际大酒店","banner":"870349c5-8916-40cf-a8ca-b18d457a8b4a.jpg","endDate":1491724800000,"eventType":"2","hot":"true","id":207,"startDate":1491613200000,"title":"2017 世界骨科创新大会暨展览会"},{"address":"复旦大学附属中山医院18号楼","banner":"79fa7e41-fc96-4c41-961f-05cc2aa74cbb.jpg","endDate":1491710400000,"eventType":"5","hot":"true","id":209,"startDate":1491541200000,"title":"2017上海国际消化内镜研讨会暨第十届中日ESD高峰论坛"},{"address":"香格里拉大酒店","banner":"a0941fe1-13e6-490c-a985-f8a3db568e5b.jpg","endDate":1489903680000,"eventType":"2","hot":"true","id":129,"startDate":1489795200000,"title":"第三届国际智慧医疗创新论坛暨智创奖颁奖盛典"},{"address":"蓝宫大饭店（上海市嘉定区博乐南路125号）","banner":"43a8652c-0585-4256-b19d-ce81002a16f8.jpg","endDate":1492250400000,"eventType":"1","hot":"true","id":206,"startDate":1492124400000,"title":"2017华夏足踝外科大会 暨第八届上海国际足踝外科高峰论坛"},{"address":"上海光大会展中心国际大酒店","banner":"84ffc12c-73c2-4d96-a33e-bdfcc368002e.png","endDate":1492941600000,"eventType":"5","hot":"","id":229,"startDate":1492768800000,"title":"2017心理创伤治疗眼动脱敏与再加工第三届亚洲会议"},{"address":"龙之梦万丽大酒店","banner":"cf98af9a-eeb3-4efc-a305-7ed3761a2c9b.jpg","endDate":1491202320000,"eventType":"1","hot":"true","id":194,"startDate":1490943120000,"title":"2017国际气道管理学会（IAMS）气道培训"}]
         * pageEnd : false
         * pages : 1
         * total : 13
         */

        private boolean pageEnd;
        private int pages;
        private int total;
        private List<ListBean> list;

        public boolean isPageEnd() {
            return pageEnd;
        }

        public void setPageEnd(boolean pageEnd) {
            this.pageEnd = pageEnd;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * address : 上海龙之梦万丽酒店
             * banner : 57cb691c-a4ae-4482-914d-ff04265dc1e7.jpg
             * endDate : 1512302400000
             * eventType : 3
             * hot :
             * id : 218
             * startDate : 1512086400000
             * title : 2017第二届全球精准医疗（中国）峰会
             */

            private String address;
            private String banner;
            private long endDate;
            private String eventType;
            private String hot;
            private int id;
            private long startDate;
            private String title;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBanner() {
                return banner;
            }

            public void setBanner(String banner) {
                this.banner = banner;
            }

            public long getEndDate() {
                return endDate;
            }

            public void setEndDate(long endDate) {
                this.endDate = endDate;
            }

            public String getEventType() {
                return eventType;
            }

            public void setEventType(String eventType) {
                this.eventType = eventType;
            }

            public String getHot() {
                return hot;
            }

            public void setHot(String hot) {
                this.hot = hot;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getStartDate() {
                return startDate;
            }

            public void setStartDate(long startDate) {
                this.startDate = startDate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
