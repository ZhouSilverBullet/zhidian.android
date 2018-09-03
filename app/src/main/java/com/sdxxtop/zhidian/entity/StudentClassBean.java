package com.sdxxtop.zhidian.entity;

import com.google.gson.annotations.SerializedName;
import com.sdxxtop.zhidian.http.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */

public class StudentClassBean extends BaseModel<StudentClassBean.DataBean> {

    public static class DataBean {
        @SerializedName("class")
        private List<ClassBean> classX;

        public List<ClassBean> getClassX() {
            return classX;
        }

        public void setClassX(List<ClassBean> classX) {
            this.classX = classX;
        }

        public static class ClassBean {
            /**
             * class_id : 8
             * class_name : 一班
             */

            private int class_id;
            private String class_name;

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
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
