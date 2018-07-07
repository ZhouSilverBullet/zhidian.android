package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class SchedulingManageBean extends BaseModel<SchedulingManageBean.DataBean> {

    public static class DataBean {
        private List<RuleBean> rule;

        public List<RuleBean> getRule() {
            return rule;
        }

        public void setRule(List<RuleBean> rule) {
            this.rule = rule;
        }

        public static class RuleBean {
            /**
             * rule_id : 8
             * rule_name : 默认规则
             * class_id : 9
             * is_default : 1
             * work_week_day : 1,2,3,4,5
             * work_holiday :
             * part_name :
             * user_name :
             * class_name : 早九晚五
             * week_day : 周一,周二,周三,周四,周五
             * holiday :
             */

            private int rule_id;
            private String rule_name;
            private int class_id;
            private int is_default;
            private String work_week_day;
            private String work_holiday;
            private String part_name;
            private String user_name;
            private String class_name;
            private String week_day;
            private String holiday;

            public int getRule_id() {
                return rule_id;
            }

            public void setRule_id(int rule_id) {
                this.rule_id = rule_id;
            }

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

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public String getWeek_day() {
                return week_day;
            }

            public void setWeek_day(String week_day) {
                this.week_day = week_day;
            }

            public String getHoliday() {
                return holiday;
            }

            public void setHoliday(String holiday) {
                this.holiday = holiday;
            }
        }
    }
}
