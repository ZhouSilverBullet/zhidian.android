package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class WorkListBean extends BaseModel<WorkListBean.DataBean> {

    public static class DataBean {
        /**
         * report : [{"report_id":17,"report_type":3,"userid":50001514,"add_time":"2018-06-25 23:05:54","summary":"得的啊","plan":"涂涂乐","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":16,"report_type":1,"userid":50001514,"add_time":"2018-06-25 23:04:57","summary":"斤斤计较","plan":"默默哦","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":15,"report_type":3,"userid":50001514,"add_time":"2018-06-25 22:02:23","summary":"贾就近近景近景5句兔兔亭吉路口啊兔兔突突突18888888855","plan":"考虑考虑看看了","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":9,"report_type":2,"userid":50001514,"add_time":"2018-06-25 11:39:39","summary":"斤斤计较","plan":"屠龙记","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":8,"report_type":3,"userid":50001514,"add_time":"2018-06-25 11:36:30","summary":"阿坝","plan":"斤斤计较","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":7,"report_type":1,"userid":50001514,"add_time":"2018-06-25 11:35:59","summary":"得得","plan":"金家街","problem":"","name":"桐谷和人","img":"#d9d6ec"},{"report_id":6,"report_type":1,"userid":50001514,"add_time":"2018-06-25 11:33:11","summary":"监控","plan":"金家街","problem":"","name":"桐谷和人","img":"#d9d6ec"}]
         * num : 7
         */

        private int num;
        private List<ReportBean> report;

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

            private int report_id;
            private int report_type;
            private int userid;
            private String add_time;
            private String summary;
            private String plan;
            private String problem;
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

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
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
