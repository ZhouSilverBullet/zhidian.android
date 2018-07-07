package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class SelectionDateBean extends BaseModel<SelectionDateBean.DataBean> {

    public static class DataBean {
        private String class_name;

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        private List<MainIndexBean.DataBean.SignLogBean> sign_log;

        public List<MainIndexBean.DataBean.SignLogBean> getSign_log() {
            return sign_log;
        }

        public void setSign_log(List<MainIndexBean.DataBean.SignLogBean> sign_log) {
            this.sign_log = sign_log;
        }

//        public static class SignLogBean implements Serializable {
//            /**
//             * sign_id : 1
//             * sign_name : 上班
//             * sign_time : 09:09
//             * sys_date : 09:00
//             * minute : 9
//             * status : 5
//             */
//
//            private int sign_id;
//            private String sign_name;
//            private String sign_time;
//            private String sys_date;
//            private int minute;
//            private int status;
//            private boolean isSelector;
//
//            public int getSign_id() {
//                return sign_id;
//            }
//
//            public void setSign_id(int sign_id) {
//                this.sign_id = sign_id;
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
//            public String getSign_time() {
//                return sign_time;
//            }
//
//            public void setSign_time(String sign_time) {
//                this.sign_time = sign_time;
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
//            public int getMinute() {
//                return minute;
//            }
//
//            public void setMinute(int minute) {
//                this.minute = minute;
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
//            public boolean isSelector() {
//                return isSelector;
//            }
//
//            public void setSelector(boolean selector) {
//                isSelector = selector;
//            }
//        }
    }
}
