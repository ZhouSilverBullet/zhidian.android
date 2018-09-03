package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class CourseIndexBean extends BaseModel<CourseIndexBean.DataBean> {

    public static class DataBean {
        private List<CourseBean> course;

        public List<CourseBean> getCourse() {
            return course;
        }

        public void setCourse(List<CourseBean> course) {
            this.course = course;
        }

        public static class CourseBean {

            //private int id; //课程ID
            //private String courseName;
            //private int section; //从第几节课开始
            //private int sectionSpan; //跨几节课
            //private int week; //周几
            //private String classRoom; //教室
            //private int courseFlag; //课程背景颜色

            private int class_id;
            private int course_id;
            private int week_day;
            private int number;
            private int begin;
            private int end;
            private String course_name;
            private String color;
            private String class_name;

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public int getCourse_id() {
                return course_id;
            }

            public void setCourse_id(int course_id) {
                this.course_id = course_id;
            }

            public int getWeek_day() {
                return week_day;
            }

            public void setWeek_day(int week_day) {
                this.week_day = week_day;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public int getBegin() {
                return begin;
            }

            public void setBegin(int begin) {
                this.begin = begin;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }
        }
    }
}
