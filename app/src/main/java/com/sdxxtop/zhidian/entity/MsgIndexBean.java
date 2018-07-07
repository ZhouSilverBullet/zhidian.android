package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/14.
 */

public class MsgIndexBean extends BaseModel<MsgIndexBean.DataBean> {

    public static class DataBean {
        /**
         * apply : {"title":"lizhaobin的集体请假申请","update_time":"2018-05-12 20:53:24","apply_num":1}
         * notice : {"title":"不我","update_time":"2018-05-11 17:46:46","notice_num":2}
         */

        private ApplyBean apply;
        private NoticeBean notice;
        private ReportBean report;

        public ApplyBean getApply() {
            return apply;
        }

        public void setApply(ApplyBean apply) {
            this.apply = apply;
        }

        public NoticeBean getNotice() {
            return notice;
        }

        public void setNotice(NoticeBean notice) {
            this.notice = notice;
        }

        public ReportBean getReport() {
            return report;
        }

        public void setReport(ReportBean report) {
            this.report = report;
        }

        public static class ApplyBean {
            /**
             * title : lizhaobin的集体请假申请
             * update_time : 2018-05-12 20:53:24
             * apply_num : 1
             */

            private String title;
            private String update_time;
            private int apply_num;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getApply_num() {
                return apply_num;
            }

            public void setApply_num(int apply_num) {
                this.apply_num = apply_num;
            }
        }

        public static class NoticeBean {
            /**
             * title : 不我
             * update_time : 2018-05-11 17:46:46
             * notice_num : 2
             */

            private String title;
            private String update_time;
            private int notice_num;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getNotice_num() {
                return notice_num;
            }

            public void setNotice_num(int notice_num) {
                this.notice_num = notice_num;
            }
        }

        public static class ReportBean {
            /**
             * title : 不我
             * update_time : 2018-05-11 17:46:46
             * notice_num : 2
             */

            private String title;
            private String update_time;
            private int report_num;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getReport_num() {
                return report_num;
            }

            public void setReport_num(int report_num) {
                this.report_num = report_num;
            }
        }
    }
}
