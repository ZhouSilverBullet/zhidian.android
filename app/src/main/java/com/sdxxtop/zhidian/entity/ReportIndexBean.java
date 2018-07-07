package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class ReportIndexBean extends BaseModel<ReportIndexBean.DataBean> {

    public static class DataBean {
        /**
         * review_num : 4
         * send_num : 0
         * num : 4
         * report : [{"report_id":9,"report_type":2,"userid":50001514,"report_date":"1000-01-01","report_month":"1000-01","start_date":"2018-06-25","end_date":"2018-06-30","add_time":"2018-06-25 11:39:39","name":"桐谷和人","img":"#d9d6ec","is_read":2},{"report_id":8,"report_type":3,"userid":50001514,"report_date":"1000-01-01","report_month":"2018-06","start_date":"1000-01-01","end_date":"1000-01-01","add_time":"2018-06-25 11:36:30","name":"桐谷和人","img":"#d9d6ec","is_read":2},{"report_id":7,"report_type":1,"userid":50001514,"report_date":"2018-12-01","report_month":"1000-01","start_date":"1000-01-01","end_date":"1000-01-01","add_time":"2018-06-25 11:35:59","name":"桐谷和人","img":"#d9d6ec","is_read":2},{"report_id":6,"report_type":1,"userid":50001514,"report_date":"2018-06-25","report_month":"1000-01","start_date":"1000-01-01","end_date":"1000-01-01","add_time":"2018-06-25 11:33:11","name":"桐谷和人","img":"#d9d6ec","is_read":2}]
         */

        private int review_num;
        private int send_num;
        private int num;
        private List<ReportBean> report;

        public int getReview_num() {
            return review_num;
        }

        public void setReview_num(int review_num) {
            this.review_num = review_num;
        }

        public int getSend_num() {
            return send_num;
        }

        public void setSend_num(int send_num) {
            this.send_num = send_num;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public List<ReportBean> getReport() {
            return report;
        }

        public void setReport(List<ReportBean> report) {
            this.report = report;
        }

        public static class ReportBean {
            /**
             * report_id : 9
             * report_type : 2
             * userid : 50001514
             * report_date : 1000-01-01
             * report_month : 1000-01
             * start_date : 2018-06-25
             * end_date : 2018-06-30
             * add_time : 2018-06-25 11:39:39
             * name : 桐谷和人
             * img : #d9d6ec
             * is_read : 2
             */

            private int report_id;
            private int report_type;
            private int userid;
            private String report_date;
            private String report_month;
            private String start_date;
            private String end_date;
            private String add_time;
            private String name;
            private String img;
            private int is_read;

            public int getReport_id() {
                return report_id;
            }

            public void setReport_id(int report_id) {
                this.report_id = report_id;
            }

            public int getReport_type() {
                return report_type;
            }

            public void setReport_type(int report_type) {
                this.report_type = report_type;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getReport_date() {
                return report_date;
            }

            public void setReport_date(String report_date) {
                this.report_date = report_date;
            }

            public String getReport_month() {
                return report_month;
            }

            public void setReport_month(String report_month) {
                this.report_month = report_month;
            }

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getIs_read() {
                return is_read;
            }

            public void setIs_read(int is_read) {
                this.is_read = is_read;
            }
        }
    }
}
