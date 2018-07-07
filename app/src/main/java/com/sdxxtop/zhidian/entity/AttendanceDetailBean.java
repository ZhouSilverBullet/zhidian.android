package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class AttendanceDetailBean extends BaseModel<AttendanceDetailBean.DataBean> {

    public static class DataBean {
        private List<SignLogBean> sign_log;

        public List<SignLogBean> getSign_log() {
            return sign_log;
        }

        public void setSign_log(List<SignLogBean> sign_log) {
            this.sign_log = sign_log;
        }

        public static class SignLogBean {
            /**
             * date : 05-31
             * week : 4
             * sign_log :
             * desc :
             */

            private String date;
            private String week;
            private String sign_log;
            private String desc;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getSign_log() {
                return sign_log;
            }

            public void setSign_log(String sign_log) {
                this.sign_log = sign_log;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
