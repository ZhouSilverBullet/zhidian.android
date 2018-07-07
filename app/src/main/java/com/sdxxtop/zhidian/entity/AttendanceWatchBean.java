package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class AttendanceWatchBean extends BaseModel<AttendanceWatchBean.DataBean> {

    public static class DataBean {
        /**
         * is_leak : 2
         * sign_num : 4
         * sign_name : 上班,下班,上班,下班
         * sign_date : 2018-05-14 09:00:00,2018-05-14 12:30:00,2018-05-14 15:00:00,2018-05-14 18:30:00
         * sign_time : 60
         * is_rest : 0
         * is_need : 1
         * apply : {}
         * abnormal : ["2018-04-09","2018-04-12","2018-04-13","2018-04-14","2018-04-16","2018-04-17","2018-04-18","2018-04-24","2018-04-25","2018-04-26","2018-04-27","2018-04-28","2018-04-29","2018-04-30"]
         * sign_log : [{"sign_id":2669,"sign_time":"2018-04-30 10:00:00","status":5,"img":"","sign_data":"","sys_date":"09:30","sign_name":"上班","is_out":2},{"sign_id":2839,"sign_time":"2018-04-30 12:10:00","status":6,"img":"","sign_data":"","sys_date":"12:30","sign_name":"下班","is_out":2},{"sign_id":3161,"sign_time":"2018-04-30 15:40:00","status":5,"img":"","sign_data":"","sys_date":"15:20","sign_name":"上班","is_out":2},{"sign_id":3162,"sign_time":"2018-04-30 18:30:00","status":6,"img":"","sign_data":"","sys_date":"18:20","sign_name":"下班","is_out":2}]
         * android_class_date : [{"short_name":"数","color":"#ffff00","class_date":"2018-04-29"},{"short_name":"数","color":"#ffff00","class_date":"2018-04-30"}]
         * class_date : {"2018-04-29":{"short_name":"数","color":"#ffff00","class_date":"2018-04-29"},"2018-04-30":{"short_name":"数","color":"#ffff00","class_date":"2018-04-30"}}
         * holiday : ["2018-04-05","2018-04-06","2018-04-07","2018-04-29","2018-04-30"]
         * rule : {"rule_name":"默认规则","class_id":9,"is_default":1,"work_week_day":"1,2,3,4,5","work_holiday":"","short_name":"白","color":"#1E9FFF"}
         */

        private int is_leak;
        private int sign_num;
        private String sign_name;
        private String sign_date;
        private int sign_time;
        private int is_rest;
        private int is_need;
        private MainIndexBean.DataBean.ApplyBean apply;
        private RuleBean rule;
        private List<String> abnormal;
        private List<String> out_date;
        private List<MainIndexBean.DataBean.SignLogBean> sign_log;
        private List<AndroidClassDateBean> android_class_date;
        private List<String> holiday;

        public List<String> getOut_date() {
            return out_date;
        }

        public void setOut_date(List<String> out_date) {
            this.out_date = out_date;
        }

        public int getIs_leak() {
            return is_leak;
        }

        public void setIs_leak(int is_leak) {
            this.is_leak = is_leak;
        }

        public int getSign_num() {
            return sign_num;
        }

        public void setSign_num(int sign_num) {
            this.sign_num = sign_num;
        }

        public String getSign_name() {
            return sign_name;
        }

        public void setSign_name(String sign_name) {
            this.sign_name = sign_name;
        }

        public String getSign_date() {
            return sign_date;
        }

        public void setSign_date(String sign_date) {
            this.sign_date = sign_date;
        }

        public int getSign_time() {
            return sign_time;
        }

        public void setSign_time(int sign_time) {
            this.sign_time = sign_time;
        }

        public int getIs_rest() {
            return is_rest;
        }

        public void setIs_rest(int is_rest) {
            this.is_rest = is_rest;
        }

        public int getIs_need() {
            return is_need;
        }

        public void setIs_need(int is_need) {
            this.is_need = is_need;
        }

        public MainIndexBean.DataBean.ApplyBean getApply() {
            return apply;
        }

        public void setApply(MainIndexBean.DataBean.ApplyBean apply) {
            this.apply = apply;
        }

        public RuleBean getRule() {
            return rule;
        }

        public void setRule(RuleBean rule) {
            this.rule = rule;
        }

        public List<String> getAbnormal() {
            return abnormal;
        }

        public void setAbnormal(List<String> abnormal) {
            this.abnormal = abnormal;
        }

        public List<MainIndexBean.DataBean.SignLogBean> getSign_log() {
            return sign_log;
        }

        public void setSign_log(List<MainIndexBean.DataBean.SignLogBean> sign_log) {
            this.sign_log = sign_log;
        }

        public List<AndroidClassDateBean> getAndroid_class_date() {
            return android_class_date;
        }

        public void setAndroid_class_date(List<AndroidClassDateBean> android_class_date) {
            this.android_class_date = android_class_date;
        }

        public List<String> getHoliday() {
            return holiday;
        }

        public void setHoliday(List<String> holiday) {
            this.holiday = holiday;
        }

//        public static class ApplyBean {
//            private String apply_type;
//            private String start_time;
//            private String end_time;
//
//            public String getApply_type() {
//                return apply_type;
//            }
//
//            public void setApply_type(String apply_type) {
//                this.apply_type = apply_type;
//            }
//
//            public String getStart_time() {
//                return start_time;
//            }
//
//            public void setStart_time(String start_time) {
//                this.start_time = start_time;
//            }
//
//            public String getEnd_time() {
//                return end_time;
//            }
//
//            public void setEnd_time(String end_time) {
//                this.end_time = end_time;
//            }
//        }

        public static class RuleBean {
            /**
             * rule_name : 默认规则
             * class_id : 9
             * is_default : 1
             * work_week_day : 1,2,3,4,5
             * work_holiday :
             * short_name : 白
             * color : #1E9FFF
             */

            private String rule_name;
            private int class_id;
            private int is_default;
            private String work_week_day;
            private String work_holiday;
            private String short_name;
            private String color;

            public String getRule_name() {
                return rule_name;
            }

            public void setRule_name(String rule_name) {
                this.rule_name = rule_name;
            }

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public int getIs_default() {
                return is_default;
            }

            public void setIs_default(int is_default) {
                this.is_default = is_default;
            }

            public String getWork_week_day() {
                return work_week_day;
            }

            public void setWork_week_day(String work_week_day) {
                this.work_week_day = work_week_day;
            }

            public String getWork_holiday() {
                return work_holiday;
            }

            public void setWork_holiday(String work_holiday) {
                this.work_holiday = work_holiday;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

//        public static class SignLogBean {
//            /**
//             * sign_id : 2669
//             * sign_time : 2018-04-30 10:00:00
//             * status : 5
//             * img :
//             * sign_data :
//             * sys_date : 09:30
//             * sign_name : 上班
//             * is_out : 2
//             */
//
//            private int sign_id;
//            private String sign_time;
//            private int status;
//            private String img;
//            private String sign_data;
//            private String sys_date;
//            private String sign_name;
//            private int is_out;
//            //漏打卡标志，客户端自己加入的
//            private boolean leakCard;
//
//            public int getSign_id() {
//                return sign_id;
//            }
//
//            public void setSign_id(int sign_id) {
//                this.sign_id = sign_id;
//            }
//
//            public String getSign_time() {
//                return sign_time;
//            }
//
//            public void setSign_time(String sign_time) {
//                this.sign_time = sign_time;
//            }
//
//            public int getStatus() {
//                return status;
//            }
//
//            public void setStatus(int status) {
//                this.status = status;
//            }
//
//            public String getImg() {
//                return img;
//            }
//
//            public void setImg(String img) {
//                this.img = img;
//            }
//
//            public String getSign_data() {
//                return sign_data;
//            }
//
//            public void setSign_data(String sign_data) {
//                this.sign_data = sign_data;
//            }
//
//            public String getSys_date() {
//                return sys_date;
//            }
//
//            public void setSys_date(String sys_date) {
//                this.sys_date = sys_date;
//            }
//
//            public String getSign_name() {
//                return sign_name;
//            }
//
//            public void setSign_name(String sign_name) {
//                this.sign_name = sign_name;
//            }
//
//            public int getIs_out() {
//                return is_out;
//            }
//
//            public void setIs_out(int is_out) {
//                this.is_out = is_out;
//            }
//
//            public boolean isLeakCard() {
//                return leakCard;
//            }
//
//            public void setLeakCard(boolean leakCard) {
//                this.leakCard = leakCard;
//            }
//        }

        public static class AndroidClassDateBean {
            /**
             * short_name : 数
             * color : #ffff00
             * class_date : 2018-04-29
             */

            private String short_name;
            private String color;
            private String class_date;

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getClass_date() {
                return class_date;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }
        }
    }
}
