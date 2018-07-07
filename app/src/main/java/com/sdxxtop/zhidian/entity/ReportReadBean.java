package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class ReportReadBean extends BaseModel<ReportReadBean.DataBean> {

    public static class DataBean {

        private ReportBean report;
        private int comment_num;
        private int is_comment;
        private List<CommentBean> comment;

        public ReportBean getReport() {
            return report;
        }

        public void setReport(ReportBean report) {
            this.report = report;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public int getIs_comment() {
            return is_comment;
        }

        public void setIs_comment(int is_comment) {
            this.is_comment = is_comment;
        }

        public static class ReportBean {
            /**
             * report_id : 9
             * report_type : 2
             * userid : 50001514
             * company_id : 1000078
             * summary : 斤斤计较
             * plan : 屠龙记
             * problem :
             * reviewer_id : 50001369,50001514,50001518
             * send_id :
             * report_month : 1000-01
             * report_date : 1000-01-01
             * start_date : 2018-06-25
             * end_date : 2018-06-30
             * add_time : 2018-06-25 11:39:39
             * update_time : 2018-06-25 11:39:39
             * name : 桐谷和人
             * reviewer_name : 王超,桐谷和人,白地市伯伯
             */

            private int report_id;
            private int report_type;
            private int userid;
            private int company_id;
            private String summary;
            private String plan;
            private String problem;
            private String reviewer_id;
            private String send_id;
            private String report_month;
            private String report_date;
            private String start_date;
            private String end_date;
            private String add_time;
            private String update_time;
            private String name;
            private String reviewer_name;
            private String send_name;

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

            public int getCompany_id() {
                return company_id;
            }

            public void setCompany_id(int company_id) {
                this.company_id = company_id;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getPlan() {
                return plan;
            }

            public void setPlan(String plan) {
                this.plan = plan;
            }

            public String getProblem() {
                return problem;
            }

            public void setProblem(String problem) {
                this.problem = problem;
            }

            public String getReviewer_id() {
                return reviewer_id;
            }

            public void setReviewer_id(String reviewer_id) {
                this.reviewer_id = reviewer_id;
            }

            public String getSend_id() {
                return send_id;
            }

            public void setSend_id(String send_id) {
                this.send_id = send_id;
            }

            public String getReport_month() {
                return report_month;
            }

            public void setReport_month(String report_month) {
                this.report_month = report_month;
            }

            public String getReport_date() {
                return report_date;
            }

            public void setReport_date(String report_date) {
                this.report_date = report_date;
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

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getReviewer_name() {
                return reviewer_name;
            }

            public void setReviewer_name(String reviewer_name) {
                this.reviewer_name = reviewer_name;
            }

            public String getSend_name() {
                return send_name;
            }

            public void setSend_name(String send_name) {
                this.send_name = send_name;
            }
        }

        public static class CommentBean {
            /**
             * comment_id : 8
             * report_id : 9
             * parent_id : 0
             * userid : 50001514
             * comment : 恐龙妹
             * com_userid : 0
             * add_time : 2018-06-25 16:10:23
             * name : 桐谷和人
             * img : #d9d6ec
             * com_name :
             */

            private int comment_id;
            private int report_id;
            private int parent_id;
            private int userid;
            private String comment;
            private int com_userid;
            private String add_time;
            private String name;
            private String img;
            private String com_name;

            public int getComment_id() {
                return comment_id;
            }

            public void setComment_id(int comment_id) {
                this.comment_id = comment_id;
            }

            public int getReport_id() {
                return report_id;
            }

            public void setReport_id(int report_id) {
                this.report_id = report_id;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public int getCom_userid() {
                return com_userid;
            }

            public void setCom_userid(int com_userid) {
                this.com_userid = com_userid;
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

            public String getCom_name() {
                return com_name;
            }

            public void setCom_name(String com_name) {
                this.com_name = com_name;
            }
        }
    }
}
