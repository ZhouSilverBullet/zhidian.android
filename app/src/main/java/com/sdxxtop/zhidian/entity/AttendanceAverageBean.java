package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class AttendanceAverageBean extends BaseModel<AttendanceAverageBean.DataBean> {

    public static class DataBean {

        private float month_ave;
        private List<DayAveBean> day_ave;

        public float getMonth_ave() {
            return month_ave;
        }

        public void setMonth_ave(float month_ave) {
            this.month_ave = month_ave;
        }

        public List<DayAveBean> getDay_ave() {
            return day_ave;
        }

        public void setDay_ave(List<DayAveBean> day_ave) {
            this.day_ave = day_ave;
        }

        public static class DayAveBean {

            private String day;
            private float work_time;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public float getWork_time() {
                return work_time;
            }

            public void setWork_time(float work_time) {
                this.work_time = work_time;
            }
        }
    }
}
