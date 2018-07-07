package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

/**
 * Created by Administrator on 2018/5/23.
 */

public class StatPeopleBean extends BaseModel<StatPeopleBean.DataBean> {

    public static class DataBean {
        /**
         * total_num : 73
         * true_num : 2
         * not_num : 71
         * late_num : 0
         * early_num : 0
         * absent_num : 0
         * out_num : 0
         * overtime_num : 1
         * leave_num : 0
         * bus_num : 0
         */

        private int total_num;
        private int true_num;
        private int not_num;
        private int late_num;
        private int early_num;
        private int absent_num;
        private int out_num;
        private int overtime_num;
        private int leave_num;
        private int bus_num;

        public int getTotal_num() {
            return total_num;
        }

        public void setTotal_num(int total_num) {
            this.total_num = total_num;
        }

        public int getTrue_num() {
            return true_num;
        }

        public void setTrue_num(int true_num) {
            this.true_num = true_num;
        }

        public int getNot_num() {
            return not_num;
        }

        public void setNot_num(int not_num) {
            this.not_num = not_num;
        }

        public int getLate_num() {
            return late_num;
        }

        public void setLate_num(int late_num) {
            this.late_num = late_num;
        }

        public int getEarly_num() {
            return early_num;
        }

        public void setEarly_num(int early_num) {
            this.early_num = early_num;
        }

        public int getAbsent_num() {
            return absent_num;
        }

        public void setAbsent_num(int absent_num) {
            this.absent_num = absent_num;
        }

        public int getOut_num() {
            return out_num;
        }

        public void setOut_num(int out_num) {
            this.out_num = out_num;
        }

        public int getOvertime_num() {
            return overtime_num;
        }

        public void setOvertime_num(int overtime_num) {
            this.overtime_num = overtime_num;
        }

        public int getLeave_num() {
            return leave_num;
        }

        public void setLeave_num(int leave_num) {
            this.leave_num = leave_num;
        }

        public int getBus_num() {
            return bus_num;
        }

        public void setBus_num(int bus_num) {
            this.bus_num = bus_num;
        }
    }
}
