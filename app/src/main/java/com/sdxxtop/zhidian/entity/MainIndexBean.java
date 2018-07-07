package com.sdxxtop.zhidian.entity;

import android.text.TextUtils;

import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.utils.DateUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class MainIndexBean extends BaseModel<MainIndexBean.DataBean> {

    public static class DataBean {
        /**
         * company_name : 北海幼儿园
         * msg_num : 1
         * is_leak : 1
         * sign_num : 2
         * sign_name : 上班,下班
         * sign_date : 2018-05-03 9:00:00,2018-05-03 18:00:00
         * sign_time : 30
         * is_rest : 0
         * is_need : 1
         * apply : []
         * abnormal : ["2018-04-30","2018-05-02","2018-05-03"]
         * sign_log : [{"sign_id":577,"sign_time":"2018-05-03 09:00:00","status":8,"img":"","sign_data":"","sys_date":"9:00","sign_name":"上班","is_out":2}]
         */

        private String company_name;
        private int msg_num;
        private int is_leak;
        private int sign_num;
        private String sign_name;
        private String sign_date;
        private int sign_time;
        private int is_rest;
        private int is_need;
        private int remind_min;
        private int is_reg_face;
        private ApplyBean apply;
        private List<String> abnormal;
        private List<SignLogBean> sign_log;
        private int work_type;
        private boolean isFirst;

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public int getMsg_num() {
            return msg_num;
        }

        public void setMsg_num(int msg_num) {
            this.msg_num = msg_num;
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

        public ApplyBean getApply() {
            return apply;
        }

        public void setApply(ApplyBean apply) {
            this.apply = apply;
        }

        public List<String> getAbnormal() {
            return abnormal;
        }

        public void setAbnormal(List<String> abnormal) {
            this.abnormal = abnormal;
        }

        public List<SignLogBean> getSign_log() {
            return sign_log;
        }

        public void setSign_log(List<SignLogBean> sign_log) {
            this.sign_log = sign_log;
        }

        public int getRemind_min() {
            return remind_min;
        }

        public void setRemind_min(int remind_min) {
            this.remind_min = remind_min;
        }

        public int getIs_reg_face() {
            return is_reg_face;
        }

        public void setIs_reg_face(int is_reg_face) {
            this.is_reg_face = is_reg_face;
        }

        public void setIsFirst(boolean isFirst) {
            this.isFirst = isFirst;
        }

        public boolean isFirst() {
            return isFirst;
        }

        public int getWork_type() {
            return work_type;
        }

        public void setWork_type(int work_type) {
            this.work_type = work_type;
        }

        public static class SignLogBean implements Serializable {
            /**
             * sign_id : 577
             * sign_time : 2018-05-03 09:00:00
             * status : 8
             * img :
             * sign_data :
             * sys_date : 9:00
             * sign_name : 上班
             * is_out : 2
             */

            private String img;
            private String sign_data;
            private int is_out;
            //漏打卡标志，客户端自己加入的
            private boolean leakCard;
            private int sign_id;
            private String sign_name;
            private String sign_time;
            private String sys_date;
            private int minute;
            private int status;
            private boolean isSelector;
            //是否已经比较过了
            private boolean isCompare;

            public int getSign_id() {
                return sign_id;
            }

            public void setSign_id(int sign_id) {
                this.sign_id = sign_id;
            }

            public String getSign_time() {
                return sign_time;
            }

            public void setSign_time(String sign_time) {
                this.sign_time = sign_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getSign_data() {
                return sign_data;
            }

            public void setSign_data(String sign_data) {
                this.sign_data = sign_data;
            }

            public String getSys_date() {
                return sys_date;
            }

            public void setSys_date(String sys_date) {
                this.sys_date = sys_date;
            }

            public String getSign_name() {
                return sign_name;
            }

            public void setSign_name(String sign_name) {
                this.sign_name = sign_name;
            }

            public int getIs_out() {
                return is_out;
            }

            public void setIs_out(int is_out) {
                this.is_out = is_out;
            }

            public boolean isLeakCard() {
                return leakCard;
            }

            public void setLeakCard(boolean leakCard) {
                this.leakCard = leakCard;
            }

            public int getMinute() {
                if (minute == 0) {
                    long sysLong = DateUtil.convertTimeToLong(sys_date, "HH:mm");
                    String tempSignTime = "";
                    if (!TextUtils.isEmpty(sign_time)) {
                        String[] split = sign_time.split(" ");
                        if (split.length == 2) {
                            tempSignTime = split[1];
                        } else {
                            tempSignTime = sign_time;
                        }
                    } else {
                        tempSignTime = sign_time;
                    }
                    long signTime = DateUtil.convertTimeToLong(tempSignTime, "HH:mm:ss");
                    int l = (int) (Math.abs(sysLong - signTime) / (1000 * 60.0) + 1);
                    return l;
                }
                return minute;
            }

            public void setMinute(int minute) {
                this.minute = minute;
            }

            public boolean isSelector() {
                return isSelector;
            }

            public void setSelector(boolean selector) {
                isSelector = selector;
            }

            public boolean isCompare() {
                return isCompare;
            }

            public void setCompare(boolean compare) {
                isCompare = compare;
            }
        }

        public static class ApplyBean {
            private int apply_type;
            private String start_time;
            private String end_time;

            public int getApply_type() {
                return apply_type;
            }

            public void setApply_type(int apply_type) {
                this.apply_type = apply_type;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }
        }
    }
}
