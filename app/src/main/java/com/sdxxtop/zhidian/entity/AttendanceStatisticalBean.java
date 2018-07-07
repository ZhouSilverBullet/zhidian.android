package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/14.
 */

public class AttendanceStatisticalBean extends BaseModel<AttendanceStatisticalBean.DataBean> {

    public static class DataBean {
        /**
         * work_day : 4天
         * bus_day : 3天4小时
         * overtime_day : 34天3小时17分钟
         * leave_day : 5天22小时
         * late_num :
         * early_num :
         * leak_num :
         * absent_day : 1次
         * out_num : 1次
         */

        private String work_day;
        private String bus_day;
        private String over_time;
        private String leave_day;
        private String late_num;
        private String early_num;
        private String leak_num;
        private String absent_day;
        private String out_num;

        public String getWork_day() {
            return work_day;
        }

        public void setWork_day(String work_day) {
            this.work_day = work_day;
        }

        public String getBus_day() {
            return bus_day;
        }

        public void setBus_day(String bus_day) {
            this.bus_day = bus_day;
        }

        public String getOvertime() {
            return over_time;
        }

        public void setOvertime(String overtime) {
            this.over_time = overtime;
        }

        public String getLeave_day() {
            return leave_day;
        }

        public void setLeave_day(String leave_day) {
            this.leave_day = leave_day;
        }

        public String getLate_num() {
            return late_num;
        }

        public void setLate_num(String late_num) {
            this.late_num = late_num;
        }

        public String getEarly_num() {
            return early_num;
        }

        public void setEarly_num(String early_num) {
            this.early_num = early_num;
        }

        public String getLeak_num() {
            return leak_num;
        }

        public void setLeak_num(String leak_num) {
            this.leak_num = leak_num;
        }

        public String getAbsent_day() {
            return absent_day;
        }

        public void setAbsent_day(String absent_day) {
            this.absent_day = absent_day;
        }

        public String getOut_num() {
            return out_num;
        }

        public void setOut_num(String out_num) {
            this.out_num = out_num;
        }
    }
}
